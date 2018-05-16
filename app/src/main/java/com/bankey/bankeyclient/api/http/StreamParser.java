package com.bankey.bankeyclient.api.http;

import java.io.InputStream;

public interface StreamParser<T> {

    /**
     * Streaming data parsing (probably will be called inside HttpClient as it require inputStream)
     * @param inputStream
     * @return
     */
    T parseStream(InputStream inputStream) throws ResponseError;

    /**
     * @return parsing result
     */
    T getResult();
}
