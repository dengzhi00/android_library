package com.dzm.http.http;

import java.util.Map;

/**
 * @author 邓治民
 *         data 2018/3/8 下午2:36
 */

public interface HttpCallback {

    void onStart(String tag, Map<String,Object> other);

    void onNext(Object o, String tag, Map<String,Object> other);

    void onCompleted(String tag, Map<String,Object> other);

    void onError(String tag, Map<String,Object> other,String msg);

}
