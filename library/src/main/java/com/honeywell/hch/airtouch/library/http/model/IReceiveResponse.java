package com.honeywell.hch.airtouch.library.http.model;

/**
 * Created by nan.liu on 2/2/15.
 */
public interface IReceiveResponse {
    /**
     * Called when response is received.
     *
     * @param httpRequestResponse The response that received from server.
     */
    public void onReceive(HTTPRequestResponse httpRequestResponse);
}
