package com.lai.mtc.mvp.base.impl;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.lai.mtc.comm.ExceptionEngine;
import com.lai.mtc.mvp.base.IPresenter;
import com.lai.mtc.mvp.base.IView;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author Lai
 * @time 2017/12/10 16:32
 * @describe describe
 */

public class BasePresenter<V extends IView> implements IPresenter<V>, LifecycleObserver {

    //Model数据
    // protected M mModel;
    //View 显示回显的接口
    protected V mRootView;

    public void setMethodObservable(Observable observable) {
        this.methodObservable = observable;
    }

    private Observable methodObservable;

    public BasePresenter() {
        onStart();
    }

    @Override
    public void onStart() {
        //mRootView一定是activity的实现类。所有通过getLifecycle().addObserver(this);方法可以将activity的生命周期监听
        if (mRootView != null && mRootView instanceof LifecycleOwner) {
            ((LifecycleOwner) mRootView).getLifecycle().addObserver(this);
        }
    }

    //监听activity的销毁事件,当他销毁的时候会自动调用该方法取消生命周期的订阅
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public void onDestroy() {
        this.mRootView = null;
    }

    @Override
    public void attachView(V view) {
        this.mRootView = view;
    }

    @Override
    public V getView() {
        return mRootView;
    }

    @Override
    public <P> Observable<P> getCurrMethod() {
        return methodObservable;
    }

    public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
            //打印具体错误
            return Observable.error(ExceptionEngine.handleException(throwable));
        }
    }


}
