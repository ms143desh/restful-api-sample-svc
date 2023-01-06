package com.sample.exception;

/** @author deshaggarwal */
public class ApiErrors {

    private ApiError[] errors;

    public ApiErrors(ApiError[] errors) {

        this.errors = errors;
    }

    /** @return the errorsArr */
    public ApiError[] getErrors() {
        return errors;
    }

    /** @param errorsArr the errorsArr to set */
    public void setErrorsArr(ApiError[] errors) {
        this.errors = errors;
    }
}
