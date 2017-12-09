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
 * Created by Jin Qian on 15/8/24.
 * Note that after addLocation successfully, getLocation and save the updates.
 */
public class AddLocationTask extends BaseRequestTask {
    private String mUserId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public AddLocationTask(IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;

        mUserId = UserInfoSharePreference.getUserId();
        mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.ADD_LOCATION);
        if (reLoginResult.isResult()) {
            ResponseResult addResult = HttpProxy.getInstance().getWebService()
                    .addLocation(UserInfoSharePreference.getUserId(),
                            UserInfoSharePreference.getSessionId(),
                            mRequestParams, mIReceiveResponse);


            if (addResult!= null && addResult.isResult()) {
                HttpProxy.getInstance().getWebService().getLocation(mUserId, mSessionId, null, mIReceiveResponse);
            }

            return addResult;
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
