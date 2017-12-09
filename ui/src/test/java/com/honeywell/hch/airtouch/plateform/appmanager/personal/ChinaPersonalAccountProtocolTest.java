package com.honeywell.hch.airtouch.plateform.appmanager.personal;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Qian Jin on 3/15/16.
 */
public class ChinaPersonalAccountProtocolTest {

    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
    }

    @Test
    public void testCanShowWeather() {
        initUserLocationDataList(4);
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowWeatherView());
    }

    @Test
    public void testCanShowNoDeviceView() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowNoDeviceView());
    }

    @Test
    public void testCanShowThreeTitleBtn() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowThreeTitleBtn());
    }

    @Test
    public void testCanShowIndiaLayout() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowIndiaLayout());
    }

    @Test
    public void testCurrentLocationIsShowGpsSuccess() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.currentLocationIsShowGpsSuccess());
    }

    @Test
    public void testCurrentLocationIsShowGpsFail() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.currentLocationIsShowGpsFail());
    }

    @Test
    public void testCurrentLocationIsShowIndiaLayout() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.currentLocationIsShowIndiaLayout());
    }

    @Test
    public void testCanShowDeleteDeviceMessageBox() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(false, protocol.canShowDeleteDeviceMessageBox());
    }

    @Test
    public void testCanShowCurrentLocationSideBar() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowCurrentLocationSideBar());
    }

    @Test
    public void testCanSetDefaultHome() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canSetDefaultHome());

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(true, protocol.canSetDefaultHome());
    }

    @Test
    public void testCanShowEmotion() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canShowEmotion(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(true, protocol.canShowEmotion(userLocationData));
    }

    @Test
    public void testCanShowArriveHome() {
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canShowArriveHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(true, protocol.canShowArriveHome(userLocationData));
    }


    @Test
    public void testCanShowWeatherEffect(){
        initUserLocationDataList(4);
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canShowWeatherEffect());
    }

    private void initUserLocationDataList(int size){
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < size; i++){
            UserLocationData userLocationData = new RealUserLocationData();
            userLocationData.setLocationID(i);
            userLocationDataList.add(userLocationData);
        }

        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);
    }

    @Test
    public void testCanNeedLocatingInGpsFragment(){
        ChinaPersonalAccountProtocol protocol = new ChinaPersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canNeedLocatingInGpsFragment());
    }
}
