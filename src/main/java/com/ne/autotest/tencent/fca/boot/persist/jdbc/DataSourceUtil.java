package com.ne.autotest.tencent.fca.boot.persist.jdbc;

import com.ne.autotest.tencent.fca.boot.common.ThreadContext;

import javax.sql.DataSource;

public class DataSourceUtil {
    public DataSourceUtil() {
    }

    public static void switchDataSource(String key) {
        ThreadContext.put(DialectRoutingDatasource.DATASOURCE_NAME_KEY, key);
    }

    public static DataSource getCurrentDataSource() {
        DataSource dataSource = (DataSource)ThreadContext.get(DialectRoutingDatasource.DATASOURCE_KEY);
        return dataSource;
    }
}