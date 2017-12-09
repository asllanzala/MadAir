package com.honeywell.hch.airtouch.ui.homemanage.manager;

import android.app.Application;
import android.os.Bundle;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.MockWebService;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.CategoryHomeCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeAndCity;
import com.honeywell.hch.airtouch.plateform.http.manager.model.RealUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Vincent on 19/12/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class HomeUiManagerTest {
    private HomeManagerManager mHomeManager;
    private HomeManagerUiManager mHomeUiManager;
    private MockWebService webService;

    protected Application application = RuntimeEnvironment.application;
    private RequestID mRequestId;
    private boolean mResult;
    private int mResponseCode;

    private ResponseResult mResponseResult;
    private Bundle mBundle;
    private SwapLocationNameTest mSwapLocationNameTest;
    private ProcessRemoveHomeTest mProcessRemoveHomeTest;
    private ProcessAddHomeTest mProcessAddHomeTest;
    private GetLocationTest mGetLocationTest;
    private CityChinaDBService mCityChinaDBService;
    private CityIndiaDBService mCityIndiaDBService;

    @Before
    public void setUp() throws Exception {
//        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);
        LibApplication.setApplication(application);
        mHomeManager = new HomeManagerManager();
        mHomeUiManager = new HomeManagerUiManager();
        webService = new MockWebService();
        HttpProxy.getInstance().setWebService(webService);
        AppConfig.isTestMode = true;

        mSwapLocationNameTest = new SwapLocationNameTest(mHomeUiManager, webService, mHomeManager);
        mProcessRemoveHomeTest = new ProcessRemoveHomeTest(mHomeUiManager, webService, mHomeManager);
        mProcessAddHomeTest = new ProcessAddHomeTest(mHomeUiManager, webService, mHomeManager);
        mGetLocationTest = new GetLocationTest(mHomeUiManager, webService, mHomeManager);
        mCityChinaDBService = new CityChinaDBService(application);
        mCityIndiaDBService = new CityIndiaDBService(application);
    }

    @Test
    public void testSwapLocationName() throws Exception {
        mSwapLocationNameTest.testSuccessSwapLocationName();
        mSwapLocationNameTest.testFailSwapLocationName();
        mSwapLocationNameTest.testLoginFailWhenSwapLocationName();
    }

    @Test
    public void testProcessRemoveHome() throws Exception {
        mProcessRemoveHomeTest.testSuccessProcessRemoveHome();
        mProcessRemoveHomeTest.testFailProcessRemoveHome();
        mProcessRemoveHomeTest.testLoginFailWhenProcessRemoveHome();
    }

    @Test
    public void testProcessAddHome() throws Exception {
        mProcessAddHomeTest.testSuccessProcessAddHome();
        mProcessAddHomeTest.testFailProcessAddHome();
        mProcessAddHomeTest.testLoginFailWhenProcessAddHome();
    }

    @Test
    public void testGetLocation() throws Exception {
        mGetLocationTest.testSuccessGetLocation();
        mGetLocationTest.testFailGetLocation();
        mGetLocationTest.testLoginFailWhenGetLocation();
    }

    @Test
    public void testPickUpItem() throws Exception {
        HomeAndCity homeAndCity = new HomeAndCity("myHome", 123, "Shanghai", true);
        List<HomeAndCity> homeAndCityList = new ArrayList<>();
        homeAndCityList.add(homeAndCity);
        CategoryHomeCity categoryHomeCity = new CategoryHomeCity();
        categoryHomeCity.setmHomeAndCityList(homeAndCityList);
        List<CategoryHomeCity> categoryHomeCityList = new ArrayList<>();
        categoryHomeCityList.add(categoryHomeCity);

        mHomeUiManager.pickUpItem(categoryHomeCityList);
        Assert.assertEquals(false, categoryHomeCityList.get(0).getmHomeAndCityList().get(0).isExpand());
    }

    @Test
    public void testIsSameName() throws Exception {
        UserLocationData userLocationData = new RealUserLocationData();
        CopyOnWriteArrayList<UserLocationData> mUserLocationDataList = new CopyOnWriteArrayList<>();
        UserAllDataContainer.shareInstance().setUserLocationDataList(mUserLocationDataList);
        userLocationData.setName("Vincent");
        userLocationData.setIsLocationOwner(true);
        userLocationData.setCity("Shanghai");
        mUserLocationDataList.add(userLocationData);

        Assert.assertEquals(true, mHomeUiManager.isSameName("Shanghai", "Vincent"));
        Assert.assertEquals(false, mHomeUiManager.isSameName("Shanghai", "MyHome"));
    }

    @Test
    public void testGetCityArrays() throws Exception {
        City city = new City();
        ArrayList<City> cityList = new ArrayList<>();
        cityList.add(city);

        Assert.assertEquals(1, mHomeUiManager.getCityArrays(cityList).length);
    }

    @Test
    public void testIsCityCanFound() throws Exception {
        UserInfoSharePreference.saveCountryCode("86");
        Assert.assertEquals(false, mHomeUiManager.isCityCanFound("Shanghai", mCityChinaDBService, mCityIndiaDBService));
    }


}
