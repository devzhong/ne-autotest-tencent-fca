package com.ne.autotest.tencent.fca.boot.persist.hbase.rowkey;

public enum Order {
    ASCENDING((byte)0),
    DESCENDING((byte)-1);

    private final byte mask;

    private Order(byte mask) {
        this.mask = mask;
    }

    byte mask() {
        return this.mask;
    }
}
