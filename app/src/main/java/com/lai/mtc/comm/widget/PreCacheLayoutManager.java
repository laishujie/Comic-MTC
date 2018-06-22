package com.lai.mtc.comm.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * @author Lai
 * @time 2018/2/6 18:05
 * @describe 预加载的LayoutManager
 * getExtraLayoutSpace()，增加不可见View的缓存的空间
 * <p>
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0209/2452.html
 */

public class PreCacheLayoutManager extends LinearLayoutManager {

    public int getExtraSpace() {
        return mExtraSpace;
    }

    private int mExtraSpace = 0;


    public PreCacheLayoutManager(Context context) {
        super(context);
    }


    public void setExtraSpace(int extraSpace) {
        mExtraSpace = extraSpace;
    }

    //getExtraLayoutSpace()，增加不可见View的缓存的空间
    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if (mExtraSpace > 0) {
            if (getOrientation() == LinearLayoutManager.HORIZONTAL) {
                return mExtraSpace * getWidth();
            } else {
                return mExtraSpace * getHeight();
            }
        }
        return 0;
    }

}
