package com.dzm.demo.http;


import com.dzm.demo.MyLibServicesLoader;
import com.dzm.http.intercept.HttpInterceptInterface;
import com.dzm.http.server.HttpServer;

import java.util.Map;

import rx.Observable;

/**
 * @author 邓治民
 *         data 2018/3/8 下午4:14
 */

public class TestIntercept implements HttpInterceptInterface<Observable> {

    private Api api;

    public TestIntercept(){
        api = MyLibServicesLoader.getService(HttpServer.class).initService(Api.class,"http://54.222.184.204:8080/vanGogh/");
    }

    @Override
    public Observable input(Map<String, String> data, Map<String, Object> other, String tag) {
        switch (tag){
            case "test":
                return api.test(data);
        }
        return null;
    }

    @Override
    public Object output(Object o, Map<String, Object> other, String tag) {
        return o;
    }

    @Override
    public Object onNext(Object o, Map<String, Object> other, String tag) {
        return o;
    }
}
