package com.lai.mtc.mvp.utlis.share;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.lai.mtc.comm.widget.BookViewLayout;

/**
 * @author Lai
 * @time 2018/1/22 13:44
 * @describe 共享元素实体类
 */

public class ShareElementInfo implements Parcelable {
    private int[] mOriginalLocation;
    private int[] mTargetLocation;
    private float[] mOriginalValues;
    private float[] mTargetValues;
    private int mOriginalWidth;
    private int mOriginalHeight;
    private int mTargetWidth;
    private int mTargetHeight;
    private int mOriginalViewWidth;
    private int mOriginalViewHeight;
    private int mTargetViewWidth;
    private int mTargetViewHeight;
    private int mCenterOffsetX;
    private int mCenterOffsetY;
    private float mScaleX;
    private float mScaleY;
    private float mPivotX;
    private float mPivotY;

    public ShareElementInfo() {
        mOriginalLocation = new int[2];
        mTargetLocation = new int[2];
        mOriginalValues = new float[9];
        mTargetValues = new float[9];
    }

    protected ShareElementInfo(Parcel in) {
        mOriginalLocation = in.createIntArray();
        mTargetLocation = in.createIntArray();
        mOriginalValues = in.createFloatArray();
        mTargetValues = in.createFloatArray();
        mOriginalWidth = in.readInt();
        mOriginalHeight = in.readInt();
        mTargetWidth = in.readInt();
        mTargetHeight = in.readInt();
        mOriginalViewWidth = in.readInt();
        mOriginalViewHeight = in.readInt();
        mTargetViewWidth = in.readInt();
        mTargetViewHeight = in.readInt();
        mCenterOffsetX = in.readInt();
        mCenterOffsetY = in.readInt();
        mScaleX = in.readFloat();
        mScaleY = in.readFloat();
        mPivotX = in.readFloat();
        mPivotY = in.readFloat();
    }

    public static final Creator<ShareElementInfo> CREATOR = new Creator<ShareElementInfo>() {
        @Override
        public ShareElementInfo createFromParcel(Parcel in) {
            return new ShareElementInfo(in);
        }

        @Override
        public ShareElementInfo[] newArray(int size) {
            return new ShareElementInfo[size];
        }
    };

    public void convertOriginalInfo(ImageView oriView) {
        if (oriView == null || oriView.getDrawable() == null) {
            throw new NullPointerException("original ImageView or ImageView drawable must not null");
        }

        //get original ImageView info
        oriView.getImageMatrix().getValues(mOriginalValues);
        Rect oriRect = oriView.getDrawable().getBounds();
        mOriginalWidth = (int) (oriRect.width() * mOriginalValues[Matrix.MSCALE_X]);
        mOriginalHeight = (int) (oriRect.height() * mOriginalValues[Matrix.MSCALE_Y]);

        mOriginalViewWidth = oriView.getWidth();
        mOriginalViewHeight = oriView.getHeight();
        oriView.getLocationOnScreen(mOriginalLocation);
    }

    void convertTargetInfo(BookViewLayout viewLayout, Context context) {
        ImageView tarView = viewLayout.getIvCover();
        if (tarView == null || tarView.getDrawable() == null) {
            throw new NullPointerException("target ImageView or ImageView drawable must not null");
        }

        //get target ImageView info
        tarView.getImageMatrix().getValues(mTargetValues);
        Rect tarRect = tarView.getDrawable().getBounds();
        mTargetWidth = (int) (tarRect.width() * mTargetValues[Matrix.MSCALE_X]);
        mTargetHeight = (int) (tarRect.height() * mTargetValues[Matrix.MSCALE_Y]);

        mTargetViewWidth = tarView.getWidth();
        mTargetViewHeight = tarView.getHeight();
        tarView.getLocationOnScreen(mTargetLocation);

        init(context);
    }

    private void init(Context context) {
        //calculator scale
        mScaleX = (float) mOriginalWidth / mTargetWidth;
        mScaleY = (float) mOriginalHeight / mTargetHeight;

        //calculator pivot position
        mPivotX = mTargetLocation[0] + mTargetValues[Matrix.MTRANS_X] + mTargetWidth / 2;
        mPivotY = mTargetLocation[1] + mTargetValues[Matrix.MTRANS_Y] + mTargetHeight / 2;

        //calculator center offeset
        mCenterOffsetX = (int) (mOriginalLocation[0] + mOriginalValues[Matrix.MTRANS_X] + mOriginalViewWidth / 2
                - mTargetLocation[0] - mTargetValues[Matrix.MTRANS_X] - mTargetViewWidth / 2);
        mCenterOffsetY = (int) (mOriginalLocation[1] + mOriginalValues[Matrix.MTRANS_Y] + mOriginalViewHeight / 2
                - mTargetLocation[1] - mTargetValues[Matrix.MTRANS_Y] - mTargetViewHeight / 2);
    }

    public int getCenterOffsetX() {
        return mCenterOffsetX;
    }

    public int getCenterOffsetY() {
        return mCenterOffsetY;
    }

    public float getScaleX() {
        return mScaleX;
    }

    public float getScaleY() {
        return mScaleY;
    }

    public float getPivotX() {
        return mPivotX;
    }

    public float getPivotY() {
        return mPivotY;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(mOriginalLocation);
        dest.writeIntArray(mTargetLocation);
        dest.writeFloatArray(mOriginalValues);
        dest.writeFloatArray(mTargetValues);
        dest.writeInt(mOriginalWidth);
        dest.writeInt(mOriginalHeight);
        dest.writeInt(mTargetWidth);
        dest.writeInt(mTargetHeight);
        dest.writeInt(mOriginalViewWidth);
        dest.writeInt(mOriginalViewHeight);
        dest.writeInt(mTargetViewWidth);
        dest.writeInt(mTargetViewHeight);
        dest.writeInt(mCenterOffsetX);
        dest.writeInt(mCenterOffsetY);
        dest.writeFloat(mScaleX);
        dest.writeFloat(mScaleY);
        dest.writeFloat(mPivotX);
        dest.writeFloat(mPivotY);
    }
}
