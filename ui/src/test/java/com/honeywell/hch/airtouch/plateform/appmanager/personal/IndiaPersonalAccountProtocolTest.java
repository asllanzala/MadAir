package com.honeywell.hch.airtouch.plateform.appmanager.personal;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Created by Qian Jin on 3/15/16.
 */
public class IndiaPersonalAccountProtocolTest {

    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
    }

    @Test
    public void testCanShowWeather() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowWeatherView());
    }

    @Test
    public void testCanShowNoDeviceView() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowNoDeviceView());
    }

    @Test
    public void testCanShowThreeTitleBtn() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowThreeTitleBtn());
    }

    @Test
    public void testCanShowIndiaLayout() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowIndiaLayout());
    }

    @Test
    public void testCurrentLocationIsShowGpsSuccess() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.currentLocationIsShowGpsSuccess());
    }

    @Test
    public void testCurrentLocationIsShowGpsFail() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.currentLocationIsShowGpsFail());
    }

    @Test
    public void testCurrentLocationIsShowIndiaLayout() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.currentLocationIsShowIndiaLayout());
    }

    @Test
    public void testCanShowDeleteDeviceMessageBox() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowDeleteDeviceMessageBox());
    }

    @Test
    public void testCanShowCurrentLocationSideBar() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowCurrentLocationSideBar());
    }

    @Test
    public void testCanSetDefaultHome() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canSetDefaultHome());

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(true, protocol.canSetDefaultHome());
    }

    @Test
    public void testCanShowEmotion() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(false, protocol.canShowEmotion(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canShowEmotion(userLocationData));
    }

    @Test
    public void testCanShowArriveHome() {
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(false, protocol.canShowArriveHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canShowArriveHome(userLocationData));
    }


    @Test
    public void testCanShowWeatherEffect(){
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowWeatherEffect());
    }

    @Test
    public void testCanNeedLocatingInGpsFragment(){
        IndiaPersonalAccountProtocol protocol = new IndiaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canNeedLocatingInGpsFragment());
    }
}
