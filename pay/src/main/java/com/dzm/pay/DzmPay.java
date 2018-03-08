package com.dzm.pay;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.dzm.act.EjPayYlActivity;
import com.dzm.pay.mod.req.DzmBaseReq;
import com.dzm.pay.mod.req.DzmPayWxReq;
import com.dzm.pay.mod.req.DzmPayYlReq;
import com.dzm.pay.mod.req.DzmPayZfbReq;
import com.dzm.pay.mod.resp.DzmBaseResp;
import com.dzm.pay.wx.DzmWxPay;
import com.dzm.pay.zfb.DzmZfbPay;
import com.tencent.mm.opensdk.modelpay.PayReq;

/**
 * @author 邓治民
 *         data 2018/2/28 下午4:13
 */

public class DzmPay implements DzmCallback, LifecycleObserver {


    public DzmPay(Context context){
        ((LifecycleOwner) context).getLifecycle().addObserver(this);
        DzmMiddle.get().addCallback(this);
    }

    public DzmPay(Fragment fragment){
        fragment.getLifecycle().addObserver(this);
        DzmMiddle.get().addCallback(this);
    }

    private DzmCallback payCallback;


    public void setPayCallback(DzmCallback payCallback) {
        this.payCallback = payCallback;
    }


    @Override
    public void onResp(DzmBaseResp ejPayResp) {
        if(null != payCallback){
            switch (ejPayResp.getType()){
                case DzmApi.PAY_WX:
                case DzmApi.PAY_ZFB:
                    payCallback.onResp(ejPayResp);
                    break;
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        DzmMiddle.get().removeCallback(this);
    }

    public void pay(DzmBaseReq baseReq, Activity activity){
        switch (baseReq.getType()){
            case DzmApi.PAY_ZFB:
                DzmZfbPay.pay((DzmPayZfbReq) baseReq,activity);
                break;
            case DzmApi.PAY_WX:
                DzmPayWxReq wxReq = (DzmPayWxReq) baseReq;
                PayReq payReq = new PayReq();
                payReq.appId = wxReq.appId;
                payReq.partnerId = wxReq.partnerId;
                payReq.prepayId = wxReq.prepayId;
                payReq.nonceStr = wxReq.nonceStr;
                payReq.timeStamp = wxReq.timeStamp;
                payReq.packageValue = wxReq.packageValue;
                payReq.sign = wxReq.sign;
                DzmWxPay.wxPay(payReq);
                break;
            case DzmApi.PAY_YL:
                DzmPayYlReq ylReq = (DzmPayYlReq) baseReq;
                Intent intent = new Intent(activity, EjPayYlActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tn",ylReq.tn);
                intent.putExtras(bundle);
                activity.startActivity(intent);
                break;
        }
    }
}
