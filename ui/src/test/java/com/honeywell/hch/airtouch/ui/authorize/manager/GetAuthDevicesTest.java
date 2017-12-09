package com.honeywell.hch.airtouch.ui.authorize.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;

import junit.framework.Assert;

/**
 * Created by Qian Jin on 2/16/16.
 */
public class GetAuthDevicesTest extends BaseManagerFunctionTest {

    private AuthorizeManager mAuthorizeManager;
    private MockWebService mWebService;


    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;

    public GetAuthDevicesTest(AuthorizeManager authorizeManager, MockWebService webService){
        mAuthorizeManager = authorizeManager;
        mWebService = webService;
    }

    public void testSuccessGetAuthDevices(){

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.GET_AUTH_DEVICES;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mResponseResult.setResponseData(mBundle);

        //test login success
        setReloginSuccess();
        mAuthorizeManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(mRequestId, responseResult.getRequestId());
                Assert.assertEquals(mResult, responseResult.isResult());
                Assert.assertEquals(mResponseCode, responseResult.getResponseCode());
            }
        };
        mWebService.setResponseResult(mResponseResult);
        mAuthorizeManager.getAuthDevices();

    }


    public void testFailGetAuthDevices(){

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.GET_AUTH_DEVICES;
        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginSuccess();
        mAuthorizeManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());
            }
        };
        mWebService.setResponseResult(responseResult);
        mAuthorizeManager.getAuthDevices();

    }

    public void testLoginFailWhenGetAuthDevices(){

        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginFail();
        mAuthorizeManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());

            }
        };
        mWebService.setResponseResult(responseResult);
        mAuthorizeManager.getAuthDevices();

    }
}
