package com.lai.mtc.comm.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Lai
 * @time 2018/1/19 14:53
 * @describe describe
 */

public class MotionEventRecyclerView extends RecyclerView {

    public boolean isInterceptTouch() {
        return isInterceptTouch;
    }

    public void setInterceptTouch(boolean interceptTouch) {
        isInterceptTouch = interceptTouch;
    }

    boolean isInterceptTouch;

    public MotionEventRecyclerView(Context context) {
        this(context, null);
    }

    public MotionEventRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
