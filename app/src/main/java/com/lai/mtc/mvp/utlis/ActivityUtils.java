package com.lai.mtc.mvp.utlis;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.LinkedList;
import java.util.List;

/**
 * 与Activity的基本操作相关的工具类
 */
public class ActivityUtils {

    private static List<Activity> mActivities = new LinkedList<>();

    /**
     * 将Fragment附加到Activity中
     * @NonNull:指明一个参数，字段或者方法的返回值不可以为null；
     */
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static void showFragmentOfActivity(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.commit();
    }

    public static void hideFragmentOfActivity(@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
    }

    /**
     * 添加Activity到自己的任务管理栈中
     *
     * @param activity 被压入栈的activity
     */
    public static void addActivity(@NonNull Activity activity) {
        if (mActivities == null) {
            mActivities = new LinkedList<>();
        }
        mActivities.add(activity);
    }

    /**
     * 弹出任务管理栈中的Activity
     */
    public static void removeActivity(@NonNull Activity activity) {
        if (mActivities != null && mActivities.contains(activity)) {
            mActivities.remove(activity);
        }
    }

    /**
     * 结束所有的Activity,退出程序
     */
    public static void removeAllActivity() {
        if (mActivities != null) {
            synchronized (ActivityUtils.class) {
                for (Activity activity : mActivities) {
                    activity.finish();
                }
            }
            mActivities.clear();
        }
    }

    /**
     * 默认的判断引用非空方法
     *
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

}
