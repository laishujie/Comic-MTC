package com.lai.mtc.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lai
 * @time 2017/10/5 16:16
 * @describe describe
 */
public class ComicTypeInfo implements MultiItemEntity {
    //Top type
    public static final int Head = 1;
    //list type
    public static final int List = 0;
    //current type
    public int currType;
    //Title
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ComicTypeInfo(int currType, String title) {
        this.currType = currType;
        this.title = title;
    }

    public ComicTypeInfo(String title) {
        this.title = title;
    }

    public static List<ComicTypeInfo> getmComicListInfos() {
        return mComicListInfos;
    }
    public static List<ComicTypeInfo> getmComicListInfos2() {
        return mComicListInfos2;
    }
    public static void setmComicListInfos(List<ComicTypeInfo> mComicListInfos) {
        ComicTypeInfo.mComicListInfos = mComicListInfos;
    }

    private static List<ComicTypeInfo> mComicListInfos;
    private static List<ComicTypeInfo> mComicListInfos2;

    static {
        if (mComicListInfos == null) {
            mComicListInfos = new ArrayList<>();
            mComicListInfos2 = new ArrayList<>();
            mComicListInfos.add(new ComicTypeInfo(Head, "今日推荐|Chinese Comics"));
            mComicListInfos.add(new ComicTypeInfo("随机推荐|Chinese Comics"));
            mComicListInfos2.addAll(mComicListInfos);
            mComicListInfos2.addAll(mComicListInfos);
            mComicListInfos2.addAll(mComicListInfos);
        }
    }


    @Override
    public int getItemType() {
        return currType;
    }
}
