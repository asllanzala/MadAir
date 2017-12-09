package com.honeywell.hch.airtouch.ui.homemanage.manager;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;
import com.honeywell.hch.airtouch.ui.authorize.manager.BaseManagerFunctionTest;

import junit.framework.Assert;

/**
 * Created by Vincent on 11/11/16.
 */
public class ProcessRemoveHomeTest extends BaseManagerFunctionTest {

    private HomeManagerManager mHomeManager;
    private HomeManagerUiManager mHomeUiManager;
    private MockWebService mWebService;


    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;

    public ProcessRemoveHomeTest(HomeManagerUiManager homeManagerUiManager, MockWebService webService, HomeManagerManager homeManagerManager) {
        mHomeUiManager = homeManagerUiManager;
        mWebService = webService;
        mHomeManager = homeManagerManager;
    }

    public void testSuccessProcessRemoveHome() {

        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_DEVICE;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mBundle.putInt(EmotionBottleResponse.EMOTION_RESP_DATA, mResponseCode);
        mResponseResult.setResponseData(mBundle);

        //test login success
        setReloginSuccess();
        mHomeManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(mRequestId, responseResult.getRequestId());
                Assert.assertEquals(mResult, responseResult.isResult());
                Assert.assertEquals(mResponseCode, responseResult.getResponseCode());
            }
        };
        mWebService.setResponseResult(mResponseResult);
        if (mHomeUiManager != null) {
            mHomeUiManager.processRemoveHome(123);
        } else {
            mHomeManager.processRemoveHome(123);
        }
    }


    public void testFailProcessRemoveHome() {

        mResult = false;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_DEVICE;
        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginSuccess();
        mHomeManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());
            }
        };
        mWebService.setResponseResult(responseResult);
        if (mHomeUiManager != null) {
            mHomeUiManager.processRemoveHome(123);
        } else {
            mHomeManager.processRemoveHome(123);
        }

    }

    public void testLoginFailWhenProcessRemoveHome() {

        ResponseResult responseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);

        //test login success
        setReloginFail();
        mHomeManager.mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                Assert.assertEquals(false, responseResult.isResult());

            }
        };
        mWebService.setResponseResult(responseResult);
        if (mHomeUiManager != null) {
            mHomeUiManager.processRemoveHome(123);
        } else {
            mHomeManager.processRemoveHome(123);
        }

    }

}
