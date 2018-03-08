package com.dzm.banner.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.dzm.banner.DzmBannerPage;
import com.dzm.banner.R;
import com.dzm.banner.holder.DzmViewHolder;
import com.dzm.banner.holder.Holder;


/**
 * @author 邓治民
 *         data 2018/2/27 下午4:58
 */

public class DzmBannerAdapter<T> extends PagerAdapter {

    private DzmViewHolder<T> viewHolder;
    private DzmBannerPage.Build<T> build;
    private SparseArray<Holder> sparseArray;

    public DzmBannerAdapter(DzmBannerPage.Build<T> build) {
        this.viewHolder = build.getViewHolder();
        this.build = build;
        sparseArray = new SparseArray<>();
    }


    @Override
    public int getCount() {
        return build.isLoop() && viewHolder.getDatas().size() != 0 ? Integer.MAX_VALUE : viewHolder.getDatas().size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int size = viewHolder.getDatas().size();
        Holder holder = sparseArray.get(position % (size*2));
        View view;
        if (null == holder) {
            holder = viewHolder.createHolder();
            view = holder.createView(container.getContext());
            sparseArray.append(position, holder);
        }else {
            view = holder.createView(container.getContext());
        }
        holder.updateUi(container.getContext(), position % size, viewHolder.getDatas().get(position % viewHolder.getDatas().size()));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void notifyDataSetChanged() {
        sparseArray.clear();
        super.notifyDataSetChanged();
    }
}
