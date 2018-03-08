package com.dzm.pay.wx;

import android.content.Context;
import android.content.Intent;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author 邓治民
 *         data 2018/2/28 下午4:41
 */

public class DzmWx {

    private static IWXAPI iwxapi;

    public static void init(Context context){
        iwxapi = WXAPIFactory.createWXAPI(context, "wxd3a9a4330c00c911");
        iwxapi.registerApp("wxd3a9a4330c00c911");
    }

    static void wxLogin(){
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_1234";
        DzmWx.iwxapi.sendReq(req);
    }

    static void wxPay(PayReq payReq){
        DzmWx.iwxapi.sendReq(payReq);
    }


    /**
     * handleIntent
     * @param intent intent
     * @param handler handler
     */
    public static void handleIntent(Intent intent, IWXAPIEventHandler handler){
        DzmWx.iwxapi.handleIntent(intent,handler);
    }



}
