package com.lai.mtc.mvp.base.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.dialog.LoadingDialog;
import com.lai.mtc.mvp.base.IView;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.AndroidInjection;

/**
 * @author Lai
 * @time 2017/9/1 9:53
 * @describe 公共的fragment,不涉及MVP
 */

public abstract class BaseFragment extends RxFragment implements IView {
    private View parentView;
    protected FragmentActivity activity;
    // 标志位 标志已经初始化完成。
    protected boolean isPrepared;
    //标志位 fragment是否可见
    protected boolean isVisible;

    //共同对话框
    protected LoadingDialog mLoadingDialog;

    private Unbinder bind;

    @LayoutRes
    public abstract int getLayoutResId();

    /**
     * 这个方法是关于Fragment完成创建的过程中，进行界面填充的方法,该方法返回的是一个view对象
     * 在这个对象中封装的就是Fragment对应的布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        parentView = inflater.inflate(getLayoutResId(), container, false);
        activity = getActivity();
        return parentView;
    }

    /**
     * 这个方法当onCreateView方法中的view创建完成之后，执行
     * 在inflate完成view的创建之后，可以将对应view中的各个控件进行查找findViewById
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
    }

    /**
     * 这个方法是在Fragment完成创建操作之后，进行数据填充操作的时候执行的方法
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        finishCreateView(savedInstanceState);
    }

    /**
     * 初始化views
     *
     * @param state
     */
    public abstract void finishCreateView(Bundle state);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bind != null)
            bind.unbind();
    }

    @Override
    public void onAttach(Activity activity) {
        AndroidInjection.inject(activity);
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        this.activity = null;
    }

    /**
     * Fragment数据的懒加载
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * fragment显示时才加载数据
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * fragment懒加载方法
     */
    protected void lazyLoad() {
    }

    /**
     * fragment隐藏
     */
    protected void onInvisible() {
    }

    @Override
    public void showLoading() {
        show(LoadingDialog.NORMAL);
    }

    public void show(@LoadingDialog.MODE int mode) {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = new LoadingDialog(activity);
        mLoadingDialog.setCurrMode(mode);
        mLoadingDialog.show();
    }

    @Override
    public void showLoading(int mode) {
        show(mode);
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setToolBar(Toolbar toolBar, String title, boolean needBackButton) {

    }

    @Override
    public void handleError(ApiException e) {
        hideLoading();
        showMessage(e.getMsg());
        e.printStackTrace();
    }
}
