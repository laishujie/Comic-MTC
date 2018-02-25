package com.lai.mtc.dao.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * @author Lai
 * @time 2018/2/1 20:19
 * @describe 组。一组默认三个本书
 */
@Entity
public class Group {
    @Id
    private long id;
    public ToMany<MyCollection> myCollections;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ToMany<MyCollection> getMyCollections() {
        return myCollections;
    }

    public void setMyCollections(ToMany<MyCollection> myCollections) {
        this.myCollections = myCollections;
    }
}
