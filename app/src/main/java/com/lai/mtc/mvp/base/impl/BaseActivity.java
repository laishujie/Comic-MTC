package com.lai.mtc.mvp.base.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lai.mtc.R;
import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.dialog.LoadingDialog;
import com.lai.mtc.mvp.base.IView;
import com.lai.mtc.mvp.utlis.ViewUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;

/**
 * @author Lai
 * @time 2017/12/10 17:18
 * @describe 公共的Activity，不涉及MVP
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IView {
    //解除绑定控件
    private Unbinder mUnbinder;

    //共同对话框
    protected LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //dagger2统一注册需要的对象
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        ViewUtils.setImmersionStateMode(this);
        try {
            int layoutResID = getLayoutResId(savedInstanceState);
            if (layoutResID != 0) {
                setContentView(layoutResID);
                //和View绑定
                mUnbinder = ButterKnife.bind(this);

                //子类返回指定的标题导航，为标题setPadding.避免遮挡状态栏
                ViewUtils.setTitleBarByTop(getTitleBar(), this);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }


    @Override
    protected void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public abstract int getLayoutResId(Bundle savedInstanceState);

    public abstract void initData(Bundle savedInstanceState);

    //public abstract void inject(BaseComponent component);


    @Override
    public void showLoading() {
        show(LoadingDialog.NORMAL);
        //Log.w("11111","showLoading");
    }

    public void show(@LoadingDialog.MODE int mode) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog(this);
        mLoadingDialog.setCurrMode(mode);
        mLoadingDialog.show();
    }

    @Override
    public void showLoading(int mode) {
        show(mode);
    }

    @Override
    public void handleError(ApiException e) {
        e.printStackTrace();
        hideLoading();
        showMessage(e.getMsg());
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        //Log.w("11111","hideLoading");
    }

    public abstract View getTitleBar();


    @Override
    public void setToolBar(Toolbar toolBar, String title, boolean needBackButton) {
        setSupportActionBar(toolBar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(needBackButton);
            supportActionBar.setDisplayShowHomeEnabled(needBackButton);
            supportActionBar.setTitle("");
        }
        TextView tvTitle = toolBar.findViewById(R.id.ac_title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (needBackButton) {
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
