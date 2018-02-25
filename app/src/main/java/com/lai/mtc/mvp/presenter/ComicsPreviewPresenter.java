package com.lai.mtc.mvp.presenter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.lai.mtc.api.ComicApi;
import com.lai.mtc.bean.ComicPreView;
import com.lai.mtc.comm.ApiException;
import com.lai.mtc.comm.HttpRxObserver;
import com.lai.mtc.mvp.base.impl.BasePresenter;
import com.lai.mtc.mvp.contract.ComicsPreviewContract;
import com.lai.mtc.mvp.utlis.glide.GlideApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Lai
 * @time 2018/1/23 16:37
 * @describe describe
 */

public class ComicsPreviewPresenter extends BasePresenter<ComicsPreviewContract.View> implements ComicsPreviewContract.Model {

    private ComicApi mComicApi;

    @Inject
    ComicsPreviewPresenter(ComicApi comicApi) {
        this.mComicApi = comicApi;
    }

    String testJson = "{\"comic_id\":19103,\"name\":\"678话\",\"track_url\":\"http://www.57mh.com/281/0578.html\",\"index\":578,\"pages\":[{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/1.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/2.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/3.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/4.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/5.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/6.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/7.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/8.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/9.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/10.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/11.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/12.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/13.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/14.jpg\"},{\"track_url\":\"http://images.720rs.com/ManHuaKu/sishen/678/15.jpg\"}]}\n";

    private String testInfoList = "{\"id\":19105,\"cover\":\"http://i.57mh.com/Uploads/bcover/2013/3/151540589_h.jpg\",\"name\":\"一拳超人\",\"author\":\"村田雄介/ONE\",\"category\":\"其他\",\"updated_date\":\"2017-06-23\",\"area\":\"日本\",\"status\":\"连载中\",\"description\":\"一拳超人漫画。主人公埼玉原本是一名整日奔波于求职的普通人。3年前的一天偶然遇到了要对淘气少年下杀手的异变螃蟹人后，回忆起年少年时“想要成为英雄”的梦想，最终拼尽全力救下了淘气少年。之后通过拼命锻炼，埼玉终于脱胎换骨获得了最强的力量，但同时失去了头发成了光头。在独自做了一段时间英雄后，正式加入英雄协会，与众多英雄一起开始了对抗各种怪人以及恶势力的生活……\",\"track_url\":\"http://www.57mh.com/10/\",\"hot\":92103988,\"source\":\"57漫画\",\"long_updated_date\":1498147200,\"chapters_count\":219,\"tag_list\":[\"冒险\",\"欢乐向\",\"格斗\"],\"chapters\":[{\"comic_id\":19105,\"name\":\"01卷\",\"track_url\":\"http://www.57mh.com/10/01.html\",\"index\":1},{\"comic_id\":19105,\"name\":\"02卷\",\"track_url\":\"http://www.57mh.com/10/02.html\",\"index\":2},{\"comic_id\":19105,\"name\":\"03卷\",\"track_url\":\"http://www.57mh.com/10/03.html\",\"index\":3},{\"comic_id\":19105,\"name\":\"04卷\",\"track_url\":\"http://www.57mh.com/10/04.html\",\"index\":4},{\"comic_id\":19105,\"name\":\"05卷\",\"track_url\":\"http://www.57mh.com/10/05.html\",\"index\":5},{\"comic_id\":19105,\"name\":\"06卷\",\"track_url\":\"http://www.57mh.com/10/06.html\",\"index\":6},{\"comic_id\":19105,\"name\":\"07卷\",\"track_url\":\"http://www.57mh.com/10/07.html\",\"index\":7},{\"comic_id\":19105,\"name\":\"08卷\",\"track_url\":\"http://www.57mh.com/10/08.html\",\"index\":8},{\"comic_id\":19105,\"name\":\"09卷\",\"track_url\":\"http://www.57mh.com/10/09.html\",\"index\":9},{\"comic_id\":19105,\"name\":\"10卷\",\"track_url\":\"http://www.57mh.com/10/010.html\",\"index\":10},{\"comic_id\":19105,\"name\":\"11卷\",\"track_url\":\"http://www.57mh.com/10/011.html\",\"index\":11},{\"comic_id\":19105,\"name\":\"12卷\",\"track_url\":\"http://www.57mh.com/10/012.html\",\"index\":12},{\"comic_id\":19105,\"name\":\"13卷\",\"track_url\":\"http://www.57mh.com/10/013.html\",\"index\":13},{\"comic_id\":19105,\"name\":\"14卷\",\"track_url\":\"http://www.57mh.com/10/014.html\",\"index\":14},{\"comic_id\":19105,\"name\":\"15卷\",\"track_url\":\"http://www.57mh.com/10/015.html\",\"index\":15},{\"comic_id\":19105,\"name\":\"16卷\",\"track_url\":\"http://www.57mh.com/10/016.html\",\"index\":16},{\"comic_id\":19105,\"name\":\"17卷\",\"track_url\":\"http://www.57mh.com/10/017.html\",\"index\":17},{\"comic_id\":19105,\"name\":\"17.5卷\",\"track_url\":\"http://www.57mh.com/10/018.html\",\"index\":18},{\"comic_id\":19105,\"name\":\"18卷\",\"track_url\":\"http://www.57mh.com/10/019.html\",\"index\":19},{\"comic_id\":19105,\"name\":\"19卷\",\"track_url\":\"http://www.57mh.com/10/020.html\",\"index\":20},{\"comic_id\":19105,\"name\":\"番外篇\",\"track_url\":\"http://www.57mh.com/10/021.html\",\"index\":21},{\"comic_id\":19105,\"name\":\"20卷\",\"track_url\":\"http://www.57mh.com/10/022.html\",\"index\":22},{\"comic_id\":19105,\"name\":\"20.5卷\",\"track_url\":\"http://www.57mh.com/10/023.html\",\"index\":23},{\"comic_id\":19105,\"name\":\"21-23卷\",\"track_url\":\"http://www.57mh.com/10/024.html\",\"index\":24},{\"comic_id\":19105,\"name\":\"24-25卷\",\"track_url\":\"http://www.57mh.com/10/025.html\",\"index\":25},{\"comic_id\":19105,\"name\":\"26-27卷\",\"track_url\":\"http://www.57mh.com/10/026.html\",\"index\":26},{\"comic_id\":19105,\"name\":\"28-29卷\",\"track_url\":\"http://www.57mh.com/10/027.html\",\"index\":27},{\"comic_id\":19105,\"name\":\"30-31卷\",\"track_url\":\"http://www.57mh.com/10/028.html\",\"index\":28},{\"comic_id\":19105,\"name\":\"32-35卷\",\"track_url\":\"http://www.57mh.com/10/029.html\",\"index\":29},{\"comic_id\":19105,\"name\":\"36-37卷\",\"track_url\":\"http://www.57mh.com/10/030.html\",\"index\":30},{\"comic_id\":19105,\"name\":\"38-39卷\",\"track_url\":\"http://www.57mh.com/10/031.html\",\"index\":31},{\"comic_id\":19105,\"name\":\"41卷\",\"track_url\":\"http://www.57mh.com/10/032.html\",\"index\":32},{\"comic_id\":19105,\"name\":\"42卷\",\"track_url\":\"http://www.57mh.com/10/033.html\",\"index\":33},{\"comic_id\":19105,\"name\":\"43卷\",\"track_url\":\"http://www.57mh.com/10/034.html\",\"index\":34},{\"comic_id\":19105,\"name\":\"44卷\",\"track_url\":\"http://www.57mh.com/10/035.html\",\"index\":35},{\"comic_id\":19105,\"name\":\"番外篇2\",\"track_url\":\"http://www.57mh.com/10/036.html\",\"index\":36},{\"comic_id\":19105,\"name\":\"45卷\",\"track_url\":\"http://www.57mh.com/10/037.html\",\"index\":37},{\"comic_id\":19105,\"name\":\"特别篇\",\"track_url\":\"http://www.57mh.com/10/038.html\",\"index\":38},{\"comic_id\":19105,\"name\":\"附赠漫画\",\"track_url\":\"http://www.57mh.com/10/039.html\",\"index\":39},{\"comic_id\":19105,\"name\":\"46卷\",\"track_url\":\"http://www.57mh.com/10/040.html\",\"index\":40},{\"comic_id\":19105,\"name\":\"47卷\",\"track_url\":\"http://www.57mh.com/10/041.html\",\"index\":41},{\"comic_id\":19105,\"name\":\"48卷\",\"track_url\":\"http://www.57mh.com/10/042.html\",\"index\":42},{\"comic_id\":19105,\"name\":\"番外篇3\",\"track_url\":\"http://www.57mh.com/10/043.html\",\"index\":43},{\"comic_id\":19105,\"name\":\"50卷\",\"track_url\":\"http://www.57mh.com/10/044.html\",\"index\":44},{\"comic_id\":19105,\"name\":\"51话\",\"track_url\":\"http://www.57mh.com/10/045.html\",\"index\":45},{\"comic_id\":19105,\"name\":\"52话\",\"track_url\":\"http://www.57mh.com/10/046.html\",\"index\":46},{\"comic_id\":19105,\"name\":\"番外篇4\",\"track_url\":\"http://www.57mh.com/10/047.html\",\"index\":47},{\"comic_id\":19105,\"name\":\"53话\",\"track_url\":\"http://www.57mh.com/10/048.html\",\"index\":48},{\"comic_id\":19105,\"name\":\"54话\",\"track_url\":\"http://www.57mh.com/10/049.html\",\"index\":49},{\"comic_id\":19105,\"name\":\"55话\",\"track_url\":\"http://www.57mh.com/10/050.html\",\"index\":50},{\"comic_id\":19105,\"name\":\"56话\",\"track_url\":\"http://www.57mh.com/10/051.html\",\"index\":51},{\"comic_id\":19105,\"name\":\"57话\",\"track_url\":\"http://www.57mh.com/10/052.html\",\"index\":52},{\"comic_id\":19105,\"name\":\"58话\",\"track_url\":\"http://www.57mh.com/10/053.html\",\"index\":53},{\"comic_id\":19105,\"name\":\"59话\",\"track_url\":\"http://www.57mh.com/10/054.html\",\"index\":54},{\"comic_id\":19105,\"name\":\"60话\",\"track_url\":\"http://www.57mh.com/10/055.html\",\"index\":55},{\"comic_id\":19105,\"name\":\"61话\",\"track_url\":\"http://www.57mh.com/10/056.html\",\"index\":56},{\"comic_id\":19105,\"name\":\"62话\",\"track_url\":\"http://www.57mh.com/10/057.html\",\"index\":57},{\"comic_id\":19105,\"name\":\"63话\",\"track_url\":\"http://www.57mh.com/10/058.html\",\"index\":58},{\"comic_id\":19105,\"name\":\"64话\",\"track_url\":\"http://www.57mh.com/10/059.html\",\"index\":59},{\"comic_id\":19105,\"name\":\"65话\",\"track_url\":\"http://www.57mh.com/10/060.html\",\"index\":60},{\"comic_id\":19105,\"name\":\"66话\",\"track_url\":\"http://www.57mh.com/10/061.html\",\"index\":61},{\"comic_id\":19105,\"name\":\"67话\",\"track_url\":\"http://www.57mh.com/10/062.html\",\"index\":62},{\"comic_id\":19105,\"name\":\"68话\",\"track_url\":\"http://www.57mh.com/10/063.html\",\"index\":63},{\"comic_id\":19105,\"name\":\"69话\",\"track_url\":\"http://www.57mh.com/10/064.html\",\"index\":64},{\"comic_id\":19105,\"name\":\"70话\",\"track_url\":\"http://www.57mh.com/10/065.html\",\"index\":65},{\"comic_id\":19105,\"name\":\"71话\",\"track_url\":\"http://www.57mh.com/10/066.html\",\"index\":66},{\"comic_id\":19105,\"name\":\"72话\",\"track_url\":\"http://www.57mh.com/10/067.html\",\"index\":67},{\"comic_id\":19105,\"name\":\"73话\",\"track_url\":\"http://www.57mh.com/10/068.html\",\"index\":68},{\"comic_id\":19105,\"name\":\"74话\",\"track_url\":\"http://www.57mh.com/10/069.html\",\"index\":69},{\"comic_id\":19105,\"name\":\"75话\",\"track_url\":\"http://www.57mh.com/10/070.html\",\"index\":70},{\"comic_id\":19105,\"name\":\"76话\",\"track_url\":\"http://www.57mh.com/10/071.html\",\"index\":71},{\"comic_id\":19105,\"name\":\"77话\",\"track_url\":\"http://www.57mh.com/10/072.html\",\"index\":72},{\"comic_id\":19105,\"name\":\"78话\",\"track_url\":\"http://www.57mh.com/10/073.html\",\"index\":73},{\"comic_id\":19105,\"name\":\"79话\",\"track_url\":\"http://www.57mh.com/10/074.html\",\"index\":74},{\"comic_id\":19105,\"name\":\"80话\",\"track_url\":\"http://www.57mh.com/10/075.html\",\"index\":75},{\"comic_id\":19105,\"name\":\"81话\",\"track_url\":\"http://www.5\n" +
            "2000\n" +
            "7mh.com/10/076.html\",\"index\":76},{\"comic_id\":19105,\"name\":\"82话\",\"track_url\":\"http://www.57mh.com/10/077.html\",\"index\":77},{\"comic_id\":19105,\"name\":\"83话\",\"track_url\":\"http://www.57mh.com/10/078.html\",\"index\":78},{\"comic_id\":19105,\"name\":\"84话\",\"track_url\":\"http://www.57mh.com/10/079.html\",\"index\":79},{\"comic_id\":19105,\"name\":\"85话\",\"track_url\":\"http://www.57mh.com/10/080.html\",\"index\":80},{\"comic_id\":19105,\"name\":\"86话\",\"track_url\":\"http://www.57mh.com/10/081.html\",\"index\":81},{\"comic_id\":19105,\"name\":\"87话\",\"track_url\":\"http://www.57mh.com/10/082.html\",\"index\":82},{\"comic_id\":19105,\"name\":\"88话\",\"track_url\":\"http://www.57mh.com/10/083.html\",\"index\":83},{\"comic_id\":19105,\"name\":\"89话\",\"track_url\":\"http://www.57mh.com/10/084.html\",\"index\":84},{\"comic_id\":19105,\"name\":\"90话\",\"track_url\":\"http://www.57mh.com/10/085.html\",\"index\":85},{\"comic_id\":19105,\"name\":\"91话\",\"track_url\":\"http://www.57mh.com/10/086.html\",\"index\":86},{\"comic_id\":19105,\"name\":\"92话\",\"track_url\":\"http://www.57mh.com/10/087.html\",\"index\":87},{\"comic_id\":19105,\"name\":\"92.5话\",\"track_url\":\"http://www.57mh.com/10/088.html\",\"index\":88},{\"comic_id\":19105,\"name\":\"40话\",\"track_url\":\"http://www.57mh.com/10/089.html\",\"index\":89},{\"comic_id\":19105,\"name\":\"41话\",\"track_url\":\"http://www.57mh.com/10/090.html\",\"index\":90},{\"comic_id\":19105,\"name\":\"41.2话\",\"track_url\":\"http://www.57mh.com/10/091.html\",\"index\":91},{\"comic_id\":19105,\"name\":\"42话\",\"track_url\":\"http://www.57mh.com/10/092.html\",\"index\":92},{\"comic_id\":19105,\"name\":\"43话\",\"track_url\":\"http://www.57mh.com/10/093.html\",\"index\":93},{\"comic_id\":19105,\"name\":\"44话\",\"track_url\":\"http://www.57mh.com/10/094.html\",\"index\":94},{\"comic_id\":19105,\"name\":\"45话\",\"track_url\":\"http://www.57mh.com/10/095.html\",\"index\":95},{\"comic_id\":19105,\"name\":\"超 45话\",\"track_url\":\"http://www.57mh.com/10/096.html\",\"index\":96},{\"comic_id\":19105,\"name\":\"46话\",\"track_url\":\"http://www.57mh.com/10/097.html\",\"index\":97},{\"comic_id\":19105,\"name\":\"真 46话\",\"track_url\":\"http://www.57mh.com/10/098.html\",\"index\":98},{\"comic_id\":19105,\"name\":\"番外篇8\",\"track_url\":\"http://www.57mh.com/10/099.html\",\"index\":99},{\"comic_id\":19105,\"name\":\"外传：弹丸天使\",\"track_url\":\"http://www.57mh.com/10/0100.html\",\"index\":100},{\"comic_id\":19105,\"name\":\"谜之更新回\",\"track_url\":\"http://www.57mh.com/10/0101.html\",\"index\":101},{\"comic_id\":19105,\"name\":\"真 45话\",\"track_url\":\"http://www.57mh.com/10/0102.html\",\"index\":102},{\"comic_id\":19105,\"name\":\"ONE老师漫画教学\",\"track_url\":\"http://www.57mh.com/10/0103.html\",\"index\":103},{\"comic_id\":19105,\"name\":\"一次人气投票\",\"track_url\":\"http://www.57mh.com/10/0104.html\",\"index\":104},{\"comic_id\":19105,\"name\":\"真 46话\",\"track_url\":\"http://www.57mh.com/10/0105.html\",\"index\":105},{\"comic_id\":19105,\"name\":\"二次人气投票\",\"track_url\":\"http://www.57mh.com/10/0106.html\",\"index\":106},{\"comic_id\":19105,\"name\":\"外传：16话\",\"track_url\":\"http://www.57mh.com/10/0107.html\",\"index\":107},{\"comic_id\":19105,\"name\":\"47话\",\"track_url\":\"http://www.57mh.com/10/0108.html\",\"index\":108},{\"comic_id\":19105,\"name\":\"ONE老师版 可爱的野猫\",\"track_url\":\"http://www.57mh.com/10/0109.html\",\"index\":109},{\"comic_id\":19105,\"name\":\"真 47话\",\"track_url\":\"http://www.57mh.com/10/0110.html\",\"index\":110},{\"comic_id\":19105,\"name\":\"村田老师版 可爱的野猫\",\"track_url\":\"http://www.57mh.com/10/0111.html\",\"index\":111},{\"comic_id\":19105,\"name\":\"同人：最上位3战士谈论波罗斯的事情\",\"track_url\":\"http://www.57mh.com/10/0112.html\",\"index\":112},{\"comic_id\":19105,\"name\":\"同人：穿越至彼端(腐向)\",\"track_url\":\"http://www.57mh.com/10/0113.html\",\"index\":113},{\"comic_id\":19105,\"name\":\"单行本01话\",\"track_url\":\"http://www.57mh.com/10/0114.html\",\"index\":114},{\"comic_id\":19105,\"name\":\"超 47话\",\"track_url\":\"http://www.57mh.com/10/0115.html\",\"index\":115},{\"comic_id\":19105,\"name\":\"番外篇附录\",\"track_url\":\"http://www.57mh.com/10/0116.html\",\"index\":116},{\"comic_id\":19105,\"name\":\"48话\",\"track_url\":\"http://www.57mh.com/10/0117.html\",\"index\":117},{\"comic_id\":19105,\"name\":\"49话\",\"track_url\":\"http://www.57mh.com/10/0118.html\",\"index\":118},{\"comic_id\":19105,\"name\":\"真49话\",\"track_url\":\"http://www.57mh.com/10/0119.html\",\"index\":119},{\"comic_id\":19105,\"name\":\"50话\",\"track_url\":\"http://www.57mh.com/10/0120.html\",\"index\":120},{\"comic_id\":19105,\"name\":\"51话\",\"track_url\":\"http://www.57mh.com/10/0121.html\",\"index\":121},{\"comic_id\":19105,\"name\":\"52话\",\"track_url\":\"http://www.57mh.com/10/0122.html\",\"index\":122},{\"comic_id\":19105,\"name\":\"53话\",\"track_url\":\"http://www.57mh.com/10/0123.html\",\"index\":123},{\"comic_id\":19105,\"name\":\"54话\",\"track_url\":\"http://www.57mh.com/10/0124.html\",\"index\":124},{\"comic_id\":19105,\"name\":\"55-56话\",\"track_url\":\"http://www.57mh.com/10/0125.html\",\"index\":125},{\"comic_id\":19105,\"name\":\"MJ番外篇\",\"track_url\":\"http://www.57mh.com/10/0126.html\",\"index\":126},{\"comic_id\":19105,\"name\":\"57话\",\"track_url\":\"http://www.57mh.com/10/0127.html\",\"index\":127},{\"comic_id\":19105,\"name\":\"58话\",\"track_url\":\"http://www.57mh.com/10/0128.html\",\"index\":128},{\"comic_id\":19105,\"name\":\"59话\",\"track_url\":\"http://www.57mh.com/10/0129.html\",\"index\":129},{\"comic_id\":19105,\"name\":\"原作版99(1)\",\"track_url\":\"http://www.57mh.com/10/0130.html\",\"index\":130},{\"comic_id\":19105,\"name\":\"60话\",\"track_url\":\"http://www.57mh.com/10/0131.html\",\"index\":131},{\"comic_id\":19105,\"name\":\"原作版68补\",\"track_url\":\"http://www.57mh.com/10/0132.html\",\"index\":132},{\"comic_id\":19105,\"name\":\"23话\",\"track_url\":\"http://www.57mh.com/10/0133.html\",\"index\":133},{\"comic_id\":19105,\"name\":\"66话\",\"track_url\":\"http://www.57mh.com/10/0134.html\",\"index\":134},{\"comic_id\":19105,\"name\":\"原作版99（2）\",\"track_url\":\"http://www.57mh.com/10/0135.html\",\"index\":135},{\"comic_id\":19105,\"name\":\"原作版99(3)\",\"track_url\":\"http://www.57mh.com/10/0136.html\",\"index\":136},{\"comic_id\":19105,\"name\":\"原作版100(1)\",\"track_url\":\"http://www.57mh.com/10/0137.html\",\"index\":137},{\"comic_id\":19105,\"name\":\"特别版\",\"track_url\":\"http://www.57mh.com/10/0138.html\",\"index\":138},{\"comic_id\":19105,\"name\":\"外传：23话\",\"track_url\":\"http://www.57mh.com/10/0139.html\",\"index\":139},{\"comic_id\":19105,\"name\":\"68话\",\"track_url\":\"http://www.57mh.com/10/0140.html\",\"index\":140},{\"comic_id\":19105,\"name\":\"原作版99(3)\",\"track_url\":\"http://www.57mh.com/10/0141.html\",\"index\":141},{\"comic_id\":19105,\"name\":\"原作版100(1)-（3）\",\"track_url\":\"http://www.57mh.com/10/0142.html\",\"index\":142},{\"comic_id\":19105,\"name\":\"特别篇\",\"track_url\":\"http://www.57mh.com/10/0143.html\",\"index\":143},{\"comic_id\":19105,\"name\":\"外传：23话\",\"track_url\":\"http://www.57mh.com/10/0144.html\",\"index\":144},{\"comic_id\":19105,\"name\":\"漫画 68话\",\"track_url\":\"http://www.57mh.com/10/0145.html\",\"index\":145},{\"comic_id\":19105,\"name\":\"69话\",\"track_url\":\"http://www.57mh.com/10/0146.html\",\"index\":146},{\"comic_id\":19105,\"name\":\"70话\",\"track_url\":\"http://www.57mh.com/10/0147.html\",\"index\":147},{\"comic_id\":19105,\"name\":\"71话\",\"track_url\":\"http://www.57mh.com/10/0148.html\",\"index\":148},{\"comic_id\":19105,\"name\":\"外传：24话\",\"track_url\":\"http://www.57mh.com/10/0149.html\",\"index\":149},{\"comic_id\":19105,\"name\":\"72话\",\"track_url\":\"http://www.57mh.com/10/0150.html\",\"index\":150},{\"comic_id\":19105,\"name\":\"73话\",\"track_url\":\"http://www.57mh.com/10/0151.html\",\"index\":151},{\"comic_id\":19105,\"name\":\"74话\",\"track_url\":\"http://www.57mh.com/10/0152.html\",\"index\":152},{\"comic_id\":19105,\"name\":\"75话\",\"track_url\":\"http://www.57mh.com/10/0153.html\",\"index\":153},{\"comic_id\":19105,\"name\":\"76话\",\"track_url\":\"http://www.57mh.com/10/0154.html\",\"index\":154},{\"comic_id\":19105,\"name\":\"77话\",\"track_url\":\"http://www.57mh.com/10/0155.html\",\"index\":155},{\"comic_id\":19105,\"name\":\"78话\",\"track_url\":\"http://www.57mh.com/10/0156.html\",\"index\":156},{\"comic_id\":19105,\"name\":\"原作版101\",\"track_url\":\"http://www.57mh.com/10/0157.html\",\"index\":157},{\"comic_id\":19105,\"name\":\"原作版101（2）\",\"track_url\":\"http://www.57mh.com/10/0158.html\",\"index\":158},{\"comic_id\":19105,\"name\":\"原作版101（3）\",\"track_url\":\"http://www.57mh.com/10/0159.html\",\"index\":159},{\"comic_id\":19105,\"name\":\"原作版102\",\"track_url\":\"http://www.57mh.co\n" +
            "1000\n" +
            "m/10/0160.html\",\"index\":160},{\"comic_id\":19105,\"name\":\"原作版102（2）\",\"track_url\":\"http://www.57mh.com/10/0161.html\",\"index\":161},{\"comic_id\":19105,\"name\":\"原作版102（3）\",\"track_url\":\"http://www.57mh.com/10/0162.html\",\"index\":162},{\"comic_id\":19105,\"name\":\"原作版102（4）\",\"track_url\":\"http://www.57mh.com/10/0163.html\",\"index\":163},{\"comic_id\":19105,\"name\":\"79话\",\"track_url\":\"http://www.57mh.com/10/0164.html\",\"index\":164},{\"comic_id\":19105,\"name\":\"09卷附录\",\"track_url\":\"http://www.57mh.com/10/0165.html\",\"index\":165},{\"comic_id\":19105,\"name\":\"原作版103（2）\",\"track_url\":\"http://www.57mh.com/10/0166.html\",\"index\":166},{\"comic_id\":19105,\"name\":\"YJ番外篇\",\"track_url\":\"http://www.57mh.com/10/0167.html\",\"index\":167},{\"comic_id\":19105,\"name\":\"原作版103（3）\",\"track_url\":\"http://www.57mh.com/10/0168.html\",\"index\":168},{\"comic_id\":19105,\"name\":\"YJ番外特別篇\",\"track_url\":\"http://www.57mh.com/10/0169.html\",\"index\":169},{\"comic_id\":19105,\"name\":\"原作人气投票←\",\"track_url\":\"http://www.57mh.com/10/0170.html\",\"index\":170},{\"comic_id\":19105,\"name\":\"80话\",\"track_url\":\"http://www.57mh.com/10/0171.html\",\"index\":171},{\"comic_id\":19105,\"name\":\"原作人气投票2\",\"track_url\":\"http://www.57mh.com/10/0172.html\",\"index\":172},{\"comic_id\":19105,\"name\":\"81话\",\"track_url\":\"http://www.57mh.com/10/0173.html\",\"index\":173},{\"comic_id\":19105,\"name\":\"07卷番外\",\"track_url\":\"http://www.57mh.com/10/0174.html\",\"index\":174},{\"comic_id\":19105,\"name\":\"82话\",\"track_url\":\"http://www.57mh.com/10/0175.html\",\"index\":175},{\"comic_id\":19105,\"name\":\"83话\",\"track_url\":\"http://www.57mh.com/10/0176.html\",\"index\":176},{\"comic_id\":19105,\"name\":\"84话\",\"track_url\":\"http://www.57mh.com/10/0177.html\",\"index\":177},{\"comic_id\":19105,\"name\":\"85话\",\"track_url\":\"http://www.57mh.com/10/0178.html\",\"index\":178},{\"comic_id\":19105,\"name\":\"与小龙卷01\",\"track_url\":\"http://www.57mh.com/10/0179.html\",\"index\":179},{\"comic_id\":19105,\"name\":\"86话\",\"track_url\":\"http://www.57mh.com/10/0180.html\",\"index\":180},{\"comic_id\":19105,\"name\":\"87话\",\"track_url\":\"http://www.57mh.com/10/0181.html\",\"index\":181},{\"comic_id\":19105,\"name\":\"088话\",\"track_url\":\"http://www.57mh.com/10/0182.html\",\"index\":182},{\"comic_id\":19105,\"name\":\"089话\",\"track_url\":\"http://www.57mh.com/10/0183.html\",\"index\":183},{\"comic_id\":19105,\"name\":\"090话\",\"track_url\":\"http://www.57mh.com/10/0184.html\",\"index\":184},{\"comic_id\":19105,\"name\":\"91话\",\"track_url\":\"http://www.57mh.com/10/0185.html\",\"index\":185},{\"comic_id\":19105,\"name\":\"原作版106（2）\",\"track_url\":\"http://www.57mh.com/10/0186.html\",\"index\":186},{\"comic_id\":19105,\"name\":\"10卷番外\",\"track_url\":\"http://www.57mh.com/10/0187.html\",\"index\":187},{\"comic_id\":19105,\"name\":\"092话\",\"track_url\":\"http://www.57mh.com/10/0188.html\",\"index\":188},{\"comic_id\":19105,\"name\":\"093话\",\"track_url\":\"http://www.57mh.com/10/0189.html\",\"index\":189},{\"comic_id\":19105,\"name\":\"YJ特别短篇\",\"track_url\":\"http://www.57mh.com/10/0190.html\",\"index\":190},{\"comic_id\":19105,\"name\":\"首页图\",\"track_url\":\"http://www.57mh.com/10/0191.html\",\"index\":191},{\"comic_id\":19105,\"name\":\"094话\",\"track_url\":\"http://www.57mh.com/10/0192.html\",\"index\":192},{\"comic_id\":19105,\"name\":\"095话\",\"track_url\":\"http://www.57mh.com/10/0193.html\",\"index\":193},{\"comic_id\":19105,\"name\":\"096话\",\"track_url\":\"http://www.57mh.com/10/0194.html\",\"index\":194},{\"comic_id\":19105,\"name\":\"三次人气投票\",\"track_url\":\"http://www.57mh.com/10/0195.html\",\"index\":195},{\"comic_id\":19105,\"name\":\"097话\",\"track_url\":\"http://www.57mh.com/10/0196.html\",\"index\":196},{\"comic_id\":19105,\"name\":\"98话\",\"track_url\":\"http://www.57mh.com/10/0197.html\",\"index\":197},{\"comic_id\":19105,\"name\":\"99话\",\"track_url\":\"http://www.57mh.com/10/0198.html\",\"index\":198},{\"comic_id\":19105,\"name\":\"100话\",\"track_url\":\"http://www.57mh.com/10/0199.html\",\"index\":199},{\"comic_id\":19105,\"name\":\"101话\",\"track_url\":\"http://www.57mh.com/10/0200.html\",\"index\":200},{\"comic_id\":19105,\"name\":\"102话\",\"track_url\":\"http://www.57mh.com/10/0201.html\",\"index\":201},{\"comic_id\":19105,\"name\":\"103话\",\"track_url\":\"http://www\n" +
            "757\n" +
            ".57mh.com/10/0202.html\",\"index\":202},{\"comic_id\":19105,\"name\":\"104话\",\"track_url\":\"http://www.57mh.com/10/0203.html\",\"index\":203},{\"comic_id\":19105,\"name\":\"105话\",\"track_url\":\"http://www.57mh.com/10/0204.html\",\"index\":204},{\"comic_id\":19105,\"name\":\"YJ特別出张短篇\",\"track_url\":\"http://www.57mh.com/10/0205.html\",\"index\":205},{\"comic_id\":19105,\"name\":\"106话\",\"track_url\":\"http://www.57mh.com/10/0206.html\",\"index\":206},{\"comic_id\":19105,\"name\":\"107话\",\"track_url\":\"http://www.57mh.com/10/0207.html\",\"index\":207},{\"comic_id\":19105,\"name\":\"108话\",\"track_url\":\"http://www.57mh.com/10/0208.html\",\"index\":208},{\"comic_id\":19105,\"name\":\"109话\",\"track_url\":\"http://www.57mh.com/10/0209.html\",\"index\":209},{\"comic_id\":19105,\"name\":\"110话\",\"track_url\":\"http://www.57mh.com/10/0210.html\",\"index\":210},{\"comic_id\":19105,\"name\":\"111话\",\"track_url\":\"http://www.57mh.com/10/0211.html\",\"index\":211},{\"comic_id\":19105,\"name\":\"112话\",\"track_url\":\"http://www.57mh.com/10/0212.html\",\"index\":212},{\"comic_id\":19105,\"name\":\"113话\",\"track_url\":\"http://www.57mh.com/10/0213.html\",\"index\":213},{\"comic_id\":19105,\"name\":\"114话\",\"track_url\":\"http://www.57mh.com/10/0214.html\",\"index\":214},{\"comic_id\":19105,\"name\":\"115话\",\"track_url\":\"http://www.57mh.com/10/0215.html\",\"index\":215},{\"comic_id\":19105,\"name\":\"13卷附录\",\"track_url\":\"http://www.57mh.com/10/0216.html\",\"index\":216},{\"comic_id\":19105,\"name\":\"116话\",\"track_url\":\"http://www.57mh.com/10/0217.html\",\"index\":217},{\"comic_id\":19105,\"name\":\"117话\",\"track_url\":\"http://www.57mh.com/10/0218.html\",\"index\":218},{\"comic_id\":19105,\"name\":\"118话\",\"track_url\":\"http://www.57mh.com/10/0219.html\",\"index\":219}],\"mirrors\":[{\"id\":111,\"source\":\"漫画簿\"},{\"id\":18850,\"source\":\"Kuku动漫\"},{\"id\":19105,\"source\":\"57漫画\"},{\"id\":47461,\"source\":\"动漫之家\"},{\"id\":73482,\"source\":\"看漫画\"},{\"id\":100087,\"source\":\"鼠绘漫画网\"}]}\n";

    //Todo：测试预加载5张图片。并加载完返回：使用 combineLatest操作符 进行测试编写
    public void testList(final Context context) {
        mRootView.showLoading();
        Observable.create(new ObservableOnSubscribe<ComicPreView>() {
            @Override
            public void subscribe(ObservableEmitter<ComicPreView> e) throws Exception {
                Gson gson = new Gson();
                e.onNext(gson.fromJson(testJson, ComicPreView.class));
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation())
                .compose(mRootView.<ComicPreView>bindToLifecycle())
                .flatMap(new Function<ComicPreView, ObservableSource<ComicPreView>>() {
                    @Override
                    public ObservableSource<ComicPreView> apply(final ComicPreView comicPreView) throws Exception {
                        final List<ComicPreView.PagesBean> list;
                        if (comicPreView.getPages().size() > 4) {
                            list = new ArrayList<>(comicPreView.getPages().subList(0, 4));
                        } else {
                            list = comicPreView.getPages();
                        }
                        List<Observable<Boolean>> glideObservable = createGlideObservable(list, context);
                        return Observable.combineLatest(glideObservable, new Function<Object[], ComicPreView>() {
                            @Override
                            public ComicPreView apply(Object[] objects) throws Exception {
                                return comicPreView;
                            }
                        }).compose(mRootView.<ComicPreView>bindToLifecycle());
                    }
                }).compose(mRootView.<ComicPreView>bindToLifecycle()).subscribe(new Consumer<ComicPreView>() {
            @Override
            public void accept(ComicPreView aBoolean) throws Exception {
                mRootView.hideLoading();
            }
        });
    }

    /**/
    public List<Observable<Boolean>> createGlideObservable(List<ComicPreView.PagesBean> list, final Context context) {
        List<Observable<Boolean>> observables = new ArrayList<>();
        for (final ComicPreView.PagesBean l : list) {
            Observable<Boolean> booleanObservable = Observable.create(new ObservableOnSubscribe<Boolean>() {

                @Override
                public void subscribe(final ObservableEmitter<Boolean> e1) throws Exception {
                    GlideApp.with(context)
                            .load(l.getTrack_url())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    e1.onNext(false);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    e1.onNext(true);
                                    return false;
                                }
                            })
                            .preload();
                }
            }).compose(mRootView.<Boolean>bindToLifecycle()).subscribeOn(AndroidSchedulers.mainThread());
            observables.add(booleanObservable);
        }

        return observables;
    }


    @Override
    public void requestPreview(int id, int index, final Context context) {
        mRootView.showLoading();
        mComicApi.getComicPreViewById(id, index).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation())
                .compose(mRootView.<ComicPreView>bindToLifecycle())
                .flatMap(new Function<ComicPreView, ObservableSource<ComicPreView>>() {
                    @Override
                    public ObservableSource<ComicPreView> apply(final ComicPreView comicPreView) throws Exception {
                        final List<ComicPreView.PagesBean> list;
                        if (comicPreView.getPages().size() > 4) {
                            list = new ArrayList<>(comicPreView.getPages().subList(0, 4));
                        } else {
                            list = comicPreView.getPages();
                        }
                        List<Observable<Boolean>> glideObservable = createGlideObservable(list, context);
                        return Observable.combineLatest(glideObservable, new Function<Object[], ComicPreView>() {
                            @Override
                            public ComicPreView apply(Object[] objects) throws Exception {
                                return comicPreView;
                            }
                        }).compose(mRootView.<ComicPreView>bindToLifecycle());
                    }
                }).compose(mRootView.<ComicPreView>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new HttpResultFunction<ComicPreView>())
                .subscribe(new HttpRxObserver<>(new HttpRxObserver.IResult<ComicPreView>() {
                    @Override
                    public void onSuccess(ComicPreView comicPreView) {
                        mRootView.hideLoading();
                        mRootView.showPreview(comicPreView);
                    }

                    @Override
                    public void onError(ApiException e) {
                        mRootView.handleError(e);
                    }
                }));
    }
}
