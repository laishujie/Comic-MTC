package com.lai.mtc.dao;

import com.lai.mtc.bean.ComicListInfo;
import com.lai.mtc.dao.bean.MyCollection;

import java.util.Date;

/**
 * @author Lai
 * @time 2018/2/1 19:25
 * @describe describe
 */

public class CollectionDao extends BaseDao<MyCollection>{

    /**
     * 添加收藏。
     *
     * @param entriesBean 实体类
     * @return id
     */
    public void addCollection(ComicListInfo.EntriesBean entriesBean) {
        //long start = System.currentTimeMillis();

        MyCollection collection = new MyCollection();
        collection.setId(entriesBean.getId());
        collection.setCover(entriesBean.getCover());
        collection.setDesc(entriesBean.getDescription());
        collection.setTime(new Date());
        collection.setStatus(entriesBean.getStatus());
        collection.setAuthor(entriesBean.getAuthor());
        collection.setName(entriesBean.getName());
        mBox.put(collection);
        //更新组件信息
       /* List<Group> all = mGroupBox.getAll();
        //是否为空
        if (!ListUtils.isEmpty(all)) {
            Group group = all.get(all.size() - 1);
            //查看最后一组的列表是否
            if (group.getMyCollections().size() == 3) {
                //满了就建立一个新的组
                newGroup(collection);
            } else {
                //没满则添加
                group.getMyCollections().add(collection);
                //更新
                mGroupBox.put(group);
            }
        } else {
            //第一次直接新建
            newGroup(collection);
        }*/
        //long time = System.currentTimeMillis() - start;
        //Log.w("11111", "时间为" + time + " ms");
    }


}
