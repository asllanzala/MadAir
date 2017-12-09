package com.honeywell.hch.airtouch.plateform.http;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.HTTPClient;
import com.honeywell.hch.airtouch.library.http.HTTPRequestManager;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestParams;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.ResponseParseManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.model.user.FeedBackDeleteImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.ChangePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SmsValidRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UpdatePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserRegisterRequest;

/**
 * Created by Jin Qian on 1/22/15
 */
public class HttpWebService extends HTTPClient implements IWebService {
    private static final String TAG = "AirTouchTccClient";
    private static final String REQUEST_SESSION = "session";
    private static final String REQUEST_REGISTER_ACCOUNT = "userAccounts";
    private static final String REQUEST_LOCATION_EMOTION = "locationAllEmotionInfo?locationId=%1$d&periodType=%2$d";
    private static final String REQUEST_SWAP_LOCATION = "locations/editLocation?locationId=%1$d";
    private static final String REQUEST_LOCATION = "locations?locationId=%1$d";
    private static final String REQUEST_ADD_LOCATION = "locations?userId=%1$s";
    private static final String REQUEST_GET_LOCATION = "locations?userId=%1$s&allData=true";
    private static final String REQUEST_CREATE_GROUP =
            "Grouping/CreateGroup?groupName=%1$s&masterDeviceId=%2$d&locationId=%3$d";
    private static final String REQUEST_DELETE_GROUP = "Grouping/DeleteGroup?groupId=%1$d";
    private static final String REQUEST_ADD_DEVICE_TO_GROUP = "Grouping/AddDeviceIntoGroup?groupId=%1$d";
    private static final String REQUEST_DELETE_DEVICE_FROM_GROUP = "Grouping/DeleteDeviceFromGroup?groupId=%1$d";
    private static final String REQUEST_UPDATE_GROUP_NAME =
            "Grouping/UpdateGroupName?groupNewName=%1$s&groupId=%2$d";
    private static final String REQUEST_GET_GROUP_BY_GROUP_ID = "Grouping/GetGroupByGroupId?groupId=%1$d";
    private static final String REQUEST_GET_GROUP_BY_LOCATION_ID = "Grouping/GetGroupByLocationId?locationId=%1$d";
    private static final String REQUEST_IS_DEVICE_MASTER = "Grouping/IsDeviceMasterDevice?deviceId=%1$d";
    private static final String REQUEST_SEND_SCENARIO_TO_GROUP = "Grouping/GroupControl?groupId=%1$d";
    private static final String REQUEST_MULTI_COMM_TASK = "commTasks";


    private static final String ADD_DEVICE = "devices?locationId=%1$d";
    private static final String COMM_TASK = "commTasks?commTaskId=%1$d";
    private static final String CHECK_MAC = "gateways/AliveStatus?macId=%1$s";
    private static final String CONTROL_DEVICE = "devices/%1$d/AirCleaner/Control";
    private static final String GET_DEVICE_STATUS = "devices/%1$d/AirCleaner/AirtouchRunStatus";
    private static final String GET_CAPABILITY = "devices/%1$d/AirCleaner/Capability";
    private static final String DELETE_DEVICE = "devices/%1$d";
    private static final String ARRIVE_HOME_TIME = "AirCleaner/%1$d/BackHome";
    private static final String CHECK_ENROLL_TYPE = "GetEnrollMode?deviceType=%1s&userType=%2s&supportType=1";
    private static final String REQUEST_GET_AUTH_UNREAD_MESSAGE = "Authority/GetAuthorityMessage";
    private static final String REQUEST_CHECK_AUTH_USER = "Authority/CheckUsersByPhoneNumber";
    private static final String REQUEST_GRANT_AUTH_TO_DEVICE =
            "Authority/GrantAuthorityToDeviceByUserList?deviceId=%1$d&tobeAssignRole=%2$d";
    private static final String REQUEST_REMOVE_DEVICE_AUTH = "Authority/RemoveDeviceAuthority?deviceId=%1$d";
    private static final String REQUEST_REMOVE_GROUP_AUTH = "Authority/RemoveGroupAuthority?groupId=%1$d";
    private static final String REQUEST_GET_AUTH_MESSAGES = "Authority/GetInvitations?pageIndex=%1$d&pagesize=%2$d";
    private static final String REQUEST_GET_AUTH_MESSAGE_BY_ID = "Authority/GetInvitation?messageId=%1$d";
    private static final String REQUEST_HANDLE_AUTH_MESSAGE = "Authority/HandleInvitationMessage?messageId=%1$d&actionId=%2$d";
    private static final String REQUEST_GET_AUTH_DEVICES = "Authority/GetDeviceList";
    private static final String REQUEST_GET_AUTH_GROUP_DEVICES = "Authority/GetGroupDeviceList";
    private static final String REQUEST_PUT_USER_CLIENTINFO = "userAccounts/userClientInfo";
    private static final String REQUEST_GET_DEVICE_LIST_BY_GROUP_ID = "Authority/GetDeviceListByGroupId";
    private static final String REQUEST_CONTROL_DEVICE = "devices/%1$d/AirCleaner/Control";
    private static final String UPDATE_PASSWORD = "passwordUpdate";
    private static final String CHANGE_PASSWORD = "userAccounts/%1$s/passwordChange";
    private static final String REQUEST_SMS_VALID = "userAccounts/smsValid";
    private static final String VERIFY_SMS_VALID = "userAccounts/smsValid?phoneNum=%1$s&validNo=%2$s";
    private static final String REQUEST_WATER_CONTROL_DEVICE = "devices/%1$d/Water/SenarioControl";
    private static final String GET_ALL_RUNSTATUS = "/device/RunStatus";
    private static final String GET_ALL_DEVICE_TYPE_CONFIG = "/device/Config";
    private static final String REQUEST_QUICK_ACTION = "devices/QuickAction";

    private static final String REQUEST_GET_MESSAGES_PER_PAGE = "Message/GetMessagesPerPage?fromRecordNo=%1$d&pageSize=%2$d&language=%3$s&compareMsgPoolId=%4$d";
    private static final String DELETE_MESSAGES = "Message/DeleteMessage";
    private static final String GET_MESSAGE_BY_ID = "Message/GetMessageByID?messageID=%1$d&language=%2$s";
    private static final String UPDATE_MESSAGE_STATUS = "Message/UpdateMessageStatus";
    private static final String REQUEST_GET_UNREAD_MESSAGE = "Message/GetUnreadMessageCount";

    private static final String FEED_BACK_TEXT = "FeedBack/SaveFeedBack";
    private static final String FEED_BACK_IMG = "FeedBack/SaveImageInfo";
    private static final String FEED_BACK_DELETE = "FeedBack/DelImageInfo?imgUrl=";
    private static final String GET_TOTAL_DUST = "locations/GetTotalDustByLocationID?locationId=%1$d";
    private static final String GET_TOTAL_VOLUME = "locations/GetTotalVolumeByLocationID?locationId=%1$d";
    private static final String GET_DUST_AND_PM25 = "locations/GetDustAndPm25ByLocationID?locationId=%1$d&from=%2$s&to=%3$s";
    private static final String GET_VOLUME_AND_TDS = "locations/GetVolumeAndTdsByLocationID?locationId=%1$d&from=%2$s&to=%3$s";
    private static final String USER_VALIDATE = "userAccounts/UserValidate";
    private String mBaseLocalUrl;

    private static final int READ_TIMEOUT = 15;
    private static final int CONNECT_TIMEOUT = 15;
    private boolean mLastNetworkIsWell = true;

    private Object mLockObj = new Object();

    public HttpWebService() {
        switch (AppConfig.urlEnv) {
            case AppConfig.PRODUCT_ENV:
                mBaseLocalUrl = "https://mservice.honeywell.com.cn/WebAPI/api/";
                break;

            case AppConfig.STAGE_ENV:
                mBaseLocalUrl = "https://stweb.chinacloudapp.cn/WebAPI/api/";
                break;

            case AppConfig.DEV_ENV:
                mBaseLocalUrl = "https://devweb.chinacloudapp.cn/WebAPI/api/";
                break;

            case AppConfig.QA_ENV:
                mBaseLocalUrl = "https://qaweb.chinacloudapp.cn/WebAPI/api/";
//                mBaseLocalUrl = "http://123.206.219.81/WebAPI/api/";
                break;

            default:
                mBaseLocalUrl = "https://mservice.honeywell.com.cn/WebAPI/api/";
                break;
        }
    }

    private String getLocalUrl(String request, Object... params) {
        String baseUrl = mBaseLocalUrl + request;
        if (params == null || params.length == 0) {
            return baseUrl;
        }
        return String.format(baseUrl, params);
    }

    @Override
    public ResponseResult userRegister(UserRegisterRequest request, IActivityReceive
            receiveResponse) {
        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(REQUEST_REGISTER_ACCOUNT), null, RequestID
                .USER_REGISTER, request);
        HTTPRequestResponse response = executeMethodHTTPRequest(httpRequestParams,
                receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
        setNetworkErroFlag(response, httpRequestParams);
        return ResponseParseManager.getRegsterResponse(response);
    }

    @Override
    public ResponseResult userLogin(UserLoginRequest request, IActivityReceive
            receiveResponse) {
        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(REQUEST_SESSION), null, RequestID.USER_LOGIN, request);
        HTTPRequestResponse response = executeMethodHTTPRequest(httpRequestParams,
                receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
        setNetworkErroFlag(response, httpRequestParams);

        return ResponseParseManager.parseUserLoginResponse(response, RequestID.USER_LOGIN);
    }

    @Override
    public ResponseResult checkMac(String macId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(CHECK_MAC, macId), sessionId, RequestID.CHECK_MAC, request);

        HTTPRequestResponse response = executeMethodHTTPRequest(httpRequestParams,
                receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
        setNetworkErroFlag(response, httpRequestParams);

        return ResponseParseManager.parseCheckMacResponse(response, RequestID.CHECK_MAC);
    }

    @Override
    public ResponseResult emotionBottle(int locationId, int periodType, String sessionId, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_LOCATION_EMOTION, locationId,
                    periodType), sessionId, RequestID.EMOTION_BOTTLE, null);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseBottleResponse(response, RequestID.EMOTION_BOTTLE);
    }

    @Override
    public ResponseResult swapLocationName(int locationId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(REQUEST_SWAP_LOCATION, locationId), sessionId,
                    RequestID.SWAP_LOCATION, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseCommonResponse(response, RequestID.SWAP_LOCATION);
    }

    @Override
    public ResponseResult deleteLocation(int locationId, String sessionId, IActivityReceive
            receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.DELETE, getLocalUrl(REQUEST_LOCATION, locationId),
                    sessionId, RequestID.DELETE_LOCATION, null);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseCommonResponse(response, RequestID.DELETE_LOCATION);
    }

    @Override
    public ResponseResult addLocation(String userId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_ADD_LOCATION, userId), sessionId,
                    RequestID.ADD_LOCATION, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseAddLocationResponse(response, RequestID.ADD_LOCATION);
    }

    @Override
    public ResponseResult addDevice(int locationId, String sessionId, IRequestParams request,
                                    IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(ADD_DEVICE, locationId), sessionId,
                    RequestID.ADD_DEVICE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseAddDeviceResponse(response, RequestID.ADD_DEVICE);
    }

    @Override
    public ResponseResult deleteDevice(int deviceId, String sessionId, IRequestParams request,
                                       IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.DELETE, getLocalUrl(DELETE_DEVICE, deviceId), sessionId,
                    RequestID.DELETE_DEVICE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseAddDeviceResponse(response, RequestID.DELETE_DEVICE);
    }

    @Override
    public ResponseResult getCommTask(int taskId, String sessionId, IRequestParams request,
                                      IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(COMM_TASK, taskId), sessionId,
                    RequestID.COMM_TASK, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseCommTaskResponse(response, RequestID.COMM_TASK);
    }

    @Override
    public ResponseResult getLocation(String userId, String sessionId, IRequestParams request,
                                      IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_LOCATION, userId), sessionId,
                    RequestID.GET_LOCATION, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetLocationResponse(response, RequestID.GET_LOCATION);
    }

    @Override
    public ResponseResult createGroup(String sessionId, String groupName, int masterDeviceId, int locationId,
                                      IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_CREATE_GROUP, groupName, masterDeviceId, locationId),
                    sessionId, RequestID.CREATE_GROUP, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseCreateGroupResponse(response, RequestID.CREATE_GROUP);
    }

    @Override
    public ResponseResult deleteGroup(String sessionId, int groupId,
                                      IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_DELETE_GROUP, groupId),
                    sessionId, RequestID.DELETE_GROUP, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGroupResponse(response, RequestID.DELETE_GROUP);
    }

    @Override
    public ResponseResult addDeviceToGroup(String sessionId, int groupId,
                                           IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_ADD_DEVICE_TO_GROUP, groupId),
                    sessionId, RequestID.ADD_DEVICE_TO_GROUP, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGroupResponse(response, RequestID.ADD_DEVICE_TO_GROUP);
    }

    @Override
    public ResponseResult deleteDeviceFromGroup(String sessionId, int groupId,
                                                IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_DELETE_DEVICE_FROM_GROUP, groupId),
                    sessionId, RequestID.DELETE_DEVICE_FROM_GROUP, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGroupResponse(response, RequestID.DELETE_DEVICE_FROM_GROUP);
    }

    @Override
    public ResponseResult updateGroupName(String sessionId, String groupName, int groupId,
                                          IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_UPDATE_GROUP_NAME, groupName, groupId),
                    sessionId, RequestID.UPDATE_GROUP_NAME, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGroupResponse(response, RequestID.UPDATE_GROUP_NAME);
    }

    @Override
    public ResponseResult getGroupByGroupId(String sessionId, int groupId,
                                            IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_GROUP_BY_GROUP_ID, groupId),
                    sessionId, RequestID.GET_GROUP_BY_GROUP_ID, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetGroupByGroupIdResponse(response, RequestID.GET_GROUP_BY_GROUP_ID);
    }

    @Override
    public ResponseResult isDeviceMaster(String sessionId, int deviceId,
                                         IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_IS_DEVICE_MASTER, deviceId),
                    sessionId, RequestID.IS_DEVICE_MASTER, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseIsMasterResponse(response, RequestID.IS_DEVICE_MASTER);
    }

    @Override
    public ResponseResult getGroupByLocationId(String sessionId, int locationId,
                                               IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_GROUP_BY_LOCATION_ID, locationId),
                    sessionId, RequestID.GET_GROUP_BY_LOCATION_ID, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetGroupByLocationIdResponse(response, RequestID.GET_GROUP_BY_LOCATION_ID, locationId);
    }

    @Override
    public ResponseResult sendScenarioToGroup(String sessionId, int groupId,
                                              IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_SEND_SCENARIO_TO_GROUP, groupId),
                    sessionId, RequestID.SEND_SCENARIO_TO_GROUP, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseScenarioResponse(response, RequestID.SEND_SCENARIO_TO_GROUP);
    }

    @Override
    public ResponseResult multiCommTask(String sessionId,
                                        IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_MULTI_COMM_TASK),
                    sessionId, RequestID.MULTI_COMM_TASK, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseMultiCommTaskResponse(response, RequestID.MULTI_COMM_TASK);
    }

    @Override
    public ResponseResult setArriveHomeTime(int locationId, String sessionId, IRequestParams
            request,
                                            IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(ARRIVE_HOME_TIME, locationId), sessionId, RequestID.ARRIVE_HOME_TIME, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    null, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseCleanTime(response, RequestID.ARRIVE_HOME_TIME);
    }

    @Override
    public ResponseResult turnOnDevice(int deviceId, String sessionId, IRequestParams request,
                                       IActivityReceive receiveResponse) {

        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(CONTROL_DEVICE, deviceId), sessionId, RequestID
                    .CONTROL_DEVICE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    null, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseTurnOnDevie(response, RequestID.ARRIVE_HOME_TIME);
    }

    @Override
    public ResponseResult checkEnrollmentStyle(String deviceType, int userType, String
            sessionId, IRequestParams
                                                       request,
                                               IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(CHECK_ENROLL_TYPE, deviceType, userType), sessionId,
                    RequestID.GET_ENROLL_TYPE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetEnrollTypeResponse(response, RequestID.GET_ENROLL_TYPE);
    }

    @Override
    public ResponseResult getAuthUnreadMessage(String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_AUTH_UNREAD_MESSAGE),
                    sessionId, RequestID.GET_AUTH_UNREAD_MESSAGE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetAuthUnreadMsgResponse(response, RequestID.GET_AUTH_UNREAD_MESSAGE);
    }

    @Override
    public ResponseResult checkAuthUser(String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_CHECK_AUTH_USER),
                    sessionId, RequestID.CHECK_AUTH_USER, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseCheckAuthUserResponse(response, RequestID.CHECK_AUTH_USER);
    }

    @Override
    public ResponseResult grantAuthToDevice(int deviceId, int assignRole, String
            sessionId,
                                            IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_GRANT_AUTH_TO_DEVICE, deviceId, assignRole),
                    sessionId, RequestID.GRANT_AUTH_TO_DEVICE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGrantAuthToDeviceResponse(response, RequestID.GRANT_AUTH_TO_DEVICE);
    }

    @Override
    public ResponseResult removeDeviceAuth(int deviceId, String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_REMOVE_DEVICE_AUTH, deviceId),
                    sessionId, RequestID.REMOVE_DEVICE_AUTH, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseRemoveDeviceAuthResponse(response, RequestID.REMOVE_DEVICE_AUTH);
    }

    @Override
    public ResponseResult getAuthMessages(int pageIndex, int pageSize, String
            sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_AUTH_MESSAGES, pageIndex, pageSize),
                    sessionId, RequestID.GET_AUTH_MESSAGES, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetAuthMessagesResponse(response, RequestID.GET_AUTH_MESSAGES);
    }

    @Override
    public ResponseResult getAuthMessageById(int messageId, String
            sessionId, IRequestParams request,
                                             IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_AUTH_MESSAGE_BY_ID, messageId),
                    sessionId, RequestID.GET_AUTH_MESSAGE_BY_ID, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetAuthMessageByIdResponse(response, RequestID.GET_AUTH_MESSAGE_BY_ID);

    }

    @Override
    public ResponseResult handleAuthMessage(int invitationId, int actionId, String
            sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_HANDLE_AUTH_MESSAGE, invitationId, actionId),
                    sessionId, RequestID.HANDLE_AUTH_MESSAGE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseHandleAuthMessageResponse(response, RequestID.HANDLE_AUTH_MESSAGE);
    }

    @Override
    public ResponseResult getAuthDevices(String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_AUTH_DEVICES),
                    sessionId, RequestID.GET_AUTH_DEVICES, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetAuthDevicesResponse(response, RequestID.GET_AUTH_DEVICES);
    }

    @Override
    public ResponseResult putUserClientInfo(String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(REQUEST_PUT_USER_CLIENTINFO),
                    sessionId, RequestID.PUSH_INFO, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseHandleUserClientInfoResponse(response, RequestID.PUSH_INFO);
    }

    public ResponseResult getAuthGroupDevices(String sessionId, IRequestParams
            request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_AUTH_GROUP_DEVICES),
                    sessionId, RequestID.Get_AUTH_GROUP_DEVICES, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetAuthDevicesResponse(response, RequestID.Get_AUTH_GROUP_DEVICES);
    }

    @Override
    public ResponseResult removeGroupAuth(int groupId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_REMOVE_GROUP_AUTH, groupId),
                    sessionId, RequestID.REMOVE_GROUP_AUTH, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseRemoveGroupAuthResponse(response, RequestID.REMOVE_GROUP_AUTH);
    }

    @Override
    public ResponseResult getDeviceListByGroupId(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_GET_DEVICE_LIST_BY_GROUP_ID),
                    sessionId, RequestID.GET_DEVICE_LIST_BY_GROUP_ID, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseDeviceListByGroupIdResponse(response, RequestID.GET_DEVICE_LIST_BY_GROUP_ID);
    }

    @Override
    public ResponseResult controlDevice(int deviceId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(REQUEST_CONTROL_DEVICE, deviceId),
                    sessionId, RequestID.CONTROL_DEVICE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseControlDeviceResponse(response, RequestID.CONTROL_DEVICE);
    }

    @Override
    public ResponseResult updatePassword(UpdatePasswordRequest request, IActivityReceive
            receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(UPDATE_PASSWORD), null, RequestID.UPDATE_PASSWORD, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "updatePassword Exception:" + e.toString());
        }
        return ResponseParseManager.parseUpdatePasswordResponse(response, RequestID.UPDATE_PASSWORD, request.getNewPassword());
    }


    @Override
    public ResponseResult feedBack(String userId, String sessionId, FeedBackRequest request, IActivityReceive
            receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(FEED_BACK_TEXT), sessionId, RequestID.FEED_BACK_TEXT, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Feedback Exception:" + e.toString());
        }
        return ResponseParseManager.parseFeedbackResponse(response, RequestID.FEED_BACK_TEXT);
    }

    @Override
    public ResponseResult feedBackImg(String userId, String sessionId, FeedBackImgRequest request, IActivityReceive
            receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(FEED_BACK_IMG), sessionId, RequestID.FEED_BACK_IMG, request);
            response = executeImgMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Feedback Exception:" + e.toString());
        }
        return ResponseParseManager.parseFeedbackImgResponse(response, RequestID.FEED_BACK_IMG);
    }

    @Override
    public ResponseResult feedBackDeleteImg(String userId, String sessionId, FeedBackDeleteImgRequest request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(FEED_BACK_DELETE) + request.getPrintableRequest(null), sessionId, RequestID.FEED_BACK_DELETE_IMG, request);
            response = executeImgMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Feedback Exception:" + e.toString());
        }
        return ResponseParseManager.parseFeedbackImgResponse(response, RequestID.FEED_BACK_IMG);
    }

    @Override
    public ResponseResult getDustAndPm25TaskByLocationId(int locationId, String fromDate, String toDate, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(GET_DUST_AND_PM25, locationId, fromDate, toDate), sessionId, RequestID.GET_DUST_AND_PM25, requestParams);
            response = executeMethodHTTPRequest(httpRequestParams,
                    iReceiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseParseManager.parseGetDustAndPm25ByLocationIDResponse(response, RequestID.GET_DUST_AND_PM25);
    }

    @Override
    public ResponseResult getVolumeAndTdsByLocationID(int locationId, String fromDate, String toDate, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(GET_VOLUME_AND_TDS, locationId, fromDate, toDate), sessionId, RequestID.GET_VOLUME_AND_TDS, requestParams);
            response = executeMethodHTTPRequest(httpRequestParams,
                    iReceiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseParseManager.parseGetVolumeAndTdsByLocationIDResponse(response, RequestID.GET_VOLUME_AND_TDS);
    }

    @Override
    public ResponseResult getTotalDustByLocationID(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(GET_TOTAL_DUST, locationId), sessionId, RequestID.GET_TOTAL_DUST, requestParams);
            response = executeMethodHTTPRequest(httpRequestParams,
                    iReceiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseParseManager.parseGetTotalDustResponse(response, RequestID.GET_TOTAL_DUST);
    }

    @Override
    public ResponseResult getTotalVolumeByLocationID(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(GET_TOTAL_VOLUME, locationId), sessionId, RequestID.GET_TOTAL_VOLUME, requestParams);
            response = executeMethodHTTPRequest(httpRequestParams,
                    iReceiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseParseManager.parseGetTotalVolumeResponse(response, RequestID.GET_TOTAL_VOLUME);
    }

    @Override
    public ResponseResult userValidate(UserLoginRequest request, RequestID requestID) {
        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(USER_VALIDATE), null, requestID, request);

        HTTPRequestResponse response = executeMethodHTTPRequest(httpRequestParams,
                null, CONNECT_TIMEOUT, READ_TIMEOUT);
        setNetworkErroFlag(response, httpRequestParams);

        return ResponseParseManager.parseUserValidateResponse(response, requestID);
    }

    @Override
    public ResponseResult changePassword(String userId, String sessionId, ChangePasswordRequest request,
                                         IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(CHANGE_PASSWORD, userId), sessionId, RequestID
                    .CHANGE_PASSWORD, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "changePassword Exception:" + e.toString());
        }
        return ResponseParseManager.parseChangePasswordResponse(response, RequestID.CHANGE_PASSWORD, request.getNewPassword());

    }

    @Override
    public ResponseResult getSmsCode(SmsValidRequest request, IActivityReceive receiveResponse) {

        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_SMS_VALID), null, RequestID.GET_SMS_CODE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "getSmsCode Exception:" + e.toString());
        }
        return ResponseParseManager.parseGetSmsCodeResponse(response, RequestID.GET_SMS_CODE);

    }

    @Override
    public ResponseResult verifySmsCode(String phoneNum, String smsCode, SmsValidRequest request,
                                        IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(VERIFY_SMS_VALID, phoneNum, smsCode), null,
                    RequestID.VERIFY_SMS_VALID, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "getSmsCode Exception:" + e.toString());
        }

        return ResponseParseManager.parseValidSmsCodeResponse(response, RequestID.VERIFY_SMS_VALID);
    }


    @Override
    public ResponseResult refreshSession(UserLoginRequest request, RequestID requestID) {
        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(REQUEST_SESSION), null, requestID, request);

        HTTPRequestResponse response = executeMethodHTTPRequest(httpRequestParams,
                null, CONNECT_TIMEOUT, READ_TIMEOUT);
        setNetworkErroFlag(response, httpRequestParams);

        return ResponseParseManager.parseRefreshSessionResponse(response, requestID);
    }

    @Override
    public ResponseResult controlWaterDevice(int deviceId, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(REQUEST_WATER_CONTROL_DEVICE, deviceId),
                    sessionId, RequestID.WATER_CONTROL_DEVICE, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseWaterControlDeviceResponse(response, RequestID.WATER_CONTROL_DEVICE);
    }

    @Override
    public ResponseResult getAllDevicesRunstatus(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(GET_ALL_RUNSTATUS),
                    sessionId, RequestID.GET_ALL_RUNSTAUS, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);


        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseAllLocationsAndDevicesRunstatus(response, RequestID.GET_ALL_RUNSTAUS);
    }


    @Override
    public ResponseResult getDevicesConfig(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(GET_ALL_DEVICE_TYPE_CONFIG),
                    sessionId, RequestID.GET_ALL_DEVICE_TYPE_CONFIG, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);


        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseAllDeviceTypeConfig(response, RequestID.GET_ALL_DEVICE_TYPE_CONFIG);
    }

    @Override
    public ResponseResult controlHomeDevice(int locationId, String sessionId, IRequestParams requestParams, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_LOCATION, locationId),
                    sessionId, RequestID.CONTROL_HOME_DEVICE, requestParams);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }
        return ResponseParseManager.parseScenarioResponse(response, RequestID.CONTROL_HOME_DEVICE);
    }

    @Override
    public ResponseResult quickAction(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.POST, getLocalUrl(REQUEST_QUICK_ACTION),
                    sessionId, RequestID.QUICK_ACTION, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseScenarioResponse(response, RequestID.QUICK_ACTION);
    }

    @Override
    public ResponseResult GetMessagesPerPage(String mSessionId, int mFromRecordNo, int mPageSize, String mLanguage, int mCompareMsgPoolId, IRequestParams mRequestParams, IActivityReceive mIReceiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_MESSAGES_PER_PAGE, mFromRecordNo, mPageSize, mLanguage, mCompareMsgPoolId),
                    mSessionId, RequestID.GET_MESSAGES_PER_PAGE, mRequestParams);
            response = executeMethodHTTPRequest(httpRequestParams,
                    mIReceiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetMessagesPerPageResponse(response, RequestID.GET_MESSAGES_PER_PAGE);
    }

    @Override
    public ResponseResult DeleteMessages(String mSessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.DELETE, getLocalUrl(DELETE_MESSAGES), mSessionId,
                    RequestID.DELETE_MESSAGES, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGroupResponse(response, RequestID.DELETE_MESSAGES);
    }

    @Override
    public ResponseResult getMessageById(int messageId, String language, String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(GET_MESSAGE_BY_ID, messageId, language),
                    sessionId, RequestID.GET_MESSAGE_BY_ID, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetMessageByIdResponse(response, RequestID.GET_MESSAGE_BY_ID);
    }

    @Override
    public ResponseResult updateMessageStatus(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.PUT, getLocalUrl(UPDATE_MESSAGE_STATUS), sessionId,
                    RequestID.UPDATE_MESSAGE_STATUS, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGroupResponse(response, RequestID.UPDATE_MESSAGE_STATUS);
    }

    @Override
    public ResponseResult getUnreadMessages(String sessionId, IRequestParams request, IActivityReceive receiveResponse) {
        HTTPRequestResponse response = null;
        try {
            HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                    .RequestType.GET, getLocalUrl(REQUEST_GET_UNREAD_MESSAGE),
                    sessionId, RequestID.GET_UNREAD_MESSAGES, request);
            response = executeMethodHTTPRequest(httpRequestParams,
                    receiveResponse, CONNECT_TIMEOUT, READ_TIMEOUT);
            setNetworkErroFlag(response, httpRequestParams);

        } catch (Exception e) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Exception:" + e.toString());
        }

        return ResponseParseManager.parseGetUnreadMsgResponse(response, RequestID.GET_UNREAD_MESSAGES);
    }

    private void setNetworkErroFlag(HTTPRequestResponse httpRequestResponse, HTTPRequestParams mHttpRequestParams) {
        if (mHttpRequestParams.isFromHoneywellServer()) {
            synchronized (mLockObj) {
                if (httpRequestResponse.getStatusCode() != StatusCode.NETWORK_TIMEOUT && httpRequestResponse.getStatusCode() != StatusCode.NETWORK_ERROR) {
                    //有网络并且是网络请求出错的情况
                    UserAllDataContainer.shareInstance().setHasNetWorkError(false);

                    //网络从不好到好的时候，第一次需要发一次广播通知界面，避免网络好的情况下一直会发广播
                    if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication()) && !mLastNetworkIsWell) {
                        Intent intent = new Intent(HPlusConstants.NETWORK_CONNECT_SERVER_WELL);
                        AppManager.getInstance().getApplication().sendBroadcast(intent);
                    }

                    mLastNetworkIsWell = true;
                } else {
                    //有网络的情况并且连接服务器错误，才发广播
                    if (NetWorkUtil.isNetworkAvailable(AppManager.getInstance().getApplication())) {
                        UserAllDataContainer.shareInstance().setHasNetWorkError(true);
                        Intent intent = new Intent(HPlusConstants.NET_WORK_ERROR);
                        AppManager.getInstance().getApplication().sendBroadcast(intent);
                    }
                    mLastNetworkIsWell = false;
                }

            }
        }

    }

}