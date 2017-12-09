package com.honeywell.hch.airtouch.plateform.http;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.model.user.FeedBackDeleteImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.ChangePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SmsValidRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UpdatePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserRegisterRequest;

/**
 * The MockWebService is used to replace HttpWebService when the unit tests are running.
 */
public class MockWebService implements IWebService {

    private ResponseResult mResponseResult;

    public void setResponseResult(ResponseResult result) {
        mResponseResult = result;
    }

    @Override
    public ResponseResult userRegister(UserRegisterRequest request, IActivityReceive
            receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult userLogin(UserLoginRequest request, IActivityReceive
            receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult checkMac(String macId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }


    @Override
    public ResponseResult swapLocationName(int locationId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult deleteLocation(int locationId, String sessionId, IActivityReceive
            receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult addLocation(String userId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult addDevice(int locationId, String sessionId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult deleteDevice(int deviceId, String sessionId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getCommTask(int taskId, String sessionId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getLocation(String userId, String sessionId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }


    @Override
    public ResponseResult createGroup(String sessionId, String groupName, int masterDeviceId,
            int locationId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult deleteGroup(String sessionId, int groupId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult addDeviceToGroup(String sessionId, int groupId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult deleteDeviceFromGroup(String sessionId, int groupId,
            IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult updateGroupName(String sessionId, String groupName, int groupId,
            IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getGroupByGroupId(String sessionId, int groupId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult isDeviceMaster(String sessionId, int deviceId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getGroupByLocationId(String sessionId, int locationId,
            IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult sendScenarioToGroup(String sessionId, int groupId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult multiCommTask(String sessionId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult turnOnDevice(int deviceId, String sessionId, IRequestParams request,
            IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult setArriveHomeTime(int locationId, String sessionId, IRequestParams request,
                                            IActivityReceive receiveResponse){
        return mResponseResult;
    }

    @Override
    public ResponseResult checkEnrollmentStyle(String deviceType, int userType, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getAuthUnreadMessage(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult checkAuthUser(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult grantAuthToDevice(int deviceId, int assignRole, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult removeDeviceAuth(int deviceId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getAuthMessages(int pageIndex, int pageSize, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getAuthMessageById(int messageId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult handleAuthMessage(int invitationId, int actionId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getAuthDevices(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult putUserClientInfo(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getAuthGroupDevices(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult removeGroupAuth(int deviceId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getDeviceListByGroupId(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult controlDevice(int deviceId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult emotionBottle(int locationId, int periodType, String sessionId, IActivityReceive receiveResponse) {
        return null;
    }

    @Override
    public ResponseResult updatePassword(UpdatePasswordRequest request, IActivityReceive receiveResponse) {
        return null;
    }

    @Override
    public ResponseResult changePassword(String userId, String sessionId, ChangePasswordRequest request, IActivityReceive receiveResponse) {
        return null;
    }

    @Override
    public ResponseResult getSmsCode(SmsValidRequest request, IActivityReceive receiveResponse) {
        return null;
    }

    @Override
    public ResponseResult verifySmsCode(String phoneNum, String smsCode, SmsValidRequest request, IActivityReceive receiveResponse) {
        return null;
    }

    @Override
    public ResponseResult refreshSession(UserLoginRequest request, RequestID requestID) {
        return null;
    }

    @Override
    public ResponseResult getAllDevicesRunstatus(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    public ResponseResult controlWaterDevice(int deviceId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getDevicesConfig(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult controlHomeDevice(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult quickAction(String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult GetMessagesPerPage(String mSessionId, int mFromRecordNo, int mPageSize, String mLanguage, int mCompareMsgPoolId, IRequestParams mRequestParams, IActivityReceive mIReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult DeleteMessages(String mSessionId, IRequestParams mRequestParams, IActivityReceive mIReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getMessageById(int messageId, String language, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult updateMessageStatus(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getUnreadMessages(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult feedBack(String userId, String sessionId, FeedBackRequest request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult feedBackImg(String userId, String sessionId, FeedBackImgRequest request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult feedBackDeleteImg(String userId, String sessionId, FeedBackDeleteImgRequest request, IActivityReceive receiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getDustAndPm25TaskByLocationId(int locationId, String fromDate, String toDate, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getVolumeAndTdsByLocationID(int locationId, String fromDate, String toDate, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getTotalDustByLocationID(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult getTotalVolumeByLocationID(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        return mResponseResult;
    }

    @Override
    public ResponseResult userValidate(UserLoginRequest request, RequestID requestID) {
        return mResponseResult;
    }
}