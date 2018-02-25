package com.lai.mtc.comm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lai.mtc.R;
import com.lai.mtc.mvp.utlis.StringUtils;
import com.lai.mtc.mvp.utlis.glide.ImageUtils;


/**
 * @author Lai
 * @time 2017/11/24 16:57
 * @describe describe
 */

public class BookViewLayout extends FrameLayout {
    Context mContext;
    BookView mBookViewCover;
    BookView mBookViewContent;
    ImageView mIvCover1;

    public ImageView getIvCover() {
        return mIvCover2;
    }

    ImageView mIvCover2;
    TextView mTvTag;
    TextView mTVTitle;
    TextView mTopTitle;

    int type;

    public BookViewLayout(@NonNull Context context) {
        this(context, null);
    }

    public BookViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BookViewLayout);
            type = typedArray.getInt(R.styleable.BookViewLayout_bookViewLayout_type, -1);
            typedArray.recycle();
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.book_layout_item, this, true);
        mBookViewCover = findViewById(R.id.book);
        mBookViewContent = findViewById(R.id.book2);
        mIvCover1 = findViewById(R.id.iv_cover);
        mIvCover2 = findViewById(R.id.iv_cover2);
        mTvTag = findViewById(R.id.tv_tag);
        mTVTitle = findViewById(R.id.tv_title);
        mTopTitle = findViewById(R.id.tv_top_title);

        if (type == 1) {
            mTopTitle.setVisibility(View.VISIBLE);
            mTvTag.setVisibility(View.GONE);
            mTVTitle.setVisibility(View.GONE);
        } else if (type == 2) {
            mTopTitle.setVisibility(View.GONE);
            mTvTag.setVisibility(View.GONE);
            mTVTitle.setVisibility(View.GONE);
        }

        /*mBookViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBookViewCover.setPivotY(0);
                mBookViewCover.setPivotX(0);
                final float cardElevation = mBookViewCover.getCardElevation();
                //
                ViewCompat.animate(mBookViewCover).rotationY(-180).setDuration(500).setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        mBookViewCover.setCardElevation(0);
                        mBookViewContent.setCardElevation(0);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        mBookViewCover.setCardElevation(cardElevation);
                        mBookViewContent.setCardElevation(cardElevation);
                        //ViewCompat.animate(mView).alpha(1).setDuration(300);
                    }
                });
            }
        });*/
    }


    public void setInfo(final String url, String title, String des) {
        /*GlideImageManager.load(mContext, url, new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
               // mIvCover1.setImageBitmap(resource);
                mIvCover2.setImageBitmap(resource);
            }
        });*/
        ImageUtils.load(mContext, url, mIvCover2);
        mTvTag.setText(StringUtils.isNull(des));
        mTVTitle.setText(StringUtils.isNull(title));
        mTopTitle.setText(StringUtils.isNull(title));
    }

}
