package com.honeywell.hch.airtouch.ui.control.manager.device;


import android.content.Intent;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceControlRequest;
import com.honeywell.hch.airtouch.plateform.http.model.device.WaterDeviceControlRequest;
import com.honeywell.hch.airtouch.plateform.http.task.CommTask;
import com.honeywell.hch.airtouch.plateform.http.task.ControlDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.ControlWaterDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetConfigTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;


/**
 * Web service for Device control and filter
 * Created by Qian Jin on 4/15/16.
 */
public class ControlManager implements IControlManager{

    private int mDeviceId = 0;

    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;


    //umeng 统计使用
    private String mProductName = "";
    private String mScenarioOrSpeed = "";

    public ControlManager() {

    }


    @Override
    public void setSuccessCallback(SuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }

    @Override
    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }

    @Override
    public void controlDevice(int deviceId, String scenarioOrSpeed,String productName) {
        mProductName = productName;
        mScenarioOrSpeed = scenarioOrSpeed;

        mDeviceId = deviceId;
        setIsFlashing(true);
        setControlModePre(mDeviceId, scenarioOrSpeed);
        DeviceControlRequest deviceControlRequest = new DeviceControlRequest(scenarioOrSpeed);

        ControlDeviceTask requestTask
                = new ControlDeviceTask(deviceId, deviceControlRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    @Override
    public void controlWaterDevice(int deviceId, int scenario,String productName) {
        mProductName = productName;
        mDeviceId = deviceId;
        setIsFlashing(true);
        setControlModePre(mDeviceId, String.valueOf(scenario));
        WaterDeviceControlRequest waterDeviceControlRequest = new WaterDeviceControlRequest(scenario);

        ControlWaterDeviceTask requestTask
                = new ControlWaterDeviceTask(deviceId, waterDeviceControlRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    @Override
    public void getConfigFromServer() {
        GetConfigTask requestTask = new GetConfigTask(mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public IActivityReceive mResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case CONTROL_DEVICE:
                case WATER_CONTROL_DEVICE:
                    if (responseResult.getResponseCode() == StatusCode.OK) {
                        if (responseResult.getResponseData() != null) {
                            int commTaskId = responseResult.getResponseData()
                                    .getInt(HPlusConstants.COMM_TASK_BUNDLE_KEY);
                            LogUtil.log(LogUtil.LogLevel.INFO, "WaterControlAndFilterFragment", "commTaskId: " + commTaskId);
//                            mControlDeviceCount++;
                            runCommTaskThread(commTaskId);
                        } else {
                            callBackErrorMethod(responseResult, R.string.enroll_error);
                            handleControlCallBack();
                            UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_FAIL,"control_failed_responseResult.getResponseData() is null");
                        }
                    } else {
                        callBackErrorMethod(responseResult, R.string.enroll_error);
                        handleControlCallBack();
                        UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_FAIL,"control_failed_"+ responseResult.getResponseCode());

                    }
                    break;

                case GET_ALL_DEVICE_TYPE_CONFIG:
                    if (responseResult.isResult()) {
                        callBackSuccessMethod(responseResult);
                    }
                    break;

                default:
//                    callBackErrorMethod(responseResult, R.string.enroll_error);
                    break;
            }
        }
    };

    @Override
    public void runCommTaskThread(final int taskId) {
        LogUtil.log(LogUtil.LogLevel.INFO, "WaterControlAndFilterFragment", "taskId: " + taskId);
        final IActivityReceive runCommTaskResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                LogUtil.log(LogUtil.LogLevel.INFO, "WaterControlAndFilterFragment", "taskId: " + responseResult);
                switch (responseResult.getRequestId()) {
                    case COMM_TASK:
                        if (responseResult.isResult()) {

                            LogUtil.log(LogUtil.LogLevel.INFO, "WaterControlAndFilterFragment", "responseResult.getFlag(): "
                                    + responseResult.getFlag());
                            switch (responseResult.getFlag()) {

                                case HPlusConstants.COMM_TASK_SUCCEED:
                                    callBackSuccessMethod(responseResult);
                                    handleControlCallBack();
                                    UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_SUCCESS,"");

                                    break;

                                case HPlusConstants.COMM_TASK_FAILED:
                                    callBackErrorMethod(responseResult, R.string.enroll_error);
                                    handleControlCallBack();
                                    UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_FAIL,"cmmtask failed");

                                    break;

                                case HPlusConstants.COMM_TASK_TIMEOUT:
                                    callBackErrorMethod(responseResult, R.string.control_timeout);
                                    handleControlCallBack();
                                    UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_SUCCESS,"");

                                    break;

                                default:
                                    callBackErrorMethod(responseResult, R.string.enroll_error);
                                    handleControlCallBack();
                                    UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_FAIL,"cmmtask failed flag_" + responseResult.getFlag());

                                    break;
                            }
                        } else {
                            callBackErrorMethod(responseResult, R.string.enroll_error);
                            handleControlCallBack();
                            UmengUtil.deviceControlEvent(mProductName,mScenarioOrSpeed, UmengUtil.DeviceControlType.DEVICE_CONTROL_FAIL,"cmmtask failed exception_" + responseResult.getExeptionMsg());
                        }

                        break;

                    default:
                        break;

                }

            }
        };

        CommTask commTask = new CommTask(taskId, null, runCommTaskResponse, false);
        AsyncTaskExecutorUtil.executeAsyncTask(commTask);

    }

    public void callBackErrorMethod(ResponseResult responseResult, int msgStrId) {
        if (mErrorCallback != null) {
            mErrorCallback.onError(responseResult, msgStrId);
        }
    }

    public void callBackSuccessMethod(ResponseResult responseResult) {
        if (mSuccessCallback != null) {
            mSuccessCallback.onSuccess(responseResult);
        }
    }

    private void handleControlCallBack(){
        setIsFlashing(false);
        sendStopFlashTask();
    }

    public void setControlModePre(int deviceId, String mode) {
        SharePreferenceUtil.setPrefString(HPlusConstants.PREFERENCE_DEVICE_CONTROL_MODE,
                Integer.toString(deviceId), mode);
    }

    public void setIsFlashing(boolean isFlashing) {
        SharePreferenceUtil.setPrefBoolean(HPlusConstants.PREFERENCE_DEVICE_CONTROL_FLASH,
                Integer.toString(mDeviceId), isFlashing);
    }

    private void sendStopFlashTask() {
        Intent intent = new Intent(HPlusConstants.DEVICE_CONTROL_BROADCAST_ACTION_STOP_FLASHINGTASK);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }


}
