package com.honeywell.hch.airtouch.plateform.http.manager.model;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

/**
 * Created by wuyuan on 10/12/15.
 */
@RunWith(RobolectricTestRunner.class)
public class UserLocationDataTest {

    public static final int WATER_TYPE = 10000;

    private UserLocationData mUserLocationData;

    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);


    @Before
    public void setup() {
        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);

        mUserLocationData = new RealUserLocationData();
        mUserLocationData.setLocationID(111);
        mUserLocationData.setCity("CHSH00000");
        mUserLocationData.setName("Shanghai");

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(1111);
        deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice = new AirTouchDeviceObject(deviceInfo.getDeviceType());
        airTouchSeriesDevice.setDeviceInfo(deviceInfo);
        AirtouchRunStatus runStatus = new AirtouchRunStatus();
        runStatus.setmPM25Value(39);
        airTouchSeriesDevice.setAirtouchDeviceRunStatus(runStatus);
        mUserLocationData.addHomeDeviceItemToList(airTouchSeriesDevice);

        DeviceInfo deviceInfo2 = new DeviceInfo();
        deviceInfo2.setDeviceID(2222);
        deviceInfo2.setDeviceType(HPlusConstants.AIRTOUCH_X_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice2 = new AirTouchDeviceObject(deviceInfo2.getDeviceType());
        airTouchSeriesDevice2.setDeviceInfo(deviceInfo2);
        AirtouchRunStatus runStatus2 = new AirtouchRunStatus();
        runStatus2.setmPM25Value(121);
        airTouchSeriesDevice2.setAirtouchDeviceRunStatus(runStatus2);
        mUserLocationData.addHomeDeviceItemToList(airTouchSeriesDevice2);


        DeviceInfo deviceInfo3 = new DeviceInfo();
        deviceInfo3.setDeviceID(3333);
        deviceInfo3.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice3 = new AirTouchDeviceObject(deviceInfo3.getDeviceType());
        airTouchSeriesDevice3.setDeviceInfo(deviceInfo3);
        AirtouchRunStatus runStatus3 = new AirtouchRunStatus();
        runStatus3.setmPM25Value(110);
        airTouchSeriesDevice3.setAirtouchDeviceRunStatus(runStatus3);
        mUserLocationData.addHomeDeviceItemToList(airTouchSeriesDevice3);

        DeviceInfo deviceInfo4 = new DeviceInfo();
        deviceInfo4.setDeviceID(4444);
        deviceInfo4.setDeviceType(WATER_TYPE);
        HomeDevice waterDevice = new WaterDeviceObject(deviceInfo4);
        waterDevice.setDeviceInfo(deviceInfo4);
        mUserLocationData.addHomeDeviceItemToList(waterDevice);

    }

//    @Test
//    public void testGetWorstDevice(){
//        AirTouchDeviceObject worstDevice = mUserLocationData.getDefaultPMDevice();
//        Assert.assertEquals(2222, worstDevice.getDeviceInfo().getDeviceID());
//
//        Assert.assertEquals(121, worstDevice.getAirtouchDeviceRunStatus().getmPM25Value());
//    }


    @Test
    public void testGetAirTouchSeriesList(){
        ArrayList<AirTouchDeviceObject> airTouchSeriesDevicesList = mUserLocationData.getAirTouchSeriesList();
        Assert.assertEquals(3, airTouchSeriesDevicesList.size());
    }

    @Test
    public void testGetHomeDeviceWithDeviceId(){
        HomeDevice homeDevice = mUserLocationData.getHomeDeviceWithDeviceId(1111);
        boolean isInstatnceof = homeDevice instanceof AirTouchDeviceObject;
        Assert.assertEquals(true, isInstatnceof);
        Assert.assertEquals(HPlusConstants.AIRTOUCH_S_TYPE, homeDevice.getDeviceType());

        HomeDevice homeDevice2 = mUserLocationData.getHomeDeviceWithDeviceId(4444);
        boolean isInstatnceof2 = homeDevice2 instanceof AirTouchDeviceObject;
        Assert.assertEquals(false, isInstatnceof2);

        Assert.assertEquals(WATER_TYPE, homeDevice2.getDeviceType());
    }
}
