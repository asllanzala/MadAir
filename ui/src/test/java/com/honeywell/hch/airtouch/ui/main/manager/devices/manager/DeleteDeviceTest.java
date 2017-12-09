package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;

import junit.framework.Assert;

/**
 * Created by Vincent on 11/1/16.
 */
public class DeleteDeviceTest extends BaseManagerFunctionTest {

    private GroupManager mGroupManager;
    private MockWebService mWebService;

    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private int mDeviceId = 001;

    public DeleteDeviceTest(GroupManager groupManager, MockWebService webService) {
        mGroupManager = groupManager;
        mWebService = webService;
    }

    public void testSuccessDeleteDevice(){

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.DELETE_DEVICE;
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
        mGroupManager.deleteDevice(mDeviceId,initDeviceInLocationData(4));

    }
    public void testFailDeleteDevice(){

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.DELETE_DEVICE;
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
        mGroupManager.deleteDevice(mDeviceId,initDeviceInLocationData(4));

    }
    public void testLoginFailWhenDeleteDevice(){

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
        mGroupManager.deleteDevice(mDeviceId,initDeviceInLocationData(4));

    }

    private UserLocationData initDeviceInLocationData(int deviceNum){
        UserLocationData userLocationData = new RealUserLocationData();
        for (int i = 0; i < deviceNum; i++){
            HomeDevice device = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_450_TYPE);
            userLocationData.addHomeDeviceItemToList(device);
        }
        return userLocationData;
    }
}
