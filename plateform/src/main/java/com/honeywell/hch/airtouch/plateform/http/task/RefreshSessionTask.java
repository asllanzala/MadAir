package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginEnterpriseRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by h127856 on 16/10/17.
 * 用于测试wifi是否可以上网
 */
public class RefreshSessionTask extends BaseRequestTask {

    private boolean isCheckWifiConnected = false;
    private boolean isAbort = false;

    @Override
    protected ResponseResult doInBackground(Object... params) {
        ResponseResult responseResult = null;
        while (!isAbort){

            if (AppManager.isEnterPriseVersion()) {
                UserLoginEnterpriseRequest userLoginRequest = new UserLoginEnterpriseRequest(UserInfoSharePreference.getMobilePhone(),
                        UserInfoSharePreference.getPassword(), HPlusConstants.ENTERPRISE_TYPE, AppConfig.APPLICATION_ID);
                responseResult = HttpProxy.getInstance().getWebService().refreshSession(userLoginRequest, RequestID.REFRESH_SESSION);
            } else {
                UserLoginRequest userLoginRequest = new UserLoginRequest(UserInfoSharePreference.getMobilePhone(),
                        UserInfoSharePreference.getPassword(), AppConfig.APPLICATION_ID);
                responseResult = HttpProxy.getInstance().getWebService().refreshSession(userLoginRequest,  RequestID.REFRESH_SESSION);
            }

            if (responseResult != null && responseResult.getResponseCode() != StatusCode.NETWORK_ERROR &&
                    responseResult.getResponseCode() != StatusCode.NETWORK_TIMEOUT){
                isCheckWifiConnected = true;

                Intent boradIntent = new Intent(HPlusConstants.REFRESH_SESSION_ACTION);
                boradIntent.putExtra(HPlusConstants.HAS_WIFI_CONNECTED,true);
                AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
            }else {
                Intent boradIntent = new Intent(HPlusConstants.REFRESH_SESSION_ACTION);
                boradIntent.putExtra(HPlusConstants.HAS_WIFI_CONNECTED,false);

                AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
            }
            try {
                Thread.sleep(3000);
            }catch (Exception e){

            }
        }
        return responseResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        super.onPostExecute(responseResult);
    }

    public void setIsAbort(boolean isAbort){
        this.isAbort = isAbort;
    }
}
