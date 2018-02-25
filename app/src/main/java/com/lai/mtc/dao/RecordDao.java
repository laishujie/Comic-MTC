package com.lai.mtc.dao;

import com.lai.mtc.dao.bean.Record;

/**
 * @author Lai
 * @time 2018/2/7 17:11
 * @describe 集数记录
 */

public class RecordDao extends BaseDao<Record> {

    public void updateRecord(long id, String name, int index, int position) {
        Record record = new Record();
        record.setId(id);
        record.setName(name);
        record.setIndex(index);
        record.setPosition(position);
        mBox.put(record);
    }
}
