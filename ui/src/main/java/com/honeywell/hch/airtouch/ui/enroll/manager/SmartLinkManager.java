package com.honeywell.hch.airtouch.ui.enroll.manager;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.task.GetEnrollDeviceTypeTask;
import com.honeywell.hch.airtouch.plateform.http.task.SmartLinkTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;

/**
 * Created by Vincent on 14/4/16.
 */
public class SmartLinkManager {
    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public SmartLinkManager() {
    }

    public interface SuccessCallback {
        void onSuccess(ResponseResult responseResult);
    }

    public interface ErrorCallback {
        void onError(ResponseResult responseResult, int id);
    }

    public void setSuccessCallback(SuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }


    IActivityReceive checkTypeResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case ENROLL_DEVICE_TYPE:
                case GET_ENROLL_TYPE:
                    handleResponseCode(responseResult);
                    break;
                default:
                    break;
            }
        }
    };

    public void executeGetDeviceType(String deviceModel) {
        SmartLinkTask requestTask
                = new SmartLinkTask(deviceModel, null, checkTypeResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void executeDownLoadDeviceType() {
        GetEnrollDeviceTypeTask requestTask = new GetEnrollDeviceTypeTask(checkTypeResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }


    private void handleResponseCode(ResponseResult result) {
        switch (result.getResponseCode()) {
            case StatusCode.OK:
                if (mSuccessCallback != null){
                    mSuccessCallback.onSuccess(result);
                }
                break;
            case StatusCode.DOWN_LOAD_FAULT:
                if (mErrorCallback != null){
                    mErrorCallback.onError(result, R.string.enroll_device_model_error);
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL,"get enroll device type error");
                }

            default:
                if (mErrorCallback != null){
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL,"checkTypeResponse_"+ result.getResponseCode() + "_message_"+ result.getExeptionMsg());
                    mErrorCallback.onError(result, R.string.enroll_error);
                }
                break;
        }
    }
}
