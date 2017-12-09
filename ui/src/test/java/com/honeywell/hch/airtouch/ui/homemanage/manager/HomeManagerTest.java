package com.honeywell.hch.airtouch.ui.homemanage.manager;

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
 * Created by Vincent on 19/12/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HomeManagerTest {
    private HomeManagerManager mHomeManagerManager;
    private MockWebService webService;
    private SwapLocationNameTest mSwapLocationNameTest;
    private ProcessRemoveHomeTest mProcessRemoveHomeTest;
    private ProcessAddHomeTest mProcessAddHomeTest;
    private GetLocationTest mGetLocationTest;

    protected Application application = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception {
//        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mHomeManagerManager = new HomeManagerManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;

        mSwapLocationNameTest = new SwapLocationNameTest(null, webService, mHomeManagerManager);
        mProcessRemoveHomeTest = new ProcessRemoveHomeTest(null, webService, mHomeManagerManager);
        mProcessAddHomeTest = new ProcessAddHomeTest(null, webService, mHomeManagerManager);
        mGetLocationTest = new GetLocationTest(null, webService, mHomeManagerManager);
    }

    @Test
    public void testSwapLocationName() throws Exception {
        mSwapLocationNameTest.testSuccessSwapLocationName();
        mSwapLocationNameTest.testFailSwapLocationName();
        mSwapLocationNameTest.testLoginFailWhenSwapLocationName();
    }

    @Test
    public void testProcessRemoveHome() throws Exception {
        mProcessRemoveHomeTest.testSuccessProcessRemoveHome();
        mProcessRemoveHomeTest.testFailProcessRemoveHome();
        mProcessRemoveHomeTest.testLoginFailWhenProcessRemoveHome();
    }

    @Test
    public void testProcessAddHome() throws Exception {
        mProcessAddHomeTest.testSuccessProcessAddHome();
        mProcessAddHomeTest.testFailProcessAddHome();
        mProcessAddHomeTest.testLoginFailWhenProcessAddHome();
    }

    @Test
    public void testGetLocation()throws Exception{
        mGetLocationTest.testSuccessGetLocation();
        mGetLocationTest.testFailGetLocation();
        mGetLocationTest.testLoginFailWhenGetLocation();
    }

}
