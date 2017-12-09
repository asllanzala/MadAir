package com.honeywell.hch.airtouch.plateform.appmanager.enterprise;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
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
public class ChinaEnterpriseAccountProtocolTest {
    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
    }

    @Test
    public void testCanShowWeather() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canShowWeatherView());
    }

    @Test
    public void testCanShowNoDeviceView() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(true, protocol.canShowNoDeviceView());
    }

    @Test
    public void testCanShowThreeTitleBtn() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canShowThreeTitleBtn());
    }

    @Test
    public void testCanShowIndiaLayout() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(true, protocol.canShowIndiaLayout());
    }

    @Test
    public void testCurrentLocationIsShowGpsSuccess() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.currentLocationIsShowGpsSuccess());
    }

    @Test
    public void testCurrentLocationIsShowGpsFail() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.currentLocationIsShowGpsFail());
    }

    @Test
    public void testCurrentLocationIsShowIndiaLayout() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(true, protocol.currentLocationIsShowIndiaLayout());
    }

    @Test
    public void testCanShowDeleteDeviceMessageBox() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canShowDeleteDeviceMessageBox());
    }

    @Test
    public void testCanShowCurrentLocationSideBar() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canShowCurrentLocationSideBar());
    }

    @Test
    public void testCanSetDefaultHome() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(false, protocol.canSetDefaultHome());

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canSetDefaultHome());
    }

    @Test
    public void testCanShowEmotion() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(false, protocol.canShowEmotion(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canShowEmotion(userLocationData));
    }

    @Test
    public void testCanShowArriveHome() {
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(false, protocol.canShowArriveHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canShowArriveHome(userLocationData));
    }

    @Test
    public void testCanShowWeatherEffect(){
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canShowWeatherEffect());
    }

    @Test
    public void testCanNeedLocatingInGpsFragment(){
        ChinaEnterpriseAccountProtocol protocol = new ChinaEnterpriseAccountProtocol();
        Assert.assertEquals(false, protocol.canNeedLocatingInGpsFragment());
    }
}
