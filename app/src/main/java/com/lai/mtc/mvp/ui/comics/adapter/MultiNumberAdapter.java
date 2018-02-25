package com.lai.mtc.mvp.ui.comics.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListDetail;

import java.util.List;

/**
 * @author Lai
 * @time 2018/1/24 15:24
 * @describe describe
 */

public class MultiNumberAdapter extends BaseQuickAdapter<ComicListDetail.ChaptersBean,BaseViewHolder> {

    public MultiNumberAdapter(int layoutResId, @Nullable List<ComicListDetail.ChaptersBean> data) {
        super(layoutResId, data);

        setMultiTypeDelegate(new MultiTypeDelegate<ComicListDetail.ChaptersBean>() {
            @Override
            protected int getItemType(ComicListDetail.ChaptersBean entity) {
                //根据你的实体类来判断布局类型
                return entity.getItemType();
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(ComicListDetail.ChaptersBean.NORMAL, R.layout.comic_item_detail_list)
                .registerItemType(ComicListDetail.ChaptersBean.BOTTOM, R.layout.comic_item_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ComicListDetail.ChaptersBean item) {

    }
}
