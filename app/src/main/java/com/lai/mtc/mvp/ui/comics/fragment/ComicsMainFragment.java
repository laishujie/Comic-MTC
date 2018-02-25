package com.lai.mtc.mvp.ui.comics.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.comm.ImageAutoLoadScrollListener;
import com.lai.mtc.comm.SpaceItemDecoration;
import com.lai.mtc.mvp.base.impl.BaseMvpFragment;
import com.lai.mtc.mvp.contract.ComicsContract;
import com.lai.mtc.mvp.presenter.ComicsPresenter;
import com.lai.mtc.mvp.ui.comics.activity.ComicListDetailActivity;
import com.lai.mtc.mvp.utlis.ListUtils;
import com.lai.mtc.mvp.utlis.StringUtils;
import com.lai.mtc.mvp.utlis.glide.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * @author Lai
 * @time 2018/1/14 15:44
 * @describe 主页漫画fragment
 */

public class ComicsMainFragment extends BaseMvpFragment<ComicsPresenter> implements ComicsContract.View {

    @BindView(R.id.rv_list)
    RecyclerView mOutRecyclerView;

    //默认为单列随机展示模式
    @ComicsPresenter.MODE
    public int currMode = ComicsPresenter.SINGLE_MODE;

    CommonAdapter<ComicListInfo> mOutAdapter;

    public void setTotalScrollRange(int totalScrollRange) {
        TotalScrollRange = totalScrollRange;
    }

    public void setVerticalOffset(int verticalOffset) {
        this.verticalOffset = verticalOffset;
    }

    private int TotalScrollRange;
    private int verticalOffset;
    Consumer<Boolean> mMenuCallBack;

    RecyclerView.RecycledViewPool mInnerRecycledViewPool;

    @Inject
    public ComicsMainFragment() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_comics;
    }

    //设置首页的列表展示样式
    public void changeMode(@ComicsPresenter.MODE int currMode, Consumer<Boolean> isOk) {
        this.mMenuCallBack = isOk;
        this.currMode = currMode;
        mPresenter.requestHome(currMode);
    }

    @Override
    public void init(Bundle state) {
        //请求网络
        mPresenter.requestHome(currMode);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setRecycleChildrenOnDetach(true);
        //linearLayoutManager.setExtraSpace(2);
        //最外层的mRecyclerView
        mOutRecyclerView.setLayoutManager(linearLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mOutRecyclerView.setHasFixedSize(true);
        mOutRecyclerView.setItemViewCacheSize(4);
    }


    @Override
    public void bindEvent() {
        mOutRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                changeNestedScrollingEnabled(recyclerView, dy);
            }
        });
        mInnerRecycledViewPool = new RecyclerView.RecycledViewPool();
        mOutAdapter = new CommonAdapter<ComicListInfo>(R.layout.comic_item_main_list, new ArrayList<ComicListInfo>()) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, ComicListInfo comicTypeInfo, final int position) {
                baseViewHolder.setText(R.id.tv_title, StringUtils.isNull(comicTypeInfo.getTitle()));
                List<ComicListInfo.EntriesBean> showList = comicTypeInfo.getShowList();
                //如果展示的数据为空则拿原始的数据
                if (ListUtils.isEmpty(showList))
                    showList = comicTypeInfo.getEntries();
                //里面这层mRecyclerView
                RecyclerView mRecyclerView = baseViewHolder.getView(R.id.rv_list);

                //刷新避免重复新建立
                if (mRecyclerView.getAdapter() == null) {
                    CommonAdapter<ComicListInfo.EntriesBean>
                            mInnerAdapter = new CommonAdapter<ComicListInfo.EntriesBean>(R.layout.comic_item_list2, showList) {
                        @Override
                        protected void convert(BaseViewHolder baseViewHolder, ComicListInfo.EntriesBean comicTypeInfo, final int position) {
                            String tag = "";
                            for (String s : comicTypeInfo.getTag_list()) {
                                tag += TextUtils.isEmpty(tag) ? s : " ".concat(s);
                            }
                            baseViewHolder.setText(R.id.tv_title, StringUtils.isNull(comicTypeInfo.getName()));
                            baseViewHolder.setText(R.id.tv_tag, tag);
                            ImageUtils.load(getContext(), StringUtils.isNull(comicTypeInfo.getCover()), (ImageView) baseViewHolder.getView(R.id.book_layout));
                            baseViewHolder.addOnClickListener(R.id.book_layout);
                        }
                    };

                    //设置adapter'
                    mInnerAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            Intent intent = new Intent(getContext(), ComicListDetailActivity.class);
                            intent.putExtra("info", (ComicListInfo.EntriesBean) adapter.getItem(position));
                            startActivity(intent);
                        }
                    });
                    //解决嵌套RecyclerView自动滚动问题
                    mRecyclerView.setFocusableInTouchMode(false);
                    mRecyclerView.addOnScrollListener(new ImageAutoLoadScrollListener(mRecyclerView.getContext()));
                    mRecyclerView.requestFocus();
                    mRecyclerView.setHasFixedSize(true);
                    //设置布局样式
                    GridLayoutManager layoutManager = new GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false);
                    layoutManager.setRecycleChildrenOnDetach(true);
                    mRecyclerView.setLayoutManager(layoutManager);
                    //内部的RecyclerView使用同一个RecycledViewPool。切记一定要半段是否为空不能重复设置否则数据错乱
                    if (mRecyclerView.getRecycledViewPool() == null)
                        mRecyclerView.setRecycledViewPool(mInnerRecycledViewPool);
                    //调整上下间距
                    mRecyclerView.addItemDecoration(new SpaceItemDecoration(3, activity.getResources().
                            getDimensionPixelSize(R.dimen.dp_10), true));
                    mInnerAdapter.bindToRecyclerView(mRecyclerView);
                    baseViewHolder.addOnClickListener(R.id.btn_change);
                } else {
                    //更新数据
                    CommonAdapter<ComicListInfo.EntriesBean> adapter = (CommonAdapter<ComicListInfo.EntriesBean>) mRecyclerView.getAdapter();
                    adapter.setData(true, showList);
                }
            }
        };
        mOutAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ComicListInfo comicListInfo = (ComicListInfo) adapter.getItem(position);
                if (comicListInfo != null) {
                    if (comicListInfo.getCurrent_page() != comicListInfo.getTotal_pages())
                        mPresenter.change(currMode, comicListInfo.getCurrent_page() + 1, position, comicListInfo.getId()
                                , comicListInfo.getTitle());
                    else
                        showMessage("没有了");
                }
            }
        });
        mOutAdapter.bindToRecyclerView(mOutRecyclerView);
    }

    public void changeNestedScrollingEnabled(RecyclerView recyclerView, int dy) {
        // dy < 0 时为手指向下滚动,
        if (dy < 0) {
            if (verticalOffset == 0) {
                setNestedScrollingEnabled(recyclerView, false);
                return;
            }
            setNestedScrollingEnabled(recyclerView, true);
            //dy > 0 时为手指向上滚动
        } else if (dy > 0) {
            if (TotalScrollRange == verticalOffset)
                setNestedScrollingEnabled(recyclerView, false);
            else if (verticalOffset == 0)
                setNestedScrollingEnabled(recyclerView, true);
        }
    }

    public void setNestedScrollingEnabled(RecyclerView recyclerView, boolean isIntercept) {
        if (isIntercept != recyclerView.isNestedScrollingEnabled()) {
            recyclerView.setNestedScrollingEnabled(isIntercept);
        }
    }


    @Override
    public void showRequestHome(final List<ComicListInfo> mInfoList) {
        if (mMenuCallBack != null) {
            try {
                mMenuCallBack.accept(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mOutAdapter.setData(true, mInfoList);
    }

    @Override
    public void showChange(ComicListInfo mInfoList, int position) {
        if (currMode == ComicsPresenter.SINGLE_MODE) {
            mOutRecyclerView.scrollToPosition(0);
        }
        mOutAdapter.getData().set(position, mInfoList);
        mOutAdapter.notifyItemChanged(position);
    }


}
