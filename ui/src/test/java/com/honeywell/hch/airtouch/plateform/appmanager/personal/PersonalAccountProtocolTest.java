package com.honeywell.hch.airtouch.plateform.appmanager.personal;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
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
public class PersonalAccountProtocolTest {
    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() throws Exception {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
    }

    @Test
    public void testCanEditHome() {
        PersonalAccountProtocol protocol = new PersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canEditHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canEditHome(userLocationData));
    }

    @Test
    public void testCanEnrollToHome() {
        PersonalAccountProtocol protocol = new PersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canEnrollToHome(userLocationData));

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(false, protocol.canEnrollToHome(userLocationData));
    }

    @Test
    public void testCanSetDefaultHome() {
        PersonalAccountProtocol protocol = new PersonalAccountProtocol();
        UserLocationData userLocationData = new RealUserLocationData();

        userLocationData.setIsLocationOwner(true);
        Assert.assertEquals(true, protocol.canSetDefaultHome());

        userLocationData.setIsLocationOwner(false);
        Assert.assertEquals(true, protocol.canSetDefaultHome());
    }

    @Test
    public void testCanSendAuthorization() {
        PersonalAccountProtocol protocol = new PersonalAccountProtocol();
        Assert.assertEquals(true, protocol.canSendAuthorization());
    }

    @Test
    public void testIsNeedReceiveRefreshInAllDeviceActivity(){

        PersonalAccountProtocol protocol = new PersonalAccountProtocol();

        UserLocationData userLocationData =  initDeviceInLocationData(19);
        Assert.assertEquals(true, protocol.isNeedReceiveRefreshInAllDeviceActivity(userLocationData));

        userLocationData =  initDeviceInLocationData(21);
        Assert.assertEquals(false, protocol.isNeedReceiveRefreshInAllDeviceActivity(userLocationData));
    }

    @Test
    public void testCanShowWaitingDotAnimation(){
        PersonalAccountProtocol protocol = new PersonalAccountProtocol();
        initUserLocationDataList(5);
        org.junit.Assert.assertEquals(true, protocol.canShowWaitingDotAnimation());

        initUserLocationDataList(21);
        org.junit.Assert.assertEquals(false, protocol.canShowWaitingDotAnimation());
    }

    private void initUserLocationDataList(int size){
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < size; i++){
            UserLocationData userLocationData = new RealUserLocationData();
            userLocationData.setLocationID(i);

            AirTouchDeviceObject homeDevice = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_S_TYPE);
            DeviceInfo deviceInfo = new DeviceInfo();

            deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);

            homeDevice.setDeviceInfo(deviceInfo);
            userLocationData.addHomeDeviceItemToList(homeDevice);

            userLocationDataList.add(userLocationData);
        }

        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);
    }

    private UserLocationData initDeviceInLocationData(int deviceNum){
        UserLocationData userLocationData = new RealUserLocationData();
        for (int i = 0; i < deviceNum; i++){

            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);
            AirTouchDeviceObject device = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_S_TYPE);
            device.setDeviceInfo(deviceInfo);
            userLocationData.addHomeDeviceItemToList(device);
        }
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        userLocationDataList.add(userLocationData);
        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);
        return userLocationData;
    }
}
