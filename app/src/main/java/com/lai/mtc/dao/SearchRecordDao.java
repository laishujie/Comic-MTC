package com.lai.mtc.dao;

import com.lai.mtc.dao.bean.SearchRecord;
import com.lai.mtc.dao.bean.SearchRecord_;

import io.objectbox.query.QueryBuilder;

/**
 * @author Lai
 * @time 2018/2/10 17:35
 * @describe 搜索记录
 */

public class SearchRecordDao extends BaseDao<SearchRecord> {

    public void addSearch(String str) {
        SearchRecord searchRecord = new SearchRecord();
        searchRecord.setInputName(str);
        mBox.put(searchRecord);
    }

    public boolean hasData(String str) {
        QueryBuilder<SearchRecord> equal = mBox.query().equal(SearchRecord_.inputName, str);
        SearchRecord unique = equal.build().findUnique();
        return unique != null;
    }
}
