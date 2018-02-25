package com.lai.mtc.comm.widget;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Lai
 * @time 2018/1/19 14:53
 * @describe describe
 */

public class InterceptAppBarLayout extends AppBarLayout {

    public InterceptAppBarLayout(Context context) {
        super(context);
    }

    public InterceptAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isIntercept() {
        return isIntercept;
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    private boolean isIntercept;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return isIntercept || super.dispatchTouchEvent(ev);
    }
}
