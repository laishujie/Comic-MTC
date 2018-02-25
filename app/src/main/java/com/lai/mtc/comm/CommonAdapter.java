package com.lai.mtc.comm;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    public CommonAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public CommonAdapter(@Nullable List<T> data) {
        super(data);
    }

    public CommonAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, T t) {
        convert(baseViewHolder, t, baseViewHolder.getLayoutPosition());
    }

    /***为了方便使用，将当前item的position返回*/
    protected abstract void convert(BaseViewHolder baseViewHolder, T t, int position);

    /***判断是否是刷新，添加数据*/
    public void setData(boolean isRefresh, List<T> data) {
        if (isRefresh) {
            setNewData(data);
        } else {
            addData(data);
        }
    }
}
