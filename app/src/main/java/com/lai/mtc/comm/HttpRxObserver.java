package com.lai.mtc.comm;


import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author Lai
 * @time 2017/9/19 11:23
 * @describe describe
 */

public class HttpRxObserver<T> implements Observer<T> {

    IResult<T> mIResult;

    public HttpRxObserver(IResult<T> IResult) {
        mIResult = IResult;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        //mIResult.onStart();
    }

    @Override
    public void onNext(@NonNull T t) {
        mIResult.onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        if (e instanceof ApiException) {
            mIResult.onError((ApiException) e);
        } else {
            mIResult.onError(new ApiException(e, ExceptionEngine.UN_KNOWN_ERROR));
        }
    }

    @Override
    public void onComplete() {

    }

    public interface IResult<T> {
        void onSuccess(T t);

        void onError(ApiException e);
    }
}
