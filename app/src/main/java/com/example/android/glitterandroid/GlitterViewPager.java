package com.example.android.glitterandroid;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by sergey on 7/22/15.
 * Simple viewpager for swiping pages right and left
 */
public class GlitterViewPager extends ViewPager {
    private boolean isPagingEnabled = true;

    public GlitterViewPager(Context context) {
        super(context);
    }

    public GlitterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
