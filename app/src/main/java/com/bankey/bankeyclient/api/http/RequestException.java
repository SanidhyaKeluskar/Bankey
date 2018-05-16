package com.bankey.bankeyclient.api.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.net.SocketTimeoutException;


/**
 * Exception will be thrown if any exception occurred during HTTP request or response code os not 200
 */
public class RequestException extends Exception {

    public enum ResultType {
        HTTP_ERROR, // Status code is not 200
        CONNECTION_ERROR,
        TIMEOUT_ERROR,
        PARSER_ERROR, // Exception occurred on parser level
        RESPONSE_ERROR, // Response 200 but it contains some errors (code and message)
    }

    public final HttpResponse response;
    public @Nullable
    final Exception exception;
    public final ResultType resultType;

    public RequestException(@NonNull HttpResponse response, @Nullable Exception exception) {
        this.response = response;
        this.exception = exception;

        if (exception == null) {
            resultType = ResultType.HTTP_ERROR;
        }

        // Response errors or parser exception
        else if (exception instanceof ResponseError) {
            ResponseError responseError = (ResponseError) exception;
            if (responseError.getException() == null) {
                resultType = ResultType.RESPONSE_ERROR;
            } else {
                resultType = ResultType.PARSER_ERROR;
            }
        }

        else if (exception instanceof SocketTimeoutException) {
            resultType = ResultType.TIMEOUT_ERROR;
        } else {
            resultType = ResultType.CONNECTION_ERROR;
        }
    }

    @Override
    public String getMessage() {
        return exception != null ? exception.getMessage() : "Response error " + response.status; // TODO Hardcoded
    }
}
