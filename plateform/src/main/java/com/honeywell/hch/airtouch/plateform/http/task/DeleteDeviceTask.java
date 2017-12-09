package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;


/**
 * Created by Jin Qian on 10/17/15.
 */
public class DeleteDeviceTask extends BaseRequestTask {
    private int mDeviceId;
    private String mSessionId;
    private String mUserId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    private UserLocationData mUserLocationData;

    public DeleteDeviceTask(int deviceId, UserLocationData userLocationDataItem, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mUserLocationData = userLocationDataItem;

        mDeviceId = deviceId;
        mSessionId = UserInfoSharePreference.getSessionId();
        mUserId =  UserInfoSharePreference.getUserId();
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.DELETE_DEVICE);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .deleteDevice(mDeviceId, mSessionId,
                            mRequestParams, mIReceiveResponse);

            if (result.isResult()){
                HttpProxy.getInstance().getWebService().getLocation(mUserId, mSessionId, null, mIReceiveResponse);
                reloadDeviceInfo();
            }
            return result;
        }
        return  reLoginResult;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        Intent boradIntent = new Intent(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
