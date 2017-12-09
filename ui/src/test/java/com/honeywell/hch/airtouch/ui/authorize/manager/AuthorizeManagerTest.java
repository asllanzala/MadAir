package com.honeywell.hch.airtouch.ui.authorize.manager;

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
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Qian Jin on 2/16/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)

public class AuthorizeManagerTest {
    private AuthorizeManager mAuthorizeManager;
    private MockWebService webService;

    private GetAuthUnreadMessageTest mGetAuthUnreadMessageManager;
    private CheckAuthUserTest mCheckAuthUserTestManager;
    private GrantAuthToDeviceTest mGrantAuthToDeviceTestManager;
    private RemoveAuthTest mRemoveAuthTestManager;
    private GetInvitationsTest mGetInvitationsTestManager;
    private HandleInvitationTest mHandleInvitationTestManager;
    private GetAuthDevicesTest mGetAuthDevicesTest;
    private GetInvitationByIdTest mGetInvitationByIdTest;
    private GetAuthGroupDevicesTest mGetAuthGroupDevicesTest;
    private RemoveAuthGroupTest mRemoveAuthGroupTest;
    private GetDeviceListByGroupIdTest mGetDeviceListByGroupIdTest;


    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mAuthorizeManager = new AuthorizeManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;

        mGetAuthUnreadMessageManager = new GetAuthUnreadMessageTest(mAuthorizeManager, webService);
        mCheckAuthUserTestManager = new CheckAuthUserTest(mAuthorizeManager, webService);
        mGrantAuthToDeviceTestManager = new GrantAuthToDeviceTest(mAuthorizeManager, webService);
        mRemoveAuthTestManager = new RemoveAuthTest(mAuthorizeManager, webService);
        mGetInvitationsTestManager = new GetInvitationsTest(mAuthorizeManager, webService);
        mHandleInvitationTestManager = new HandleInvitationTest(mAuthorizeManager, webService);
        mGetAuthDevicesTest = new GetAuthDevicesTest(mAuthorizeManager, webService);
        mGetInvitationByIdTest = new GetInvitationByIdTest(mAuthorizeManager, webService);
        mGetAuthGroupDevicesTest = new GetAuthGroupDevicesTest(mAuthorizeManager, webService);
        mRemoveAuthGroupTest = new RemoveAuthGroupTest(mAuthorizeManager,webService);
        mGetDeviceListByGroupIdTest = new GetDeviceListByGroupIdTest(mAuthorizeManager,webService);
    }

    @Test
    public void testGetAuthUnreadMessage() throws Exception {
        mGetAuthUnreadMessageManager.testSuccessGetAuthUnreadMessage();
        mGetAuthUnreadMessageManager.testFailGetAuthUnreadMessage();
        mGetAuthUnreadMessageManager.testLoginFailWhenGetAuthUnreadMessage();
    }

    @Test
    public void testCheckAuthUser() throws Exception {
        mCheckAuthUserTestManager.testSuccessCheckAuthUser();
        mCheckAuthUserTestManager.testFailCheckAuthUser();
        mCheckAuthUserTestManager.testLoginFailWhenCheckAuthUser();
    }

    @Test
    public void testGrantAuthToDevice() throws Exception {
        mGrantAuthToDeviceTestManager.testGrantAuthToDevice();
        mGrantAuthToDeviceTestManager.testFailGrantAuthToDevice();
        mGrantAuthToDeviceTestManager.testLoginFailWhenGrantAuthToDevice();
    }

    @Test
    public void testRemoveAuth() throws Exception {
        mRemoveAuthTestManager.testSuccessRemoveAuth();
        mRemoveAuthTestManager.testFailRemoveAuth();
        mRemoveAuthTestManager.testLoginFailWhenRemoveAuth();
    }

    @Test
    public void testGetInvitations() throws Exception {
        mGetInvitationsTestManager.testSuccessGetInvitations();
        mGetInvitationsTestManager.testFailGetInvitations();
        mGetInvitationsTestManager.testLoginFailWhenGetInvitations();
    }

    @Test
    public void testHandleInvitation() throws Exception {
        mHandleInvitationTestManager.testSuccessHandleInvitation();
        mHandleInvitationTestManager.testFailHandleInvitation();
        mHandleInvitationTestManager.testLoginFailWhenHandleInvitation();
    }

    @Test
    public void testGetAuthDevices() throws Exception {
        mGetAuthDevicesTest.testSuccessGetAuthDevices();
        mGetAuthDevicesTest.testFailGetAuthDevices();
        mGetAuthDevicesTest.testLoginFailWhenGetAuthDevices();
    }

    @Test
    public void testGetInvitationById() throws Exception {
        mGetInvitationByIdTest.testSuccessGetInvitationById();
        mGetInvitationByIdTest.testFailGetInvitationById();
        mGetInvitationByIdTest.testLoginFailWhenGetInvitationById();
    }

    @Test
    public void testGetAuthGroupDevices() throws Exception {
        mGetAuthGroupDevicesTest.testSuccessGetAuthDevices();
        mGetAuthGroupDevicesTest.testFailGetAuthDevices();
        mGetAuthGroupDevicesTest.testLoginFailWhenGetAuthDevices();
    }

    @Test
    public void testRemoveAuthGroup() throws Exception {
        mRemoveAuthGroupTest.testSuccessRemoveAuth();
        mRemoveAuthGroupTest.testFailRemoveAuth();
        mRemoveAuthGroupTest.testLoginFailWhenRemoveAuth();
    }

    @Test
    public void testGetDeviceListByGroupId() throws Exception {
        mGetDeviceListByGroupIdTest.testSuccessCheckAuthUser();
        mGetDeviceListByGroupIdTest.testFailCheckAuthUser();
        mGetDeviceListByGroupIdTest.testLoginFailWhenCheckAuthUser();
    }

}
