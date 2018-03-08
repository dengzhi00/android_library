package com.dzm.pay.zfb;


import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.dzm.pay.DzmApi;
import com.dzm.pay.DzmMiddle;
import com.dzm.pay.mod.req.DzmPayZfbReq;
import com.dzm.pay.mod.resp.DzmPayResp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author 邓治民
 *         data 2017/12/22 下午2:43
 */

public class DzmZfbPay {


    public static void pay(DzmPayZfbReq req, final Activity activity) {
        Observable.just(req.payInfo)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, Map<String, String>>() {
                    @Override
                    public Map<String, String> call(String s) {
                        PayTask alipay = new PayTask(activity);
                        return alipay.payV2(s, true);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Map<String, String>>() {
                    @Override
                    public void call(Map<String, String> map) {
                        /**
                         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = "";// 同步返回需要验证的信息
                        String resultStatus = "";
                        for (String key : map.keySet()) {
                            if (TextUtils.equals(key, "resultStatus")) {
                                resultStatus = map.get(key);
                            } else if (TextUtils.equals(key, "result")) {
                                resultInfo = map.get(key);
                            } else if (TextUtils.equals(key, "memo")) {
//                    memo = s.get(key);
                            }
                        }
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, "9000")) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            try {
                                JSONObject jsonObject = new JSONObject(resultInfo);
                                JSONObject jsPbj = jsonObject.optJSONObject("alipay_trade_app_pay_response");
                                if (null != jsPbj) {
                                    String out_trade_no = jsPbj.optString("out_trade_no", null);
                                    String trade_no = jsPbj.optString("trade_no", null);
                                    DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_ZFB);
                                    resp.outSignId = out_trade_no;
                                    resp.isSuccess = true;
                                    resp.message = "支付成功";
                                    DzmMiddle.get().onResp(resp);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_ZFB);
                                resp.isSuccess = false;
                                resp.message = "支付结果解析异常";
                                DzmMiddle.get().onResp(resp);
                            }

                        } else {
                            DzmPayResp resp = DzmPayResp.create(DzmApi.PAY_ZFB);
                            resp.isSuccess = false;
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                // 正在处理中
                                resp.message = "正在处理";
                            } else if (TextUtils.equals(resultStatus, "6001")) {
                                // 用户取消支付
                                resp.message = "支付取消";
                            } else {
                                // 支付失败
                                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                                resp.message = "支付失败";
                            }
                            DzmMiddle.get().onResp(resp);
                        }

                    }
                });
    }


}
