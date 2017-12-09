package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceListRequest;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;

import junit.framework.Assert;

/**
 * Created by wuyuan on 1/8/16.
 */
public class CreateMethodTest extends BaseManagerFunctionTest {

    private GroupManager mGroupManager;
    private MockWebService mWebService;


    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private String mGroupName = "A";
    private int masterDeviceId = 2;
    private int locationId = 3;
    private int[] ids = new int[]{3,2,1};
    private DeviceListRequest request = new DeviceListRequest(ids);

    public CreateMethodTest(GroupManager groupManager, MockWebService webService){
        mGroupManager = groupManager;
        mWebService = webService;
    }

    public void testSuccessCreateGroup(){

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.CREATE_GROUP;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mBundle.putInt(CreateGroupResponse.CODE_ID, mResponseCode);
        mResponseResult.setResponseData(mBundle);

        //test login success
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
        mGroupManager.createGroup(mGroupName, masterDeviceId, locationId, request);

    }


    public void testFailCreateGroup(){

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.CREATE_GROUP;
        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginSuccess();
        mGroupManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());
            }
        };
        mWebService.setResponseResult(responseResult);
        mGroupManager.createGroup(mGroupName, masterDeviceId, locationId, request);

    }

    public void testLoginFailWhenCreateGroup(){

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
        mGroupManager.createGroup(mGroupName, masterDeviceId, locationId, request);

    }
}
