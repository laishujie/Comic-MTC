package com.lai.mtc.mvp.presenter;

import com.lai.mtc.api.ComicApi;
import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.HttpRxObserver;
import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.ComicsListDetailContract;
import com.lai.mtc.mvp.utlis.ListUtils;
import com.lai.mtc.mvp.utlis.RxUtlis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Lai
 * @time 2017/12/11 17:04
 * @describe 漫画详情业务桥梁P
 *
 * @see  SimplePresenter
 */
public class ComicsListDetailPresenter extends BasePresenter<ComicsListDetailContract.View> implements ComicsListDetailContract.Model {

    private ComicApi mComicApi;

    @Inject
    ComicsListDetailPresenter(ComicApi comicApi) {
        mComicApi = comicApi;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mComicApi = null;
    }

    @Override
    public void getComicById(final int id) {
        mRootView.showLoading();
        Observable.timer(1, TimeUnit.SECONDS).compose(mRootView.<Long>bindToLifecycle()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                mComicApi.getComicById(id).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation())
                        .compose(mRootView.<ComicListDetail>bindToLifecycle()).map(new Function<ComicListDetail, ComicListDetail>() {
                    @Override
                    public ComicListDetail apply(ComicListDetail comicListDetail) throws Exception {
                        if (comicListDetail != null && !ListUtils.isEmpty(comicListDetail.getChapters())) {
                            List<ComicListDetail.ChaptersBean> mirrors = comicListDetail.getChapters();
                            if (mirrors.size() > 52) {
                                comicListDetail.setShowChapters(new ArrayList<>(mirrors.subList(0, 52)));
                                comicListDetail.setLastChapters(new ArrayList<>(mirrors.subList(52, mirrors.size())));
                            }
                        }
                        return comicListDetail;
                    }
                }).compose(mRootView.<ComicListDetail>bindToLifecycle())
                        .onErrorResumeNext(new HttpResultFunction<ComicListDetail>())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new HttpRxObserver<ComicListDetail>(new HttpRxObserver.IResult<ComicListDetail>() {
                    @Override
                    public void onSuccess(ComicListDetail comicListDetail) {
                        mRootView.showDetail(comicListDetail);
                        mRootView.hideLoading();
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.handleError(e);
                    }
                }));
            }
        });
    }

    @Override
    public void reverse(final ComicListDetail comicListDetail) {
        mRootView.showLoading();
        Observable.create(new ObservableOnSubscribe<ComicListDetail>() {
            @Override
            public void subscribe(ObservableEmitter<ComicListDetail> e) throws Exception {
                if (comicListDetail != null && !ListUtils.isEmpty(comicListDetail.getChapters())) {
                    Collections.reverse(comicListDetail.getChapters());
                    List<ComicListDetail.ChaptersBean> mirrors = comicListDetail.getChapters();
                    if (mirrors.size() > 52) {
                        comicListDetail.setShowChapters(new ArrayList<>(mirrors.subList(0, 52)));
                        comicListDetail.setLastChapters(new ArrayList<>(mirrors.subList(52, mirrors.size())));
                    }
                }
                e.onNext(comicListDetail);
            }
        }).compose(RxUtlis.<ComicListDetail>toMain())
                .onErrorResumeNext(new HttpResultFunction<ComicListDetail>())
                .subscribe(new HttpRxObserver<ComicListDetail>(new HttpRxObserver.IResult<ComicListDetail>() {
                    @Override
                    public void onSuccess(ComicListDetail comicListDetail) {
                        mRootView.hideLoading();
                        mRootView.reverse(comicListDetail);
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.handleError(e);
                    }
                }));
    }
}
