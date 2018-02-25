package com.lai.mtc.mvp.contract;


import android.content.Context;

import com.lai.mtc.bean.ComicListDetail;
import com.lai.mtc.bean.ComicPreView;
import com.lai.mtc.mvp.base.IModel;
import com.lai.mtc.mvp.base.IView;

/**
 * @author Lai
 * @time 2017/12/11 17:01
 * @describe 模版
 */

public class ComicsPreviewContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    public interface View extends IView {
        void showPreview(ComicPreView comicPreView);
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
    public interface Model extends IModel {
        void requestPreview(int id, int cid, Context context);
    }
}
