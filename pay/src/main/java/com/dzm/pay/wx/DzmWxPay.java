package com.dzm.pay.wx;


import com.dzm.pay.DzmApi;
import com.dzm.pay.DzmMiddle;
import com.dzm.pay.mod.resp.DzmPayResp;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * @author 邓治民
 *         data 2017/12/25 上午9:37
 */

public class DzmWxPay {

    public static void onReq(BaseReq baseReq) {

    }

    public static void onResp(BaseResp baseResp) {
        switch (baseResp.getType()){
            case ConstantsAPI.COMMAND_PAY_BY_WX://支付
                if(baseResp.errCode == 0 ){
                    DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_WX);
                    resp.isSuccess = true;
                    resp.message = "支付成功";
                    DzmMiddle.get().onResp(resp);
                }else if(baseResp.errCode == -2 ){
                    DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_WX);
                    resp.isSuccess = false;
                    resp.message = "支付失败";
                    DzmMiddle.get().onResp(resp);
                }
                break;
        }
    }

    public static void wxPay(PayReq payReq){
        DzmWx.wxPay(payReq);
    }

}
