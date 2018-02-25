package com.lai.mtc.comm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.lai.mtc.mvp.utlis.glide.GlideApp;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @author Lai
 * @time 2018/2/19 21:02
 * @describe 监听滚动来对图片加载进行判断处理
 */
public class ImageAutoLoadScrollListener extends RecyclerView.OnScrollListener {
    private Context mContext;

    public ImageAutoLoadScrollListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        switch (newState) {
            case SCROLL_STATE_IDLE: // The RecyclerView is not currently scrolling.
                //当屏幕停止滚动，加载图片
                try {
                    if (mContext != null) GlideApp.with(mContext).resumeRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SCROLL_STATE_DRAGGING: // The RecyclerView is currently being dragged by outside input such as user touch input.
                //当屏幕滚动且用户使用的触碰或手指还在屏幕上，停止加载图片
                try {
                    if (mContext != null) GlideApp.with(mContext).pauseRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SCROLL_STATE_SETTLING: // The RecyclerView is currently animating to a final position while not under outside control.
                //由于用户的操作，屏幕产生惯性滑动，停止加载图片
                try {
                    if (mContext != null) GlideApp.with(mContext).pauseRequests();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
