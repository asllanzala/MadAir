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
public class HandleAuthMessageTask extends BaseRequestTask {
    private String mSessionId;
    private int mInvitationId;
    private int mActionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public HandleAuthMessageTask(int invitationId, int actionId, IRequestParams requestParams, IActivityReceive
            iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mInvitationId = invitationId;
        mActionId = actionId;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.HANDLE_AUTH_MESSAGE);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .handleAuthMessage(mInvitationId, mActionId, mSessionId, mRequestParams, mIReceiveResponse);

            if (result != null && result.isResult()){
                if (HPlusConstants.ACCEPTACTION == mActionId){
                    ResponseResult getLocationResult = HttpProxy.getInstance().getWebService().getLocation(UserInfoSharePreference.getUserId(), UserInfoSharePreference.getSessionId(), null, null);
                    if (getLocationResult.isResult()) {
                        reloadDeviceInfo();
                    }
                }
            }

            return result;
        }

        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        if (HPlusConstants.ACCEPTACTION == mActionId){
            Intent boradIntent = new Intent(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
            AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
        }
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
