package com.honeywell.hch.airtouch.library.http.model;

/**
 * Created by nan.liu on 2/2/15.
 */
public interface IActivityReceive
{
    /**
     * Called when response is received.
     *
     * @param responseResult ResponseResult from request.
     */
    void onReceive(ResponseResult responseResult);
}
