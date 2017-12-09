package com.honeywell.hch.airtouch.library.http.model;

import com.honeywell.hch.airtouch.library.http.HTTPRequestManager;

/**
 * Created by nan.liu on 1/13/15.
 */
public class HTTPRequestParams {

    private HTTPRequestManager.RequestType mType;
    private String mUrl;
    private String mSessionID;
    private RequestID mRequestID;
    private int mRandomRequestID;
    private IRequestParams mOtherParams;
    private boolean isFromHoneywellServer = true;

    public HTTPRequestParams() {
    }

    public HTTPRequestParams(HTTPRequestManager.RequestType type, String url, String sessionID, RequestID requestID,
                             IRequestParams otherParams) {
        setType(type);
        setUrl(url);
        setSessionID(sessionID);
        setRequestID(requestID);
        setOtherParams(otherParams);
    }




    public boolean isFromHoneywellServer() {
        return isFromHoneywellServer;
    }

    public void setFromHoneywellServer(boolean fromHoneywellServer) {
        isFromHoneywellServer = fromHoneywellServer;
    }

    public HTTPRequestManager.RequestType getType() {
        return mType;
    }

    public void setType(HTTPRequestManager.RequestType type) {
        this.mType = type;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getSessionID() {
        return mSessionID;
    }

    public void setSessionID(String sessionID) {
        this.mSessionID = sessionID;
    }

    public RequestID getRequestID() {
        return mRequestID;
    }

    public void setRequestID(RequestID requestID) {
        this.mRequestID = requestID;
    }

    public int getRandomRequestID() {
        return mRandomRequestID;
    }

    public void setRandomRequestID(int randomRequestID) {
        mRandomRequestID = randomRequestID;
    }

    public IRequestParams getOtherParams() {
        return mOtherParams;
    }

    public void setOtherParams(IRequestParams otherParams) {
        this.mOtherParams = otherParams;
    }

}
