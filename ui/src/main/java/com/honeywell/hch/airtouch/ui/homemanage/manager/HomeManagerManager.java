package com.honeywell.hch.airtouch.ui.homemanage.manager;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.AddLocationRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SwapLocationRequest;
import com.honeywell.hch.airtouch.plateform.http.task.AddLocationTask;
import com.honeywell.hch.airtouch.plateform.http.task.DeleteLocationTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetLocationTask;
import com.honeywell.hch.airtouch.plateform.http.task.SwapLocationNameTask;

/**
 * Created by Vincent on 13/7/16.
 */
public class HomeManagerManager {
    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public HomeManagerManager() {

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
                    case GET_LOCATION:
                    case ADD_LOCATION:
                    case SWAP_LOCATION:
                    case DELETE_LOCATION:
                        sendSuccessCallBack(responseResult);
                        break;
                }
            } else {
                //fail response
                switch (responseResult.getRequestId()) {
                    case GET_LOCATION:
                        sendErrorCallBack(responseResult, R.string.enroll_error);
                        break;
                    case ADD_LOCATION:
                        sendErrorCallBack(responseResult, R.string.home_manager_add_home_fail);
                        break;
                    case SWAP_LOCATION:
                        if(responseResult.getResponseCode() == StatusCode.NOT_FOUND){
                            sendErrorCallBack(responseResult, R.string.home_manager_delete_home_device_already);
                        }else {
                            sendErrorCallBack(responseResult, R.string.home_manager_edit_home_fail);
                        }
                        break;
                    case DELETE_LOCATION:
                        if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
                            sendErrorCallBack(responseResult, R.string.home_manager_delete_home_device_fail);
                        } else if(responseResult.getResponseCode() == StatusCode.NOT_FOUND){
                            sendErrorCallBack(responseResult, R.string.home_manager_delete_home_device_already);
                        } else {
                            sendErrorCallBack(responseResult, R.string.home_manager_delete_home_fail);
                        }
                        break;
                }
            }
        }
    };

    public void processRenameHome(String name, int locationId) {
        SwapLocationRequest swapLocationRequest = new SwapLocationRequest();
        swapLocationRequest.setName(name);
        SwapLocationNameTask requestTask = new SwapLocationNameTask(locationId,
                swapLocationRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void processRemoveHome(int locationId) {
        DeleteLocationTask requestTask
                = new DeleteLocationTask(locationId, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void processAddHome(String cityName, String homeName) {
        AddLocationRequest addLocationRequest = new AddLocationRequest();
        addLocationRequest.setCity(cityName);
        addLocationRequest.setName(homeName);
        AddLocationTask requestTask
                = new AddLocationTask(addLocationRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getLocation() {
        GetLocationTask requestTask
                = new GetLocationTask(mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public IActivityReceive getmResponse() {
        return mResponse;
    }


}
