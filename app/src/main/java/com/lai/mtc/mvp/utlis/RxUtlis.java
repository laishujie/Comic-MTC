package com.lai.mtc.mvp.utlis;

import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Lai
 * @time 2017/9/18 17:32
 * @describe describe
 */

public class RxUtlis {
    /**
     * 统一线程处理
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> bind(final Fragment mLifecycleProvider) {
        final LifecycleProvider<ActivityEvent>  lifecycleProvider = (LifecycleProvider<ActivityEvent>) mLifecycleProvider;
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).compose(lifecycleProvider.<T>bindToLifecycle());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> toMain() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public static <T> FlowableTransformer<T, T> toFlowableMain() {

        return new FlowableTransformer<T, T>() {

            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public interface Lifecycle{
        <T> LifecycleTransformer<T> bindToLifecycle();
    }

    public static <T> ObservableTransformer<T, T> handler(final Lifecycle lifecycle) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.compose(RxUtlis.<T>toMain()).compose(lifecycle.<T>bindToLifecycle());
            }
        };
    }





}
