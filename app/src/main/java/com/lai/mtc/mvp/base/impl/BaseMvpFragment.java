package com.lai.mtc.mvp.base.impl;

import android.os.Bundle;

import com.lai.mtc.mvp.base.IPresenter;

import javax.inject.Inject;

/**
 * @author Lai
 * @time 2017/9/1 9:53
 * @describe describe
 */

public abstract class BaseMvpFragment<P extends IPresenter> extends BaseFragment {

    @Inject
    protected P mPresenter;

    @Override
    public void finishCreateView(Bundle state) {
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        init(state);
        bindEvent();
    }

    public abstract void init(Bundle state);

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }

    public void bindEvent() {
    }

}
