package com.ne.autotest.tencent.fca.boot.common.util;

import com.ne.autotest.tencent.fca.boot.common.ThreadContext;

public class SignUtil {
    public SignUtil() {
    }

    public static String getAppId() {
        return (String) ThreadContext.get("appId");
    }
}