package com.lai.mtc.di.module;


import com.lai.mtc.api.ComicApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author Lai
 * @time 2017/12/12 15:11
 * @describe 专门提供api仓库
 */
@Module
public class ApiModule {


    @Provides
    @Singleton
    ComicApi provideComicApi(Retrofit retrofit) {
        return retrofit.create(ComicApi.class);
    }
}
