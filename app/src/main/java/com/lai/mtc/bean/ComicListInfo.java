package com.lai.mtc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lai
 * @time 2017/9/5 11:56
 * @describe describe
 */

public class ComicListInfo implements Serializable {
    private int total_count;
    private int total_pages;
    private int current_page;
    //所有的集合
    private List<EntriesBean> entries;
    //显示用的list,只放6个作为显示
    private List<EntriesBean> mShowList;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public List<EntriesBean> getShowList() {
        return mShowList;
    }

    public void setShowList(List<EntriesBean> showList) {
        mShowList = showList;
    }

    public static class EntriesBean implements Serializable {
        /**
         * id : 47467
         * cover : https://images.dmzj.com/webpic/11/0210yitiaogoufml.jpg
         * name : 一条狗
         * author : 使徒子
         * category : 少年漫画
         * updated_date : 2016-02-07
         * area : 内地
         * status : 已完结
         * description : 一个人和狗互换身体的故事……
         * track_url : http://www.dmzj.com/info/yitiaogou.html
         * hot : 8627042
         * source : 动漫之家
         * long_updated_date : 1454774400
         * chapters_count : 73
         * tag_list : ["搞笑"]
         */

        private int id;
        private String cover;
        private String name;
        private String author;
        private String category;
        private String updated_date;
        private String area;
        private String status;
        private String description;
        private String track_url;
        private int hot;
        private String source;
        private int long_updated_date;
        private int chapters_count;
        private List<String> tag_list;

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
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<EntriesBean> getEntries() {
        return entries;
    }

    public void setEntries(List<EntriesBean> entries) {
        this.entries = entries;
    }
}
