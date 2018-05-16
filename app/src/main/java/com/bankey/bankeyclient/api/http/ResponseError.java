package com.bankey.bankeyclient.api.http;

import android.support.annotation.Nullable;

/**
 * Throws if request is success (200) but response contains some error or exception occurred during parsing
 */
public class ResponseError extends Exception {

    public static final int INTERNAL_ERROR = -1; // Unknown exception on client side

    private final int errorCode;
    private final String errorMessage;
    private final Exception exception;

    public ResponseError(int errCode, String errMessage) {
        this.errorCode = errCode;
        this.errorMessage = errMessage;
        this.exception = null;
    }

    public ResponseError(Exception exception) {
        this.errorCode = INTERNAL_ERROR;
        this.errorMessage = exception.getClass().getSimpleName() + " " + exception.getMessage();
        this.exception = exception;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public @Nullable
    Exception getException() {
        return exception;
    }

    @Override
    public String getMessage() {
        return exception != null ? exception.getClass() + " " + exception.getMessage() : errorCode + " " + errorMessage;
    }
}
