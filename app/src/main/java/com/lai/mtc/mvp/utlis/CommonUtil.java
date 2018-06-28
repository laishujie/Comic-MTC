package com.lai.mtc.mvp.utlis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lai.mtc.comm.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 通用工具类
 */
public class CommonUtil {

    /**
     * 检查是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isAvailable();
    }


    /**
     * 检查是否是WIFI
     */
    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查是否是移动网络
     */
    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }


    /**
     * 以52为分页标准，切割最后
     *
     * @param list
     * @return
     */
    public static <T> List<T> transformationLastList(List<T> list) {
        if (!ListUtils.isEmpty(list) && list.size() > Parameter.PAGING_STANDARD) {
            return new ArrayList<>(list.subList(Parameter.PAGING_STANDARD, list.size()));
        }
        return null;
    }

    /**
     * 以52为分页标准，切割最前
     *
     * @param list
     * @return
     */
    public static <T> List<T> transformationStartList(List<T> list) {
        if (!ListUtils.isEmpty(list) && list.size() > Parameter.PAGING_STANDARD) {
            return new ArrayList<>(list.subList(0, Parameter.PAGING_STANDARD));
        }
        return null;
    }
}
