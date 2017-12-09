package com.honeywell.hch.airtouch.ui.control.manager.device;


import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.home.request.HomeControlRequest;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskRequest;
import com.honeywell.hch.airtouch.plateform.http.task.ControlHomeDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.MultiCommTask;

/**
 * Created by h127856 on 16/8/11.
 * 所有control类的基类：deviceControl,HomeControl以及groupControl
 */
public class ControlBaseManager {

    protected String mLastTaskId = "";


    protected SuccessCallback mSuccessCallback;
    protected ErrorCallback mErrorCallback;
    protected int mGroupId = 0;

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


    public void sendSuccessCallBack(ResponseResult responseResult) {
        if (mSuccessCallback != null) {
            mSuccessCallback.onSuccess(responseResult);
        }
    }

    public void sendErrorCallBack(ResponseResult responseResult, int strId) {
        if (mErrorCallback != null && !responseResult.isAutoRefresh()) {
            mErrorCallback.onError(responseResult, strId);
        }
    }


    public IActivityReceive mResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            handleResponseReceive(responseResult);
        }
    };


    public void runMultiCommTask(final MultiCommTaskRequest request, String controlTaskId,int commType) {
        final IActivityReceive runCommTaskResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {

                if (responseResult.isResult()) {
                    switch (responseResult.getRequestId()) {
                        case MULTI_COMM_TASK:
                            switch (responseResult.getFlag()) {
                                case HPlusConstants.COMM_TASK_SUCCEED:
                                case HPlusConstants.COMM_TASK_PART_FAILED:
                                case HPlusConstants.COMM_TASK_ALL_FAILED:
                                    dealMultiCommTaskResult(responseResult, true);
                                    break;
                            }
                            break;

                        default:
                            break;
                    }
                } else {
                    dealMultiCommTaskResult(responseResult, false);
                }
            }
        };

        MultiCommTask commTask = new MultiCommTask(request, runCommTaskResponse, controlTaskId,commType);
        AsyncTaskExecutorUtil.executeAsyncTask(commTask);
    }


    /**
     * 需要子类override
     */
    public void handleResponseReceive(ResponseResult responseResult) {

    }


    /**
     * 存储multi task的对象的唯一标识和对应model的一一对应关系
     */
    public void storageMultiTaskStatus(String taskId) {

    }


    public void dealMultiCommTaskResult(ResponseResult responseResult, boolean isResultTrue) {

    }

    public void controlHomeDevice(int locationId, int scenario) {
    }

    public void removeAllMessage(){
    }
}
