package com.lai.mtc.mvp.presenter;

import android.support.annotation.IntDef;

import com.google.gson.Gson;
import com.lai.mtc.api.ComicApi;
import com.lai.mtc.bean.ComicCategories;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.HttpRxObserver;
import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.ComicsContract;
import com.lai.mtc.mvp.http.JsonTest;
import com.lai.mtc.mvp.utlis.ListUtils;
import com.lai.mtc.mvp.utlis.RxUtlis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
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

    private List<ComicListInfo> mInfoList;

    @Override
    //@RequestLoading
    public void requestHome(@MODE final int currMode) {
        mRootView.showLoading();
        //2秒后请求,看一会加载动画 -v-!!!
        Observable.timer(2, TimeUnit.SECONDS)
                .compose(mRootView.<Long>bindToLifecycle())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if (currMode == SINGLE_MODE) {
                            testData();
                        } else {
                            getRandomComic();
                        }
                    }
                });
        //提供单列与多列
        // mRootView.showLoading();

        /*Observable.timer(3, TimeUnit.SECONDS).compose(mRootView.<Long>bindToLifecycle()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                int st = SPUtils.getInstance().getInt("page");
                st = st == -1 ? 1 : st;
                int page = new Random().nextInt(st);
                Log.w("11111", "生成的随机页面" + page);
                mComicApi.getAllComic(page).compose(RxUtlis.<ComicListInfo>toMain()).compose(mRootView.<ComicListInfo>bindToLifecycle())
                        .subscribe(new Consumer<ComicListInfo>() {
                            @Override
                            public void accept(ComicListInfo comicListInfo) throws Exception {
                                mRootView.hideLoading();
                                ArrayList<ComicListInfo> comicListInfos = new ArrayList<>();
                                comicListInfos.add(comicListInfo);
                                mRootView.showRequestHome(comicListInfos);
                                SPUtils.getInstance().put("page", comicListInfo.getTotal_pages());
                            }
                        });

            }
        });*/
    }

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
                    public void accept(Response<ComicListInfo> comicListInfoResponse) throws Exception {
                        setShowInfo(comicListInfoResponse);
                    }
                }).compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                .onErrorResumeNext(new HttpResultFunction<Response<ComicListInfo>>())
                .subscribe(new HttpRxObserver<>(new HttpRxObserver.IResult<Response<ComicListInfo>>() {
                    @Override
                    public void onSuccess(Response<ComicListInfo> comicListInfo) {
                        mRootView.hideLoading();
                        mRootView.showChange(comicListInfo.body(), position);
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.handleError(e);
                    }
                }));
    }


    private void testData() {
        Observable.create(new ObservableOnSubscribe<ComicListInfo>() {
            @Override
            public void subscribe(ObservableEmitter<ComicListInfo> e) throws Exception {
                Gson gson = new Gson();
                ComicListInfo comicListInfo = gson.fromJson(JsonTest.home, ComicListInfo.class);
                e.onNext(comicListInfo);
            }
        }).compose(RxUtlis.<ComicListInfo>toMain()).subscribe(new Consumer<ComicListInfo>() {
            @Override
            public void accept(ComicListInfo comicListInfo) throws Exception {
                comicListInfo.setTitle("随机推荐");
                mRootView.hideLoading();
                ArrayList<ComicListInfo> comicListInfos = new ArrayList<>();
                comicListInfos.add(comicListInfo);
                mRootView.showRequestHome(comicListInfos);
            }
        });
    }

    public void request(ComicCategories comicCategories) {
        mComicApi.getTypeComicsById(comicCategories.getId(), 1);
    }

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

        Observable.fromIterable(news).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io()).compose(mRootView.<ComicCategories>bindToLifecycle())
                .flatMap(new Function<ComicCategories, ObservableSource<Response<ComicListInfo>>>() {
                    @Override
                    public ObservableSource<Response<ComicListInfo>> apply(ComicCategories comicCategories) throws Exception {
                        return mComicApi.getTypeComicsByResponseId(comicCategories.getId(), 1, comicCategories.getName());
                    }
                }).compose(mRootView.<Response<ComicListInfo>>bindToLifecycle()).doOnNext(new Consumer<Response<ComicListInfo>>() {
            @Override
            public void accept(Response<ComicListInfo> comicListInfoResponse) throws Exception {
                setShowInfo(comicListInfoResponse);
            }
        }).compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
                .onErrorResumeNext(new HttpResultFunction<Response<ComicListInfo>>())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //TODO 所有循环结束后返回数据
                        mRootView.hideLoading();
                        mRootView.showRequestHome(mInfoList);
                    }
                })
                .compose(mRootView.<Response<ComicListInfo>>bindToLifecycle())
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
