package com.honeywell.hch.airtouch.ui.authorize.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.GetAuthMessagesResponse;

import junit.framework.Assert;

/**
 * Created by Qian Jin on 2/16/16.
 */
public class RemoveAuthGroupTest extends BaseManagerFunctionTest {

    private AuthorizeManager mAuthorizeManager;
    private MockWebService mWebService;


    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;

    public RemoveAuthGroupTest(AuthorizeManager authorizeManager, MockWebService webService){
        mAuthorizeManager = authorizeManager;
        mWebService = webService;
    }

    public void testSuccessRemoveAuth(){

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.REMOVE_DEVICE_AUTH;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mBundle.putInt(GetAuthMessagesResponse.AUTH_MESSAGE_DATA, mResponseCode);
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
        mAuthorizeManager.removeAuthGroup(1);

    }


    public void testFailRemoveAuth(){

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.REMOVE_DEVICE_AUTH;
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
        mAuthorizeManager.removeAuthGroup(1);

    }

    public void testLoginFailWhenRemoveAuth(){

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
        mAuthorizeManager.removeAuthGroup(1);

    }
}
