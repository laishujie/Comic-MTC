package com.lai.mtc.mvp.base.impl;

import android.os.Bundle;

import com.lai.mtc.mvp.base.IPresenter;
import com.lai.mtc.mvp.base.IView;

import javax.inject.Inject;

/**
 * @author Lai
 * @time 2017/12/10 17:18
 * @describe MVP Activity
 */

public abstract class BaseMvpActivity<P extends IPresenter> extends BaseActivity implements IView {
    //实例化P.桥梁，辅助activity和model之间的交互
    @Inject
    protected P mPresenter;


    @Override
    public void initData(Bundle savedInstanceState) {
        //和View绑定
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        init(savedInstanceState);
        bindEvent();
    }

    protected void bindEvent(){

    }

    public abstract void init(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        //把所有的数据销毁掉
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }

}
