package com.lai.mtc.mvp.presenter;

import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.SimpleContract;

import javax.inject.Inject;

/**
 * @author Lai
 * @time 2018/1/23 16:37
 * @describe describe
 */

public class SimplePresenter extends BasePresenter<SimpleContract.View> implements SimpleContract.Model {

    @Inject
    SimplePresenter() {

    }
}
