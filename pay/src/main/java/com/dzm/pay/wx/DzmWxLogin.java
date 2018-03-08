package com.dzm.pay.wx;

import android.util.Log;

import com.dzm.pay.DzmApi;
import com.dzm.pay.DzmMiddle;
import com.dzm.pay.mod.resp.DzmLoginResp;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

/**
 * @author 邓治民
 *         data 2018/2/28 下午4:44
 */

public class DzmWxLogin {

    public static void onReq(BaseReq baseReq) {

    }

    public static void onResp(BaseResp baseResp) {
        switch (baseResp.getType()){
            case ConstantsAPI.COMMAND_SENDAUTH:
                Log.d("TAG","**************************wx login********************");
                if(baseResp.errCode == 0){
                    SendAuth.Resp resp = (SendAuth.Resp) baseResp;
                    DzmLoginResp loginResp = DzmLoginResp.create(DzmApi.LOGIN_WX);
                    loginResp.resCode = resp.code;
                    loginResp.isSuccess = true;
                    loginResp.message = "登录成功";
                    DzmMiddle.get().onResp(loginResp);
                } else {
                    DzmLoginResp loginResp = DzmLoginResp.create(DzmApi.LOGIN_WX);
                    loginResp.isSuccess = false;
                    loginResp.message = "登录失败";
                    DzmMiddle.get().onResp(loginResp);
                }
                break;
        }
    }

    public static void wxLogin(){
        DzmWx.wxLogin();
    }

}
