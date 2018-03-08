package com.dzm.pay.mod.req;

import com.dzm.pay.DzmApi;

/**
 * @author 邓治民
 *         data 2018/2/28 下午5:38
 */

public class DzmPayYlReq extends DzmBaseReq{

    public DzmPayYlReq(){
        this.type = DzmApi.PAY_YL;
    }

    public String tn;

}
