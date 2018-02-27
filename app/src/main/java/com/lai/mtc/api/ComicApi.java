package com.lai.mtc.api;


import com.lai.mtc.bean.ComicCategories;
import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.bean.ComicPreView;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Lai
 * @time 2017/9/5 11:55
 * @describe 动漫API管理类
 */

public interface ComicApi {

    /**
     * 根据传入的类型来获取数据
     *
     * @param area
     * @return
     */
    @GET("/comics?page=1")
    Observable<ComicListInfo> getTypeComics(@Query("q[area_eq]") String area);
    //http://api.cookacg.com/comics?q%5Btags_id_eq%5D=2&page=1&package_name=com.vjson.anime&version_code=77&version_name=1.0.7.7&channel=coolapk&sign=dcf692dc1d4cead44ce1d5d1b9409e26&platform=android

    @GET("/comics")
    Observable<Response<ComicListInfo>> getTypeComicsById(@Query("q[tags_id_eq]") int id, @Query("page") int page);

    @GET("/comics")
    Observable<Response<ComicListInfo>> getTypeComicsByResponseId(@Query("q[tags_id_eq]") int id, @Query("page") int page, @Query("name") String name);

    @GET("/comics/categories")
    Observable<List<ComicCategories>> getComicCategories();

    @GET("/comics")
    Observable<Response<ComicListInfo>> getAllComic(@Query("page") int page);

    @GET("/comics/{id}")
    Observable<ComicListDetail> getComicById(@Path("id") int id);

    @GET("/comics/{id}/{index}")
    Observable<ComicPreView> getComicPreViewById(@Path("id") int id, @Path("index") int index);

    @GET("/comics")
    Observable<ComicListInfo> getComicsByName(@Query("q[name_cont]") String name, @Query("page") int page);

}
