package com.dzm.http.server;


import android.content.Context;
import android.support.v4.app.Fragment;

import com.dzm.http.http.HttpInterface;
import com.dzm.http.intercept.HttpInterceptInterface;


/**
 * @author 邓治民
 *         data 2018/3/8 上午9:27
 */

public interface HttpServer {

    <T> T initService(Class<T> service,String url,TimeBuild timeout);

    <T> T initService(Class<T> service,String url);

    void addIntercepte(HttpInterceptInterface interceptInterface);

    void addDefultIntercepte(HttpInterceptInterface interceptInterface);

    HttpInterface http(Context context);

    HttpInterface http(Fragment fragment);

    class TimeBuild{
        public int connectTimeout;
        public int readTimeout;
        public int writeTimeout;

        public TimeBuild(){}

        public TimeBuild(int connectTimeout,int readTimeout,int writeTimeout){
            this.connectTimeout = connectTimeout;
            this.readTimeout = readTimeout;
            this.writeTimeout = writeTimeout;
        }
    }
}
