package com.lai.mtc.dao.bean;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * @author Lai
 * @time 2018/2/10 17:34
 * @describe 搜索记录
 */
@Entity
public class SearchRecord {
    @Id
    private long id;
    private String inputName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInputName() {
        return inputName;
    }

    public void setInputName(String inputName) {
        this.inputName = inputName;
    }
}
