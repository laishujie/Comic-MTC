package com.lai.mtc.bean;

import com.lai.mtc.mvp.utlis.IPopMenu;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lai
 * @time 2018/1/20 22:57
 * @describe describe
 */

public class ComicListDetail implements Serializable, IPopMenu {

    private int id;
    private String cover;
    private String name;
    private String author;
    private String category;
    private String updated_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrack_url() {
        return track_url;
    }

    public void setTrack_url(String track_url) {
        this.track_url = track_url;
    }

    public int getHot() {
        return hot;
    }

    public void setHot(int hot) {
        this.hot = hot;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getLong_updated_date() {
        return long_updated_date;
    }

    public void setLong_updated_date(int long_updated_date) {
        this.long_updated_date = long_updated_date;
    }

    public int getChapters_count() {
        return chapters_count;
    }

    public void setChapters_count(int chapters_count) {
        this.chapters_count = chapters_count;
    }

    public List<String> getTag_list() {
        return tag_list;
    }

    public void setTag_list(List<String> tag_list) {
        this.tag_list = tag_list;
    }

    public List<ChaptersBean> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChaptersBean> chapters) {
        this.chapters = chapters;
    }

    public List<MirrorsBean> getMirrors() {
        return mirrors;
    }

    public void setMirrors(List<MirrorsBean> mirrors) {
        this.mirrors = mirrors;
    }

    private String area;
    private String status;
    private String description;
    private String track_url;
    private int hot;
    private String source;
    private int long_updated_date;
    private int chapters_count;
    private java.util.List<String> tag_list;
    private java.util.List<ChaptersBean> chapters;

    public List<ChaptersBean> getShowChapters() {
        return mShowChapters;
    }

    public void setShowChapters(List<ChaptersBean> showChapters) {
        mShowChapters = showChapters;
    }

    private java.util.List<ChaptersBean> mShowChapters;

    public List<ChaptersBean> getLastChapters() {
        return mLastChapters;
    }

    public void setLastChapters(List<ChaptersBean> lastChapters) {
        mLastChapters = lastChapters;
    }

    private java.util.List<ChaptersBean> mLastChapters;
    private java.util.List<MirrorsBean> mirrors;

    public static class ChaptersBean implements Serializable {
        private int comic_id;
        private int index;
        private String name;
        private String track_url;
        public static final int NORMAL = 1;
        public static final int BOTTOM = 2;

        public int getItemType() {
            return itemType;
        }

        private int itemType = NORMAL;

        public int getComic_id() {
            return comic_id;
        }

        public void setComic_id(int comic_id) {
            this.comic_id = comic_id;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTrack_url() {
            return track_url;
        }

        public void setTrack_url(String track_url) {
            this.track_url = track_url;
        }


    }

    public static class MirrorsBean implements IPopMenu,Serializable {
        private int id;
        private String source;

        @Override
        public String getName() {
            return source;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }

}
