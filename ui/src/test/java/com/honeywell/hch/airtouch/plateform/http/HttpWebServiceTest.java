package com.honeywell.hch.airtouch.plateform.http;

import android.app.Application;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.library.http.HTTPRequestManager;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestParams;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.HPlusFileUtils;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.ResponseParseManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.CapabilityResponse;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserRegisterRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by lynnliu on 24/9/15.
 */
@RunWith(RobolectricTestRunner.class)
public class HttpWebServiceTest {

    // result value
    private static final String TEST_REGISTER_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("register_result");
    private static final String TEST_LOGIN_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("login_result");

    private static final String TEST_GET_DEVICE_CAPABILITY_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("device_capability_result");

    private static final String TEST_GET_DEVICE_RUNSTATUS_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("device_runstatus_result");
    private static final String TEST_GET_LOCATION_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("get_location_result");

    private static final String TEST_CREATE_GROUP_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("create_group");

    private static final String TEST_DELETE_GROUP_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("delete_group");

    private static final String TEST_ADD_DEVICE_TO_GROUP_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("add_device_to_group");

    private static final String TEST_DELETE_DEVICE_FROM_GROUP_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("delete_device_from_group");

    private static final String TEST_UPDATE_GROUP_NAME_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("update_group_name");

    private static final String TEST_DELETE_DEVICE_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("delete_device");

    private static final String TEST_GET_GROUP_BY_GTOUP_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("get_group_by_groupId");

    private static final String TEST_SEND_SCENARIO_TO_GROUP_RESULT = HPlusFileUtils
            .readFileFromWebTestsAsString("send_scenario_goup");

    private static final String TEST_GET_AUTH_UNREAD_MESSAGE = HPlusFileUtils
            .readFileFromWebTestsAsString("get_auth_unread_message");

    private static final String TEST_CHECK_AUTH_USER = HPlusFileUtils
            .readFileFromWebTestsAsString("check_auth_user");

    private static final String TEST_GRANT_AUTH_TO_DEVICE = HPlusFileUtils
            .readFileFromWebTestsAsString("grant_auth_to_device");

    private static final String TEST_REMOVE_AUTH = HPlusFileUtils
            .readFileFromWebTestsAsString("remove_auth");

    private static final String TEST_GET_AUTH_MESSAGES = HPlusFileUtils
            .readFileFromWebTestsAsString("get_auth_messages");

    private static final String TEST_HANDLE_AUTH_MESSAGE = HPlusFileUtils
            .readFileFromWebTestsAsString("handle_auth_message");

    private static final String TEST_GET_AUTH_DEVICES = HPlusFileUtils
            .readFileFromWebTestsAsString("get_auth_devices");

    private static final String TEST_GET_ALL_RUNSTATUS = HPlusFileUtils
            .readFileFromWebTestsAsString("get_all_runstatus");

    private static final String TEST_GET_SENARIO_CONTROLL = HPlusFileUtils
            .readFileFromWebTestsAsString("senario_controll");

    private static final String TEST_GET_DEVICE_CONFIGS = HPlusFileUtils
            .readFileFromWebTestsAsString("get_devices_config");

    // url constant
    private static final String REQUEST_REGISTER_ACCOUNT = "userAccounts";

    public MockHTTPClient mMockHTTPClient;
    private String mBaseLocalUrl = "https://qaweb.chinacloudapp.cn/WebAPI/api/";


    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setup() {
        mMockHTTPClient = new MockHTTPClient();
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);

    }

    private String getLocalUrl(String request, Object... params) {
        String baseUrl = mBaseLocalUrl + request;
        if (params == null || params.length == 0) {
            return baseUrl;
        }
        return String.format(baseUrl, params);
    }

    @Test
    public void testUserRegister() {
        mMockHTTPClient.reset();

        UserRegisterRequest request = new UserRegisterRequest("nickname", "password", "telephone", "+86");
        request.setEmail("email");

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(REQUEST_REGISTER_ACCOUNT), null, RequestID
                .USER_REGISTER, request);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_REGISTER_RESULT);
        HTTPRequestResponse response = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult userRegisterResponse = ResponseParseManager.getRegsterResponse(response);
        //verify url, status code, result value, balabalabala...
    }

    @Test
    public void testGetDeviceCapability(){
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_DEVICE_CAPABILITY, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_GET_DEVICE_CAPABILITY_RESULT);

        HTTPRequestResponse response = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult deviceCapabilityResponse = ResponseParseManager.parseGetDeviceCapabilityResponse(response, RequestID
                .GET_DEVICE_CAPABILITY);
        Assert.assertEquals(true, deviceCapabilityResponse.isResult());

        CapabilityResponse capabilityResponse = (CapabilityResponse)deviceCapabilityResponse.getResponseData().getSerializable(HPlusConstants.DEVICE_CAPABILITY_KEY);
        Assert.assertEquals(800, capabilityResponse.getFilter1ExpiredTime());
        Assert.assertEquals(5000, capabilityResponse.getFilter2ExpiredTime());
        Assert.assertEquals(1400, capabilityResponse.getFilter3ExpiredTime());
    }

    @Test
    public void testGetDeviceRunStatus(){
//        mMockHTTPClient.reset();
//
//        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
//                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
//                .GET_DEVICE_STATUS, null);
//        mMockHTTPClient.setHTTPRequestResponseData(TEST_GET_DEVICE_RUNSTATUS_RESULT);
//
//        HTTPRequestResponse response = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
//        ResponseResult deviceRunstatusResponse = ResponseParseManager.parseGetDeviceRunStatusResponse(response, RequestID
//                .GET_DEVICE_STATUS);
//
//        Assert.assertEquals(true, deviceRunstatusResponse.isResult());
//
//        AirtouchRunStatus runstatusResponse = (AirtouchRunStatus)deviceRunstatusResponse.getResponseData().getSerializable(HPlusConstants.DEVICE_RUNSTATUS_KEY);
//
//        Assert.assertEquals(39, runstatusResponse.getmPM25Value());
//        Assert.assertEquals("NotRunning", runstatusResponse.getFanSpeedStatus());
//        Assert.assertEquals(1, runstatusResponse.getScenarioMode());
    }

    @Test
    public void testGetLocation(){
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_LOCATION, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_GET_LOCATION_RESULT);

        HTTPRequestResponse response = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult locationResponse = ResponseParseManager.parseGetLocationResponse(response, RequestID
                .GET_LOCATION);

        Assert.assertEquals(true, locationResponse.isResult());
        Assert.assertEquals(4, UserAllDataContainer.shareInstance().getUserLocationDataList().size());
    }

    @Test
    public void testUserLogin(){
//        mMockHTTPClient.reset();
//
//        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
//                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
//                .USER_LOGIN, null);
//        mMockHTTPClient.setHTTPRequestResponseData(TEST_LOGIN_RESULT);
//
//        HTTPRequestResponse response = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
//        ResponseResult loginResponse = ResponseParseManager.parseUserLoginResponse(response, RequestID
//                .GET_LOCATION);
//
//        Assert.assertEquals(true, loginResponse.isResult());
//        Assert.assertEquals(null, UserInfoSharePreference.getSessionId());
//        Assert.assertEquals(null, UserInfoSharePreference.getUserId());
//        Assert.assertEquals(null, UserInfoSharePreference.getNickName());

    }

    @Test
    public void testCreateGroup(){
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .CREATE_GROUP, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_CREATE_GROUP_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseCreateGroupResponse(successResponse, RequestID
                .CREATE_GROUP);

        Assert.assertEquals(200, groupResult.getResponseCode());
        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseCreateGroupResponse(failResponse, RequestID
                .CREATE_GROUP);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testDeleteGroup() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .DELETE_GROUP, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_DELETE_GROUP_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseGroupResponse(successResponse, RequestID
                .DELETE_GROUP);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseGroupResponse(failResponse, RequestID
                .DELETE_GROUP);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testAddDeviceToGroup() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .ADD_DEVICE, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_ADD_DEVICE_TO_GROUP_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseGroupResponse(successResponse, RequestID
                .ADD_DEVICE);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseGroupResponse(failResponse, RequestID
                .ADD_DEVICE);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testDeleteDeviceFromGroup() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .DELETE_DEVICE_FROM_GROUP, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_DELETE_DEVICE_FROM_GROUP_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseGroupResponse(successResponse, RequestID
                .DELETE_DEVICE_FROM_GROUP);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseGroupResponse(failResponse, RequestID
                .DELETE_DEVICE_FROM_GROUP);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testUpdateGroupName() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .UPDATE_GROUP_NAME, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_UPDATE_GROUP_NAME_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseGroupResponse(successResponse, RequestID
                .UPDATE_GROUP_NAME);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseGroupResponse(failResponse, RequestID
                .UPDATE_GROUP_NAME);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testIsMasterDevice() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .IS_DEVICE_MASTER, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_DELETE_DEVICE_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseIsMasterResponse(successResponse, RequestID
                .IS_DEVICE_MASTER);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseIsMasterResponse(failResponse, RequestID
                .DELETE_DEVICE);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testDeleteDevice() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .DELETE_DEVICE, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_DELETE_DEVICE_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseAddDeviceResponse(successResponse, RequestID
                .DELETE_DEVICE);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));


        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseAddDeviceResponse(failResponse, RequestID
                .DELETE_DEVICE);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void testSendScenarioToGroup() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .SEND_SCENARIO_TO_GROUP, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_SEND_SCENARIO_TO_GROUP_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseScenarioResponse(successResponse, RequestID
                .GET_GROUP_BY_GROUP_ID);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));

        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseScenarioResponse(failResponse, RequestID
                .SEND_SCENARIO_TO_GROUP);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void getGroupByGroupId() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_GROUP_BY_GROUP_ID, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_GET_GROUP_BY_GTOUP_RESULT);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult groupResult = ResponseParseManager.parseCommonResponse(successResponse, RequestID
                .GET_GROUP_BY_GROUP_ID);

        Assert.assertEquals(200, groupResult.getResponseCode());
//        Assert.assertEquals(2, groupResult.getResponseData().getInt(CreateGroupResponse.GROUP_ID));

        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseCommonResponse(failResponse, RequestID
                .GET_GROUP_BY_GROUP_ID);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }

    @Test
    public void getAuthUnreadMessage() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_AUTH_UNREAD_MESSAGE, null);

        testSuccessResponse(httpRequestParams, RequestID.GET_AUTH_UNREAD_MESSAGE, TEST_GET_AUTH_UNREAD_MESSAGE);
        testFailResponse(httpRequestParams, RequestID.GET_AUTH_UNREAD_MESSAGE);

    }

    @Test
    public void getAuthDevices() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_AUTH_DEVICES, null);

        testSuccessResponse(httpRequestParams, RequestID.GET_AUTH_DEVICES, TEST_GET_AUTH_DEVICES);
        testFailResponse(httpRequestParams, RequestID.GET_AUTH_DEVICES);

    }

    @Test
    public void checkAuthUser() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(mBaseLocalUrl), null, RequestID
                .CHECK_AUTH_USER, null);

        testSuccessResponse(httpRequestParams, RequestID.CHECK_AUTH_USER, TEST_CHECK_AUTH_USER);
        testFailResponse(httpRequestParams, RequestID.CHECK_AUTH_USER);
    }

    @Test
    public void grantAuthToDevice() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GRANT_AUTH_TO_DEVICE, null);

        testSuccessResponse(httpRequestParams, RequestID.GRANT_AUTH_TO_DEVICE, TEST_GRANT_AUTH_TO_DEVICE);
        testFailResponse(httpRequestParams, RequestID.GRANT_AUTH_TO_DEVICE);
    }

    @Test
    public void removeAuth() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(mBaseLocalUrl), null, RequestID
                .REMOVE_DEVICE_AUTH, null);

        testSuccessResponse(httpRequestParams, RequestID.REMOVE_DEVICE_AUTH, TEST_REMOVE_AUTH);
        testFailResponse(httpRequestParams, RequestID.REMOVE_DEVICE_AUTH);
    }

    @Test
    public void getAuthMessages() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_AUTH_MESSAGES, null);

        testSuccessResponse(httpRequestParams, RequestID.GET_AUTH_MESSAGES, TEST_GET_AUTH_MESSAGES);
        testFailResponse(httpRequestParams, RequestID.GET_AUTH_MESSAGES);
    }

    @Test
    public void handleAuthMessage() throws Exception {
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.POST, getLocalUrl(mBaseLocalUrl), null, RequestID
                .HANDLE_AUTH_MESSAGE, null);

        testSuccessResponse(httpRequestParams, RequestID.HANDLE_AUTH_MESSAGE, TEST_HANDLE_AUTH_MESSAGE);
        testFailResponse(httpRequestParams, RequestID.HANDLE_AUTH_MESSAGE);
    }

    private void testSuccessResponse(HTTPRequestParams httpRequestParams, RequestID requestID, String responseData) {
        mMockHTTPClient.setHTTPRequestResponseData(responseData);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult result = ResponseParseManager.parseCommonResponse(successResponse, requestID);
        Assert.assertEquals(200, result.getResponseCode());
    }

    private void testFailResponse(HTTPRequestParams httpRequestParams, RequestID requestID) {
        MockHTTPClient.Error error = new MockHTTPClient.Error(404,"error");
        mMockHTTPClient.setError(error);
        HTTPRequestResponse failResponse = mMockHTTPClient.getHTTPRequestFailureResponse(httpRequestParams);
        ResponseResult failGroupResult = ResponseParseManager.parseCommonResponse(failResponse, requestID);
        Assert.assertEquals(404, failGroupResult.getResponseCode());
    }



    @Test
    public void testGetAllDevicesRunstatus() throws Exception {
        mMockHTTPClient.reset();


        UserLocationData userLocation = new RealUserLocationData();
        userLocation.setLocationID(10497);
        userLocation.setCity("CHSH00000");
        userLocation.setName("Shanghai");
        userLocation.setIsLocationOwner(false);
        ArrayList<HomeDevice> homeDeviceArrayList = new ArrayList<>();
        homeDeviceArrayList.add(contructionAirtouchDevice(28354,HPlusConstants.AIRTOUCH_S_TYPE));
        homeDeviceArrayList.add(contructionWaterDevice(28355));
        userLocation.setHomeDevicesList(homeDeviceArrayList);


        UserLocationData userLocation2 = new RealUserLocationData();
        userLocation2.setLocationID(10498);
        userLocation2.setCity("CHSH00001");
        userLocation2.setName("beijing");
        userLocation2.setIsLocationOwner(false);

        ArrayList<HomeDevice> homeDeviceArrayList2 = new ArrayList<>();
        homeDeviceArrayList2.add(contructionAirtouchDevice(28356,HPlusConstants.AIRTOUCH_S_TYPE));
        homeDeviceArrayList2.add(contructionAirtouchDevice(28357,HPlusConstants.AIRTOUCH_S_TYPE));

        homeDeviceArrayList2.add(contructionWaterDevice(28358));
        homeDeviceArrayList2.add(contructionWaterDevice(28359));
        userLocation2.setHomeDevicesList(homeDeviceArrayList2);

        CopyOnWriteArrayList<UserLocationData> locations = new CopyOnWriteArrayList<>();
        locations.add(userLocation);
        locations.add(userLocation2);

        UserAllDataContainer.shareInstance().setUserLocationDataList(locations);

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .UPDATE_GROUP_NAME, null);
        mMockHTTPClient.setHTTPRequestResponseData(TEST_GET_ALL_RUNSTATUS);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult allDeviceStatusResponse = ResponseParseManager.parseAllLocationsAndDevicesRunstatus(successResponse, RequestID
                .GET_ALL_RUNSTAUS);

        Assert.assertEquals(true, allDeviceStatusResponse.isResult());

        Assert.assertEquals(2, UserAllDataContainer.shareInstance().getUserLocationDataList().size());

        Assert.assertEquals(10497, UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getLocationID());
        Assert.assertEquals(true, UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getHomeDevicesList().get(1) instanceof WaterDeviceObject);
        Assert.assertEquals(200, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getHomeDevicesList().get(1)).getAquaTouchRunstatus().getInflowTDS());
        Assert.assertEquals(2, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getHomeDevicesList().get(1)).getAquaTouchRunstatus().getErrFlags()[0]);
        Assert.assertEquals(2, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getHomeDevicesList().get(1)).getAquaTouchRunstatus().getmFilterInfoList().size());
        Assert.assertEquals(15, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getHomeDevicesList().get(1)).getAquaTouchRunstatus().getmFilterInfoList().get(0).getTimeCapabilitySetByUser());
        Assert.assertEquals(10, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getHomeDevicesList().get(1)).getAquaTouchRunstatus().getmFilterInfoList().get(1).getTimeCapabilitySetByUser());



        Assert.assertEquals(10498, UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getLocationID());
        Assert.assertEquals(4, UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().size());

        Assert.assertEquals(222, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(2)).getAquaTouchRunstatus().getInflowTDS());
        Assert.assertEquals(4, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(2)).getAquaTouchRunstatus().getErrFlags()[0]);
        Assert.assertEquals(2, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(2)).getAquaTouchRunstatus().getmFilterInfoList().size());
        Assert.assertEquals(150, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(2)).getAquaTouchRunstatus().getmFilterInfoList().get(0).getTimeCapabilitySetByUser());
        Assert.assertEquals(888, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(2)).getAquaTouchRunstatus().getmFilterInfoList().get(1).getVolumeCapabilitySetByUser());

        Assert.assertEquals(1000, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(3)).getAquaTouchRunstatus().getOutflowVolume());
        Assert.assertEquals(1, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(3)).getAquaTouchRunstatus().getErrFlags()[0]);

        Assert.assertEquals(100, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(3)).getAquaTouchRunstatus().getmFilterInfoList().get(0).getTimeCapabilitySetByUser());
        Assert.assertEquals(101, ((WaterDeviceObject) UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getHomeDevicesList().get(3)).getAquaTouchRunstatus().getmFilterInfoList().get(1).getVolumeCapabilitySetByUser());

    }

    private AirTouchDeviceObject contructionAirtouchDevice(int deviceId, int deviceType){
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(deviceId);
        deviceInfo.setDeviceType(deviceType);
        AirTouchDeviceObject airTouchDeviceObject = new AirTouchDeviceObject(deviceType);
        airTouchDeviceObject.setDeviceInfo(deviceInfo);

        return airTouchDeviceObject;
    }

    private WaterDeviceObject contructionWaterDevice(int deviceId){
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(deviceId);
        deviceInfo.setDeviceType(HPlusConstants.WATER_SMART_RO_400_TYPE);
        WaterDeviceObject waterDeviceObject = new WaterDeviceObject(deviceInfo);
        waterDeviceObject.setDeviceInfo(deviceInfo);

        return waterDeviceObject;
    }

    @Test
    public void controlWaterDevice() throws Exception{
        mMockHTTPClient.reset();

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.PUT, getLocalUrl(mBaseLocalUrl), null, RequestID
                .WATER_CONTROL_DEVICE, null);

        testSuccessResponse(httpRequestParams, RequestID.WATER_CONTROL_DEVICE, TEST_GET_SENARIO_CONTROLL);
        testFailResponse(httpRequestParams, RequestID.WATER_CONTROL_DEVICE);
    }

    @Test
    public void testGetDevicesConfig() throws Exception {
        mMockHTTPClient.reset();

        mMockHTTPClient.setHTTPRequestResponseData(TEST_GET_DEVICE_CONFIGS);

        HTTPRequestParams httpRequestParams = new HTTPRequestParams(HTTPRequestManager
                .RequestType.GET, getLocalUrl(mBaseLocalUrl), null, RequestID
                .GET_ALL_DEVICE_TYPE_CONFIG, null);

        HTTPRequestResponse successResponse = mMockHTTPClient.getHTTPRequestSuccessResponse(httpRequestParams);
        ResponseResult result = ResponseParseManager.parseAllDeviceTypeConfig(successResponse, RequestID.GET_ALL_DEVICE_TYPE_CONFIG);
        Assert.assertEquals(200, result.getResponseCode());

        Map<Integer,AirtouchCapability> airtouchCapabilityMap = UserAllDataContainer.shareInstance().getAirtouchCapabilityMap();
        Assert.assertEquals(5, airtouchCapabilityMap.size());

        Assert.assertEquals(4444, airtouchCapabilityMap.get(HPlusConstants.AIRTOUCH_450_TYPE).getFilter1ExpiredTime());
        Assert.assertEquals(6480, airtouchCapabilityMap.get(HPlusConstants.AIRTOUCH_S_TYPE).getFilter1ExpiredTime());
        Assert.assertEquals(1280, airtouchCapabilityMap.get(HPlusConstants.AIRTOUCH_X_TYPE).getFilter1ExpiredTime());
        Assert.assertEquals(3333, airtouchCapabilityMap.get(HPlusConstants.AIRTOUCH_JD_TYPE).getFilter1ExpiredTime());
        Assert.assertEquals(5555, airtouchCapabilityMap.get(HPlusConstants.AIRTOUCH_FFAC_TYPE).getFilter1ExpiredTime());

        Map<Integer,SmartROCapability> smartROCapabilityMap = UserAllDataContainer.shareInstance().getSmartROCapabilityMap();
        Assert.assertEquals(2, smartROCapabilityMap.get(HPlusConstants.WATER_SMART_RO_600_TYPE).getmEveryFilterCapabilityDetailList().size());
        Assert.assertEquals(3000, smartROCapabilityMap.get(HPlusConstants.WATER_SMART_RO_600_TYPE).getmEveryFilterCapabilityDetailList().get(0).getmMaxTimeSetting());
        Assert.assertEquals("1", smartROCapabilityMap.get(HPlusConstants.WATER_SMART_RO_600_TYPE).getmEveryFilterCapabilityDetailList().get(0).getmFilterType());
        Assert.assertEquals(5000, smartROCapabilityMap.get(HPlusConstants.WATER_SMART_RO_600_TYPE).getmEveryFilterCapabilityDetailList().get(1).getmMaxTimeSetting());

    }
}
