package com.dzm.banner;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

/**
 * @author 邓治民
 *         data 2018/2/28 上午10:53
 */

public interface DzmIndicator extends ViewPager.OnPageChangeListener{

    void setViewPage(@NonNull ViewPager viewPage);

    void setPageCount(int count);
}
