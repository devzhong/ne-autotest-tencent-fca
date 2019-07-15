package com.ne.autotest.tencent.fca.boot.common.exception;

/**
 * desc:
 * author:zhongjianbin
 * Date:2019/7/13 20:15
 */
public interface IError {
    String getNamespace();

    String getErrorCode();

    String getErrorMessage();
}
