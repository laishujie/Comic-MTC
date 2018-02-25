package com.lai.mtc.mvp.ui.comics.adapter;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.library.ButtonStyle;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.dao.RecordDao;
import com.lai.mtc.mvp.ui.comics.activity.ComicPreviewActivity;
import com.lai.mtc.mvp.utlis.StringUtils;

import java.util.List;

/**
 * @author Lai
 * @time 2018/1/28 16:51
 * @describe 漫画详情集数adapter
 */

public class ChapterDetailAdapter extends CommonAdapter<ComicListDetail.ChaptersBean> {

    private int lastPosition;
    private int index;
    private RecordDao mRecordDao;

    public void setComicListDetail(ComicListDetail comicListDetail) {
        mComicListDetail = comicListDetail;
    }

    private ComicListDetail mComicListDetail;

    public ChapterDetailAdapter(@Nullable List<ComicListDetail.ChaptersBean> data, RecordDao mDao) {
        super(R.layout.comic_item_detail_list, data);
        init();
        mRecordDao = mDao;
        updateOnItemChildClickListener();
    }

    public void updateOnItemChildClickListener() {
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ButtonStyle buttonStyle = (ButtonStyle) adapter.getViewByPosition(position, R.id.list_item);
                if (buttonStyle != null)
                    buttonStyle.setNormalColor(R.color.colorEEE);

                if (lastPosition != -1 && lastPosition != position) {
                    ButtonStyle viewByPosition = (ButtonStyle) adapter.getViewByPosition(lastPosition, R.id.list_item);
                    if (viewByPosition != null)
                        viewByPosition.setNormalColor(R.color.white);
                }
                lastPosition = position;
                ComicListDetail.ChaptersBean item = getItem(position);
                if (item != null) {
                    mRecordDao.updateRecord(item.getComic_id(), item.getName(), item.getIndex(), position);
                    Intent intent = new Intent(mContext, ComicPreviewActivity.class);
                    intent.putExtra("index", item.getIndex());
                    intent.putExtra("comicListDetail", mComicListDetail);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void init() {
        lastPosition = -1;
        index = -1;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ComicListDetail.ChaptersBean chaptersBean, int position) {
        baseViewHolder.addOnClickListener(R.id.list_item);
        ButtonStyle buttonStyle = baseViewHolder.getView(R.id.list_item);
        if (lastPosition != -1 && lastPosition == position) {
            buttonStyle.setNormalColor(R.color.colorEEE);
        } else if (index == chaptersBean.getIndex()) {
            lastPosition = position;
            buttonStyle.setNormalColor(R.color.colorEEE);
        } else {
            buttonStyle.setNormalColor(R.color.white);
        }
        buttonStyle.setText(StringUtils.isNull(chaptersBean.getName()));
    }

    /**
     * 更新单选
     *
     * @param index 位置索引
     */
    public void updatePosition(int currPosition, int index) {
        ButtonStyle lastView = (ButtonStyle) getViewByPosition(lastPosition, R.id.list_item);
        if (lastView != null)
            lastView.setNormalColor(R.color.white);
        this.index = index;
        lastPosition = -1;
        notifyItemChanged(currPosition);
        if (currPosition != 0) {
            notifyItemChanged(currPosition - 1);
        }
    }


}
