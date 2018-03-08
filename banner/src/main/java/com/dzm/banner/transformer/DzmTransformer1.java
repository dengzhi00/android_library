package com.dzm.banner.transformer;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author 邓治民
 *         data 2018/1/29 上午10:57
 */

public class DzmTransformer1 implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.70f;
    private static final float MIN_ALPHA = 0.5f;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void transformPage(View page, float position) {
        if (position < -1 || position > 1) {
//            page.setAlpha(MIN_ALPHA);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            if(position > 1){
                page.setTranslationZ(-200*position);
            }else {
                page.setTranslationZ(200*position);
            }

        } else if (position <= 1) { // [-1,1]
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            if (position < 0) {
                float scaleX = 1 + 0.3f * position;
//                Log.d("google_lenve_fb", "transformPage: scaleX:" + scaleX);
                page.setScaleX(scaleX);
                page.setScaleY(scaleX);
                page.setTranslationZ(200*position);
            } else {
                float scaleX = 1 - 0.3f * position;
                page.setScaleX(scaleX);
                page.setScaleY(scaleX);
                page.setTranslationZ(-200*position);
            }

//            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }

        page.setTranslationX(-200*position);
    }
}
