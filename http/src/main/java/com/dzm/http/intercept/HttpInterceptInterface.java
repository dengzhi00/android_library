package com.dzm.http.intercept;

import java.util.Map;

/**
 * @author 邓治民
 *         data 2018/3/8 上午9:56
 */

public interface HttpInterceptInterface<D> {

    D input(Map<String,String> data,Map<String,Object> other,String tag);

    Object output(Object o,Map<String,Object> other,String tag);

    Object onNext(Object o,Map<String,Object> other,String tag);

}
