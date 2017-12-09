package com.honeywell.hch.airtouch.plateform.appmanager.enterprise;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)

/**
 * Created by Qian Jin on 3/15/16.
 */
public class EnterpriseAccountProtocolTest {
    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
    }

    @Test
    public void testCanEditHome() {
        EnterpriseAccountProtocol protocol = new EnterpriseAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canEditHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canEditHome(userLocationData));
    }

    @Test
    public void testCanEnrollToHome() {
        EnterpriseAccountProtocol protocol = new EnterpriseAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canEnrollToHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canEnrollToHome(userLocationData));
    }

    @Test
    public void testCanSetDefaultHome() {
        EnterpriseAccountProtocol protocol = new EnterpriseAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(false, protocol.canSetDefaultHome());

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canSetDefaultHome());
    }

    @Test
    public void testCanSendAuthorization() {
        EnterpriseAccountProtocol protocol = new EnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canSendAuthorization());
    }

    @Test
    public void testIsNeedReceiveRefreshInAllDeviceActivity(){

        EnterpriseAccountProtocol protocol = new EnterpriseAccountProtocol();

        UserLocationData userLocationData =  initDeviceInLocationData(21);
        Assert.assertEquals(false, protocol.isNeedReceiveRefreshInAllDeviceActivity(userLocationData));

        userLocationData =  initDeviceInLocationData(19);
        Assert.assertEquals(false, protocol.isNeedReceiveRefreshInAllDeviceActivity(userLocationData));
    }

    private UserLocationData initDeviceInLocationData(int deviceNum){
        UserLocationData userLocationData = new RealUserLocationData();
        for (int i = 0; i < deviceNum; i++){
            HomeDevice device = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_450_TYPE);
            userLocationData.addHomeDeviceItemToList(device);
        }
        return userLocationData;
    }

    @Test
    public void testCanShowWaitingDotAnimation(){
        EnterpriseAccountProtocol protocol = new EnterpriseAccountProtocol();
        org.junit.Assert.assertEquals(false, protocol.canShowWaitingDotAnimation());

    }
}
