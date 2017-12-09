package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;


/**
 * Created by Jin Qian on 15/7/2.
 */
public class DeleteLocationTask extends BaseRequestTask {
    private int mLocationId;
    private String mUserId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;

    public DeleteLocationTask(int locationId, IActivityReceive iReceiveResponse) {
        this.mLocationId = locationId;
        this.mIReceiveResponse = iReceiveResponse;

        mUserId = UserInfoSharePreference.getUserId();
        mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.DELETE_LOCATION);
        if (reLoginResult.isResult()) {
            ResponseResult deleteResult = HttpProxy.getInstance().getWebService()
                    .deleteLocation(mLocationId, mSessionId, mIReceiveResponse);
            if (deleteResult != null && deleteResult.isResult()){
                HttpProxy.getInstance().getWebService().getLocation(mUserId, mSessionId, null, mIReceiveResponse);
            }

            return deleteResult;
        }
        return reLoginResult;

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
