package com.lai.mtc.mvp.ui.comics.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lai.library.ButtonStyle;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.comm.SpaceItemDecoration;
import com.lai.mtc.comm.widget.BookViewLayout;
import com.lai.mtc.dao.CollectionDao;
import com.lai.mtc.dao.RecordDao;
import com.lai.mtc.dao.bean.MyCollection;
import com.lai.mtc.dao.bean.Record;
import com.lai.mtc.mvp.base.impl.BaseMvpActivity;
import com.lai.mtc.mvp.contract.ComicsListDetailContract;
import com.lai.mtc.mvp.presenter.ComicsListDetailPresenter;
import com.lai.mtc.mvp.ui.comics.adapter.ChapterDetailAdapter;
import com.lai.mtc.mvp.utlis.CommonUtil;
import com.lai.mtc.mvp.utlis.IPopMenu;
import com.lai.mtc.mvp.utlis.ListUtils;
import com.lai.mtc.mvp.utlis.PopupMenuUtil;
import com.lai.mtc.mvp.utlis.RxUtlis;
import com.lai.mtc.mvp.utlis.StringUtils;
import com.lai.mtc.mvp.utlis.ViewUtils;
import com.lai.mtc.mvp.utlis.glide.ImageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.util.ArrayListSupplier;

/**
 * @author Lai
 * @time 2018/1/20 23:06
 * @describe 漫画详情集数展示
 */

public class ComicListDetailActivity extends BaseMvpActivity<ComicsListDetailPresenter> implements ComicsListDetailContract.View {
    //封面
    @BindView(R.id.activity_des_cover)
    BookViewLayout mCoverImageView;

    //CoordinatorLayout
    @BindView(R.id.cl_layout)
    View mBgView;

    //标题栏
    @BindView(R.id.ac_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.ac_appBar)
    AppBarLayout mAppBarLayout;
    //标题
    @BindView(R.id.tv_name)
    TextView mTvTitle;
    //作者
    @BindView(R.id.tv_author)
    TextView mTvAuthor;
    //line分割线
    @BindView(R.id.activity_desc_line)
    View mActivityDescLine;
    //描述
    @BindView(R.id.tv_des_content)
    TextView mTvDesContent;
    //集数列表
    @BindView(R.id.rv_list)
    RecyclerView mRvList;

    //数据源
    View dataSource;
    ButtonStyle mTvCollection;

    @BindView(R.id.btn_find)
    ButtonStyle mFindButtonStyle;
    //适配器
    ChapterDetailAdapter commonAdapter;
    //列表实体
    ComicListDetail mComicListDetail;
    //数据库操作类
    //实体类
    ComicListInfo.EntriesBean entriesBean;
    @Inject
    CollectionDao mCollectionDao;

    private int comicId;

    @Inject
    RecordDao mRecordDao;

    Record mRecord;

    ComicListDetail.ChaptersBean mChaptersBean;

    @Override
    public int getLayoutResId(Bundle savedInstanceState) {
        return R.layout.comic_activity_list_detail;
    }

    @OnClick(R.id.iv_reverse)
    public void reverse() {
        if (commonAdapter != null) {
            Collections.reverse(commonAdapter.getData());
            if (commonAdapter != null) {
               // commonAdapter.setIndex(mRecord.getIndex());
                commonAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.btn_find)
    public void preview() {
        if (mComicListDetail != null) {
            Intent intent = new Intent(this, ComicPreviewActivity.class);
            intent.putExtra("comicListDetail", mComicListDetail);
            if (mRecord != null) {
                intent.putExtra("index", mRecord.getIndex());
            } else {
                if (mChaptersBean != null) {
                    intent.putExtra("index", mChaptersBean.getIndex());
                    mRecordDao.updateRecord(mComicListDetail.getId(), mChaptersBean.getName(), mChaptersBean.getIndex(), 0);
                }
            }
            startActivity(intent);
        }
    }


    public static void startActivity(MyCollection myCollection, Context context) {
        ComicListInfo.EntriesBean entriesBean = new ComicListInfo.EntriesBean();
        entriesBean.setId((int) myCollection.getId());
        entriesBean.setAuthor(myCollection.getAuthor());
        entriesBean.setName(myCollection.getName());
        entriesBean.setCover(myCollection.getCover());
        entriesBean.setStatus(myCollection.getStatus());
        entriesBean.setDescription(myCollection.getDesc());
        Intent intent = new Intent(context, ComicListDetailActivity.class);
        intent.putExtra("info", entriesBean);
        context.startActivity(intent);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        entriesBean = (ComicListInfo.EntriesBean) getIntent().getSerializableExtra("info");

        init(entriesBean);

        ViewUtils.setTitleBarByTop(mAppBarLayout, this);

        setToolBar(mToolbar, getTitle().toString(), true);
    }

    @Override
    protected void bindEvent() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int toolbarHeight = appBarLayout.getTotalScrollRange();
                int dy = Math.abs(verticalOffset);
                if (dy <= toolbarHeight) {
                    float scale = (float) dy / toolbarHeight;
                    float alpha = scale * 255;
                    mToolbar.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                }
            }
        });
        //设置布局样式
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        mRvList.setLayoutManager(layoutManager);
        //调整上下间距
        mRvList.addItemDecoration(new SpaceItemDecoration(4, getResources().getDimensionPixelSize(R.dimen.dp_10), true));
        commonAdapter = new ChapterDetailAdapter(Collections.<ComicListDetail.ChaptersBean>emptyList(), mRecordDao);
        commonAdapter.bindToRecyclerView(mRvList);
        commonAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                List<ComicListDetail.ChaptersBean> chaptersBeans = CommonUtil.transformationLastList(mComicListDetail.getChapters());
                if (chaptersBeans != null)
                    commonAdapter.setData(false, chaptersBeans);
                commonAdapter.loadMoreEnd();
            }
        }, mRvList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //返回的时候刷新当前看到哪一集
        updateCurrRecordInfo();
    }

    /**
     * 更新当前列表记录
     */
    public void updateCurrRecordInfo() {
        mRecord = mRecordDao.getById(comicId);
        if (mRecord != null) {
            mFindButtonStyle.setText("继续: ".concat(StringUtils.isNull(mRecord.getName())));
            if (commonAdapter != null)
                commonAdapter.updatePosition(mRecord.getPosition(), mRecord.getIndex());
        } else {
            mFindButtonStyle.setText(getString(R.string.preview));
            if (commonAdapter != null) {
                commonAdapter.init();
                commonAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //ShareElementUtils.finishAnimator(mCoverImageView, this,mBgView);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.action_like_source, menu);
        dataSource = menu.findItem(R.id.action_chang).getActionView();
        dataSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mComicListDetail != null && !ListUtils.isEmpty(mComicListDetail.getMirrors())) {
                    List<ComicListDetail.MirrorsBean> mirrors = mComicListDetail.getMirrors();
                    PopupMenuUtil.showPopupMenuList(ComicListDetailActivity.this, new ArrayList<IPopMenu>(mirrors), dataSource, new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            showMessage(item.getTitle().toString());
                            mPresenter.getComicById(item.getItemId());
                            return true;
                        }
                    });
                }
            }
        });
        mTvCollection = (ButtonStyle) menu.findItem(R.id.action_chang2).getActionView();
        if (entriesBean != null && mCollectionDao.isCollection(entriesBean.getId())) {
            mTvCollection.setText("已收藏");
            mTvCollection.setTextColor(ContextCompat.getColor(ComicListDetailActivity.this, R.color.colorPrimary));
        }
        mTvCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCollectionDao.isCollection(entriesBean.getId())) {
                    mCollectionDao.delete(entriesBean.getId());
                    mTvCollection.setText("收藏");
                    mTvCollection.setTextColor(ContextCompat.getColor(ComicListDetailActivity.this, R.color.color7979));
                } else {
                    mCollectionDao.addCollection(entriesBean);
                    mTvCollection.setText("已收藏");
                    mTvCollection.setTextColor(ContextCompat.getColor(ComicListDetailActivity.this, R.color.colorPrimary));
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View getTitleBar() {
        return mToolbar;
    }

    public void init(ComicListInfo.EntriesBean info) {
        ImageUtils.load(this, info.getCover(), mCoverImageView.getIvCover());
        String name = StringUtils.isNull(info.getName());
        mTvTitle.setText(name);
        setToolBar(mToolbar, name, true);
        mTvAuthor.setText(StringUtils.isNull(info.getAuthor()));
        mTvDesContent.setText(StringUtils.isNull(info.getDescription()));
        comicId = info.getId();
        //请求网络获取级数
        mPresenter.getComicById(info.getId());
    }

    @Override
    public void showDetail(ComicListDetail comicListDetail) {
        mComicListDetail = comicListDetail;
        //裁剪前面的第52个。太多 加载会很慢
        List<ComicListDetail.ChaptersBean> chaptersBeans = CommonUtil.transformationStartList(comicListDetail.getChapters());
        commonAdapter.setData(true, chaptersBeans);
        commonAdapter.setComicListDetail(comicListDetail);

        if (!ListUtils.isEmpty(chaptersBeans)) {
            mChaptersBean = chaptersBeans.get(0);

            //更当前列表记录
            comicId = mChaptersBean.getComic_id();
        }

        updateCurrRecordInfo();
    }


    @Override
    public void reverse(ComicListDetail comicListDetail) {
        mComicListDetail = comicListDetail;

        List<ComicListDetail.ChaptersBean> showChapters = comicListDetail.getShowChapters();
        List<ComicListDetail.ChaptersBean> chapters = !ListUtils.isEmpty(showChapters) ? showChapters : comicListDetail.getChapters();
        if (commonAdapter != null) {
            commonAdapter.init();
            commonAdapter.setData(true, chapters);
        }
    }
}
