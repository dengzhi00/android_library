package com.dzm.pay.mod.resp;

/**
 * @author 邓治民
 *         data 2018/2/28 下午4:48
 */

public class DzmLoginResp extends DzmBaseResp{


    public String resCode;

    public String message;

    public boolean isSuccess;

    public static DzmLoginResp create(String type){
        return new DzmLoginResp(type);
    }

    private DzmLoginResp(String type){
        this.type = type;
    }

}
