package com.lai.mtc.mvp.utlis;

import java.util.List;

/**
 * List相关的辅助类
 * Created by chenmingzhen on 16-6-1.
 */
public class ListUtils {
    /**
     * 获取list大小
     *
     * @param sourceList
     * @return sourceList 返回list的大小, 若是null或者空的话返回 0
     */
    public static <V> int getSize(List<V> sourceList) {
        return null == sourceList ? 0 : sourceList.size();
    }

    /**
     * 判断list是否为空
     *
     * @param sourceList
     * @return 是否为空
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (null == sourceList || 0 == sourceList.size());
    }


    /**
     * 返回不多于最大数限制的列表元素个数
     *
     * @param list
     * @param maxNum
     * @param <T>
     * @return
     */
    public static <T> List<T> atMost(List<T> list, int maxNum) {
        if (ListUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > maxNum) {
            return list.subList(0, maxNum);
        } else {
            return list;
        }
    }

}
