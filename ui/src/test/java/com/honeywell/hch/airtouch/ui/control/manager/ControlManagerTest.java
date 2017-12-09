package com.honeywell.hch.airtouch.ui.control.manager;

import android.app.Application;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlManager;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Qian Jin on 2/16/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)

public class ControlManagerTest {
    private ControlManager mControlManager;
    private MockWebService webService;

    private ControlWaterDeviceTest mControlWaterDeviceTest;
    private ControlDeviceTest mControlDeviceTest;
    private GetConfigFromServerTest mGetConfigFromServerTest;


    protected Application application = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception {
//        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mControlManager = new ControlManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;

        mControlDeviceTest = new ControlDeviceTest(mControlManager, webService,null);
        mControlWaterDeviceTest = new ControlWaterDeviceTest(mControlManager, webService,null);
        mGetConfigFromServerTest = new GetConfigFromServerTest(mControlManager, webService,null);
    }

    @Test
    public void testControlDevice() throws Exception {
        mControlDeviceTest.testSuccessControlDevice();
        mControlDeviceTest.testFailControlDevice();
        mControlDeviceTest.testLoginFailWhenControlDevice();
    }

    @Test
    public void testControlWaterDevice() throws Exception {
        mControlWaterDeviceTest.testSuccessControlDevice();
        mControlWaterDeviceTest.testFailControlDevice();
        mControlWaterDeviceTest.testLoginFailWhenControlDevice();
    }

    @Test
    public void testGetConfigFromServerTest() throws Exception {
        mGetConfigFromServerTest.testSuccessGetConfigFromServer();
        mGetConfigFromServerTest.testFailGetConfigFromServer();
        mGetConfigFromServerTest.testLoginFailWhenGetConfigFromServer();
    }

    @Test
    public void testSharePreference() {
        LibApplication.setApplication(application);
        ControlUIManager controlUIManager = new ControlUIManager();
        mControlManager.setControlModePre(123, "123");
        Assert.assertEquals("123", controlUIManager.getControlModePre(123));

        mControlManager.setIsFlashing(true);
        Assert.assertEquals(true, controlUIManager.getIsFlashing(0));
    }

}
