package com.lai.mtc.mvp.presenter;

import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.SimpleContract;

import javax.inject.Inject;

/**
 * @author Lai
 * @time 2018/1/23 16:37
 * @describe
 *
 *      如果不懂可以先看看我的关于MVp的文章：
 *
 *      http://blog.csdn.net/a8688555/article/details/79383270
 *
 *      项目当架构
 *
 *      关于P层，按照规范来说这里应该还需要实例化一个Model层。通过Model在请求请求或处理数据
 *
 *    然后Model的实体类可以通过dagger2去管理实例会添加很多的仓库Module。因为有很多的model仅仅只是做网络请求的操作,不太复杂
 *
 *    我觉得写的东西太多了，所以我这边的就直接省去了M层。直接在P层实现了M的方法。
 *
 *    如果你举得需要Model层
 *
 *    你可以参考一下MVPArms开源项目： https://github.com/JessYanCoding/MVPArms
 *
 *    MVPArms项目实例
 *    https://github.com/GitLqr/LQRBiliBlili
 *
 */

public class SimplePresenter extends BasePresenter<SimpleContract.View> implements SimpleContract.Model {

    @Inject
    SimplePresenter() {

    }
}
