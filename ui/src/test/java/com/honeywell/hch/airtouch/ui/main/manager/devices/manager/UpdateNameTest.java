package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;
import com.honeywell.hch.airtouch.ui.control.manager.group.GroupControlUIManager;

import junit.framework.Assert;

/**
 * Created by Vincent on 11/1/16.
 */
public class UpdateNameTest extends BaseManagerFunctionTest {
    private GroupManager mGroupManager;
    private MockWebService mWebService;

    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private int mGroupId = 001;
    private String groupName = "groupName";
    private GroupControlUIManager mGroupControlUIManager;

    public UpdateNameTest(GroupManager groupManager, MockWebService webService, GroupControlUIManager groupControlUIManager) {
        mGroupManager = groupManager;
        mWebService = webService;
        mGroupControlUIManager = groupControlUIManager;
    }

    public void testSuccessUpdateGroupName() {

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.UPDATE_GROUP_NAME;
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
        if (mGroupControlUIManager != null) {
            mGroupControlUIManager.updateGroupName(groupName, mGroupId);
        } else {
            mGroupManager.updateGroupName(groupName, mGroupId);
        }

    }

    public void testFailUpdateGroupName() {

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.UPDATE_GROUP_NAME;
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
        if (mGroupControlUIManager != null) {
            mGroupControlUIManager.updateGroupName(groupName, mGroupId);
        } else {
            mGroupManager.updateGroupName(groupName, mGroupId);
        }

    }

    public void testLoginFailWhenUpdateGroupName() {

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
        if (mGroupControlUIManager != null) {
            mGroupControlUIManager.updateGroupName(groupName, mGroupId);
        } else {
            mGroupManager.updateGroupName(groupName, mGroupId);
        }

    }
}
