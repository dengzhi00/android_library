package com.dzm.banner.callback;

import android.content.Context;

/**
 * @author 邓治民
 *         data 2018/2/28 上午9:15
 */

public interface OnItemClickCallback<T> {

    void onItemClick(Context context,int position,T t);

}
