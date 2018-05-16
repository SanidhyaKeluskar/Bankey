package com.bankey.bankeyclient.api.http;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientImpl {

    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_JSON = "application/json";

    interface Listener {
        void onRequestSuccess(HttpResponse response);
        void onRequestError(RequestException exception);
    }

    private final OkHttpClient mClient;
    private final List<Listener> mListeners = Collections.synchronizedList(new CopyOnWriteArrayList<Listener>());

    public OkHttpClientImpl() {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.MINUTES);
        builder.readTimeout(1, TimeUnit.MINUTES);
        builder.writeTimeout(1, TimeUnit.MINUTES);
        mClient = builder.build();
    }

    public HttpResponse performGET(String url, @NonNull Map<String, String> headers) throws RequestException {
        return performGET(url, headers, null);
    }

    public HttpResponse performGET(String url, @NonNull Map<String, String> headers, StreamParser parser) throws RequestException {
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .build();
        return performRequest(request, parser);
    }

    public HttpResponse performPUT(String url, String contentType, String body, @NonNull Map<String, String> headers, StreamParser parser) throws RequestException {
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), body);
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .put(requestBody)
                .build();
        return performRequest(request, parser);
    }

    public HttpResponse performPOST(String url, String contentType, String body, @NonNull Map<String, String> headers) throws RequestException {
        return performPOST(url, contentType, body, headers, null);
    }

    public HttpResponse performPOST(String url, String contentType, String body, @NonNull Map<String, String> headers, StreamParser parser) throws RequestException {
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), body);
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(requestBody)
                .build();
        return performRequest(request, parser);
    }

    public HttpResponse performPOST(String url, Map<String, String> headers, RequestBody requestBody, StreamParser parser) throws RequestException {
        Request request = new Request.Builder()
                .url(url)
                .headers(Headers.of(headers))
                .post(requestBody)
                .build();

        return performRequest(request, parser);
    }

    private HttpResponse performRequest(Request request, StreamParser parser) throws RequestException {
        String requestUrl = request.url().toString();
        int statusCode = HttpResponse.STATUS_INTERNAL_EXCEPTION;
        Map<String, List<String>> header = Collections.EMPTY_MAP;
        String body = "";

        RequestException exception = null;
        try {
            Response response = mClient.newCall(request).execute();
            boolean isSuccess = response.isSuccessful();
            statusCode = response.code();
            header = response.headers().toMultimap();

            if (parser == null) {
                // Get all body if stream parser is not specified
                body = response.body().string();
            } else if (isSuccess) {
                // Parse response using InputStream if parser specified
                parser.parseStream(response.body().byteStream());
            }

            if (!isSuccess) {
                exception = new RequestException(new HttpResponse(requestUrl, statusCode, header, body), null);
            }
        } catch (Exception e) {
            exception = new RequestException(new HttpResponse(requestUrl, statusCode, header, body), e);
        }

        if (exception != null) {
            // Notify error
            for (Listener l : mListeners) {
                l.onRequestError(exception);
            }
            throw exception;
        }

        HttpResponse response = new HttpResponse(request.url().toString(), statusCode, header, body);

        // Notify success
        for (Listener l : mListeners) {
            l.onRequestSuccess(response);
        }

        return response;
    }

    public void addListener(Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(Listener listener) {
        mListeners.remove(listener);
    }

}