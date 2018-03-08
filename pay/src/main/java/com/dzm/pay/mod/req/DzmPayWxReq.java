package com.dzm.pay.mod.req;

import com.dzm.pay.DzmApi;

/**
 * @author 邓治民
 *         data 2018/2/28 下午5:19
 */

public class DzmPayWxReq extends DzmBaseReq{

    public DzmPayWxReq(){
        this.type = DzmApi.PAY_WX;
    }

    public String appId;
    public String partnerId;
    public String prepayId;
    public String nonceStr;
    public String timeStamp;
    public String packageValue;
    public String sign;

}
