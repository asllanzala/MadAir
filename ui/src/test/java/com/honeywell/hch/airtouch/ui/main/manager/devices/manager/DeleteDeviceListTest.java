package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceListRequest;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;
import com.honeywell.hch.airtouch.ui.control.manager.group.GroupControlUIManager;

import junit.framework.Assert;

/**
 * Created by Vincent on 11/1/16.
 */
public class DeleteDeviceListTest extends BaseManagerFunctionTest {
    private MockWebService mWebService;

    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private int mGroupId = 001;
    private int[] ids = new int[]{3, 2, 1};
    private UserLocationData request = new RealUserLocationData();
    private GroupControlUIManager mGroupControlUIManager;
    private AllDeviceUIManager mAllDeviceUIManager;
    private GroupManager mGroupManager;

    public DeleteDeviceListTest(AllDeviceUIManager allDeviceUIManager, MockWebService webService, GroupControlUIManager groupControlUIManager,GroupManager groupManager) {
        mAllDeviceUIManager = allDeviceUIManager;
        mWebService = webService;
        mGroupControlUIManager = groupControlUIManager;
        mGroupManager = groupManager;
    }

    public void testSuccessDeleteDeviceFromGroup() {

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.DELETE_DEVICE_FROM_GROUP;
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
        if(mAllDeviceUIManager!=null){
            mAllDeviceUIManager.deleteDevice();
        } else if (mGroupControlUIManager != null) {
            mGroupControlUIManager.deleteDeviceFromGroup(123);
        } else {
            mGroupManager.deleteDeviceList(null, request);
        }
    }

    public void testFailDeleteDeviceFromGroup() {

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.DELETE_DEVICE_FROM_GROUP;
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
        if(mAllDeviceUIManager!=null){
            mAllDeviceUIManager.deleteDevice();
        } else if (mGroupControlUIManager != null) {
            mGroupControlUIManager.deleteDeviceFromGroup(123);
        } else {
            mGroupManager.deleteDeviceList(null, request);
        }

    }

    public void testLoginFailWhenDeleteDeviceFromGroup() {

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
        if(mAllDeviceUIManager!=null){
            mAllDeviceUIManager.deleteDevice();
        } else if (mGroupControlUIManager != null) {
            mGroupControlUIManager.deleteDeviceFromGroup(123);
        } else {
            mGroupManager.deleteDeviceList(null, request);
        }
    }
}
