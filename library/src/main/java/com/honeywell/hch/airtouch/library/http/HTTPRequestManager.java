package com.honeywell.hch.airtouch.library.http;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestParams;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

/**
 * Manage http request
 * Created by nan.liu on 1/13/15.
 */
public class HTTPRequestManager {

    public enum RequestType {
        DELETE,
        POST,
        PUT,
        GET
    }

    private static final String TAG = HTTPRequestManager.class.getSimpleName();

    private HTTPRequestParams mHttpRequestParams;

    private long mConnectTimeout = 15 * 1000;
    private long mReadTimeout = 15 * 1000;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private boolean mLastNetworkIsWell = true;

    public HTTPRequestManager(HTTPRequestParams httpRequestParams) {
        this.mHttpRequestParams = httpRequestParams;
    }

    /**
     * Sets the default read timeout for new connections.
     *
     * @param readTimeout unit:second
     */
    public void setReadTimeout(long readTimeout) {
        mReadTimeout = readTimeout * 1000;
    }

    /**
     * Sets the connect timeout for new connections.
     *
     * @param connectTimeout unit:second
     */
    public void setConnectTimeout(long connectTimeout) {
        mConnectTimeout = connectTimeout * 1000;
    }

    /**
     * execute request
     */
    public HTTPRequestResponse sendRequest(OkHttpClient okHttpClient) {
        okHttpClient.setConnectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(mReadTimeout, TimeUnit.MILLISECONDS);
        Request.Builder requestBuilder = new Request.Builder()
                .url(mHttpRequestParams.getUrl());
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mConnectTimeout:" + mConnectTimeout
                + " mReadTimeout:" + mReadTimeout);

        if (mHttpRequestParams.getRequestID() == RequestID.COMM_TASK) {
            LogUtil.log(LogUtil.LogLevel.VERBOSE, TAG, "request [type]:" + mHttpRequestParams.getType().name()
                    + " [url]:" + mHttpRequestParams.getUrl()
                    + " [session]:" + mHttpRequestParams.getSessionID());
        } else {
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "request [type]:" + mHttpRequestParams.getType().name()
                    + " [url]:" + mHttpRequestParams.getUrl()
                    + " [session]:" + mHttpRequestParams.getSessionID());
        }

        RequestBody body = null;
        if (mHttpRequestParams.getOtherParams() != null) {
            Gson gson = new Gson();
            if (mHttpRequestParams.getRequestID() == RequestID.COMM_TASK) {
                LogUtil.log(LogUtil.LogLevel.VERBOSE, TAG, " [data]:" + mHttpRequestParams.getOtherParams().getPrintableRequest(gson));
            } else {
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, " [data]:" + mHttpRequestParams.getOtherParams().getPrintableRequest(gson));
            }
            body = RequestBody.create(JSON, mHttpRequestParams.getOtherParams()
                    .getRequest(gson));
        }
        switch (mHttpRequestParams.getType()) {
            case DELETE:
                requestBuilder.delete(body);
                break;
            case POST:
                requestBuilder.post(body);
                break;
            case PUT:
                requestBuilder.put(body);
                break;
            default:
                break;
        }
        if (!StringUtil.isEmpty(mHttpRequestParams.getSessionID())) {
            requestBuilder.addHeader("Cookie", "sessionId=" + mHttpRequestParams
                    .getSessionID());
        }
        Request request = requestBuilder.build();

        HTTPRequestResponse httpRequestResponse = new HTTPRequestResponse();
        httpRequestResponse.setRandomRequestID(mHttpRequestParams.getRandomRequestID());
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response != null) {
                httpRequestResponse.setStatusCode(response.code());
                String result = response.body().string();
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "response [data]:" + result);
                JsonElement jsonElem = new JsonParser().parse(result);
                if (jsonElem.isJsonArray() || jsonElem.isJsonObject() || jsonElem.isJsonPrimitive()) {
                    httpRequestResponse.setData(result);
                } else {
                    httpRequestResponse.setException(new JSONException(""));
                }
            }

        } catch (SocketTimeoutException e) {
            httpRequestResponse.setStatusCode(StatusCode.NETWORK_TIMEOUT);
        } catch (IOException e) {
            httpRequestResponse.setStatusCode(StatusCode.NETWORK_ERROR);
        } catch (Exception e) {
            httpRequestResponse.setStatusCode(StatusCode.REQUEST_ERROR);
            httpRequestResponse.setException(e);
        }

        httpRequestResponse.setRequestID(mHttpRequestParams.getRequestID());
        return httpRequestResponse;
    }

    public HTTPRequestResponse sendImgRequest(OkHttpClient okHttpClient) {
        okHttpClient.setConnectTimeout(mConnectTimeout, TimeUnit.MILLISECONDS);
        okHttpClient.setReadTimeout(mReadTimeout, TimeUnit.MILLISECONDS);
        Request.Builder requestBuilder = new Request.Builder()
                .url(mHttpRequestParams.getUrl());

        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mConnectTimeout:" + mConnectTimeout
                + " mReadTimeout:" + mReadTimeout);

        if (mHttpRequestParams.getRequestID() == RequestID.COMM_TASK) {
            LogUtil.log(LogUtil.LogLevel.VERBOSE, TAG, "request [type]:" + mHttpRequestParams.getType().name()
                    + " [url]:" + mHttpRequestParams.getUrl()
                    + " [session]:" + mHttpRequestParams.getSessionID());
        } else {
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "request [type]:" + mHttpRequestParams.getType().name()
                    + " [url]:" + mHttpRequestParams.getUrl()
                    + " [session]:" + mHttpRequestParams.getSessionID());
        }
//        String srt2 =null;
//        try {
//            srt2 = new String(mHttpRequestParams.getOtherImgParams().getRequest(),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        if(srt2 ==null){
//            return null;
//        }

        Gson gson = new Gson();

        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, " [data]:" + mHttpRequestParams.getOtherParams().getRequest(gson));
        RequestBody body = RequestBody.create(JSON, mHttpRequestParams.getOtherParams().getRequest(gson));
        requestBuilder.post(body);
        if (!StringUtil.isEmpty(mHttpRequestParams.getSessionID())) {
            requestBuilder.addHeader("Cookie", "sessionId=" + mHttpRequestParams
                    .getSessionID());
        }

        Request request = requestBuilder.build();
        final HTTPRequestResponse httpRequestResponse = new HTTPRequestResponse();
        httpRequestResponse.setRandomRequestID(mHttpRequestParams.getRandomRequestID());


//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                System.out.println("request = " + request.url());
//                System.out.println("上传失败:e.getLocalizedMessage() = " + e.getLocalizedMessage());
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//                if (response != null) {
//                    httpRequestResponse.setStatusCode(response.code());
//                    String result = response.body().string();
//                    LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "response [data]:" + result);
//                    JsonElement jsonElem = new JsonParser().parse(result);
//                    if (jsonElem.isJsonArray() || jsonElem.isJsonObject()) {
//                        httpRequestResponse.setData(result);
//                    } else {{
//                        httpRequestResponse.setException(new JSONException(""));
//                    }
//                        httpRequestResponse.setException(new JSONException(""));
//                    }
//                }
//            }
//        });


        try {
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "save image >>>>>>>>>>>>>>");
            Response response = okHttpClient.newCall(request).execute();
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "save image response [data]:" + response);
            if (response != null) {
                httpRequestResponse.setStatusCode(response.code());
                String result = response.body().string();
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "response [data]:" + result);
                JsonElement jsonElem = new JsonParser().parse(result);
                httpRequestResponse.setData(result);
//                if (jsonElem.isJsonArray() || jsonElem.isJsonObject()) {
//                    httpRequestResponse.setData(result);
//                } else {
//                    httpRequestResponse.setException(new JSONException(""));
//                }
            }

        } catch (SocketTimeoutException e) {
            httpRequestResponse.setStatusCode(StatusCode.NETWORK_TIMEOUT);
        } catch (IOException e) {
            httpRequestResponse.setStatusCode(StatusCode.NETWORK_ERROR);
        } catch (Exception e) {
            httpRequestResponse.setStatusCode(StatusCode.REQUEST_ERROR);
            httpRequestResponse.setException(e);
        }

        httpRequestResponse.setRequestID(mHttpRequestParams.getRequestID());
        return httpRequestResponse;
    }


    public HTTPRequestParams getHttpRequestParams() {
        return mHttpRequestParams;
    }

}
