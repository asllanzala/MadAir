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
 * Created by lynnliu on 9/15/15.
 */
public interface IWebService {

    public ResponseResult userRegister(UserRegisterRequest request, IActivityReceive
            receiveResponse);

    public ResponseResult userLogin(UserLoginRequest request, IActivityReceive
            receiveResponse);

    public ResponseResult checkMac(String macId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse);

    public ResponseResult emotionBottle(int locationId, int periodType, String sessionId, IActivityReceive receiveResponse);

    public ResponseResult swapLocationName(int locationId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse);

    public ResponseResult deleteLocation(int locationId, String sessionId, IActivityReceive
            receiveResponse);

    public ResponseResult addLocation(String userId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse);

    public ResponseResult addDevice(int locationId, String sessionId, IRequestParams request,
                                    IActivityReceive receiveResponse);

    public ResponseResult deleteDevice(int deviceId, String sessionId, IRequestParams request,
                                       IActivityReceive receiveResponse);

    public ResponseResult getCommTask(int taskId, String sessionId, IRequestParams request,
                                      IActivityReceive receiveResponse);

    public ResponseResult getLocation(String userId, String sessionId, IRequestParams request,
                                      IActivityReceive receiveResponse);


    public ResponseResult createGroup(String sessionId, String groupName, int masterDeviceId, int locationId,
                                      IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult deleteGroup(String sessionId, int groupId,
                                      IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult addDeviceToGroup(String sessionId, int groupId,
                                           IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult deleteDeviceFromGroup(String sessionId, int groupId,
                                                IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult updateGroupName(String sessionId, String groupName, int groupId,
                                          IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult getGroupByGroupId(String sessionId, int groupId,
                                            IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult isDeviceMaster(String sessionId, int deviceId,
                                         IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult getGroupByLocationId(String sessionId, int locationId,
                                               IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult sendScenarioToGroup(String sessionId, int groupId,
                                              IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult multiCommTask(String sessionId,
                                        IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult turnOnDevice(int deviceId, String sessionId, IRequestParams request,
                                       IActivityReceive receiveResponse);

    public ResponseResult setArriveHomeTime(int locationId, String sessionId, IRequestParams request,
                                            IActivityReceive receiveResponse);

    public ResponseResult checkEnrollmentStyle(String deviceType, int userType, String sessionId, IRequestParams request,
                                               IActivityReceive receiveResponse);

    public ResponseResult getAuthUnreadMessage(String sessionId, IRequestParams request,
                                               IActivityReceive receiveResponse);

    public ResponseResult checkAuthUser(String sessionId, IRequestParams request,
                                        IActivityReceive receiveResponse);

    public ResponseResult grantAuthToDevice(int deviceId, int assignRole, String sessionId, IRequestParams request,
                                            IActivityReceive receiveResponse);

    public ResponseResult removeDeviceAuth(int deviceId, String sessionId, IRequestParams request,
                                           IActivityReceive receiveResponse);

    public ResponseResult getAuthMessages(int pageIndex, int pageSize, String sessionId, IRequestParams request,
                                          IActivityReceive receiveResponse);

    public ResponseResult getAuthMessageById(int messageId, String sessionId, IRequestParams request,
                                             IActivityReceive receiveResponse);

    public ResponseResult handleAuthMessage(int invitationId, int actionId, String sessionId, IRequestParams request,
                                            IActivityReceive receiveResponse);

    public ResponseResult getAuthDevices(String sessionId, IRequestParams request,
                                         IActivityReceive receiveResponse);

    public ResponseResult putUserClientInfo(String sessionId, IRequestParams request,
                                            IActivityReceive receiveResponse);

    public ResponseResult getAuthGroupDevices(String sessionId, IRequestParams request,
                                              IActivityReceive receiveResponse);

    public ResponseResult removeGroupAuth(int deviceId, String sessionId, IRequestParams request,
                                          IActivityReceive receiveResponse);

    public ResponseResult getDeviceListByGroupId(String sessionId, IRequestParams request,
                                                 IActivityReceive receiveResponse);

    public ResponseResult controlDevice(int deviceId, String sessionId, IRequestParams request,
                                        IActivityReceive receiveResponse);

    public ResponseResult updatePassword(UpdatePasswordRequest request, IActivityReceive
            receiveResponse);

    public ResponseResult changePassword(String userId, String sessionId, ChangePasswordRequest request,
                                         IActivityReceive receiveResponse);

    public ResponseResult getSmsCode(SmsValidRequest request, IActivityReceive receiveResponse);

    public ResponseResult verifySmsCode(String phoneNum, String smsCode, SmsValidRequest request,
                                        IActivityReceive receiveResponse);


    public ResponseResult refreshSession(UserLoginRequest request, RequestID requestID);

    public ResponseResult controlWaterDevice(int deviceId, String sessionId, IRequestParams request,
                                             IActivityReceive receiveResponse);

    public ResponseResult getAllDevicesRunstatus(String sessionId, IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult getDevicesConfig(String sessionId, IRequestParams request, IActivityReceive receiveResponse);

    public ResponseResult controlHomeDevice(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    public ResponseResult quickAction(String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    public ResponseResult GetMessagesPerPage(String mSessionId, int mFromRecordNo, int mPageSize, String mLanguage, int mCompareMsgPoolId, IRequestParams mRequestParams,
                                             IActivityReceive mIReceiveResponse);

    public ResponseResult DeleteMessages(String mSessionId, IRequestParams mRequestParams,
                                         IActivityReceive mIReceiveResponse);

    public ResponseResult getMessageById(int messageId, String language, String sessionId, IRequestParams request,
                                         IActivityReceive receiveResponse);

    public ResponseResult updateMessageStatus(String sessionId, IRequestParams request,
                                              IActivityReceive receiveResponse);

    public ResponseResult getUnreadMessages(String sessionId, IRequestParams request,
                                            IActivityReceive receiveResponse);

    public ResponseResult feedBack(String userId, String sessionId, FeedBackRequest request, IActivityReceive
            receiveResponse);

    public ResponseResult feedBackImg(String userId, String sessionId, FeedBackImgRequest request, IActivityReceive
            receiveResponse);

    public ResponseResult feedBackDeleteImg(String userId, String sessionId, FeedBackDeleteImgRequest request, IActivityReceive
            receiveResponse);

    public ResponseResult getDustAndPm25TaskByLocationId(int locationId, String fromDate, String toDate, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    public ResponseResult getVolumeAndTdsByLocationID(int locationId, String fromDate, String toDate, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    public ResponseResult getTotalDustByLocationID(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    public ResponseResult getTotalVolumeByLocationID(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse);

    public ResponseResult userValidate(UserLoginRequest request, RequestID requestID);
}