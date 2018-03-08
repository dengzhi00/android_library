package com.dzm.http.http;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import com.dzm.http.intercept.HttpInterceptInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓治民
 *         data 2018/3/8 上午11:19
 */

public class HttpSir {

    private Build build;

    private SparseArray<Http> sparseArray = new SparseArray<>();

    private static volatile HttpSir ejHttp;

    public static HttpSir get(){
        if(null == ejHttp){
            synchronized (HttpSir.class){
                if(null == ejHttp){
                    ejHttp = new HttpSir();
                }
            }
        }
        return ejHttp;
    }

    public HttpInterface initHttp(Context context){
        Http httpInterface = sparseArray.get(context.hashCode());
        if(null == httpInterface){
            httpInterface = new Http();
            httpInterface.init(context);
            sparseArray.append(context.hashCode(),httpInterface);
        }
        return httpInterface;
    }

    public HttpInterface initHttp(Fragment fragment){
        Http httpInterface = sparseArray.get(fragment.hashCode());
        if(null == httpInterface){
            httpInterface = new Http();
            httpInterface.init(fragment);
            sparseArray.append(fragment.hashCode(),httpInterface);
        }
        return httpInterface;
    }

    void removeHttp(int code){
        sparseArray.remove(code);
    }

    private HttpSir(){
        this.build = new Build();
    }

    public Build getBuild() {
        return build;
    }

    public static class Build{

        private Map<Class<? extends HttpInterceptInterface>,HttpInterceptInterface> interfaceMap = new HashMap<>();
        private HttpInterceptInterface defult;

        public Build addDefultIntercept(HttpInterceptInterface interceptInterface){
            this.defult = interceptInterface;
            interfaceMap.put(interceptInterface.getClass(),interceptInterface);
            return this;
        }

        public Build addIntercept(HttpInterceptInterface interceptInterface){
            interfaceMap.put(interceptInterface.getClass(),interceptInterface);
            return this;
        }

        public HttpInterceptInterface getDefult() {
            return defult;
        }

        public Map<Class<? extends HttpInterceptInterface>, HttpInterceptInterface> getInterfaceMap() {
            return interfaceMap;
        }

        public HttpInterceptInterface getIntercept(Class<? extends HttpInterceptInterface> clz) {
            return interfaceMap.get(clz);
        }

    }

}
