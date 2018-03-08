package com.dzm.banner.holder;

import android.content.Context;
import android.view.View;

/**
 * @author 邓治民
 *         data 2018/2/27 下午5:53
 */

public interface Holder<T> {

    View createView(Context context);

    void updateUi(Context context,int position,T t);

}
