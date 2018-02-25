package com.lai.mtc.comm.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

/**
 * @author Lai
 * @time 2017/8/31 14:28
 * @describe describe
 */

public class BookView extends CardView {
    GradientDrawable gradientDrawable;

    public BookView(Context context) {
        this(context,null);
    }

    public BookView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
        init();
    }

    private void init() {
        gradientDrawable = new GradientDrawable();
        //左上 右上 右下 左下
        gradientDrawable.setCornerRadii(new float[]{0,5,5,0});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
