package com.dzm.pay;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.dzm.pay.mod.req.DzmBaseReq;
import com.dzm.pay.mod.resp.DzmBaseResp;
import com.dzm.pay.wx.DzmWxLogin;

/**
 * @author 邓治民
 *         data 2018/2/28 下午5:27
 */

public class DzmLogin implements DzmCallback, LifecycleObserver {

    public DzmLogin(Context context){
        ((LifecycleOwner) context).getLifecycle().addObserver(this);
        DzmMiddle.get().addCallback(this);
    }

    public DzmLogin(Fragment fragment){
        fragment.getLifecycle().addObserver(this);
        DzmMiddle.get().addCallback(this);
    }

    private DzmCallback payCallback;


    public void setPayCallback(DzmCallback payCallback) {
        this.payCallback = payCallback;
    }

    @Override
    public void onResp(DzmBaseResp ejPayResp) {
        if(null != payCallback){
            switch (ejPayResp.getType()){
                case DzmApi.LOGIN_WX:
                    payCallback.onResp(ejPayResp);
                    break;
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        DzmMiddle.get().removeCallback(this);
    }

    public void login(DzmBaseReq baseReq){
        switch (baseReq.getType()){
            case DzmApi.LOGIN_WX:
                DzmWxLogin.wxLogin();
                break;
        }
    }
}
