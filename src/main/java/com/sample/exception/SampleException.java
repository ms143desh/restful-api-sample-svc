package com.sample.exception;

public class SampleException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String message;
    private String code;
    private String logref;
    private String errorMessageParameter;

    public SampleException() {
        super();
    }

    public SampleException(final String message, final Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public SampleException(String code, String message, String logref) {
        this.code = code;
        this.message = message;
        this.logref = logref;
    }

    public SampleException(final String message) {
        super(message);
        this.message = message;
    }

    public SampleException(final String message, String errorMessageParameter) {
        super(message);
        this.message = message;
        this.errorMessageParameter = errorMessageParameter;
    }

    public SampleException(final Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLogref() {
        return logref;
    }

    public void setLogref(String logref) {
        this.logref = logref;
    }

    public String getErrorMessageParameter() {
        return errorMessageParameter;
    }

    public void setErrorMessageParameter(String errorMessageParameter) {
        this.errorMessageParameter = errorMessageParameter;
    }
}
