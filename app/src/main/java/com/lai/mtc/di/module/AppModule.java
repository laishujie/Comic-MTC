package com.lai.mtc.di.module;

import android.app.Application;

import com.lai.mtc.MTC;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lai
 * @time 2017/12/11 11:43
 * @describe 将会提供Application 的context引用
 */
@Module
public class AppModule {
    @Provides
    @Singleton
    Application provideContext(MTC application) {
        return application;
    }
}
