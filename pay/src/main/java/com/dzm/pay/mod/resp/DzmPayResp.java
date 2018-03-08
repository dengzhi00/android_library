package com.dzm.pay.mod.resp;

/**
 * @author 邓治民
 *         data 2017/12/25 上午9:34
 */

public class DzmPayResp extends DzmBaseResp{

    public String outSignId;

    public boolean isSuccess;

    public String message;

    public static DzmPayResp create(String type){
        return new DzmPayResp(type);
    }

    private DzmPayResp(String type){
        this.type = type;
    }


}
