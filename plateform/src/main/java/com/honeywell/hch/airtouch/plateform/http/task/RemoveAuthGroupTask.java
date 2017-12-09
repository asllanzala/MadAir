package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;


/**
 * Created by Jin Qian on 2/14/16.
 */
public class RemoveAuthGroupTask extends BaseRequestTask {
    private String mSessionId;
    private int mDeviceId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public RemoveAuthGroupTask(int deviceId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mDeviceId = deviceId;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.REMOVE_GROUP_AUTH);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .removeGroupAuth(mDeviceId, mSessionId, mRequestParams, mIReceiveResponse);

            HttpProxy.getInstance().getWebService().getLocation(UserInfoSharePreference.getUserId(), UserInfoSharePreference.getSessionId(), null, null);

            return result;
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
