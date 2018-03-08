package com.dzm.banner;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dzm.banner.adapter.DzmBannerAdapter;
import com.dzm.banner.callback.OnItemClickCallback;
import com.dzm.banner.holder.DzmViewHolder;
import com.dzm.banner.view.DzmLoopViewPager;
import com.dzm.banner.view.PagerIndicator;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * @author 邓治民
 *         data 2018/2/27 下午4:29
 */

public class DzmBannerPage<T> extends FrameLayout implements LifecycleObserver{

    private Build<T> build;
    private DzmLoopViewPager viewPager;
    private boolean turning;
    private SlideRunnable slideRunnable;
    private DzmBannerAdapter<T> bannerAdapter;
    private DzmViewPagerScroller scroller;
    private DzmIndicator dzmIndicator;

    private FrameLayout dzm_indecator;

    public DzmBannerPage(Context context,Build<T> build) {
        super(context);
        this.build = build;
        initAdapter();
        init(context);
    }

    public DzmBannerPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DzmBannerPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        dzm_indecator = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.dzm_banner_page,null);
        viewPager = new DzmLoopViewPager.Build<T>().setBuild(build).build(getContext());
        viewPager.setOffscreenPageLimit(2);
        addView(viewPager);
        addView(dzm_indecator);
        FrameLayout.LayoutParams idParms = new LayoutParams(LayoutParams.MATCH_PARENT,40,Gravity.BOTTOM);
        dzm_indecator.setLayoutParams(idParms);
        slideRunnable = new SlideRunnable(this);
        initViewPagerScroll();
        ((LifecycleOwner) getContext()).getLifecycle().addObserver(this);
    }

    private void initViewPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            this.scroller = new DzmViewPagerScroller(this.viewPager.getContext());
            mScroller.set(this.viewPager, this.scroller);
        } catch (NoSuchFieldException var2) {
            var2.printStackTrace();
        } catch (IllegalArgumentException var3) {
            var3.printStackTrace();
        } catch (IllegalAccessException var4) {
            var4.printStackTrace();
        }

    }

    public void setBuild(Build<T> build) {
        this.build = build;
        initAdapter();
        onResume();
    }

    private void initAdapter(){
        if(null == build){
            return;
        }
        if(null != build.transformer){
            viewPager.setPageTransformer(true,build.transformer);
        }

        if(null != build.indicator && build.indicator instanceof View){
            dzmIndicator = build.indicator;
            dzm_indecator.addView((View) build.indicator);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT,build.idPosition == -1?Gravity.CENTER:build.idPosition);
            ((View) build.indicator).setLayoutParams(params);
        }else {
            if(build.neadIndicator){
                PagerIndicator dzm_pagerindecator = (PagerIndicator) LayoutInflater.from(getContext()).inflate(R.layout.dzm_indecator, null);
                dzmIndicator = dzm_pagerindecator;
                dzm_indecator.addView(dzm_pagerindecator);
                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT,build.idPosition == -1?Gravity.CENTER:build.idPosition);
                dzm_pagerindecator.setLayoutParams(params);
            }
        }
        refreshIndicator();
        bannerAdapter = new DzmBannerAdapter<>(build);
        viewPager.setBuild(build);
        viewPager.setAdapter(bannerAdapter);

    }

    public void notifyDataSetChanged() {
        bannerAdapter.notifyDataSetChanged();
        refreshIndicator();
        onResume();
    }

    private void refreshIndicator(){
        if(null != dzmIndicator){
            dzmIndicator.setPageCount(build.viewHolder.getDatas().size());
            dzmIndicator.setViewPage(viewPager);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume(){
        if(null == build || !build.isSlide || null == build.viewHolder || build.viewHolder.getDatas().size() == 0){
           return;
        }
        if(this.turning){
            onPause();
        }
        this.turning = true;
        this.postDelayed(slideRunnable,build.slideTime);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.turning = false;
        this.removeCallbacks(slideRunnable);
    }

    public static class Build<D>{
        //是否循环
        private boolean isLoop = true;
        //自动滑动间隔时间
        private long slideTime = 5000;
        //是否自动滑动
        private boolean isSlide = true;
        //页面
        private DzmViewHolder<D> viewHolder;
        //点击事件
        private OnItemClickCallback<D> onItemClickCallback;
        //设置indicator
        private DzmIndicator indicator;
        //是否需要indicator
        private boolean neadIndicator = true;
        //设置indicator 位置
        private int idPosition = -1;
        //
        private ViewPager.PageTransformer transformer;

        public Build<D> setLoop(boolean loop) {
            isLoop = loop;
            return this;
        }

        public Build<D> setSlideTime(long slideTime) {
            this.slideTime = slideTime;
            return this;
        }

        public Build<D> setSlide(boolean slide) {
            isSlide = slide;
            return this;
        }

        public Build<D> setViewHolder(DzmViewHolder<D> viewHolder) {
            this.viewHolder = viewHolder;
            return this;
        }

        public Build<D> setOnItemClickCallback(OnItemClickCallback<D> onItemClickCallback) {
            this.onItemClickCallback = onItemClickCallback;
            return this;
        }

        public Build<D> setIndicator(DzmIndicator indicator) {
            this.indicator = indicator;
            return this;
        }

        public Build<D> setNeadIndicator(boolean neadIndicator) {
            this.neadIndicator = neadIndicator;
            return this;
        }

        public Build<D> setIdPosition(int idPosition) {
            this.idPosition = idPosition;
            return this;
        }

        public Build<D> setTransformer(ViewPager.PageTransformer transformer) {
            this.transformer = transformer;
            return this;
        }

        public boolean isLoop() {
            return isLoop;
        }

        public DzmViewHolder<D> getViewHolder() {
            return viewHolder;
        }

        public OnItemClickCallback<D> getOnItemClickCallback() {
            return onItemClickCallback;
        }

        public DzmBannerPage<D> build(Context context){
            return new DzmBannerPage<D>(context,this);
        }
    }

    static class SlideRunnable implements Runnable{

        private final WeakReference<DzmBannerPage> reference;

        SlideRunnable(DzmBannerPage bannerPage){
            reference = new WeakReference<>(bannerPage);
        }

        @Override
        public void run() {
            DzmBannerPage bannerPage = this.reference.get();
            if(null != bannerPage
                    && null != bannerPage.viewPager
                    && null != bannerPage.build
                    && bannerPage.build.isSlide
                    && bannerPage.turning){
                if(bannerPage.viewPager.isSelfScroll()){
                    int page = bannerPage.viewPager.getCurrentItem()+1;
                    bannerPage.viewPager.setCurrentItem(page);
                }
                bannerPage.postDelayed(bannerPage.slideRunnable,bannerPage.build.slideTime);
            }
        }
    }

}
