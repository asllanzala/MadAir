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
public class GetInvitationByIdTest extends BaseManagerFunctionTest {

    private static final int MANUAL_REFRESH = 1;
    private static final int MANUAL_LOADING = 2;
    private AuthorizeManager mAuthorizeManager;
    private MockWebService mWebService;


    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;

    public GetInvitationByIdTest(AuthorizeManager authorizeManager, MockWebService webService) {
        mAuthorizeManager = authorizeManager;
        mWebService = webService;
    }

    public void testSuccessGetInvitationById() {

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.GET_AUTH_MESSAGE_BY_ID;
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
        mAuthorizeManager.getInvitationsById(123);

    }


    public void testFailGetInvitationById() {

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.GET_AUTH_MESSAGE_BY_ID;
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
        mAuthorizeManager.getInvitationsById(123);

    }

    public void testLoginFailWhenGetInvitationById() {

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
        mAuthorizeManager.getInvitationsById(123);
    }
}
