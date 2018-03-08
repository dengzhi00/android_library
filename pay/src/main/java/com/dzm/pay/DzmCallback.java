package com.dzm.pay;

import com.dzm.pay.mod.resp.DzmBaseResp;

/**
 * @author 邓治民
 *         data 2018/2/28 下午4:26
 */

public interface DzmCallback {

    void onResp(DzmBaseResp ejPayResp);

}
