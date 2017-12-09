package com.honeywell.hch.airtouch.library.http.model;

import android.os.Bundle;

/**
 * Created by wuyuan on 15/5/20.
 * return to activity
 */
public class ResponseResult
{
    /**
     * response result is true or false
     */
    private boolean result;

    /**
     * response result set for special cases
     */
    private int flag;

    /**
     * if result == true,responseCode is ignored.otherwise
     * responseCode is the value of response code like 400,500
     */
    private int responseCode = 200;

    /**
     * response data after data parse
     */
    private Bundle responseData = new Bundle();

    /**
     *if result == true,responseCode is ignored.otherwise
     * errorMsg is the exact error message from server
     */
    private String exceptionMsg = "";

    /**
     * if isAutoRefresh is true, it will not be give the error message to UI
     */
    private boolean isAutoRefresh = false;

    private RequestID requestId ;

    public ResponseResult() {}

    public ResponseResult(boolean result, int responseCode, String exceptionMsg, RequestID requestId)
    {
        this.result = result;
        this.responseCode = responseCode;
        this.exceptionMsg = exceptionMsg;
        this.requestId = requestId;
    }

    public ResponseResult(boolean result, RequestID requestId)
    {
        this.result = result;
        this.requestId = requestId;
    }

    public Bundle getResponseData() {
        return responseData;
    }

    public void setResponseData(Bundle responseData) {
        this.responseData = responseData;
    }

    public boolean isResult()
    {
        return result;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getExeptionMsg()
    {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg)
    {
        this.exceptionMsg = exceptionMsg;
    }

    public RequestID getRequestId()
    {
        return requestId;
    }

    public void setRequestId(RequestID requestId)
    {
        this.requestId = requestId;
    }

    public boolean isAutoRefresh() {
        return isAutoRefresh;
    }

    public void setIsAutoRefresh(boolean isAutoRefresh) {
        this.isAutoRefresh = isAutoRefresh;
    }
}
