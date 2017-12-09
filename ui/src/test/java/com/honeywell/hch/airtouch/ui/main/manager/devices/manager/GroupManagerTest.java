package com.honeywell.hch.airtouch.ui.main.manager.devices.manager;

import android.app.Application;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.ui.main.manager.common.HomeControlManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.net.URLEncoder;

/**
 * Created by Qian Jin on 1/5/16.
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class GroupManagerTest {

    private GroupManager groupManager;
    private HomeControlManager mHomeControlManager;
    private MockWebService webService;

    private CreateMethodTest mCreateGroupManager;
    private DeleteMethodTest mDeleteGroupManager;
    private AddDeviceToTest mAddDeviceToGroupManager;
    private DeleteDeviceFromTest mDeleteDeviceFromGroupManager;
    private UpdateNameTest mUpdateGroupNameManager;
    private IsMasterDeviceTest mIsMasterDeviceManager;
    private DeleteDeviceTest mDeleteDeviceManager;
    private SendScenarioToTest mSendScenarioManager;
    private GetGroupByIdTest mGetGroupByGroupIdManager;
    private GroupManagerResponseTest mGroupManagerResponse;
    private ControlHomeTest mControlHomeTest;
    private DeleteDeviceListTest mDeleteDeviceListTest;
    protected Application application = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception {
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        groupManager = new GroupManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;
        mHomeControlManager = new HomeControlManager();
        mCreateGroupManager = new CreateMethodTest(groupManager, webService);
        mDeleteGroupManager = new DeleteMethodTest(groupManager, webService, null);
        mAddDeviceToGroupManager = new AddDeviceToTest(groupManager, webService);
        mDeleteDeviceFromGroupManager = new DeleteDeviceFromTest(groupManager, webService, null);
        mUpdateGroupNameManager = new UpdateNameTest(groupManager, webService, null);
        mIsMasterDeviceManager = new IsMasterDeviceTest(groupManager, webService);
        mDeleteDeviceManager = new DeleteDeviceTest(groupManager, webService);
        mSendScenarioManager = new SendScenarioToTest(groupManager, webService, null);
        mGetGroupByGroupIdManager = new GetGroupByIdTest(groupManager, webService);
        mGroupManagerResponse = new GroupManagerResponseTest(groupManager);
        mControlHomeTest = new ControlHomeTest(mHomeControlManager, webService);
        mDeleteDeviceListTest = new DeleteDeviceListTest(null, webService, null, groupManager);

    }

    @Test
    public void testCreateGroup() throws Exception {

        mCreateGroupManager.testSuccessCreateGroup();
        mCreateGroupManager.testLoginFailWhenCreateGroup();
        mCreateGroupManager.testFailCreateGroup();
    }


    @Test
    public void testDeleteGroup() throws Exception {
        mDeleteGroupManager.testSuccessDeleteGroup();
        mDeleteGroupManager.testFailDeleteGroup();
        mDeleteGroupManager.testLoginFailWhenDeleteGroup();
    }

    @Test
    public void testAddDeviceToGroup() throws Exception {
        mAddDeviceToGroupManager.testSuccessAddDeviceToGroup();
        mAddDeviceToGroupManager.testFailAddDeviceToGroup();
        mAddDeviceToGroupManager.testLoginFailWhenAddDeviceToGroup();
    }

    @Test
    public void testDeleteDeviceFromGroup() throws Exception {
        mDeleteDeviceFromGroupManager.testSuccessDeleteDeviceFromGroup();
        mDeleteDeviceFromGroupManager.testFailDeleteDeviceFromGroup();
        mDeleteDeviceFromGroupManager.testLoginFailWhenDeleteDeviceFromGroup();
    }

    @Test
    public void testUpdateGroupName() throws Exception {
        mUpdateGroupNameManager.testSuccessUpdateGroupName();
        mUpdateGroupNameManager.testFailUpdateGroupName();
        mUpdateGroupNameManager.testLoginFailWhenUpdateGroupName();
    }

    @Test
    public void testIsMasterDevice() throws Exception {
        mIsMasterDeviceManager.testSuccessIsMasterDevice();
        mIsMasterDeviceManager.testFailIsMasterDevice();
        mIsMasterDeviceManager.testLoginFailWhenIsMasterDevice();
    }

    @Test
    public void testDeleteDevice() throws Exception {
        mDeleteDeviceManager.testSuccessDeleteDevice();
        mDeleteDeviceManager.testFailDeleteDevice();
        mDeleteDeviceManager.testLoginFailWhenDeleteDevice();
    }

    @Test
    public void testSendScenarioToGroup() throws Exception {
        mSendScenarioManager.testSuccessSendScenarioToGroup();
        mSendScenarioManager.testFailSendScenarioToGroup();
        mSendScenarioManager.testLoginFailWhenSendScenarioToGroup();
    }

    @Test
    public void testToURLEncoded() throws Exception {
        String testStr = "";
        Assert.assertEquals("", URLEncodedTest(testStr));
        testStr = null;
        Assert.assertEquals("", URLEncodedTest(testStr));
        testStr = "sljfsl";
        Assert.assertEquals(testStr, URLEncodedTest(testStr));
    }

    @Test
    public void getGroupByGroupId() throws Exception {
        mGetGroupByGroupIdManager.testSuccessGetGroupByGroupId();
        mGetGroupByGroupIdManager.testFailGetGroupByGroupId();
        mGetGroupByGroupIdManager.testLoginFailWhenGetGroupByGroupId();
    }

    @Test
    public void controlHomeDevice() throws Exception {
        mControlHomeTest.testSuccessControlHome();
        mControlHomeTest.testFailControlHome();
        mControlHomeTest.testLoginFailWhenControlHome();
    }

    private String URLEncodedTest(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), "UTF-8");
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    @Test
    public void testResponse() {
        mGroupManagerResponse.testGroupResponse();
        mGroupManagerResponse.testGetSucceedDevice();
    }

    @Test
    public void testDeleteDeviceList() throws Exception {
        mDeleteDeviceListTest.testSuccessDeleteDeviceFromGroup();
        mDeleteDeviceListTest.testFailDeleteDeviceFromGroup();
        mDeleteDeviceListTest.testLoginFailWhenDeleteDeviceFromGroup();
    }


}
