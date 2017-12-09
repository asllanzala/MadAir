package com.honeywell.hch.airtouch.plateform.http.task;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.HashMap;

/**
 * Created by Jin Qian on 10/17/15.
 */
public class DeleteDeviceListTask extends BaseRequestTask {
    private Integer[] mDeviceIdList;
    private String mSessionId;
    private String mUserId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public DeleteDeviceListTask(Integer[] deviceIdList, UserLocationData userLocationDataItem, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;

        mDeviceIdList = deviceIdList;
        mSessionId = UserInfoSharePreference.getSessionId();
        mUserId =  UserInfoSharePreference.getUserId();
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.DELETE_DEVICE);
        if (reLoginResult.isResult()) {
            boolean isHasSuccess = false;
            int errorCode = StatusCode.OK;
            HashMap<Integer,Integer> idAndCmmKeyMap = new HashMap<>();
            for (int deviceIdItem : mDeviceIdList){
                idAndCmmKeyMap.put(deviceIdItem, HPlusConstants.DEFAULT_COMD_TASK_KEY);
                ResponseResult result = HttpProxy.getInstance().getWebService()
                        .deleteDevice(deviceIdItem, mSessionId,
                                mRequestParams, mIReceiveResponse);
                if (result.isResult()){
                    isHasSuccess = true;
                    idAndCmmKeyMap.put(deviceIdItem, result.getResponseData().getInt(HPlusConstants.COMM_TASK_BUNDLE_KEY));
                }else{
                    errorCode = result.getResponseCode();
                }
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(HPlusConstants.COMM_TASK_MAP_BUNDLE_KEY, idAndCmmKeyMap);
            if (!isHasSuccess){
                reLoginResult.setResult(false);
                reLoginResult.setResponseCode(errorCode);
                reLoginResult.setExceptionMsg("");
            }
            reLoginResult.setResponseData(bundle);
        }
        return  reLoginResult;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
