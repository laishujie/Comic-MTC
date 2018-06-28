package com.lai.mtc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.bean.ComicPreView;
import com.lai.mtc.mvp.base.impl.BaseMvpActivity;
import com.lai.mtc.mvp.contract.ComicsPreviewContract;
import com.lai.mtc.mvp.presenter.ComicsPreviewPresenter;
import com.lai.mtc.mvp.utlis.ScreenUtils;
import com.lai.mtc.mvp.utlis.glide.GlideCacheUtil;
import com.lai.mtc.mvp.utlis.glide.ImageUtils;

import butterknife.BindView;

public class TestMainActivity extends BaseMvpActivity<ComicsPreviewPresenter> implements ComicsPreviewContract.View{

    @BindView(R.id.iv_cover)
    ImageView mImageView;
    String url = "http://images.720rs.com/manhuatuku/106/comicdata_ss_ss008081917_1219139525268_1219139531883.jpg";
    @Override
    public void init(Bundle savedInstanceState) {

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] imageWidthHeight = ScreenUtils.getImageWidthHeight(url);
                Log.w("1111","ima");
                //mPresenter.testList(TestMainActivity.this);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageUtils.loadByModule(TestMainActivity.this,url,mImageView,0);
            }
        });

        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlideCacheUtil.getInstance().clearImageAllCache(TestMainActivity.this);
            }
        });
    }

    @Override
    public int getLayoutResId(Bundle savedInstanceState) {
        return R.layout.test_activity;
    }

    @Override
    public View getTitleBar() {
        return null;
    }

    public void showPreview(ComicPreView comicPreView) {

    }


    public void showInfo(ComicListDetail comicListDetail) {

    }

    @Override
    public void showPreview(ComicPreView comicPreView, boolean isRefresh) {

    }

    //@Inject
   // Retrofit mComicApi;



}
