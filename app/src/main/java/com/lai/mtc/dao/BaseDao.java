package com.lai.mtc.dao;

import com.lai.mtc.MTC;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import io.objectbox.Box;

/**
 * @author Lai
 * @time 2018/2/10 17:36
 * @describe 增删改查基类
 */
public class BaseDao<T> {

    public Box<T> getBox() {
        return mBox;
    }

    protected Box<T> mBox;

    private Class<T> mTClass;

    protected BaseDao() {
        //获取 T.class
        mTClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        mBox = MTC.getBoxStore().boxFor(mTClass);
    }

    /**
     * 根据Id查找
     *
     * @param id
     * @return
     */
    public T getById(long id) {
        return getBox().get(id);
    }

    /**
     * 是否存在
     *
     * @param id id
     * @return 是否
     */
    public boolean isCollection(int id) {
        return getById(id) != null;
    }

    /**
     * 取得所有的集合
     *
     * @return
     */
    public List<T> getAll() {
        return mBox.getAll();
    }

    /**
     * 根据Id删除
     *
     * @param id
     */
    public void delete(int id) {
        mBox.remove(id);
    }

    public void deleteAll() {
        mBox.removeAll();
    }
}
