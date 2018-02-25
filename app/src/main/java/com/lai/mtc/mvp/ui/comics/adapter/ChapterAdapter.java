package com.lai.mtc.mvp.ui.comics.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.mvp.utlis.StringUtils;

import java.util.List;

/**
 * @author Lai
 * @time 2018/1/28 16:51
 * @describe 漫画预览集数adapter
 */

public class ChapterAdapter extends CommonAdapter<ComicListDetail.ChaptersBean> {

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    private SparseIntArray mindexMap = new SparseIntArray();

    public int getLastPosition() {
        return lastPosition;
    }

    private int lastPosition = -1;

    public void setIndex(int index) {
        this.index = index;
    }

    private int index;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private OnItemClickListener mItemClickListener;

    public ChapterAdapter(@Nullable List<ComicListDetail.ChaptersBean> data) {
        super(R.layout.comic_item_preview_left, data);
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorEEE));
                if (lastPosition != -1 && lastPosition != position) {
                    View viewByPosition = adapter.getViewByPosition(lastPosition, R.id.tv_name);
                    if (viewByPosition != null)
                        viewByPosition.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
                    ComicListDetail.ChaptersBean item = getItem(position);
                    if (item != null) {
                        index = item.getIndex();
                        mItemClickListener.onItemClick(adapter, view, position);
                    }
                }
                lastPosition = position;
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ComicListDetail.ChaptersBean chaptersBean, int position) {
        baseViewHolder.setText(R.id.tv_name, StringUtils.isNull(chaptersBean.getName()))
                .addOnClickListener(R.id.tv_name);
        if (index == chaptersBean.getIndex()) {
            lastPosition = position;
            baseViewHolder.setBackgroundColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.colorEEE));
        } else if (lastPosition != -1 && lastPosition == position) {
            baseViewHolder.setBackgroundColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.colorEEE));
        } else {
            baseViewHolder.setBackgroundColor(R.id.tv_name, ContextCompat.getColor(mContext, R.color.white));
        }
    }

    /**
     * 更新单选
     *
     * @param currPosition 当前位置
     * @param index        位置索引
     */
    public void updatePosition(int currPosition, int index) {
        View lastView = getViewByPosition(lastPosition, R.id.tv_name);
        if (lastView != null)
            lastView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        this.lastPosition = currPosition;
        this.index = index;
        View currView = getViewByPosition(lastPosition, R.id.tv_name);
        if (currView != null)
            currView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorEEE));
    }


}
