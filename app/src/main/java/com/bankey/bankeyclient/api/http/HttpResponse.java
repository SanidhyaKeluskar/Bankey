package com.bankey.bankeyclient.api.http;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    public static final int STATUS_INTERNAL_EXCEPTION = -1;

    public final int status;
    public final String url;
    public final Map<String, List<String>> headers;
    public final String body;

    public HttpResponse(String url) {
        this.url = url;
        this.status = 0;
        this.headers = Collections.EMPTY_MAP;
        this.body = "";
    }

    public HttpResponse(String url, int status, Map<String, List<String>> headers, String body) {
        this.url = url;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

}
