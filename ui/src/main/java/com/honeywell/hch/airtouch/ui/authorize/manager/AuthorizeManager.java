package com.honeywell.hch.airtouch.ui.authorize.manager;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.request.AuthGroupIdsRequest;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.request.AuthUserRequest;
import com.honeywell.hch.airtouch.plateform.http.task.CheckAuthUserTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetAuthDevicesTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetAuthGroupDevicesTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetAuthMessageByIdTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetAuthMessagesTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetAuthUnreadMessageTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetDeviceListByGroupIdTask;
import com.honeywell.hch.airtouch.plateform.http.task.GrantAuthToDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.HandleAuthMessageTask;
import com.honeywell.hch.airtouch.plateform.http.task.RemoveAuthDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.RemoveAuthGroupTask;
import com.honeywell.hch.airtouch.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 2/1/16.
 */
public class AuthorizeManager {
    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;
    private List<String> mPhoneNumbers;

    public AuthorizeManager() {
        mPhoneNumbers = new ArrayList<>();
        mPhoneNumbers.add("15800349135");
    }

    public interface SuccessCallback {
        void onSuccess(ResponseResult responseResult);
    }

    public interface ErrorCallback {
        void onError(ResponseResult responseResult, int id);
    }

    public void setSuccessCallback(SuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }


    IActivityReceive mResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case CHECK_AUTH_USER:
                case GRANT_AUTH_TO_DEVICE:
                case GET_AUTH_MESSAGES:
                case HANDLE_AUTH_MESSAGE:
                case REMOVE_DEVICE_AUTH:
                case GET_AUTH_DEVICES:
                case GET_AUTH_MESSAGE_BY_ID:
                case Get_AUTH_GROUP_DEVICES:
                case REMOVE_GROUP_AUTH:
                case GET_DEVICE_LIST_BY_GROUP_ID:
                    if (responseResult.isResult()) {
                        handleResponseCode(responseResult);
                    } else {
                        if (!responseResult.isAutoRefresh() ||
                                (responseResult.isAutoRefresh() && responseResult.getResponseCode() != StatusCode.NETWORK_ERROR && responseResult.getResponseCode() != StatusCode.NETWORK_TIMEOUT)) {
                            mErrorCallback.onError(responseResult, R.string.authorize_response_error);
                        }
                    }
                    break;

                case GET_AUTH_UNREAD_MESSAGE:
                    if (responseResult.isResult()) {
                        handleResponseCode(responseResult);
                    }

                default:
                    break;
            }
        }
    };

    public void getAuthUnreadMessage() {
        GetAuthUnreadMessageTask requestTask
                = new GetAuthUnreadMessageTask(null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void checkAuthUser(List<String> phoneNumbers) {
        AuthUserRequest request = new AuthUserRequest(phoneNumbers);
        CheckAuthUserTask requestTask
                = new CheckAuthUserTask(request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void grantAuthToDevice(int deviceId, int assignRole, List<String> phoneNumbers) {
        AuthUserRequest request = new AuthUserRequest(phoneNumbers);
        GrantAuthToDeviceTask requestTask
                = new GrantAuthToDeviceTask(deviceId, assignRole, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void removeAuthDevice(int deviceId) {
        AuthUserRequest request = new AuthUserRequest(mPhoneNumbers);
        RemoveAuthDeviceTask requestTask
                = new RemoveAuthDeviceTask(deviceId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getInvitations(int pageIndex, int pageSize, int loadMode) {
        GetAuthMessagesTask requestTask
                = new GetAuthMessagesTask(pageIndex, pageSize, null, mResponse, loadMode);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getInvitationsById(int messageId) {
        GetAuthMessageByIdTask requestTask
                = new GetAuthMessageByIdTask(messageId, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void handleInvitation(int invitationId, int actionId) {
        AuthUserRequest request = new AuthUserRequest(mPhoneNumbers);
        HandleAuthMessageTask requestTask
                = new HandleAuthMessageTask(invitationId, actionId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getAuthDevices() {
        GetAuthDevicesTask requestTask
                = new GetAuthDevicesTask(null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getAuthGroupDevices() {
        GetAuthGroupDevicesTask requestTask
                = new GetAuthGroupDevicesTask(null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }


    public void removeAuthGroup(int groupId) {
        AuthUserRequest request = new AuthUserRequest(mPhoneNumbers);
        RemoveAuthGroupTask requestTask
                = new RemoveAuthGroupTask(groupId, request, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getDeviceListByGroupId(List<Integer> groupIds, boolean isAutoRefresh) {
        AuthGroupIdsRequest request = new AuthGroupIdsRequest(groupIds);
        GetDeviceListByGroupIdTask requestTask
                = new GetDeviceListByGroupIdTask(request, mResponse, isAutoRefresh);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    private void handleResponseCode(ResponseResult result) {
        switch (result.getResponseCode()) {
            case StatusCode.OK:
                if (mSuccessCallback != null) {
                    mSuccessCallback.onSuccess(result);
                }
                break;
            default:
                if (mErrorCallback != null) {
                    mErrorCallback.onError(result, R.string.enroll_error);
                }
                break;
        }
    }

}
