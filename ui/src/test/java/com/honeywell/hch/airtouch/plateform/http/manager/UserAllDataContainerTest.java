package com.honeywell.hch.airtouch.plateform.http.manager;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLocation;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.AirQuality;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.AirQualityIndex;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Now;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Weather;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by wuyuan on 10/12/15.
 */
@RunWith(RobolectricTestRunner.class)
public class UserAllDataContainerTest {

    private UserLocationData mUserLocationData1;
    private UserLocationData mUserLocationData2;
    private UserLocationData mUserLocationData3;
    private UserLocationData mUserLocationData4;

    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setup() {

        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);

        mUserLocationData1 = new RealUserLocationData();
        mUserLocationData1.setLocationID(111);
        mUserLocationData1.setCity("CHSH00000");
        mUserLocationData1.setName("Shanghai");

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(1111);
        deviceInfo.setIsAlive(true);
        deviceInfo.setPermission(300);
        deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_S_TYPE);
        airTouchSeriesDevice.setDeviceInfo(deviceInfo);
        AirtouchRunStatus runStatus = new AirtouchRunStatus();
        runStatus.setmPM25Value(121);
//        runStatus.setScenarioMode("Off");
        runStatus.setScenarioMode(1);

        airTouchSeriesDevice.setAirtouchDeviceRunStatus(runStatus);
        mUserLocationData1.addHomeDeviceItemToList(airTouchSeriesDevice);

        DeviceInfo deviceInfo2 = new DeviceInfo();
        deviceInfo2.setDeviceID(2222);
        deviceInfo2.setDeviceType(HPlusConstants.AIRTOUCH_X_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice2 = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_X_TYPE);
        airTouchSeriesDevice2.setDeviceInfo(deviceInfo2);
        AirtouchRunStatus runStatus2 = new AirtouchRunStatus();
        runStatus2.setmPM25Value(121);
        airTouchSeriesDevice2.setAirtouchDeviceRunStatus(runStatus2);
        mUserLocationData1.addHomeDeviceItemToList(airTouchSeriesDevice2);


        DeviceInfo deviceInfo3 = new DeviceInfo();
        deviceInfo3.setDeviceID(3333);
        deviceInfo3.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice3 = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_S_TYPE);
        airTouchSeriesDevice3.setDeviceInfo(deviceInfo3);
        AirtouchRunStatus runStatus3 = new AirtouchRunStatus();
        runStatus3.setmPM25Value(110);
        airTouchSeriesDevice3.setAirtouchDeviceRunStatus(runStatus3);
        mUserLocationData1.addHomeDeviceItemToList(airTouchSeriesDevice3);


        mUserLocationData2 = new RealUserLocationData();
        mUserLocationData2.setLocationID(222);
        mUserLocationData2.setCity("CHSH00001");
        mUserLocationData2.setName("TianJing");


        mUserLocationData3 = new RealUserLocationData();
        mUserLocationData3.setLocationID(333);
        mUserLocationData3.setCity("CHSH00003");
        mUserLocationData3.setName("Nanjing");

    }


    @Test
    public void testShareInstance(){
        Assert.assertNotEquals(null, UserAllDataContainer.shareInstance());
    }


    @Test
    public void testSetUserLocationDataList(){
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        userLocationDataList.add(mUserLocationData1);
        userLocationDataList.add(mUserLocationData2);
        userLocationDataList.add(mUserLocationData3);

        //test add
        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);
        Assert.assertEquals(3, UserAllDataContainer.shareInstance().getUserLocationDataList().size());
    }


    @Test
    public void testGetUserLocationDataList(){
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        userLocationDataList.add(mUserLocationData1);
        userLocationDataList.add(mUserLocationData2);
        userLocationDataList.add(mUserLocationData3);

        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);

        Assert.assertEquals(mUserLocationData1, UserAllDataContainer.shareInstance().getUserLocationDataList().get(0));
    }


    @Test
    public void testUpdateUsrdataList(){
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        userLocationDataList.add(mUserLocationData1);
        userLocationDataList.add(mUserLocationData2);
        userLocationDataList.add(mUserLocationData3);
        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);
        ArrayList<UserLocationData> tempList = new ArrayList<>();
        tempList.add(mUserLocationData1);
         LocationAndDeviceParseManager.getInstance().updateUsrdataList(tempList);
         Assert.assertEquals(1, UserAllDataContainer.shareInstance().getUserLocationDataList().size());
         Assert.assertEquals(mUserLocationData1.getLocationID(), UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getLocationID());


        tempList.clear();
        tempList.add(mUserLocationData1);
        tempList.add(mUserLocationData2);
        tempList.add(mUserLocationData3);
        mUserLocationData2.setIsLocationOwner(false);
        LocationAndDeviceParseManager.getInstance().updateUsrdataList(tempList);
        Assert.assertEquals(3, UserAllDataContainer.shareInstance().getUserLocationDataList().size());
        Assert.assertEquals(mUserLocationData1.getLocationID(), UserAllDataContainer.shareInstance().getUserLocationDataList().get(0).getLocationID());
        Assert.assertEquals(mUserLocationData3.getLocationID(), UserAllDataContainer.shareInstance().getUserLocationDataList().get(1).getLocationID());
        Assert.assertEquals(mUserLocationData2.getLocationID(), UserAllDataContainer.shareInstance().getUserLocationDataList().get(2).getLocationID());

    }

    @Test
    public void testAddLocationDataFromGetLocationAPI(){

        UserLocation userLocation = new UserLocation();
        userLocation.setLocationID(111);
        userLocation.setCity("CHSH00000");
        userLocation.setName("Shanghai");

        ArrayList<UserLocationData> tempList = new ArrayList<>();
        LocationAndDeviceParseManager.getInstance().addLocationDataFromGetLocationAPI(userLocation, tempList);
        Assert.assertEquals(0, tempList.size());

        userLocation.setIsLocationOwner(false);
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceID(1111);
        deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);
        AirTouchDeviceObject airTouchSeriesDevice = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_S_TYPE);
        airTouchSeriesDevice.setDeviceInfo(deviceInfo);
        deviceInfo.setOwnerName("test");
        ArrayList<DeviceInfo> deviceInfoArrayList = new ArrayList<>();
        deviceInfoArrayList.add(deviceInfo);
        userLocation.setDeviceInfo(deviceInfoArrayList);

        LocationAndDeviceParseManager.getInstance().addLocationDataFromGetLocationAPI(userLocation, tempList);
        Assert.assertEquals(1, tempList.size());
        Assert.assertEquals("test", tempList.get(0).getLocationOwnerName());
        Assert.assertEquals(111, tempList.get(0).getLocationID());

    }

    @Test
    public void testGetUserLocationByID() {
        CopyOnWriteArrayList<UserLocationData> mList = new CopyOnWriteArrayList<>();
        mList.add(mUserLocationData1);
        mList.add(mUserLocationData2);
        UserAllDataContainer.shareInstance().setUserLocationDataList(mList);

        Assert.assertEquals("CHSH00000", UserDataOperator.getUserLocationByID(111,UserAllDataContainer.shareInstance().getUserLocationDataList()).getCity());

        Assert.assertEquals(null, UserDataOperator.getLocationWithId(555,UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()));
    }

    @Test
    public void testGetLocationWithId() {
        CopyOnWriteArrayList<UserLocationData> mList = new CopyOnWriteArrayList<>();
        mList.add(mUserLocationData1);
        mList.add(mUserLocationData2);
        UserAllDataContainer.shareInstance().setUserLocationDataList(mList);

        Assert.assertEquals("CHSH00000", UserDataOperator.getLocationWithId(111,
                UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).getCity());

        Assert.assertEquals(null, UserDataOperator.getLocationWithId(555,
                UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()));
    }

    @Test
    public void testGetWeatherRefreshManager(){
        Assert.assertNotEquals(null, UserAllDataContainer.shareInstance().getWeatherRefreshManager());

    }

    @Test
    public void testGetCurrentDeviceId(){
//        UserAllDataContainer.getInstance().setCurrentDeviceId(111);
//        Assert.assertEquals(111, UserAllDataContainer.getInstance().getCurrentDeviceId());
    }

    @Test
    public void testSetCurrentDeviceId(){
//        UserAllDataContainer.getInstance().setCurrentDeviceId(222);
//        Assert.assertEquals(222, UserAllDataContainer.getInstance().getCurrentDeviceId());
    }

    @Test
    public void testGetGpsUserLocation(){
    }

    @Test
    public void testGetNeedOpenDevice(){

        AirQualityIndex airQualityIndex = new AirQualityIndex();
        airQualityIndex.setPm25("111");
        AirQuality airQuality = new AirQuality();
        airQuality.setAirQualityIndex(airQualityIndex);
        Now now = new Now();
        now.setAirQuality(airQuality);
        Weather weather = new Weather();
        weather.setNow(now);
        WeatherPageData cityWeather = new WeatherPageData();
        cityWeather.setWeather(weather);
        mUserLocationData1.setCityWeatherData(cityWeather);

        CopyOnWriteArrayList<UserLocationData> mList = new CopyOnWriteArrayList<>();
        mList.add(mUserLocationData1);
        mList.add(mUserLocationData2);
        UserAllDataContainer.shareInstance().setUserLocationDataList(mList);

//        Assert.assertEquals(0, UserAllDataContainer.shareInstance().getNeedOpenDevice().size());

    }

    @Test
    public void testGetDeviceWithDeviceId(){
        CopyOnWriteArrayList<UserLocationData> userLocationDataList = new CopyOnWriteArrayList<>();
        userLocationDataList.add(mUserLocationData1);
        userLocationDataList.add(mUserLocationData2);
        userLocationDataList.add(mUserLocationData3);
        UserAllDataContainer.shareInstance().setUserLocationDataList(userLocationDataList);

        Assert.assertEquals(1111, UserDataOperator.getDeviceWithDeviceId(1111,
                UserAllDataContainer.shareInstance().getUserLocationDataList(),UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList()).getDeviceInfo().getDeviceID());
    }


    @Test
    public void testGetAllDeviceNumber(){
        initUserLocationDataList(5);
        Assert.assertEquals(5, UserAllDataContainer.shareInstance().getAllDeviceNumber());
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


    private UserLocationData initUserLocationData(int deviceSize){
        UserLocationData userLocationData = new RealUserLocationData();
        for (int i = 0; i < deviceSize; i++){

            AirTouchDeviceObject homeDevice = new AirTouchDeviceObject(HPlusConstants.AIRTOUCH_S_TYPE);
            DeviceInfo deviceInfo = new DeviceInfo();

            deviceInfo.setDeviceType(HPlusConstants.AIRTOUCH_S_TYPE);

            homeDevice.setDeviceInfo(deviceInfo);
            userLocationData.addHomeDeviceItemToList(homeDevice);
        }
        return userLocationData;

    }

}
