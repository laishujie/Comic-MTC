package com.lai.mtc.mvp.presenter;

import android.annotation.SuppressLint;
import android.support.annotation.IntDef;

import com.lai.mtc.api.ComicApi;
import com.lai.mtc.bean.ComicCategories;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.HttpRxObserver;
import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.ComicsContract;
import com.lai.mtc.mvp.utlis.ListUtils;
import com.lai.mtc.mvp.utlis.RxUtlis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import retrofit2.Response;

/**
 * @author Lai
 * @time 2017/12/11 17:04
 * @describe 漫画业务桥梁P
 * @see SimplePresenter
 */

public class ComicsPresenter extends BasePresenter<ComicsContract.View> implements ComicsContract.Model {

    private ComicApi mComicApi;

    //单列模式
    public static final int SINGLE_MODE = 1;
    //多列模式
    public static final int MANY_MODE = 2;

    @IntDef({SINGLE_MODE, MANY_MODE})
    public @interface MODE {
    }

    @Inject
    ComicsPresenter(ComicApi comicApi) {
        mComicApi = comicApi;
    }

    //列表ITEM
    private List<ComicListInfo> mInfoList;

    /**
     * 首页请求
     *
     * @param currMode 当前单列还是多列
     */
    @SuppressLint("CheckResult")
    @Override
    public void requestHome(@MODE final int currMode) {
        mRootView.showLoading();
        //2秒后请求,看一会加载动画 -v-!!!
        Observable.timer(2, TimeUnit.SECONDS)
                .compose(mRootView.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        if (currMode == SINGLE_MODE) {
                            getHome();
                        } else {
                            getRandomComic();
                        }
                    }
                });
    }

    /**
     * 换一批
     *
     * @param module   当前是单列还是多列
     * @param pager    当前页面
     * @param position 当前位置
     * @param id       id
     * @param name     标题
     */
    @Override
    public void change(int module, int pager, final int position, int id, String name) {
        mRootView.showLoading();
        Observable<Response<ComicListInfo>> allComic;
        if (module == SINGLE_MODE) {
            allComic = mComicApi.getAllComic(pager);
        } else {
            allComic = mComicApi.getTypeComicsByResponseId(id, pager, name);
        }

        allComic.compose(RxUtlis.<Response<ComicListInfo>>toMain())
                .doOnNext(new Consumer<Response<ComicListInfo>>() {
                    @Override
                    public void accept(Response<ComicListInfo> comicListInfoResponse) {
                        setShowInfo(comicListInfoResponse);
                    }
                })
                .compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                .onErrorResumeNext(new HttpResultFunction<Response<ComicListInfo>>())
                .subscribe(new HttpRxObserver<>(new HttpRxObserver.IResult<Response<ComicListInfo>>() {
                    @Override
                    public void onSuccess(Response<ComicListInfo> comicListInfo) {
                        mRootView.hideLoading();
                        mRootView.showChange(comicListInfo.body(), position);
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.hideLoading();
                        mRootView.handleError(e);
                    }
                }));
    }


    /**
     * 首页请求
     */
    private void getHome() {
        mComicApi.getAllComic(1)
                .compose(RxUtlis.<Response<ComicListInfo>>toMain())
                .compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                .onErrorResumeNext(new HttpResultFunction<Response<ComicListInfo>>())
                .subscribe(new HttpRxObserver<>(new HttpRxObserver.IResult<Response<ComicListInfo>>() {
                    @Override
                    public void onSuccess(Response<ComicListInfo> comicListInfoResponse) {
                        ComicListInfo comicListInfo = comicListInfoResponse.body();
                        if (comicListInfo != null) {
                            comicListInfo.setTitle("随机推荐");
                            mRootView.hideLoading();
                            ArrayList<ComicListInfo> comicListInfos = new ArrayList<>();
                            comicListInfos.add(comicListInfo);
                            mRootView.showRequestHome(comicListInfos);
                        }
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.handleError(e);
                        mRootView.hideLoading();
                    }
                }));
    }


    /**
     * 多个分类请求
     */
    private void getRandomComic() {
        mInfoList = new ArrayList<>();
        // 热血2 冒险5 魔幻41 神鬼11 搞笑30 萌系7 治愈 校园10 爱情9 科幻16 魔法6 格斗26 武侠39 机战23 战争28 竞技1
        // 生活8 励志33 历史22 伪娘17 耽美15 百合25 后宫18 治愈13 美食24 悬疑12 恐怖38 四格31
        //TODO: 逻辑：1选取4个分类
        //TODO       2在每个分类中随机选6个作品并设置分类名称。
        //TODO       3合并数据
        List<ComicCategories> news = new ArrayList<>();
        news.add(new ComicCategories(2, "热血"));
        news.add(new ComicCategories(5, "冒险"));
        news.add(new ComicCategories(41, "魔幻"));
        news.add(new ComicCategories(7, "萌系"));

        Observable.fromIterable(news)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .compose(mRootView.<ComicCategories>bindToLifecycle())
                //将分类转换为 请求漫画列表
                .flatMap(transformationComicListInfo())
                //绑定生命周期
                .compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                //拼接每个Item条目的信息
                .doOnNext(new Consumer<Response<ComicListInfo>>() {
                    @Override
                    public void accept(Response<ComicListInfo> comicListInfoResponse) {
                        setShowInfo(comicListInfoResponse);
                    }
                })
                .compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                //错误统一处理
                .onErrorResumeNext(new HttpResultFunction<Response<ComicListInfo>>())
                .observeOn(AndroidSchedulers.mainThread())
                //所有循环结束后返回数据
                .doOnComplete(new Action() {
                    @Override
                    public void run() {
                        mRootView.hideLoading();
                        mRootView.showRequestHome(mInfoList);
                    }
                })
                .compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                //回调
                .subscribe(new HttpRxObserver<>(new HttpRxObserver.IResult<Response<ComicListInfo>>() {
                    @Override
                    public void onSuccess(Response<ComicListInfo> comicListInfoResponse) {
                        mInfoList.add(comicListInfoResponse.body());
                    }

                    @Override
                    public void onError(ApiException e) {
                        e.printStackTrace();
                        mRootView.handleError(e);
                    }
                }));
    }

    /**
     * 根据分类转换为 请求漫画列表
     *
     * @return 漫画列表
     */
    private Function<ComicCategories, ObservableSource<Response<ComicListInfo>>> transformationComicListInfo() {
        return new Function<ComicCategories, ObservableSource<Response<ComicListInfo>>>() {
            @Override
            public ObservableSource<Response<ComicListInfo>> apply(ComicCategories comicCategories) {
                //分类请求漫画列表
                return mComicApi.getTypeComicsByResponseId(comicCategories.getId(), 1, comicCategories.getName());
            }
        };
    }

    /**
     * 拼接每个Item条目的信息
     *
     * @param comicListInfoResponse ComicListInfo
     */
    private void setShowInfo(Response<ComicListInfo> comicListInfoResponse) {
        HttpUrl url = comicListInfoResponse.raw().request().url();
        String id = String.valueOf(url.queryParameter("q[tags_id_eq]"));
        String type = String.valueOf(url.queryParameter("name"));
        ComicListInfo body = comicListInfoResponse.body();
        if (body != null) {
            if (!"null".equals(id)) {
                body.setId(Integer.valueOf(id));
                body.setTitle(type);
                List<ComicListInfo.EntriesBean> entries = body.getEntries();
                if (!ListUtils.isEmpty(entries)) {
                    //将漫画作品中 选取六个作品 进行展示
                    List<ComicListInfo.EntriesBean> mShow = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        mShow.add(entries.get(i));
                    }
                    body.setShowList(mShow);
                }
            } else {
                body.setTitle("随便推荐");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mComicApi = null;
    }
}
