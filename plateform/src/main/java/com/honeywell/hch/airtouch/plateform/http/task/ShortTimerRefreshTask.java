package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;


/**
 * Created by wuyuan on 13/10/2015.
 * this task is used for refresh the datas that need to refresh frequently
 * like location,device runstatus.
 */
public class ShortTimerRefreshTask extends BaseRequestTask {

    public static boolean isRefreshTaskRunning = false;

    private IActivityReceive mActivityReceive;

    public ShortTimerRefreshTask() {
    }

    public ShortTimerRefreshTask(IActivityReceive iReceiveResponse) {
        mActivityReceive = iReceiveResponse;
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {
        isRefreshTaskRunning = true;

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.SHORT_TIMER_REFRESH);
        if (reLoginResult.isResult()) {

            //get location
            ResponseResult getLocationResult = HttpProxy.getInstance().getWebService().getLocation(UserInfoSharePreference.getUserId(), UserInfoSharePreference.getSessionId(), null, null);
            if (getLocationResult.isResult()) {
                reloadDeviceInfo();
            }

            getUnreadMessage();

            getLocationResult.setRequestId(RequestID.SHORT_TIMER_REFRESH);
            return getLocationResult;
        } else {
            UserAllDataContainer.shareInstance().setDashboardLoadFailed();
        }

        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        isRefreshTaskRunning = false;

        //如果返回码为NETWORK_ERROR，NETWORK_TIMEOUT，就不需要更新界面，界面会收到 NetWORK_ERROR的广播
        if (responseResult != null &&
                responseResult.getResponseCode() != StatusCode.NETWORK_ERROR && responseResult.getResponseCode() != StatusCode.NETWORK_TIMEOUT) {
            Intent boradIntent = new Intent(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
            AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
        }

        if (mActivityReceive != null) {
            mActivityReceive.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
