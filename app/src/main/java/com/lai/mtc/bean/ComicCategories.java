package com.lai.mtc.bean;

import java.io.Serializable;

/**
 * @author Lai
 * @time 2017/12/1 15:51
 * @describe describe
 */

public class ComicCategories implements Serializable {

    /**
     * name : 热血
     * cover : http://img.1whour.com/xpic/hy47.jpg
     * track_url : http://comic.kukudm.com/comiclist/3/index.htm
     * query : /comics?q%5Btags_id_eq%5D=2
     */
    private int id;

    public ComicCategories() {

    }

    public ComicCategories(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private String cover;
    private String track_url;
    private String query;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTrack_url() {
        return track_url;
    }

    public void setTrack_url(String track_url) {
        this.track_url = track_url;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
