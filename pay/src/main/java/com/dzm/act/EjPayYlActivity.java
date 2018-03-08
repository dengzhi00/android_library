package com.dzm.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.dzm.pay.DzmApi;
import com.dzm.pay.DzmMiddle;
import com.dzm.pay.mod.resp.DzmPayResp;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author 邓治民
 * data 2017/12/20 上午9:28
 * Uri.parse("ejpay://ylpay/act")
 */

public class EjPayYlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tn = null;
        if(null != getIntent().getExtras()){
            tn = getIntent().getExtras().getString("tn",null);
        }
        if(TextUtils.isEmpty(tn)){
            finish();
            return;
        }
        // mMode参数解释：
        // 0 - 启动银联正式环境
        // 1 - 连接银联测试环境
        UPPayAssistEx.startPay(this, null, null, tn, "00");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data || null == data.getExtras()) {
            return;
        }
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result",null);
        if ("success".equalsIgnoreCase(str)) {

            // 如果想对结果数据验签，可使用下面这段代码，但建议不验签，直接去商户后台查询交易结果
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
//                    String dataOrg = resultJson.getString("data");
                    // 此处的verify建议送去商户后台做验签
                    // 如要放在手机端验，则代码必须支持更新证书
//                    boolean ret = verify(dataOrg, sign, mMode);
//                    if (ret) {
//                        // 验签成功，显示支付结果
//                        msg = "支付成功！";
//                    } else {
//                        // 验签失败
//                        msg = "支付失败！";
//                    }
                    DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_YL);
                    resp.outSignId = sign;
                    resp.isSuccess = true;
                    resp.message = "支付成功";
                    DzmMiddle.get().onResp(resp);
                } catch (JSONException e) {
                    DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_YL);
                    resp.isSuccess = false;
                    resp.message = "支付结果解析异常";
                    DzmMiddle.get().onResp(resp);
                }
            }
            // 结果result_data为成功时，去商户后台查询一下再展示成功
//            msg = "支付成功！";
        } else if (str.equalsIgnoreCase("fail")) {
            DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_YL);
            resp.isSuccess = false;
            resp.message = "支付失败";
            DzmMiddle.get().onResp(resp);
        } else if (str.equalsIgnoreCase("cancel")) {
            DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_YL);
            resp.isSuccess = false;
            resp.message = "用户取消了支付";
            DzmMiddle.get().onResp(resp);
        }
        finish();
    }
}
