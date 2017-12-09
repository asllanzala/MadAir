package com.honeywell.hch.airtouch.ui.emotion.manager;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.task.EmotionalBottleTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetDustAndPm25Task;
import com.honeywell.hch.airtouch.plateform.http.task.GetTotalDustTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetTotalVolumeTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetVolumeAndTdsTask;
import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by Vincent on 15/8/16.
 */
public class EmotionManager {
    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public EmotionManager() {
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

    IActivityReceive mResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {

            if (responseResult.isResult()
                    && (responseResult.getResponseCode() == StatusCode.OK)) {
                switch (responseResult.getRequestId()) {
                    case EMOTION_BOTTLE:
                    case GET_DUST_AND_PM25:
                    case GET_VOLUME_AND_TDS:
                    case GET_TOTAL_DUST:
                    case GET_TOTAL_VOLUME:
                        sendSuccessCallBack(responseResult);
                        break;
                }
            } else {
                //fail response
                switch (responseResult.getRequestId()) {
                    case EMOTION_BOTTLE:
                    case GET_DUST_AND_PM25:
                    case GET_VOLUME_AND_TDS:
                    case GET_TOTAL_DUST:
                    case GET_TOTAL_VOLUME:
                        sendErrorCallBack(responseResult, R.string.enroll_error);
                        break;
                }
            }
        }
    };

    //获得emotion 数据
    public void getPMLevelFromServer(int locationId, int requestTimeParamer) {
        EmotionalBottleTask requestTask = new EmotionalBottleTask(locationId, requestTimeParamer, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getDustAndPm25ByLocationID(int locationId, String fromDate, String toDate) {
        GetDustAndPm25Task getDustAndPm25Task = new GetDustAndPm25Task(locationId, fromDate, toDate, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(getDustAndPm25Task);
    }

    public void getVolumeAndTdsByLocationID(int locationId, String fromDate, String toDate) {
        GetVolumeAndTdsTask getVolumeAndTdsTask = new GetVolumeAndTdsTask(locationId, fromDate, toDate, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(getVolumeAndTdsTask);
    }

    public void getTotalDustByLocationID(int locationId) {
        GetTotalDustTask getTotalDustTask = new GetTotalDustTask(locationId, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(getTotalDustTask);
    }

    public void getTotalVolumeByLocationID(int locationId) {
        GetTotalVolumeTask getTotalVolumeTask = new GetTotalVolumeTask(locationId, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(getTotalVolumeTask);
    }

    public IActivityReceive getmResponse() {
        return mResponse;
    }
}
