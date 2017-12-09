package com.honeywell.hch.airtouch.plateform.location.manager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.location.model.CityInfo;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by wuyuan on 1/19/16.
 */
public class GpsUtil {

    public static final int FROM_START_ACTIVITY = 1;
    public static final int FROM_ENROLL_PROCESS = 2;
    public static final int FROM_SELECTED_CITY = 3;
    public static final String GPS_FROME_WHERE = "gps_from_where";

    private static final String TAG = "GpsUtil";

    private CityChinaDBService mCityChinaDBService = null;
    private CityIndiaDBService mCityIndiaDBService = null;

    private AppConfig mAppConfig;

    private static final int GPS_TIMEOUT = 30 * 1000;

    private City mSelectedGPSCity;

    public GpsUtil(CityChinaDBService cityChinaDBService, CityIndiaDBService cityIndiaDBService) {
        mCityChinaDBService = cityChinaDBService;
        mCityIndiaDBService = cityIndiaDBService;
        mAppConfig = AppConfig.shareInstance();
    }


    /**
     * Receive GPS location update
     */
//    private Handler mMessageHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg == null)
//                return;
//            switch (msg.what) {
//                //GPS find the city
//                case LocationManager.HANDLER_GPS_LOCATION:
//                    Bundle bundle = msg.getData();
//                    if (bundle != null) {
//                        CityInfo cityLocation = (CityInfo) bundle
//                                .getSerializable(LocationManager.HANDLER_MESSAGE_KEY_GPS_LOCATION);
//                        if (StringUtil.isEmpty(mAppConfig.getGpsCityCode()) || isNeedUpdateGpsInfo(cityLocation)) {
//                            processLocation(cityLocation);
//                        }
//                    }
//                    break;
//
//                default:
//                    break;
//            }
//        }
//    };

    /**
     * when the current location is located success and is different with the last .need to update
     *
     * @param cityLocation
     * @return
     */
    private boolean isNeedUpdateGpsInfo(CityInfo cityLocation) {
        if (cityLocation == null || cityLocation.getCity() == null) {
            return false;
        } else if (!StringUtil.isEmpty(cityLocation.getCity()) && !cityLocation.getCity().equals(mAppConfig.getGpsCityCode())) {
            return true;
        }

        return false;
    }

    public void initGps(final int whereFrom) {

        final Handler mMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg == null)
                    return;
                switch (msg.what) {
                    //GPS find the city
                    case LocationManager.HANDLER_GPS_LOCATION:
                        Bundle bundle = msg.getData();
                        if (bundle != null) {
                            CityInfo cityLocation = (CityInfo) bundle
                                    .getSerializable(LocationManager.HANDLER_MESSAGE_KEY_GPS_LOCATION);
                            if (StringUtil.isEmpty(mAppConfig.getGpsCityCode()) || isNeedUpdateGpsInfo(cityLocation)) {
                                processLocation(cityLocation, whereFrom);
                            }
                        }
                        break;

                    default:
                        break;
                }
            }
        };

        LocationManager.getInstance()
                .registerGPSLocationListener(mMessageHandler);

        //Start GPS timeout thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(GPS_TIMEOUT);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mSelectedGPSCity == null) {
                    LocationManager.getInstance().unRegisterGPSLocationListener(mMessageHandler);
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "GPS locating timeout");
                    processLocation(null, whereFrom);
                }
            }
        }).start();

    }

//    /**
//     * 20S GPS timeout
//     */
//    private class TimeoutCheckThread extends
//            AsyncTask<String, Integer, String> {
//
//        public TimeoutCheckThread() {
//
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            Date date1 = new Date();
////            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "GPS locating start：" + date1.toLocaleString());
//
//            try {
//                Thread.sleep(GPS_TIMEOUT);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Date date2 = new Date();
////            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "GPS locating end：" + date2.toLocaleString());
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (mSelectedGPSCity == null) {
//                LocationManager.getInstance().unRegisterGPSLocationListener(mMessageHandler);
//                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "GPS locating timeout");
//                processLocation(null);
//            }
//        }
//    }


    private void processLocation(CityInfo cityLocation, int whereFrom) {

        if (cityLocation != null) {
            City city = mCityChinaDBService.getCityByName(cityLocation.getCity());
            // India version
            if (city.getNameEn() != null) {
                UserInfoSharePreference.saveGpsCountryCode(HPlusConstants.CHINA_CODE);
            } else {
                city = mCityIndiaDBService.getCityByKey(cityLocation.getCity());
                if (city.getNameEn() != null) {
                    UserInfoSharePreference.saveGpsCountryCode(HPlusConstants.INDIA_CODE);
                } else {
                    UserInfoSharePreference.saveGpsCountryCode(HPlusConstants.CHINA_CODE);
                }
            }
            if (cityLocation.getCity() != null) {
                //gps success
                if (city.getNameEn() != null && city.getCode() != null) {
                    mSelectedGPSCity = city;
                    mAppConfig.setGpsCityCode(mSelectedGPSCity.getCode());
                } else {
                    // The located city is not in database.
                    mAppConfig.setGpsCityCode(cityLocation.getCity());
                }
            } else if (whereFrom != FROM_ENROLL_PROCESS) {
                mAppConfig.setGpsCityCode(AppConfig.LOCATION_FAIL);
            }
        } else if (whereFrom != FROM_ENROLL_PROCESS) {
            mAppConfig.setGpsCityCode(AppConfig.LOCATION_FAIL);
        }

        Intent intent = new Intent();
        intent.putExtra(GPS_FROME_WHERE, whereFrom);
        intent.setAction(HPlusConstants.GPS_RESULT);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }


}
