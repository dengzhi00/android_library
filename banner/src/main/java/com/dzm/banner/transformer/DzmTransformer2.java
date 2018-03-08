package com.dzm.banner.transformer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * @author 邓治民
 *         data 2018/1/2 上午9:56
 */

public class DzmTransformer2 implements ViewPager.PageTransformer  {

    //旋转角度
    private static final float DEFAULT_MAX_ROTATE = 45f;
    private float mMaxRotate = DEFAULT_MAX_ROTATE;

    private int pPix;

    public DzmTransformer2(Context context) {
        this.pPix = dip2px(context,75);
        Log.d("pWidth","    "+pPix);
    }

    public DzmTransformer2(int mPx) {
        this.pPix = mPx;
        Log.d("pWidth","    "+pPix);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void transformPage(View view, float position) {
        view.setPivotY(view.getHeight() / 2);
        if (position < -1) { // [-Infinity,-1)
            //设置旋转轴
            view.setPivotX(0);
            //以y轴为平行旋转
            view.setRotationY(1 * mMaxRotate);

        } else if (position <= 1) { // [-1,1]
            if (position < 0)//[0,-1]
            {
                view.setPivotX(0);
            } else//[1,0]
            {
                view.setPivotX(view.getWidth());
            }
            view.setRotationY(-position * mMaxRotate);


        } else { // (1,+Infinity]
            view.setPivotX(view.getWidth());
            view.setRotationY(-1 * mMaxRotate);
        }

//        Log.d("pageTransform",view.toString()+"    "+position+"   "+view.getWidth());

        view.setTranslationX(-(view.getWidth() - pPix - 40)*position);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}


//-0.5810811
//0.47297296
//1.527027