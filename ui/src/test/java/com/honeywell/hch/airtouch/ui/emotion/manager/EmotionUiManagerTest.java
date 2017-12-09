package com.honeywell.hch.airtouch.ui.emotion.manager;

import android.app.Application;
import android.os.Bundle;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.EmotionBottleResponse;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 17/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EmotionUiManagerTest {
    private EmotionManager mEmotionManager;
    private EmotionUiManager mEmotionUiManager;
    private MockWebService webService;
    private GetPmLevelFromServerTest mGetPmLevelFromServerTest;

    protected Application application = RuntimeEnvironment.application;
    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;

    @Before
    public void setUp() throws Exception {
//        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mEmotionManager = new EmotionManager();
        mEmotionUiManager = new EmotionUiManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;

        mGetPmLevelFromServerTest = new GetPmLevelFromServerTest(mEmotionUiManager, webService, mEmotionManager);
    }

    @Test
    public void test() {
        mEmotionUiManager = new EmotionUiManager(123);
        Assert.assertEquals(123, mEmotionUiManager.getmLocationId());

    }

    @Test
    public void testControlDevice() throws Exception {
        mGetPmLevelFromServerTest.testSuccessGetPmLevelFromServer();
        mGetPmLevelFromServerTest.testFailGetPmLevelFromServer();
        mGetPmLevelFromServerTest.testLoginFailWhenGetPmLevelFromServer();
    }

    @Test
    public void testGetEmotionBottleResponse() throws Exception {
        EmotionBottleResponse emotionBottleResponse = new EmotionBottleResponse();
        List<EmotionBottleResponse> emotionBottleResponseList = new ArrayList<>();
        mResult = true;
        mResponseCode = 200;
        mRequestId = RequestID.CONTROL_DEVICE;
        mResponseResult = new ResponseResult(mResult, mResponseCode, "", mRequestId);
        mBundle = new Bundle();
        mBundle.putSerializable(EmotionBottleResponse.EMOTION_RESP_DATA, (Serializable) emotionBottleResponseList);
        mResponseResult.setResponseData(mBundle);

        Assert.assertEquals(0, mEmotionUiManager.getEmotionBottleResponse(mResponseResult, null).size());
    }
}
