package com.lai.mtc.di.component;

import com.lai.mtc.MTC;
import com.lai.mtc.di.module.ApiModule;
import com.lai.mtc.di.module.AppModule;
import com.lai.mtc.di.module.BuildersModule;
import com.lai.mtc.di.module.NetModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author Lai
 * @time 2017/12/12 16:52
 * @describe describe
 */
@Component(modules = {
        AppModule.class,
        BuildersModule.class,
        NetModule.class,
        ApiModule.class,
        AndroidSupportInjectionModule.class
})
@Singleton
public interface AppComponent extends AndroidInjector<MTC> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(MTC application);

        @BindsInstance
        Builder baseUrl(String url);

        AppComponent build();
    }

    void inject(MTC app);
}
