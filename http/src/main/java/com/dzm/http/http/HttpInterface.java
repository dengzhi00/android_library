package com.dzm.http.http;


import com.dzm.http.intercept.HttpInterceptInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓治民
 *         data 2018/3/8 下午3:05
 */

public interface HttpInterface {

    void build(Build build);

    class Build{

        private Map<String,String> data = new HashMap<>();
        private Map<String,Object> other = new HashMap<>();
        private String tag;
        private Class<? extends HttpInterceptInterface> hInterface;
        private HttpCallback callback;

        public Build(String tag){
            this.tag = tag;
        }

        public Build addData(String key,String value){
            data.put(key,value);
            return this;
        }

        public Build addDataAll(Map<String,String> data){
            this.data.putAll(data);
            return this;
        }

        public Build addTag(String tag){
            this.tag = tag;
            return this;
        }

        public Build addOther(String key,Object value){
            this.other.put(key,value);
            return this;
        }

        public Build addOtherAll(Map<String,Object> other){
            this.other.putAll(other);
            return this;
        }

        public Build sethInterface(Class<? extends HttpInterceptInterface> hInterface) {
            this.hInterface = hInterface;
            return this;
        }

        public Build setCallback(HttpCallback callback) {
            this.callback = callback;
            return this;
        }

        public Map<String, String> getData() {
            return data;
        }

        public Map<String, Object> getOther() {
            return other;
        }

        public String getTag() {
            return tag;
        }

        public Class<? extends HttpInterceptInterface> gethInterface() {
            return hInterface;
        }

        public HttpCallback getCallback() {
            return callback;
        }
    }
}
