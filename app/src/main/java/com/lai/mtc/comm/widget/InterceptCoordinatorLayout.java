package com.lai.mtc.comm.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Lai
 * @time 2018/1/19 14:53
 * @describe describe
 */

public class InterceptCoordinatorLayout extends CoordinatorLayout {


    public boolean isInterceptBehavior() {
        return isInterceptBehavior;
    }

    public void setInterceptBehavior(boolean interceptBehavior) {
        isInterceptBehavior = interceptBehavior;
    }

    private boolean isInterceptBehavior;

    public InterceptCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public InterceptCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return super.onInterceptTouchEvent(ev)||isInterceptBehavior;
    }
}
