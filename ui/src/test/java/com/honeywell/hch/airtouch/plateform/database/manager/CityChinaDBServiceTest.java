package com.honeywell.hch.airtouch.plateform.database.manager;

import android.app.Application;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.database.model.City;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuyuan on 4/18/16.
 */
@RunWith(RobolectricTestRunner.class)
public class CityChinaDBServiceTest {

    protected Application application = Mockito.mock(Application.class, Mockito.RETURNS_DEEP_STUBS);

    protected CityChinaDBService mDbService;

    @Before
    public void setUp() throws Exception {

        Mockito.when(application.getApplicationContext()).thenReturn(application);
        AppManager.getInstance().setHPlusApplication(application);
        LibraryInitUtil.getInstance().initApplication(application);

        mDbService = new CityChinaDBService(application);
    }

    @Test
    public void testInsertOrUpdate(){
        List<City> cityList = new ArrayList<>();
        City city = new City();
        city.setCode("cn0010023");
        city.setNameZh("南京");
        city.setNameZh("nanjing");
        city.setCurrent(0);
        cityList.add(city);

        mDbService.insertAllCity(cityList);

//        City dbCity = mDbService.getCityByName("南京");
//        Assert.assertEquals("cn0010023", dbCity.getCode());

    }

}
