package com.dzm.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dzm.pay.wx.DzmWx;
import com.dzm.pay.wx.DzmWxLogin;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 *
 * @author 邓治民
 * date 2017/9/6 15:07
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DzmWx.handleIntent(getIntent(),this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        DzmWx.handleIntent(getIntent(),this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
//        EjShareWx.onReq(baseReq);
//        EjLoginWx.onReq(baseReq);
        DzmWxLogin.onReq(baseReq);
    }

    @Override
    public void onResp(BaseResp baseResp) {
//        EjShareWx.onResp(baseResp);
//        EjLoginWx.onResp(baseResp);
        DzmWxLogin.onResp(baseResp);
        finish();
    }
}
