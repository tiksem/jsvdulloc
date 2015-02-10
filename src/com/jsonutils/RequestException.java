package com.jsonutils;

import java.io.IOException;

/**
 * Created by CM on 12/26/2014.
 */
public class RequestException extends IOException {
    private ExceptionInfo exceptionInfo;

    public RequestException(ExceptionInfo exceptionInfo) {
        super(exceptionInfo.getMessage());
        this.exceptionInfo = exceptionInfo;
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }
}
