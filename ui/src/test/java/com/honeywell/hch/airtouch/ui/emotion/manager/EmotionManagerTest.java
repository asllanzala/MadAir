package com.honeywell.hch.airtouch.ui.emotion.manager;

import android.app.Application;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Vincent on 17/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EmotionManagerTest {
    private EmotionManager mEmotionManager;
    private MockWebService webService;
    private GetPmLevelFromServerTest mGetPmLevelFromServerTest;

    protected Application application = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception {
//        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mEmotionManager = new EmotionManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;

        mGetPmLevelFromServerTest = new GetPmLevelFromServerTest(null, webService, mEmotionManager);
    }

    @Test
    public void testControlDevice() throws Exception {
        mGetPmLevelFromServerTest.testSuccessGetPmLevelFromServer();
        mGetPmLevelFromServerTest.testFailGetPmLevelFromServer();
        mGetPmLevelFromServerTest.testLoginFailWhenGetPmLevelFromServer();
    }
}
