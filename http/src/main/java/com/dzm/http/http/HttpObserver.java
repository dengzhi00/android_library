package com.dzm.http.http;

import android.text.TextUtils;
import android.util.Log;

import com.dzm.http.intercept.HttpInterceptInterface;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author 邓治民
 *         data 2018/3/8 下午3:57
 */

class HttpObserver {

    private HttpInterface.Build build;

    HttpObserver(HttpInterface.Build build){
        this.build = build;
    }

    public void execute(Observable<Object> observable,CompositeSubscription mCompositeSubscription){
        mCompositeSubscription.add(observable.subscribeOn(Schedulers.io()).map(new Func1<Object, Object>() {
            @Override
            public Object call(Object o) {
                HttpInterceptInterface<Observable> interceptInterface = null;
                if(null == build.gethInterface()){
                    interceptInterface = HttpSir.get().getBuild().getDefult();
                }else {
                    interceptInterface = HttpSir.get().getBuild().getIntercept(build.gethInterface());
                }
                if(null == interceptInterface){
                    return o;
                }
                return interceptInterface.output(o,build.getOther(),build.getTag());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Object>() {

            @Override
            public void onStart() {
                if(null != build.getCallback()){
                    build.getCallback().onStart(build.getTag(),build.getOther());
                }
            }

            @Override
            public void onCompleted() {
                if(null != build.getCallback()){
                    build.getCallback().onCompleted(build.getTag(),build.getOther());
                }
            }

            @Override
            public void onError(Throwable e) {
                if(null != build.getCallback()){
                    if (null != e) {
                        Log.d("HttpRequest","http请求异常："+e.toString());
                        build.getCallback().onError(build.getTag(),build.getOther(),getErrer(e));
                    } else {
                        build.getCallback().onError(build.getTag(),build.getOther(),"出现未知错误");
                    }
                }
            }

            @Override
            public void onNext(Object o) {
                HttpInterceptInterface<Observable> interceptInterface = null;
                if(null == build.gethInterface()){
                    interceptInterface = HttpSir.get().getBuild().getDefult();
                }else {
                    interceptInterface = HttpSir.get().getBuild().getIntercept(build.gethInterface());
                }
                Object next;
                if(null != interceptInterface){
                    next = interceptInterface.onNext(o,build.getOther(),build.getTag());
                }else {
                    next = o;
                }
                if(null != build.getCallback()){
                    build.getCallback().onNext(next,build.getTag(),build.getOther());
                }
            }
        }));
    }

    private String getErrer(Throwable e) {
        String errMessage = "请求失败";
        String errCode = "-2";

        if(TextUtils.isEmpty(e.getMessage())){
            Log.d("HttpRequest","retrofit返回的错误信息: null");
            errCode = "-1";
            errMessage = "连接服务器失败，请检查网络设置";
        }else if (e.getMessage().contains("ailed to connect")) {
            if (e.getMessage().contains("after")) {
                errMessage = "连接超时，请检查网络设置";
                errCode = "-1";
            } else {
                errMessage = "连接服务器失败，请检查网络设置";
                errCode = "-1";
            }
        } else if (e.getMessage().contains("404") || e.getMessage().contains("5")) {//504网关超时
            errMessage = "服务器异常，请稍候再试";
            errCode = "0";
        } else if (e.getMessage().contains("timeout")) {
            errMessage = "连接超时，请检查网络设置";
            errCode = "-1";
        }
        return errMessage;
    }

}
