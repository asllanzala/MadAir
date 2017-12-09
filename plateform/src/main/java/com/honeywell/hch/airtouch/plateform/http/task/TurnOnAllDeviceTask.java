package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceControlRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.List;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class TurnOnAllDeviceTask extends BaseRequestTask {

    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private List<Integer> mDeviceIdList;

    public TurnOnAllDeviceTask(List<Integer> deviceIdList, String mCmmdStr, IActivityReceive iReceiveResponse) {
        DeviceControlRequest deviceControlRequest = new DeviceControlRequest(mCmmdStr);
        this.mRequestParams = deviceControlRequest;
        this.mIReceiveResponse = iReceiveResponse;
        mDeviceIdList = deviceIdList;
        mSessionId = UserInfoSharePreference.getSessionId();

    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        if (mDeviceIdList != null || mDeviceIdList.size() ==0){
            new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", RequestID.CONTROL_DEVICE);
        }

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.CONTROL_DEVICE);
        if (reLoginResult.isResult()) {
            for (int i = 0; i < mDeviceIdList.size(); i++){
                HttpProxy.getInstance().getWebService().turnOnDevice(mDeviceIdList.get(i),mSessionId,mRequestParams,null);
            }
        }
        return  new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", RequestID.CONTROL_DEVICE);

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
