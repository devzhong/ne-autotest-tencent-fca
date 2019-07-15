package com.ne.autotest.tencent.fca.boot.persist.jdbc;

public interface Dialect {
    String getPageString(String var1, Long var2, Long var3);

    String getLimitString(Long var1, Long var2);

    String getCountString(String var1);

    String getIdentityString();
}
