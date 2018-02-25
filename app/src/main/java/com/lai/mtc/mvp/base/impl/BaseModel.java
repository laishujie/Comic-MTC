package com.lai.mtc.mvp.base.impl;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.lai.mtc.mvp.base.IModel;

/**
 * @author Lai
 * @time 2017/12/10 16:16
 * @describe describe
 */

public class BaseModel implements IModel, LifecycleObserver {
    @Override
    public void onDestroy() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }
}
