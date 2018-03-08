package com.dzm.banner.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

import com.dzm.banner.DzmIndicator;
import com.dzm.banner.R;


public class PagerIndicator extends View implements DzmIndicator {

    private static final int TRANSITION_FADE = 0;

    private static final int TRANSITION_SCALE = 1;

    private static final int TRANSITION_SLIDE = 2;

    private Drawable mDrawable;

    private int mSpacing;

    private int mTransition;

    private float mFromAlpha, mToAlpha, mFromScaleX, mFromScaleY, mToScaleX, mToScaleY;

    private int mBgWidth, mBgHeight, mWidth, mHeight;

    private int mMaxWidth, mMaxHeight;

    private int mStartX, mStepWidth, mCenterY;

    private int mCount, mCurrent;

    private float mOffset;

    private ViewPager mViewPager;

    private OnPageChangeListener mListener;

    public PagerIndicator(Context context) {
        this(context, null);
    }

    public PagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.dzmPagerIndicatorStyle);
    }

    public PagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.dzmPagerIndicator);
        mDrawable = a.getDrawable(R.styleable.dzmPagerIndicator_android_drawable);
        mSpacing = a.getDimensionPixelSize(R.styleable.dzmPagerIndicator_android_spacing, 0);
        mTransition = a.getInt(R.styleable.dzmPagerIndicator_transition, TRANSITION_FADE);
        if (mTransition == TRANSITION_FADE) {
            mFromAlpha = a.getFloat(R.styleable.dzmPagerIndicator_fromAlpha, 0.5f);
            mToAlpha = a.getFloat(R.styleable.dzmPagerIndicator_toAlpha, 1.0f);
        } else if (mTransition == TRANSITION_SCALE) {
            mFromScaleX = a.getFloat(R.styleable.dzmPagerIndicator_fromScaleX, 0.5f);
            mFromScaleY = a.getFloat(R.styleable.dzmPagerIndicator_fromScaleY, 0.5f);
            mToScaleX = a.getFloat(R.styleable.dzmPagerIndicator_toScaleX, 1.0f);
            mToScaleY = a.getFloat(R.styleable.dzmPagerIndicator_toScaleY, 1.0f);
        }
        a.recycle();

        if (mDrawable == null) {
            mDrawable = new ColorDrawable(0xff000000);
        }

        if (mDrawable.isStateful()) {
            mBgWidth = mDrawable.getIntrinsicWidth();
            mBgHeight = mDrawable.getIntrinsicHeight();
            mDrawable.setState(SELECTED_STATE_SET);
        }
        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec), height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
        if (mCount <= 0) {
            return;
        }
        final int contentWidth = width - getPaddingLeft() - getPaddingRight();
        mMaxWidth = Math.round((contentWidth - mSpacing * (mCount - 1)) * 1f / mCount);
        if (Math.min(mBgWidth, mWidth) < 0) {
            mStartX = getPaddingLeft() + (mMaxWidth >> 1);
            mStepWidth = mMaxWidth + mSpacing;
        } else {
            final int maxWidth = Math.max(mBgWidth, mWidth);
            mStartX = getPaddingLeft() + (maxWidth >> 1) + ((contentWidth - maxWidth * mCount - mSpacing * (mCount - 1)) >> 1);
            mStepWidth = maxWidth + mSpacing;
        }
        mMaxHeight = height - getPaddingTop() - getPaddingBottom();
        mCenterY = getPaddingTop() + (mMaxHeight >> 1);
    }

    private int measureWidth(int measureSpec) {
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            final int minWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                return Math.min(minWidth, specSize);
            }
            return minWidth;
        }
    }

    private int measureHeight(int measureSpec) {
        final int specMode = MeasureSpec.getMode(measureSpec);
        final int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            final int minHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                return Math.min(minHeight, specSize);
            }
            return minHeight;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mCount <= 0) {
            return;
        }
        int left, top, width, height;
        if (mDrawable.isStateful()) {
            mDrawable.setState(EMPTY_STATE_SET);
            width = mBgWidth < 0 ? mMaxWidth : mBgWidth;
            height = mBgHeight < 0 ? mMaxHeight : mBgHeight;
            if (width > 0 && height > 0) {
                top = mCenterY - (height >> 1);
                for (int i = 0; i < mCount; i++) {
                    left = mStartX + mStepWidth * i - (width >> 1);
                    mDrawable.setBounds(left, top, left + width, top + height);
                    mDrawable.draw(canvas);
                }
            }
            mDrawable.setState(SELECTED_STATE_SET);
        }
        width = mWidth < 0 ? mMaxWidth : mWidth;
        height = mHeight < 0 ? mMaxHeight : mHeight;
        if (width <= 0 || height <= 0) {
            return;
        }
        if (mTransition == TRANSITION_FADE) {
            top = mCenterY - (height >> 1);
            int alpha = (int) (255 * mFromAlpha);
            for (int i = 0; i < mCount; i++) {
                if (i == mCurrent) {
                    mDrawable.setAlpha((int) (255 * ((mToAlpha - mFromAlpha) * (1 - mOffset) + mFromAlpha)));
                } else if (i == mCurrent + 1) {
                    mDrawable.setAlpha((int) (255 * ((mToAlpha - mFromAlpha) * mOffset + mFromAlpha)));
                } else {
                    mDrawable.setAlpha(alpha);
                }
                left = mStartX + mStepWidth * i - (width >> 1);
                mDrawable.setBounds(left, top, left + width, top + height);
                mDrawable.draw(canvas);
            }
            mDrawable.setAlpha(255);
        } else if (mTransition == TRANSITION_SCALE) {
            int scaleWidth, scaleHeight;
            for (int i = 0; i < mCount; i++) {
                if (i == mCurrent) {
                    scaleWidth = (int) (width * ((mToScaleX - mFromScaleX) * (1 - mOffset) + mFromScaleX));
                    scaleHeight = (int) (height * ((mToScaleY - mFromScaleY) * (1 - mOffset) + mFromScaleY));
                } else if (i == mCurrent + 1) {
                    scaleWidth = (int) (width * ((mToScaleX - mFromScaleX) * mOffset + mFromScaleX));
                    scaleHeight = (int) (height * ((mToScaleY - mFromScaleY) * mOffset + mFromScaleY));
                } else {
                    scaleWidth = (int) (width * mFromScaleX);
                    scaleHeight = (int) (height * mFromScaleY);
                }
                left = mStartX + mStepWidth * i - (scaleWidth >> 1);
                top = mCenterY - (scaleHeight >> 1);
                mDrawable.setBounds(left, top, left + scaleWidth, top + scaleHeight);
                mDrawable.draw(canvas);
            }
        } else if (mTransition == TRANSITION_SLIDE) {
            left = mStartX + (int) (mStepWidth * (mCurrent + mOffset)) - (width >> 1);
            top = mCenterY - (height >> 1);
            mDrawable.setBounds(left, top, left + width, top + height);
            mDrawable.draw(canvas);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

    public void setViewPage(@NonNull ViewPager viewPage) {
        mViewPager = viewPage;
        mViewPager.addOnPageChangeListener(this);
    }

    public void setPageCount(int count) {
        mCount = count;
        if (count > 0) {
            setMinimumWidth(Math.max(Math.max(mBgWidth, mWidth), 0) * count + mSpacing * (count - 1));
            if (mBgHeight > 0 || mHeight > 0) {
                setMinimumHeight(Math.max(mBgHeight, mHeight));
            }
        } else {
            setMinimumWidth(0);
            setMinimumHeight(0);
        }
        requestLayout();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mCount != 0) {
            if ((position % mCount != mCount - 1) || positionOffset == 0) {
                mCurrent = position % mCount;
                mOffset = positionOffset;
                invalidate();
            }
        } else {
            mCurrent = position;
            mOffset = positionOffset;
            invalidate();
        }

        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

    }

    @Override
    public void onPageSelected(int position) {
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }

}