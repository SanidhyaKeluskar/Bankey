package com.bankey.bankeyclient.api;

/**
 * Created by Dima on 24.02.2018.
 */

public interface GenericListener<T> {
    void onSuccess(T result);
    void onError(Exception error);
}
