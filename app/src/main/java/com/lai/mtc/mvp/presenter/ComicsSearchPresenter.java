package com.lai.mtc.mvp.presenter;

import com.lai.mtc.api.ComicApi;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.HttpRxObserver;
import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.ComicsSearchContract;
import com.lai.mtc.mvp.utlis.RxUtlis;

import javax.inject.Inject;

/**
 * @author Lai
 * @time 2018/1/23 16:37
 * @describe describe
 */

public class ComicsSearchPresenter extends BasePresenter<ComicsSearchContract.View> implements ComicsSearchContract.Model {

    private ComicApi mComicApi;

    @Inject
    ComicsSearchPresenter(ComicApi mComicApi) {
        this.mComicApi = mComicApi;
    }

    @Override
    public void search(String name, int pager, final boolean isFresh) {
        if (isFresh) {
            pager = 1;//下拉刷新默认只请求第一页
            mRootView.showLoading();
        }

        mComicApi.getComicsByName(name, pager)
                .compose(mRootView.<ComicListInfo>bindToLifecycle())
                .compose(RxUtlis.<ComicListInfo>toMain())
                .onErrorResumeNext(new HttpResultFunction<ComicListInfo>())
                .subscribe(new HttpRxObserver<>(new HttpRxObserver.IResult<ComicListInfo>() {
                    @Override
                    public void onSuccess(ComicListInfo comicListInfo) {
                        if (isFresh)
                            mRootView.hideLoading();
                        mRootView.showList(comicListInfo);
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.handleError(e);
                    }
                }));
    }
}
