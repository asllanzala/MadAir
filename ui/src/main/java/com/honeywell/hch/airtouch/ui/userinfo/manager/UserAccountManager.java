package com.honeywell.hch.airtouch.ui.userinfo.manager;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.request.AuthUserRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.ChangePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SmsValidRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UpdatePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserRegisterRequest;
import com.honeywell.hch.airtouch.plateform.http.task.ChangePasswordTask;
import com.honeywell.hch.airtouch.plateform.http.task.CheckAuthUserTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetSmsCodeTask;
import com.honeywell.hch.airtouch.plateform.http.task.RegisterUserTask;
import com.honeywell.hch.airtouch.plateform.http.task.UpdatePasswordTask;
import com.honeywell.hch.airtouch.plateform.http.task.UserLoginTask;
import com.honeywell.hch.airtouch.plateform.http.task.ValidSmsCodeTask;

import java.util.List;

/**
 * create by H127856
 */
public class UserAccountManager {

    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public UserAccountManager() {

    }

    public interface SuccessCallback {
        void onSuccess(ResponseResult responseResult);
    }

    public interface ErrorCallback {
        void onError(ResponseResult responseResult);
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

    public void sendErrorCallBack(ResponseResult responseResult) {
        if (mErrorCallback != null && !responseResult.isAutoRefresh()) {
            mErrorCallback.onError(responseResult);
        }
    }

    public void startLogin(String moblePhone, String password) {
        UserLoginRequest userLoginRequest
                = new UserLoginRequest(moblePhone, password, AppConfig.APPLICATION_ID);
        UserLoginTask userLoginTask = new UserLoginTask(mReceiver, userLoginRequest);
        AsyncTaskExecutorUtil.executeAsyncTask(userLoginTask);
    }

    public void validSmsCode(String phoneNum, String smsNum) {
        ValidSmsCodeTask requestTask
                = new ValidSmsCodeTask(phoneNum, smsNum, null, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getSmsCode(String phoneNumber, String countryCode) {
        SmsValidRequest smsValidRequest
                = new SmsValidRequest(0, phoneNumber, "", countryCode);
        GetSmsCodeTask requestTask
                = new GetSmsCodeTask(smsValidRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void updatePassword(String mobilePhone, String newPassword) {
        UpdatePasswordRequest resetPasswordRequest = new UpdatePasswordRequest(mobilePhone, newPassword);
        UpdatePasswordTask requestTask
                = new UpdatePasswordTask(resetPasswordRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void changePassword(String oldPassword, String password) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, password);
        ChangePasswordTask requestTask
                = new ChangePasswordTask(changePasswordRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void registerNewUser(String nickname, String mobilePhone, String password, String countryCode) {
        UserRegisterRequest userRegisterRequest
                = new UserRegisterRequest(nickname, mobilePhone, password, countryCode);
        RegisterUserTask requestTask
                = new RegisterUserTask(userRegisterRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    /**
     * 验证账号是否是hplus用户
     *
     * @param phoneNumbers
     */
    public void checkAuthUser(List<String> phoneNumbers) {
        AuthUserRequest request = new AuthUserRequest(phoneNumbers);
        CheckAuthUserTask requestTask
                = new CheckAuthUserTask(request, mReceiver, false);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }


    IActivityReceive mReceiver = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case USER_LOGIN:
                    if (responseResult.isResult()) {
                        sendAfterLoginBroadCast();
                    }
                case GET_SMS_CODE:
                case VERIFY_SMS_VALID:
                case USER_REGISTER:
                case CHANGE_PASSWORD:
                case UPDATE_PASSWORD:
                case CHECK_AUTH_USER:
                    if (responseResult.isResult()) {
                        sendSuccessCallBack(responseResult);

                    } else {
                        sendErrorCallBack(responseResult);
                    }
                    break;

            }

        }
    };

    private void sendAfterLoginBroadCast() {
        Intent intent = new Intent(HPlusConstants.AFTER_USER_LOGIN);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }
}
