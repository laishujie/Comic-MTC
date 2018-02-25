package com.lai.mtc.di.module;

import com.lai.mtc.dao.CollectionDao;
import com.lai.mtc.dao.RecordDao;
import com.lai.mtc.dao.SearchRecordDao;
import com.lai.mtc.mvp.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author Lai
 * @time 2018/2/1 19:43
 * @describe 数据库管理仓库
 */
@Module
public class DaoModule {

    @Provides
    @ActivityScope
    CollectionDao provideCollectionDao() {
        return new CollectionDao();
    }

    @Provides
    @ActivityScope
    RecordDao provideRecordDao() {
        return new RecordDao();
    }

    @Provides
    @ActivityScope
    SearchRecordDao provideSearchRecordDao() {
        return new SearchRecordDao();
    }
}
