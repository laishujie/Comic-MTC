package com.lai.mtc.di.module;

import android.app.Application;
import android.os.Environment;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lai.mtc.MTC;
import com.lai.mtc.mvp.utlis.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Lai
 * @time 2017/12/11 11:44
 * @describe 专门提供网络相关
 */
@Module
public class NetModule {


    @Provides
    @Singleton
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }


    @Provides
    @Singleton
    CommonInterceptor provideCommonInterceptor() {
        return new CommonInterceptor();
    }


    @Provides
    @Singleton
    CacheInterceptor provideCacheInterceptor(MTC context) {
        return new CacheInterceptor(context);
    }

    /**
     * 添加公共参数
     */
    public class CommonInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();
            // 添加新的参数
            HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
                    .addQueryParameter("package_name", "com.vjson.anime")
                    .addQueryParameter("version_code", "87")
                    .addQueryParameter("version_name", "1.0.8.7")
                    .addQueryParameter("channel", "coolapk")
                    .addQueryParameter("sign", "dcf692dc1d4cead44ce1d5d1b9409e26")
                    .addQueryParameter("platform", "android");
            // 新的请求
            Request newRequest = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(authorizedUrlBuilder.build())
                    .build();

            return chain.proceed(newRequest);
        }
    }

    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    public class CacheInterceptor implements Interceptor {
        Application mContext;

        CacheInterceptor(Application context) {
            this.mContext = context;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            // 有网络时 设置缓存超时时间1个小时
            int maxAge = 60 * 60;
            // 无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();
            if (CommonUtil.isNetworkAvailable(mContext)) {
                //有网络时只从网络获取
                request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
            } else {
                //无网络时只从缓存中读取
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response response = chain.proceed(request);
            if (CommonUtil.isNetworkAvailable(mContext)) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }


    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, CommonInterceptor commonInterceptor, CacheInterceptor cacheInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cache(new Cache(new File(MTC.getApp().getFilesDir(), "HttpCache"), 1024 * 1024 * 10)).
                addInterceptor(httpLoggingInterceptor)
                .addInterceptor(commonInterceptor)
                .addNetworkInterceptor(cacheInterceptor).retryOnConnectionFailure(true)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Gson gson, OkHttpClient mOkHttpClient, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(mOkHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
