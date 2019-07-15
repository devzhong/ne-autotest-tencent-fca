package com.ne.autotest.tencent.fca.boot.persist.hbase;

import org.apache.hadoop.hbase.client.Table;

public interface TableCallback<T> {
    T doInTable(Table var1) throws Throwable;
}

