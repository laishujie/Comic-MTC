package com.lai.mtc.mvp.ui.comics.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lai.mtc.R;
import com.lai.mtc.bean.ComicPreView;
import com.lai.mtc.comm.CommonAdapter;
import com.lai.mtc.mvp.utlis.SPUtils;
import com.lai.mtc.mvp.utlis.ScreenUtils;
import com.lai.mtc.mvp.utlis.glide.GlideApp;
import com.lai.mtc.mvp.utlis.glide.GlideRequest;

import java.util.List;

/**
 * @author Lai
 * @time 2018/2/5 21:26
 * @describe describe
 */

public class PreviewAdapter extends CommonAdapter<ComicPreView.PagesBean> {

    //预览模式
    private int module;

    private SparseArray<ComicPreView> mComicPreViewSparseArray = new SparseArray<>();

    public void setComicPreView(ComicPreView comicPreView) {
        mComicPreView = comicPreView;
        if (comicPreView != null) {
            mComicPreView.setPagerSize(comicPreView.getPages().size());
            mComicPreViewSparseArray.put(comicPreView.getIndex(), comicPreView);
        }
    }


    public ComicPreView getComicPreViewByIndex(int index) {
        return mComicPreViewSparseArray.get(index);
    }

    private ComicPreView mComicPreView;

    public PreviewAdapter(int layoutResId, @Nullable List<ComicPreView.PagesBean> data, ComicPreView currComicPreView) {
        super(layoutResId, data);
        setComicPreView(currComicPreView);
        module = SPUtils.getInstance("config").getInt("module", 0);
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, ComicPreView.PagesBean pagesBean, int position) {
        //查看是翻页模式还是往下滑的模式
        final ImageView imageView = baseViewHolder.getView(R.id.iv_cover);
        if (pagesBean.getIndex() == -1)
            pagesBean.setIndex(mComicPreView.getIndex());
        GlideRequest<Bitmap> transition = GlideApp.with(mContext)
                .asBitmap()
                .load(pagesBean.getTrack_url())
                .placeholder(new ColorDrawable(Color.BLACK))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(new GenericTransitionOptions<Bitmap>());
        if (module == 0) {
            RequestListener<Bitmap> requestListener = new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap bitmap, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    if (imageView == null) {
                        return false;
                    }

                    if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                  /*  ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                    float scale = (float) vw / (float) resource.getWidth();
                    int vh = Math.round(resource.getHeight() * scale);
                    params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                    imageView.setLayoutParams(params);*/

                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    float scale = ((float) height) / width;
                    ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                    layoutParams.width = ScreenUtils.getScreenWidth(mContext);
                    layoutParams.height = (int) (scale * ScreenUtils.getScreenWidth(mContext));
                    imageView.setLayoutParams(layoutParams);

                    return false;
                }
            };
            transition.listener(requestListener).into(imageView);
        } else {
            transition.into(imageView);
        }
    }
}
