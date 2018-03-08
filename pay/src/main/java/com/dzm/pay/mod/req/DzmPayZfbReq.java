package com.dzm.pay.mod.req;

import com.dzm.pay.DzmApi;

/**
 * @author 邓治民
 *         data 2018/2/28 下午5:09
 */

public class DzmPayZfbReq extends DzmBaseReq{

    public DzmPayZfbReq(){
        this.type = DzmApi.PAY_ZFB;
    }

    public String payInfo;

}
