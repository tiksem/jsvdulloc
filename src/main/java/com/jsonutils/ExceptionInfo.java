package com.jsonutils;

/**
 * Created by CM on 12/26/2014.
 */
public class ExceptionInfo {
    private String error;
    private String message;
    private String cause;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
