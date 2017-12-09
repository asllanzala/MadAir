package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.plateform.http.model.group.ScenarioGroupRequest;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;
import com.honeywell.hch.airtouch.ui.main.manager.common.HomeControlManager;

import junit.framework.Assert;

/**
 * Created by Vincent on 8/8/16.
 */
public class ControlHomeTest extends BaseManagerFunctionTest {
    private HomeControlManager mGroupManager;
    private MockWebService mWebService;

    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private int mLocation = 001;
    int scenarioMode = ScenarioGroupRequest.SCENARIO_SLEEP;
    ScenarioGroupRequest request = new ScenarioGroupRequest(scenarioMode);

    public ControlHomeTest(HomeControlManager groupManager, MockWebService webService) {
        mGroupManager = groupManager;
        mWebService = webService;
    }

    public void testSuccessControlHome(){

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_HOME_DEVICE;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mBundle.putInt(CreateGroupResponse.CODE_ID, mResponseCode);
        mResponseResult.setResponseData(mBundle);
//
//        //test login success
        setReloginSuccess();
        mGroupManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(mRequestId, responseResult.getRequestId());
                Assert.assertEquals(mResult, responseResult.isResult());
                Assert.assertEquals(mResponseCode, responseResult.getResponseCode());
            }
        };
        mWebService.setResponseResult(mResponseResult);
        mGroupManager.controlHomeDevice(mLocation, scenarioMode);

    }
    public void testFailControlHome(){

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_HOME_DEVICE;
        ResponseResult mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginSuccess();
        mGroupManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());
            }
        };
        mWebService.setResponseResult(mResponseResult);
        mGroupManager.controlHomeDevice(mLocation, scenarioMode);

    }
    public void testLoginFailWhenControlHome(){

        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginFail();
        mGroupManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());

            }
        };
        mWebService.setResponseResult(responseResult);
        mGroupManager.controlHomeDevice(mLocation, scenarioMode);

    }
}
