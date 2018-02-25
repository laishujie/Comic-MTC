package com.lai.mtc.mvp.ui.comics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.comm.widget.BookViewLayout;
import com.lai.mtc.dao.SearchRecordDao;
import com.lai.mtc.mvp.base.impl.BaseMvpActivity;
import com.lai.mtc.mvp.contract.ComicsSearchContract;
import com.lai.mtc.mvp.presenter.ComicsSearchPresenter;
import com.lai.mtc.mvp.ui.comics.dialog.SearchDialog;
import com.lai.mtc.mvp.utlis.glide.ImageUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Lai
 * @time 2018/2/10 17:29
 * @describe 搜索页面
 */

public class ComicSearchActivity extends BaseMvpActivity<ComicsSearchPresenter> implements ComicsSearchContract.View {
    @BindView(R.id.ac_toolbar)
    Toolbar mAcToolbar;

    @Inject
    SearchRecordDao mSearchRecordDao;

    @BindView(R.id.rv_list)
    RecyclerView mRecyclerView;

    @BindView(R.id.ac_title)
    TextView mTvTitle;

    @OnClick(R.id.ac_title)
    public void titleClick() {
        openSearchDialog();
    }

    //当前实体类
    ComicListInfo mComicListInfo;

    CommonAdapter<ComicListInfo.EntriesBean> commonAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void openSearchDialog() {
        SearchDialog searchDialog = new SearchDialog(this, mSearchRecordDao);
        searchDialog.setITextClick(new SearchDialog.ITextClick() {
            @Override
            public void text(String input) {
                mTvTitle.setText(input);
                mPresenter.search(input, 1, true);
            }
        });
        searchDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            openSearchDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void init(Bundle savedInstanceState) {
        setToolBar(mAcToolbar, "点击输入", true);
        mTvTitle.performClick();
    }

    @Override
    protected void bindEvent() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapter = new CommonAdapter<ComicListInfo.EntriesBean>(R.layout.comic_item_search_list, new ArrayList<ComicListInfo.EntriesBean>()) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, final ComicListInfo.EntriesBean entriesBean, int position) {
                BookViewLayout bookViewLayout = baseViewHolder.getView(R.id.activity_des_cover);
                ImageUtils.load(mContext, entriesBean.getCover(), bookViewLayout.getIvCover());
                baseViewHolder.setText(R.id.tv_name, entriesBean.getName());
                String author = entriesBean.getAuthor();
                String tag = "";
                for (String e : entriesBean.getTag_list()) {
                    tag += TextUtils.isEmpty(tag) ? e : "|" + e;
                }
                baseViewHolder.setText(R.id.tv_author, author.concat("\n").concat(tag));
                baseViewHolder.setText(R.id.tv_des_content, entriesBean.getDescription());
                baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ComicSearchActivity.this, ComicListDetailActivity.class);
                        intent.putExtra("info", entriesBean);
                        startActivity(intent);
                    }
                });
            }
        };
        commonAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (mComicListInfo != null && mComicListInfo.getCurrent_page() == mComicListInfo.getTotal_pages()) {
                    commonAdapter.loadMoreEnd();
                } else {
                    if (mComicListInfo != null) {
                        int current_page = mComicListInfo.getCurrent_page() + 1;
                        mPresenter.search(mTvTitle.getText().toString(), current_page, false);
                    } else {
                        commonAdapter.loadMoreFail();
                    }
                }
            }
        }, mRecyclerView);
        mRecyclerView.setAdapter(commonAdapter);
    }

    @Override
    public int getLayoutResId(Bundle savedInstanceState) {
        return R.layout.comic_activity_search;
    }

    @Override
    public View getTitleBar() {
        return mAcToolbar;
    }

    @Override
    public void showList(ComicListInfo comicListInfo) {
        mComicListInfo = comicListInfo;
        commonAdapter.setData(comicListInfo.getCurrent_page() == 1
                , comicListInfo.getEntries());
        commonAdapter.loadMoreComplete();
    }
}
