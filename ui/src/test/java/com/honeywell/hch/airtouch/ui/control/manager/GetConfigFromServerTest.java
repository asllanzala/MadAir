package com.honeywell.hch.airtouch.ui.control.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthMessagesResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlManager;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIManager;

import junit.framework.Assert;

/**
 * Created by Vincent on 11/11/16.
 */
public class GetConfigFromServerTest extends BaseManagerFunctionTest {

    private ControlManager mControlManager;
    private MockWebService mWebService;
    private ControlUIManager mControlUIManager;

    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;

    public GetConfigFromServerTest(ControlManager controlManager, MockWebService webService, ControlUIManager controlUIManager) {
        mControlManager = controlManager;
        mWebService = webService;
        mControlUIManager = controlUIManager;
    }

    public void testSuccessGetConfigFromServer() {

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_DEVICE;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mBundle.putInt(GetAuthMessagesResponse.AUTH_MESSAGE_DATA, mResponseCode);
        mResponseResult.setResponseData(mBundle);

        //test login success
        setReloginSuccess();
        mControlManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(mRequestId, responseResult.getRequestId());
                Assert.assertEquals(mResult, responseResult.isResult());
                Assert.assertEquals(mResponseCode, responseResult.getResponseCode());
            }
        };
        mWebService.setResponseResult(mResponseResult);
        if (mControlUIManager != null) {
            mControlUIManager.getConfigFromServer();
        } else {
            mControlManager.getConfigFromServer();
        }

    }


    public void testFailGetConfigFromServer() {

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_DEVICE;
        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginSuccess();
        mControlManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());
            }
        };
        mWebService.setResponseResult(responseResult);
        if (mControlUIManager != null) {
            mControlUIManager.getConfigFromServer();
        } else {
            mControlManager.getConfigFromServer();
        }


    }

    public void testLoginFailWhenGetConfigFromServer() {

        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginFail();
        mControlManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());

            }
        };
        mWebService.setResponseResult(responseResult);
        if (mControlUIManager != null) {
            mControlUIManager.getConfigFromServer();
        } else {
            mControlManager.getConfigFromServer();
        }
    }

}
