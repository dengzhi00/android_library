package com.dzm.pay;

import android.util.SparseArray;

import com.dzm.pay.mod.resp.DzmBaseResp;

/**
 * @author 邓治民
 *         data 2018/2/28 下午4:23
 */

public class DzmMiddle implements DzmCallback {

    private static DzmMiddle dzmMiddle = new DzmMiddle();

    private SparseArray<DzmCallback> sparseArray = new SparseArray<>();

    public static DzmMiddle get(){
        return dzmMiddle;
    }

    void addCallback(DzmCallback callback){
        sparseArray.append(callback.hashCode(),callback);
    }

    void removeCallback(DzmCallback callback){
        sparseArray.remove(callback.hashCode());
    }

    @Override
    public void onResp(DzmBaseResp ejPayResp) {
        for(int i = 0;i<sparseArray.size();i++){
            sparseArray.valueAt(i).onResp(ejPayResp);
        }
    }


}
