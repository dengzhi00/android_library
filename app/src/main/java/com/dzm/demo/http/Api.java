package com.dzm.demo.http;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author 邓治民
 *         data 2018/3/8 下午4:17
 */

public interface Api {

    @POST("getBannerList.do")
    Observable<BaseResp<TestBean>> test(@QueryMap Map<String, String> map);

    @POST("getBannerList.do")
    Observable<BaseResp<TestBean>> test2(@QueryMap Map<String, String> map);
}
