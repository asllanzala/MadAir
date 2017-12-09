package com.honeywell.hch.airtouch.library.http.model;


/**
 * Created by nan.liu on 1/21/15.
 */
public class StatusCode {

    public static final int OK = 200;
    public static final int CREATE_OK = 201;
    public static final int SMS_OK = 204;
    public static final int BAD_REQUEST = 400;
    public static final int UNAUTHORIZED = 401;
    public static final int NOT_FOUND = 404;
    public static final int EXCEPTION = 601;
    public static final int NETWORK_TIMEOUT = 999;
    public static final int NETWORK_ERROR = 1000;


    public static final int DOWN_LOAD_FAULT = 2000;

    /**
     * erren response code isor code wh 200 but no response data
     */
    public static final int NO_RESPONSE_DATA = 1001;



    /**
     * error code when return response is null
     */
    public static final int RETURN_RESPONSE_NULL = 1002;

    /**
     * error code when register error
     */
    public static final int NO_RIGSTER_ERROR = 1003;

    public static final int SEND_VERIFY_CODE_ERROR = 1004;
    public static final int VERIFY_CODE_ERROR = 1005;


    /**
     * 请求错误，或是解析数据错误
     */
    public static final int REQUEST_ERROR = 1006;

    public static final int UNACTIVATED_ACCOUNT = 9001;

    /**
     * 刷新session时候，服务器返回密码错误的，需要提示用户名密码错误，并退出
     */
    public static final int UPDATE_SESSION_PASSWORD_CHANGE = 9002;


    /**
     * 手机和设备端交互时候，设备重连wifi失败后。
     */
    public static final int RE_CONNECT_DEVICE_WIFI_ERROR = 9003;

    /**
     * 手机和设备端交互时候，手机连接上设备网络，但是由于走的是其他网络通道，导致和设备通信无法正常完成,。
     */
    public static final int CONNECT_WIFI_BUT_CANNOT_COMMUNICATION = 9004;

    /**
     * 手机和设备端交互时候，手机连接上设备网络，但是没有走其他网络通道，通信失败
     */
    public static final int CONNECT_WIFI_BUT_SOCKETTIMEOUT = 9005;
}

