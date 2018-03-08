package com.dzm.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @author 邓治民
 *         data 2018/2/28 上午9:11
 */

public class DzmViewPagerScroller extends Scroller{

    private int mScrollDuration = 800;
    private boolean zero;

    public DzmViewPagerScroller(Context context) {
        super(context);
    }

    public DzmViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.zero?0:this.mScrollDuration);
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, this.zero?0:this.mScrollDuration);
    }

    public int getScrollDuration() {
        return this.mScrollDuration;
    }

    public void setScrollDuration(int scrollDuration) {
        this.mScrollDuration = scrollDuration;
    }

    public boolean isZero() {
        return this.zero;
    }

    public void setZero(boolean zero) {
        this.zero = zero;
    }
}
