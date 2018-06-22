package com.lai.mtc.mvp.ui.comics.activity;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.bean.ComicPreView;
import com.lai.mtc.comm.widget.MyPagerSnapHelper;
import com.lai.mtc.comm.widget.PreCacheLayoutManager;
import com.lai.mtc.comm.widget.TouchRecyclerView;
import com.lai.mtc.dao.RecordDao;
import com.lai.mtc.mvp.base.impl.BaseMvpActivity;
import com.lai.mtc.mvp.contract.ComicsPreviewContract;
import com.lai.mtc.mvp.presenter.ComicsPreviewPresenter;
import com.lai.mtc.mvp.ui.comics.adapter.ChapterAdapter;
import com.lai.mtc.mvp.ui.comics.adapter.PreviewAdapter;
import com.lai.mtc.mvp.ui.comics.dialog.ModuleDialog;
import com.lai.mtc.mvp.ui.comics.pop.WindowLightPop;
import com.lai.mtc.mvp.utlis.ListUtils;
import com.lai.mtc.mvp.utlis.RxUtlis;
import com.lai.mtc.mvp.utlis.SPUtils;
import com.lai.mtc.mvp.utlis.ScreenUtils;
import com.lai.mtc.mvp.utlis.ViewUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * @author Lai
 * @time 2018/1/24 22:20
 * @describe 漫画详情预览activity
 */

public class ComicPreviewActivity extends BaseMvpActivity<ComicsPreviewPresenter> implements ComicsPreviewContract.View {
    //列表
    @BindView(R.id.rv_list)
    TouchRecyclerView mRvList;

    //底部菜单
    @BindView(R.id.ll_bottom)
    LinearLayout mBottomLinearLayout;

    //左边集数菜单
    @BindView(R.id.ll_left_layout)
    LinearLayout mLeftLinearLayout;

    //左边集数列表
    @BindView(R.id.rv_left_list)
    RecyclerView mRvLeftList;

    //标题
    @BindView(R.id.ac_toolbar)
    Toolbar mToolbar;

    //root
    @BindView(R.id.fl_layout)
    FrameLayout mFrameLayout;

    //进度条
    @BindView(R.id.sb_bar)
    SeekBar mSeekBar;

    //集数适配器
    ChapterAdapter mLeftChaptersAdapter;

    //预览适配器
    PreviewAdapter mPreviewAdapter;

    //漫画列表详情实体
    ComicListDetail mComicListDetail;
    //当前在屏幕可见的实体类
    ComicPreView firstVisibleItem;
    //当前集数标识
    private int index;
    //下一话的位置
    private int nextPosition;

    //RecyclerView帮助类。主要是变成Viewpager的模式需要
    PagerSnapHelper mPagerSnapHelper;

    @BindView(R.id.tv_curr_pager)
    TextView mTvCurrPager;

    @Inject
    RecordDao mRecordDao;

    boolean isUpdate;

    @Override
    public int getLayoutResId(Bundle savedInstanceState) {
        return R.layout.comic_activity_preview;
    }

    /**
     * 底部菜单点击事件
     *
     * @param view 当前点击View
     */
    @OnClick({R.id.tv_menu, R.id.tv_brightness, R.id.tv_switch_screen, R.id.tv_switch_module})
    public void menu(View view) {
        switch (view.getId()) {
            case R.id.tv_menu:
                switchLeftMenu();
                break;
            case R.id.tv_brightness:
                openLightPop();
                break;
            case R.id.tv_switch_screen:
                ScreenUtils.switchScreen(this);
                break;
            case R.id.tv_switch_module:
                openModuleDialog();
                break;
        }
    }

    private void openModuleDialog() {
        switchBAndTMenu();
        ModuleDialog moduleDialog = new ModuleDialog(this);
        moduleDialog.show();
        moduleDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initModule(SPUtils.getInstance("config").getInt("module", 0));
                request(index);
            }
        });
    }


    private void openLightPop() {
        switchBAndTMenu();
        WindowLightPop windowLightPop = new WindowLightPop(this);
        windowLightPop.showAtLocation(mFrameLayout, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        mComicListDetail = (ComicListDetail) getIntent().getSerializableExtra("comicListDetail");
        index = getIntent().getIntExtra("index", 1);
        hideLayout();
        if (mComicListDetail != null) {
            setToolBar(mToolbar, mComicListDetail.getName(), true);
            showInfo(mComicListDetail);
            request(index);
        }
    }

    /**
     * 根据标识去请求集数图片
     *
     * @param index 标识
     */
    public void request(int index) {
        this.index = index;
        if (mComicListDetail != null)
            mPresenter.requestPreview(mComicListDetail.getId(), index, this);
    }

    @Override
    protected void bindEvent() {
        //为了更好的提高滚动的流畅性，可以加大 RecyclerView 的缓存，用空间换时间
        mRvList.setHasFixedSize(true);
        mRvList.setItemViewCacheSize(10);
        mRvList.setDrawingCacheEnabled(true);
        mRvList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        initModule(SPUtils.getInstance("config").getInt("module", 0));
        mRvList.setITouchCallBack(new TouchRecyclerView.ITouchCallBack() {
            @Override
            public void click() {
                if (!returnAllStatus()) {
                    if (mBottomLinearLayout.getTranslationY() == 0)
                        switchLeftMenu();
                    else
                        switchBAndTMenu();
                }
            }
        });
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                //当屏幕停止滚动，
                if (newState == SCROLL_STATE_IDLE) {
                    //更新当前对象在更新信息
                    updateCurrIndex(updateCurrObject(recyclerView));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (isUpdate) {
                    updateCurrIndex(updateCurrObject(recyclerView));
                    isUpdate = false;
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //先更新当前图片所在的对象
                updateCurrObject(mRvList);
                int progress = seekBar.getProgress() <= 0 ? 0 : seekBar.getProgress() - 1;
                ComicPreView.PagesBean pagesBean = firstVisibleItem.getPages().get(progress);
                if (pagesBean != null) {
                    int i = mPreviewAdapter.getData().indexOf(pagesBean);
                    if (i != -1) {
                        mRvList.scrollToPosition(i);
                        isUpdate = true;
                    }
                }
            }
        });
    }

    /**
     * 更新当前图片所在的对象
     *
     * @param recyclerView re
     * @return 当前位置
     */
    public int updateCurrObject(RecyclerView recyclerView) {
        LinearLayoutManager l = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = l.findFirstVisibleItemPosition();
        ComicPreView.PagesBean item = mPreviewAdapter.getItem(firstVisibleItemPosition);
        if (item != null) {
            ComicPreView comicPreViewByIndex = mPreviewAdapter.getComicPreViewByIndex(item.getIndex());
            if (comicPreViewByIndex != null)
                firstVisibleItem = comicPreViewByIndex;
        }
        return firstVisibleItemPosition;
    }

    /**
     * 更新当前右下角最新集数以及更新seekBar进度
     *
     * @param currPosition 根据当前的位置更新信息
     */
    public void updateCurrIndex(int currPosition) {
        if (firstVisibleItem != null) {
            int process = firstVisibleItem.getPages().indexOf(mPreviewAdapter.getItem(currPosition)) + 1;
            if (firstVisibleItem != null) {
                int max = firstVisibleItem.getPagerSize();
                mSeekBar.setMax(max);
                mSeekBar.setProgress(process);
                String format = String.format(getString(R.string.current_set_number), firstVisibleItem.getName(), process, max);
                mTvCurrPager.setText(format);
            }
        }
    }

    /**
     * 设置TranslationY,X隐藏底部/顶部布局/左边菜单
     */
    public void hideLayout() {
        mBottomLinearLayout.setTranslationY(ViewUtils.getViewMeasuredHeight(mBottomLinearLayout));
        mToolbar.setTranslationY(-ViewUtils.getViewMeasuredHeight(mToolbar));
        mLeftLinearLayout.setTranslationX(-ScreenUtils.getScreenWidth());
    }

    /**
     * 顶部和底部菜单切换
     */
    private void switchBAndTMenu() {
        if (mBottomLinearLayout.getTranslationY() != 0) {
            ViewCompat.animate(mBottomLinearLayout).translationY(0).setDuration(300);
            ViewCompat.animate(mToolbar).translationY(0).setDuration(300);
        } else {
            ViewCompat.animate(mBottomLinearLayout).translationY(mBottomLinearLayout.getHeight()).setDuration(300);
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setDuration(300);
        }
    }

    /**
     * 左边菜单切换
     */
    public void switchLeftMenu() {
        switchBAndTMenu();
        if (mLeftLinearLayout.getTranslationX() != 0) {
            ViewCompat.animate(mLeftLinearLayout).translationX(0).setDuration(300);
        } else {
            ViewCompat.animate(mLeftLinearLayout).translationX(-mLeftLinearLayout.getWidth()).setDuration(300);
        }
    }

    /**
     * 启动动画隐藏所有的菜单
     *
     * @return 是否
     */
    private boolean returnAllStatus() {
        boolean isReturn = false;
        if (mLeftLinearLayout.getTranslationX() == 0 && mLeftLinearLayout.getVisibility() == View.VISIBLE) {
            ViewCompat.animate(mLeftLinearLayout).translationX(-mLeftLinearLayout.getWidth()).setDuration(300);
            isReturn = true;
        }
        if (mBottomLinearLayout.getTranslationY() == 0) {
            ViewCompat.animate(mBottomLinearLayout).translationY(mBottomLinearLayout.getHeight()).setDuration(300);
            ViewCompat.animate(mToolbar).translationY(-mToolbar.getHeight()).setDuration(300);
            isReturn = true;
        }
        return isReturn;
    }

    @Override
    public View getTitleBar() {
        return mToolbar;
    }

    @Override
    public void showPreview(ComicPreView comicPreView) {
        final int module = SPUtils.getInstance("config").getInt("module", 0);
        //设置设置的预览模式
        //用哪个布局
        int layoutRes = module == 0 ? R.layout.comic_item_preview : R.layout.comic_item_preview2;
        //
        List<ComicPreView.PagesBean> pages = comicPreView.getPages();
        if (mPreviewAdapter == null) {
            mPreviewAdapter = new PreviewAdapter(layoutRes, pages, comicPreView);
            firstVisibleItem = comicPreView;
            int size = pages.size();
            String format = String.format(getString(R.string.current_set_number), comicPreView.getName(), 1, size);
            mTvCurrPager.setText(format);
            mSeekBar.setMax(size);
            mPreviewAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    //这里可以提前加载下一话
                    int lastPosition = mLeftChaptersAdapter.getLastPosition();
                    List<ComicListDetail.ChaptersBean> data = mLeftChaptersAdapter.getData();
                    nextPosition = lastPosition + 1;
                    if ((nextPosition == data.size())) {
                        //已经是最后一话不在自动加载
                        mPreviewAdapter.loadMoreEnd();
                    } else {
                        ComicListDetail.ChaptersBean item = mLeftChaptersAdapter.getItem(nextPosition);
                        if (item != null) {
                            request(item.getIndex());
                        }
                    }
                }
            }, mRvList);
            mRvList.setAdapter(mPreviewAdapter);
        } else {
            //更新记录
            mPreviewAdapter.setComicPreView(comicPreView);
            mPreviewAdapter.setData(false, pages);
            //更新左边菜单
            mLeftChaptersAdapter.updatePosition(nextPosition, index);
            mPreviewAdapter.loadMoreComplete();
        }
    }

    /**
     * 初始化观看模式
     *
     * @param module 模式
     */
    private void initModule(int module) {
        PreCacheLayoutManager linearLayoutManager = new PreCacheLayoutManager(this);
        linearLayoutManager.setExtraSpace(2);
        //默认的模式
        if (module == 0) {
            mRvList.setLayoutManager(linearLayoutManager);
            if (mPagerSnapHelper != null)
                mPagerSnapHelper.attachToRecyclerView(null);
        } else {
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mRvList.setLayoutManager(linearLayoutManager);
            mPagerSnapHelper = new MyPagerSnapHelper(linearLayoutManager.getExtraSpace());
            mPagerSnapHelper.attachToRecyclerView(mRvList);
        }
        mPreviewAdapter = null;
    }

    public void showInfo(ComicListDetail comicListDetail) {
        if (comicListDetail == null || comicListDetail.getChapters() == null || mLeftChaptersAdapter != null)
            return;
        mRvLeftList.setLayoutManager(new LinearLayoutManager(this));
        List<ComicListDetail.ChaptersBean> showChapters = comicListDetail.getShowChapters();
        List<ComicListDetail.ChaptersBean> chapters = !ListUtils.isEmpty(showChapters) ? showChapters : comicListDetail.getChapters();
        mLeftChaptersAdapter = new ChapterAdapter(chapters);
        mRvLeftList.setAdapter(mLeftChaptersAdapter);
        mLeftChaptersAdapter.setIndex(index);
        mLeftChaptersAdapter.bindToRecyclerView(mRvLeftList);
        mLeftChaptersAdapter.setItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                returnAllStatus();
                mPreviewAdapter = null;
                ComicListDetail.ChaptersBean o = (ComicListDetail.ChaptersBean) adapter.getData().get(position);
                index = o.getIndex();
                request(index);
            }
        });
        //如果展示的集合不为空，说明还有集数没加载完
        if (!ListUtils.isEmpty(showChapters))
            mLeftChaptersAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    Observable.timer(1, TimeUnit.SECONDS)
                            .compose(ComicPreviewActivity.this.<Long>bindToLifecycle())
                            .compose(RxUtlis.<Long>toMain())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    mLeftChaptersAdapter.setData(false, mComicListDetail.getLastChapters());
                                    mLeftChaptersAdapter.loadMoreEnd();
                                }
                            });
                }
            }, mRvList);
    }

    @Override
    public void onBackPressed() {
        if (!returnAllStatus())
            super.onBackPressed();
    }

    @Override
    public void finish() {
        //更新当前数据库记录
        if (mComicListDetail != null && firstVisibleItem != null) {
            mRecordDao.updateRecord(
                    mComicListDetail.getId(),
                    firstVisibleItem.getName()
                    , firstVisibleItem.getIndex()
                    , mLeftChaptersAdapter.getLastPosition());
        }
        super.finish();
    }
}
