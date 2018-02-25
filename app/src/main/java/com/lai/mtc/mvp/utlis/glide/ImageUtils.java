package com.lai.mtc.mvp.utlis.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.lai.mtc.R;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * @author Lai
 * @time 2018/1/17 15:11
 * @describe describe
 */

public class ImageUtils {
    /**
     * 默认的加载方式。如有其他需求自己定吧-v-
     *
     * @param context
     * @param url
     * @param imageView
     */
    public static void load(Context context, String url, ImageView imageView) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.color.colorEEE)
                .error(R.drawable.img_cover_default)
                .centerCrop()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into(imageView);
    }

    public static void load(Context context, String url, SimpleTarget<Drawable> target) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.mipmap.ic_place_holder)
                .error(R.drawable.img_cover_default)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into(target);
    }

    public static void loadByModule(Context context, final String imageUrl, final ImageView imageView, int module) {
        GlideRequest<Bitmap> transition = GlideApp.with(context)
                .asBitmap()
                .load(imageUrl)
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
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    if (imageView == null) {
                        return false;
                    }
                    if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                    float scale = (float) vw / (float) resource.getWidth();
                    int vh = Math.round(resource.getHeight() * scale);
                    params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                    imageView.setLayoutParams(params);
                    return false;
                }
            };
            transition.listener(requestListener).into(imageView);
        } else {
            transition.into(imageView);
        }
    }

    // public static void

    public static void loadN(Context context, final String imageUrl, final ImageView imageView) {
        GlideApp.with(context)
                .asBitmap()
                .load(imageUrl)
                .placeholder(new ColorDrawable(Color.BLACK))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getWidth();
                        int vh = Math.round(resource.getHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .transition(new GenericTransitionOptions<Bitmap>())
                .into(imageView);
    }
}
