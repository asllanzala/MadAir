package com.honeywell.hch.airtouch.ui.control.manager.group;

import android.app.Application;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeleteDeviceFromTest;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeleteDeviceListTest;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.DeleteMethodTest;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.GroupManager;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.SendScenarioToTest;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.UpdateNameTest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Vincent on 14/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)

public class GroupControlUiManagerTest {
    private GroupControlUIManager mGroupControlUIManager;
    private MockWebService webService;
    private GroupManager groupManager;
    private DeleteDeviceFromTest mDeleteDeviceFromGroupManager;
    private DeleteMethodTest mDeleteGroupManager;
    private UpdateNameTest mUpdateGroupNameManager;
    private SendScenarioToTest mSendScenarioManager;
    private DeleteDeviceListTest mDeleteDeviceListTest;


    protected Application application = RuntimeEnvironment.application;

    @Before
    public void setUp() throws Exception {
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;
        mGroupControlUIManager = new GroupControlUIManager(123);
        groupManager = new GroupManager();
        mDeleteDeviceFromGroupManager = new DeleteDeviceFromTest(groupManager, webService, mGroupControlUIManager);
        mDeleteGroupManager = new DeleteMethodTest(groupManager, webService, mGroupControlUIManager);
        mUpdateGroupNameManager = new UpdateNameTest(groupManager, webService, mGroupControlUIManager);
        mSendScenarioManager = new SendScenarioToTest(groupManager, webService, null);
        mDeleteDeviceListTest = new DeleteDeviceListTest(null,webService,mGroupControlUIManager,groupManager);
    }

    @Test
    public void testGetGroupDeviceCategoryData() {
        Assert.assertEquals(false, mGroupControlUIManager.isHasMasterDeviceInSelectedIds());
        Assert.assertEquals(true, mGroupControlUIManager.canClcikable());

    }

    @Test
    public void testDeleteDeviceFromGroup() throws Exception {
        mDeleteDeviceFromGroupManager.testSuccessDeleteDeviceFromGroup();
        mDeleteDeviceFromGroupManager.testFailDeleteDeviceFromGroup();
        mDeleteDeviceFromGroupManager.testLoginFailWhenDeleteDeviceFromGroup();
    }

    @Test
    public void testDeleteGroup() throws Exception {
        mDeleteGroupManager.testSuccessDeleteGroup();
        mDeleteGroupManager.testFailDeleteGroup();
        mDeleteGroupManager.testLoginFailWhenDeleteGroup();
    }

    @Test
    public void testUpdateGroupName() throws Exception {
        mUpdateGroupNameManager.testSuccessUpdateGroupName();
        mUpdateGroupNameManager.testFailUpdateGroupName();
        mUpdateGroupNameManager.testLoginFailWhenUpdateGroupName();
    }

    @Test
    public void testSendScenarioToGroup() throws Exception {
        mSendScenarioManager.testSuccessSendScenarioToGroup();
        mSendScenarioManager.testFailSendScenarioToGroup();
        mSendScenarioManager.testLoginFailWhenSendScenarioToGroup();
    }

    @Test
    public void testSharePreference() throws Exception {
        Assert.assertEquals(DeviceMode.MODE_UNDEFINE, mGroupControlUIManager.getSendGroupMode(4));
        Assert.assertEquals(DeviceMode.MODE_UNDEFINE, mGroupControlUIManager.getSuccessGroupMode(4));
        Assert.assertEquals(DeviceMode.IS_REFLASHING, mGroupControlUIManager.getIsFlashing(4));
        mGroupControlUIManager.clearFlashPreference(application);
        Assert.assertEquals(DeviceMode.IS_REFLASHING, mGroupControlUIManager.getIsFlashing(4));
    }

    @Test
    public void testDeleteDeviceList() throws Exception {
        mDeleteDeviceListTest.testSuccessDeleteDeviceFromGroup();
        mDeleteDeviceListTest.testFailDeleteDeviceFromGroup();
        mDeleteDeviceListTest.testLoginFailWhenDeleteDeviceFromGroup();
    }

}
