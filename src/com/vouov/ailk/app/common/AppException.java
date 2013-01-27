package com.vouov.ailk.app.common;

/**
 * User: yuml
 * Date: 13-1-17
 * Time: 下午7:50
 */
public class AppException extends  Exception {
    public AppException(String detailMessage) {
        super(detailMessage);
    }

    public AppException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AppException(Throwable throwable) {
        super(throwable);
    }
}
