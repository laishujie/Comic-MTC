package com.lai.mtc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.comm.SpaceItemDecoration;
import com.lai.mtc.comm.widget.BookViewLayout;
import com.lai.mtc.dao.CollectionDao;
import com.lai.mtc.dao.bean.MyCollection;
import com.lai.mtc.dao.bean.MyCollection_;
import com.lai.mtc.mvp.base.impl.BaseActivity;
import com.lai.mtc.mvp.presenter.ComicsPresenter;
import com.lai.mtc.mvp.ui.comics.activity.ComicListDetailActivity;
import com.lai.mtc.mvp.ui.comics.activity.ComicSearchActivity;
import com.lai.mtc.mvp.ui.comics.fragment.ComicsMainFragment;
import com.lai.mtc.mvp.utlis.FragmentUtils;
import com.lai.mtc.mvp.utlis.ListUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.objectbox.android.AndroidScheduler;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscription;
import io.reactivex.functions.Consumer;


/**
 * @author Lai
 * @class MainActivity
 * @time 2018/1/14
 * @describe 主页 因为不需要mvp,所以继承基本的即可。
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //导航根布局。主要是配合layout_scrollFlags让标题滚动
    @BindView(R.id.main_fl)
    AppBarLayout mAppBarLayout;

    //漫画Fragment
    @Inject
    ComicsMainFragment mComicsMainFragment;

    //底部我的书架列表
    @BindView(R.id.rv_list)
    RecyclerView mBottomRecyclerView;

    //底部标题
    @BindView(R.id.design_bottom_sheet)
    View mBottomSheet;

    //root
    @BindView(R.id.cl_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.tl_search)
    Toolbar mSearchToolbar;

    @OnClick(R.id.btn_search)
    public void openSearch() {
        startActivity(new Intent(this, ComicSearchActivity.class));
    }

    @Inject
    CollectionDao mCollectionDao;

    private Fragment currentFragment;

    //设置底部导航 初始化标识
    private boolean isSetBottomSheetHeight;

    MenuItem mMenuItem;

    //设置底部导航是否需要自动隐藏
    private boolean isSetBottomSheetHide = true;

    CommonAdapter<MyCollection> mBottomAdapter;

    @Override
    public int getLayoutResId(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    DataSubscription observer;

    @Override
    public void initData(Bundle savedInstanceState) {
        setToolBar(mToolbar, "", false);
        currentFragment = mComicsMainFragment;
        FragmentUtils.addFragment(getSupportFragmentManager(), currentFragment, R.id.ll_view_container);
        setListener();

        //设置布局样式
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        mBottomRecyclerView.setLayoutManager(layoutManager);

        //调整上下间距
        mBottomRecyclerView.addItemDecoration(new SpaceItemDecoration(3, getResources().getDimensionPixelSize(R.dimen.dp_10), true));

        //底部隐藏书签
        mBottomAdapter = new CommonAdapter<MyCollection>(R.layout.comic_item_bottom2, Collections.<MyCollection>emptyList()) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, MyCollection comicTypeInfo, int position) {
                BookViewLayout bookViewLayout = baseViewHolder.getView(R.id.bl_book1);
                bookViewLayout.setInfo(comicTypeInfo.getCover(), comicTypeInfo.getName(), comicTypeInfo.getDesc());
                baseViewHolder.addOnClickListener(R.id.bl_book1);
            }
        };
        mBottomAdapter.bindToRecyclerView(mBottomRecyclerView);

        //点击监听事件
        mBottomAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MyCollection item = (MyCollection) adapter.getItem(position);
                if (item != null) {
                    ComicListDetailActivity.startActivity(item, MainActivity.this);
                }
            }
        });

        //监听数据的变化自动更新列表
        observer = mCollectionDao.getBox().query().order(MyCollection_.time).build().subscribe().on(AndroidScheduler.mainThread())
                .observer(new DataObserver<List<MyCollection>>() {
                    @Override
                    public void onData(@NonNull List<MyCollection> data) {
                        mBottomAdapter.setData(true, data);
                    }
                });

    }


    @Override
    protected void onDestroy() {
        if (observer != null)
            observer.cancel();
        super.onDestroy();
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //调整底部导航偏移的Y轴 达到底部隐藏效果
                if (isSetBottomSheetHide)
                    mBottomSheet.setTranslationY(-verticalOffset);
                ((ComicsMainFragment) currentFragment).setVerticalOffset(Math.abs(verticalOffset));
                ((ComicsMainFragment) currentFragment).setTotalScrollRange(appBarLayout.getTotalScrollRange());
            }
        });
        //监听AppBarLayout滚动事件
        BottomSheetBehavior.from(mBottomSheet).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    if (ListUtils.isEmpty(mBottomAdapter.getData())) {
                        mBottomAdapter.setData(true, mCollectionDao.getAll());
                    }
                    isSetBottomSheetHide = false;
                    setSearchScrollFlags(false);
                } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    isSetBottomSheetHide = true;
                    setSearchScrollFlags(true);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //真正的窗体完成初始化可见获取焦点可交互。可获得每个控件的宽高
        //只执行一次,并且重新设置底部导航的高度。
        if (!isSetBottomSheetHeight) {
            CoordinatorLayout.LayoutParams linearParams = (CoordinatorLayout.LayoutParams) mBottomSheet.getLayoutParams();
            linearParams.height = mCoordinatorLayout.getHeight() - mAppBarLayout.getHeight();
            mBottomSheet.setLayoutParams(linearParams);
            isSetBottomSheetHeight = true;
        }
    }

    @Override
    public View getTitleBar() {
        return mAppBarLayout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_changes, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_module) {
            mMenuItem = item;
            if ("多列".equals(item.getTitle())) {
                ((ComicsMainFragment) currentFragment).changeMode(ComicsPresenter.SINGLE_MODE, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        mMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_line_weight_black_24dp));
                        mMenuItem.setTitle("单列");
                    }
                });
            } else {
                ((ComicsMainFragment) currentFragment).changeMode(ComicsPresenter.MANY_MODE, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        mMenuItem.setTitle("多列");
                        mMenuItem.setIcon(getResources().getDrawable(R.drawable.ic_dashboard_black_24dp));
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 改变搜索布局的行为 ScrollFlags
     *
     * @param isScroll 是否滚动
     */
    public void setSearchScrollFlags(boolean isScroll) {
        AppBarLayout.LayoutParams mParams = (AppBarLayout.LayoutParams) mSearchToolbar.getLayoutParams();
        if (isScroll) {
            mParams.setScrollFlags(5);
        } else {
            mParams.setScrollFlags(0);
        }
        mSearchToolbar.setLayoutParams(mParams);
    }

}
