package com.honeywell.hch.airtouch.ui.control.manager.device;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;

/**
 * Created by h127856 on 16/11/2.
 */
public interface IControlManager {

     interface SuccessCallback {
        void onSuccess(ResponseResult responseResult);
    }

     interface ErrorCallback {
        void onError(ResponseResult responseResult, int id);
     }

    void setSuccessCallback(SuccessCallback successCallback);


    void setErrorCallback(ErrorCallback errorCallback);

    void controlDevice(int deviceId, String scenarioOrSpeed,String productName);


    void controlWaterDevice(int deviceId, int scenario,String productName);

    void getConfigFromServer();

    void runCommTaskThread(final int taskId);
}
