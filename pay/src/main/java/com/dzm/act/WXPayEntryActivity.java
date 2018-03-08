package com.dzm.act;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.dzm.pay.wx.DzmWx;
import com.dzm.pay.wx.DzmWxPay;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
	public void onReq(BaseReq req) {
		DzmWxPay.onReq(req);
	}

	@Override
	public void onResp(BaseResp resp) {
		DzmWxPay.onResp(resp);
		finish();
	}
}