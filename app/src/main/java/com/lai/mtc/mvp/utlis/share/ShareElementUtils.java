package com.lai.mtc.mvp.utlis.share;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.lai.mtc.R;
import com.lai.mtc.comm.widget.BookViewLayout;

/**
 * @author Lai
 * @time 2018/1/22 13:45
 * @describe 共享元素工具类
 */
public class ShareElementUtils {

    public static final String EXTRA_SHARE_ELEMENT_INFO = "share_element_info";
    public static final String TRANSITION_NAME_SHARE = "share";
    public static final String IMAGE_URL = "ImageUrl";

    public final static long ANIMATOR_DURATION = 300;


    private ShareElementUtils() {
    }

    public static ShareElementUtils getInstance() {
        return ShareElementHolder.sInstance;
    }

    private static class ShareElementHolder {
        private static final ShareElementUtils sInstance = new ShareElementUtils();
    }


    public static void startActivity(ImageView imageView, Activity context, Class<?> clazz) {
        ShareElementInfo info = new ShareElementInfo();
        info.convertOriginalInfo(imageView);
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_SHARE_ELEMENT_INFO, info);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }

    public static void startActivity(ImageView imageView, Activity context, Class<?> clazz, String url) {
        ShareElementInfo info = new ShareElementInfo();
        info.convertOriginalInfo(imageView);
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_SHARE_ELEMENT_INFO, info);
        intent.putExtra(IMAGE_URL, url);
        context.startActivity(intent);

        context.overridePendingTransition(0, 0);
    }

    public static void startAnimator(Intent intent, Activity context, BookViewLayout imageView, View rootView) {
        if (intent == null)
            return;
        ShareElementUtils instance = getInstance();
        ShareElementInfo info = intent.getExtras().getParcelable(EXTRA_SHARE_ELEMENT_INFO);
        instance.setBgView(rootView);
        instance.setInfo(info);
        instance.setContext(context);
        instance.convert(imageView).
                setDuration(ANIMATOR_DURATION).setInterpolator(new LinearInterpolator()).startEnterAnimator();
    }

    public static void finishAnimator(BookViewLayout mCoverImageView, final Activity activity,View mBgView) {
        getInstance().startBackgroundAlphaAnimation(mBgView, new ColorDrawable(ContextCompat.getColor(activity, R.color.white)),255,0);
        getInstance().convert(mCoverImageView).setDuration(ANIMATOR_DURATION)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        activity.finish();
                        activity.overridePendingTransition(0, 0);
                    }
                })
                .startExitAnimator();
    }


    private long mDuration = ANIMATOR_DURATION;
    private ShareElementInfo mInfo;
    private AnimatorListenerAdapter mListener;
    private ViewPropertyAnimator mAnimator;
    private ViewPropertyAnimator mFinishAnimator;
    private Interpolator mInterpolator;
    private boolean mEnter;
    private Context mContext;
    private View mBgView;

    public void setInfo(ShareElementInfo info) {
        mInfo = info;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setBgView(View bgView) {
        mBgView = bgView;
    }


    /**
     * convert target ImageView info and if enter animation to init
     *
     * @param tarView the second page share view
     * @return Class self
     */
    public ShareElementUtils convert(final BookViewLayout tarView) {
        if (mInfo == null) {
            throw new NullPointerException("ShareElementInfo must not null");
        }
        tarView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                tarView.getViewTreeObserver().removeOnPreDrawListener(this);
                mInfo.convertTargetInfo(tarView, mContext);
                //init
                if (mEnter) {
                    tarView.setPivotX(mInfo.getPivotX());
                    tarView.setPivotY(mInfo.getPivotY());
                    tarView.setTranslationX(mInfo.getCenterOffsetX());
                    tarView.setTranslationY(mInfo.getCenterOffsetY());
                    tarView.setScaleX(mInfo.getScaleX());
                    tarView.setScaleY(mInfo.getScaleY());
                    mAnimator = tarView.animate();

                   /* ObjectAnimator x = ObjectAnimator.ofFloat(tarView, "translationY", tarView.getTranslationY(), 0f);
                    ObjectAnimator y = ObjectAnimator.ofFloat(tarView, "translationX", tarView.getTranslationX(), 0f);
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(tarView, "scaleX", tarView.getScaleX(), 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(tarView, "scaleY", tarView.getScrollY(), 1f);
                    ObjectAnimator alpha = ObjectAnimator.ofFloat(mBgView,"alpha", 0f,1f);
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(300);
                    if (mListener != null) {
                        animatorSet.addListener(mListener);
                    }
                    animatorSet.playTogether(x,y,scaleX,scaleY,alpha);//依次执行动画
                    animatorSet.start();*/

                    start();
                    startBackgroundAlphaAnimation(mBgView, new ColorDrawable(ContextCompat.getColor(mContext, R.color.white)));
                }
                return true;
            }
        });
        return this;
    }

    public ShareElementUtils setDuration(long duration) {
        mDuration = duration;
        return this;
    }

    public ShareElementUtils setListener(AnimatorListenerAdapter listener) {
        mListener = listener;
        return this;
    }

    public ShareElementUtils setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
        return this;
    }

    private void start() {
        mAnimator.setDuration(mDuration)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .translationX(0)
                .translationY(0);

        if (mListener != null) {
            mAnimator.setListener(mListener);
        }
        if (mInterpolator != null) {
            mAnimator.setInterpolator(mInterpolator);
        }

        mAnimator.start();
    }

    public void startEnterAnimator() {
        mEnter = true;
    }

    public void startExitAnimator() {
        mEnter = false;
        mAnimator.setDuration(mDuration)
                .scaleX(mInfo.getScaleX())
                .scaleY(mInfo.getScaleY())
                .translationX(mInfo.getCenterOffsetX())
                .translationY(mInfo.getCenterOffsetY());
        if (mListener != null) {
            mAnimator.setListener(mListener);
        }
        if (mInterpolator != null) {
            mAnimator.setInterpolator(mInterpolator);
        }
        mAnimator.start();

    }

    private void startBackgroundAlphaAnimation(final View bgView, final ColorDrawable colorDrawable, int... value) {
        if (bgView == null)
            return;
        if (value == null || value.length == 0) {
            value = new int[]{0, 255};
        }
        ObjectAnimator animator = ObjectAnimator.ofInt(colorDrawable, "alpha", value);
        animator.setDuration(ANIMATOR_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgView.setBackgroundDrawable(colorDrawable);
            }
        });
        animator.start();
    }


}
