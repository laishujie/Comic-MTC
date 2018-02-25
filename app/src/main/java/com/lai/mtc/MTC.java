package com.lai.mtc;

import android.app.Activity;
import android.app.Application;

import com.lai.mtc.dao.bean.MyObjectBox;
import com.lai.mtc.di.component.DaggerAppComponent;
import com.lai.mtc.mvp.http.RetrofitHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.objectbox.BoxStore;

/**
 * @author Lai
 * @time 2017/12/12 16:21
 * @describe describe
 */

public class MTC extends Application implements HasActivityInjector {

    public static MTC getApp() {
        return mMTC;
    }

    public static MTC mMTC;

    private static BoxStore boxStore;

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    public static BoxStore getBoxStore() {
        return boxStore;
    }

    public static void setBoxStore(BoxStore boxStore) {
        MTC.boxStore = boxStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMTC = this;
        setBoxStore(MyObjectBox.builder().androidContext(this).build());
        DaggerAppComponent.builder().application(this).baseUrl(RetrofitHelper.BASE_URL).build().inject(this);
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }
}
