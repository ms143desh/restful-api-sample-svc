package com.sample.exception;

/** @author deshaggarwal */
public class ApiError {

    private String code;
    private String message;
    private String logref;

    public ApiError(String code, String logref, String message) {
        this.code = code;
        this.logref = logref;
        this.message = message;
    }

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ApiError that = (ApiError) o;
        return (code != null) ? (code.equals(that.code)) : (that.code == null);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    public String getLogref() {
        return logref;
    }
}
