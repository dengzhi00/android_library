package com.dzm.banner.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dzm.banner.DzmBannerPage;
import com.dzm.banner.callback.OnItemClickCallback;

/**
 * @author 邓治民
 *         data 2018/2/27 下午4:51
 */

public class DzmLoopViewPager<T> extends ViewPager {

    private DzmBannerPage.Build<T> build;
    private OnItemClickCallback<T> onItemClickCallback;
    private boolean selfScroll = true;
    private float oldX = 0.0F;
    private float newX = 0.0F;

    public DzmLoopViewPager(Context context,Build<T> build) {
        super(context);
        setBuild(build.build);
        init(context);
    }

    public DzmLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setBuild(DzmBannerPage.Build<T> build) {
        this.build = build;
        if(null != build){
            onItemClickCallback = build.getOnItemClickCallback();
        }
    }

    public boolean isSelfScroll() {
        return selfScroll;
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                selfScroll = false;
                this.oldX = ev.getX();
                break;
            case 1:
                selfScroll = true;
                this.newX = ev.getX();
                if (Math.abs(this.oldX - this.newX) < 5.0F  && null != build && null != onItemClickCallback) {
                    int size = 0;
                    if (null != build.getViewHolder().getDatas()) {
                        size = build.getViewHolder().getDatas().size();
                    }
                    if (size != 0) {
                        int position = getCurrentItem() % size;
                        this.onItemClickCallback.onItemClick(getContext(), position, build.getViewHolder().getDatas().get(position));
                    }
                }
                this.oldX = 0.0F;
                this.newX = 0.0F;
                break;
        }
        return super.onTouchEvent(ev);
    }

    public static class Build<D>{
        private DzmBannerPage.Build<D> build;

        public Build<D> setBuild(DzmBannerPage.Build<D> build) {
            this.build = build;
            return this;
        }

        public DzmLoopViewPager build(Context context){
            return new DzmLoopViewPager<>(context,this);
        }
    }

}
