package com.dzm.demo.http;

import java.util.List;

/**
 * @author 邓治民
 *         data 2018/3/8 下午4:29
 */

public class TestBean {

    public List<DatalistBean> datalist;

    public static class DatalistBean {
        /**
         * id : 20
         * name : 热门推荐3
         * url : http://54.222.184.204:8080/images/banner/banner_3@3x20180111172708.png
         * link : www.sumainfor.com
         * type : 0
         * flag : 1
         * createTime : 2018-01-11 17:27:08.0
         */

        public int id;
        public String name;
        public String url;
        public String link;
        public int type;
        public int flag;
        public String createTime;
    }
}
