package com.lai.mtc.di.module;

import com.lai.mtc.TestMainActivity;
import com.lai.mtc.MainActivity;
import com.lai.mtc.mvp.scope.ActivityScope;
import com.lai.mtc.mvp.ui.comics.activity.ComicListDetailActivity;
import com.lai.mtc.mvp.ui.comics.activity.ComicPreviewActivity;
import com.lai.mtc.mvp.ui.comics.activity.ComicSearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author Lai
 * @time 2017/12/12 16:54
 * @describe Activity 统一注入
 */
@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector(modules = DaoModule.class)
    @ActivityScope
    abstract MainActivity mainActivityInjector();

    @ContributesAndroidInjector
    @ActivityScope
    abstract TestMainActivity bmainActivityInjector();

    @ContributesAndroidInjector(modules = DaoModule.class)
    @ActivityScope
    abstract ComicListDetailActivity comicListDetailActivityInject();

    @ContributesAndroidInjector(modules = DaoModule.class)
    @ActivityScope
    abstract ComicPreviewActivity cmicPreviewActivityInject();

    @ContributesAndroidInjector(modules = DaoModule.class)
    @ActivityScope
    abstract ComicSearchActivity comicSearchActivityActivityInject();
}
