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
public class DeleteMethodTest extends BaseManagerFunctionTest {
    private GroupManager mGroupManager;
    private MockWebService mWebService;

    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private int mGroupId = 001;
    private GroupControlUIManager mGroupControlUIManager;

    public DeleteMethodTest(GroupManager groupManager, MockWebService webService, GroupControlUIManager groupControlUIManager) {
        mGroupControlUIManager = groupControlUIManager;
        mGroupManager = groupManager;
        mWebService = webService;
    }

    public void testSuccessDeleteGroup() {

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.DELETE_GROUP;
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
        mGroupManager.deleteGroup(mGroupId);
        if (mGroupControlUIManager != null) {
            mGroupControlUIManager.deleteGroup(mGroupId);
        } else {
            mGroupManager.deleteGroup(mGroupId);
        }

    }

    public void testFailDeleteGroup() {

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.DELETE_GROUP;
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
            mGroupControlUIManager.deleteGroup(mGroupId);
        } else {
            mGroupManager.deleteGroup(mGroupId);
        }
    }

    public void testLoginFailWhenDeleteGroup() {

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
            mGroupControlUIManager.deleteGroup(mGroupId);
        } else {
            mGroupManager.deleteGroup(mGroupId);
        }
    }
}
