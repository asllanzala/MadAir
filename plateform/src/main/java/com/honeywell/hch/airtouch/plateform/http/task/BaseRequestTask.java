package com.honeywell.hch.airtouch.plateform.http.task;

import android.os.AsyncTask;

import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.BCryptUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.water.model.UnSupportDeviceObject;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.LocationIdListRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginEnterpriseRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLoginResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserValidateResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.List;

/**
 * Created by wuyuan on 15/5/19.
 */
public class BaseRequestTask extends AsyncTask<Object, Object, ResponseResult> {

    private Long timeDeviation = 0L;

        private static final int SESSION_TIMEOUT = 15 * 60;
//    private static final int SESSION_TIMEOUT = 30;

    protected static long mLastestUpdateSession = 0;

    protected final String SAULT_PREFIX = "$2a$12$";

    public BaseRequestTask() {
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {
        return null;
    }

    @Override
    protected void onPostExecute(ResponseResult result) {
        super.onPostExecute(result);
    }

    public ResponseResult reloginSuccessOrNot(RequestID requestId) {
        ResponseResult result = new ResponseResult(false, requestId);
        if (UserInfoSharePreference.isUserAccountHasData()) {
            timeDeviation = System.currentTimeMillis()
                    - UserInfoSharePreference.getLastUpdateSession();
            timeDeviation /= 1000;
            if (timeDeviation > SESSION_TIMEOUT || timeDeviation <= 0|| StringUtil.isEmpty(UserInfoSharePreference.getSessionId())) {
                String sault = UserInfoSharePreference.getUserSault();
                String brcryptPwd = UserInfoSharePreference.getBycryptPd();
                //TODO: 本地有sault值，就用brcypt密码登录，没有就走login流程
                if (!"".equals(sault)) {
                    if (AppManager.isEnterPriseVersion()) {
                        UserLoginEnterpriseRequest userLoginRequest = new UserLoginEnterpriseRequest(UserInfoSharePreference.getMobilePhone(),
                                brcryptPwd, HPlusConstants.ENTERPRISE_TYPE, AppConfig.APPLICATION_ID);
                        userLoginRequest.setBCrypt(true);
                        return HttpProxy.getInstance().getWebService().refreshSession(userLoginRequest, requestId);
                    } else {
                        UserLoginRequest userLoginRequest = new UserLoginRequest(UserInfoSharePreference.getMobilePhone(),
                                brcryptPwd, AppConfig.APPLICATION_ID);
                        userLoginRequest.setBCrypt(true);
                        ResponseResult responseResult =HttpProxy.getInstance().getWebService().refreshSession(userLoginRequest, requestId);
                        return responseResult;
                    }
                } else {
                    return getLoginResult(UserInfoSharePreference.getMobilePhone(), UserInfoSharePreference.getPassword(), AppConfig.APPLICATION_ID);
                }

            } else {
                result.setResult(true);
                return result;
            }
        }
        return result;
    }

    /*
     * Combine AirtouchRunStatus into device and send broadcast to main page
     */
    public boolean reloadDeviceInfo() {

        List<UserLocationData> locations = UserAllDataContainer.shareInstance().getUserLocationDataList();
        int[] locationids = new int[locations.size()];
        for (int i = 0; i < locations.size(); i++) {
            locationids[i] = locations.get(i).getLocationID();
        }

        LocationIdListRequest locationIdListRequest = new LocationIdListRequest(locationids);
        ResponseResult runstatusData = HttpProxy.getInstance().getWebService().getAllDevicesRunstatus(
                UserInfoSharePreference.getSessionId(), locationIdListRequest, null);

        if (runstatusData.isResult()) {
            for (UserLocationData userLocationData : UserAllDataContainer.shareInstance().getUserLocationDataList()) {
                getWorstDeviceAsDefaultDevice(userLocationData);
            }

            return true;
        }
        return false;
    }

    //login api
    protected ResponseResult getLoginResult(String userName, String password, String applicationId) {

        UserLoginRequest userLoginRequest = new UserLoginRequest(userName, password, applicationId);
        ResponseResult userValidateResult = HttpProxy.getInstance().getWebService().userValidate(userLoginRequest, RequestID.USER_LOGIN);
        if (!userValidateResult.isResult()) {
            return userValidateResult;
        }
        //TODO:userValidate 调用成功
        UserValidateResponse userValidateResponse = (UserValidateResponse) userValidateResult.getResponseData().get(UserValidateResponse.USER_VALIDATE_DATA);
        String sault = SAULT_PREFIX + userValidateResponse.getmBcryptSalt();
        int isSuccess = userValidateResponse.getIsSuccess();
        String bcryptPassword = BCryptUtil.hashpw(password, sault);
        //TODO:新用户，有Bcrypt 密码 传bcrypt 密码，老用户传明码
        if (isSuccess == 1) {
            userLoginRequest.setPassword(bcryptPassword);
            userLoginRequest.setBCrypt(true);
        }
        ResponseResult loginResult = HttpProxy.getInstance().getWebService().userLogin(userLoginRequest, null);
        if (!loginResult.isResult()) {
            return loginResult;
        }
        //TODO: 登录成功，保存bcypt和sault
        UserInfoSharePreference.saveUserSault(sault);
        UserInfoSharePreference.saveBycryptPd(bcryptPassword);
        //-----------------------------------------------------------------------------------------------------------------------------
        UserInfoSharePreference.saveLastUpdateSession(System.currentTimeMillis());
        UserLoginResponse userLoginResult = (UserLoginResponse) loginResult.getResponseData().get(HPlusConstants.USER_LOGIN_INFO);

        String userId = userLoginResult.getUserInfo().getUserID();
        String sessionId = userLoginResult.getSessionId();

        //get location
        ResponseResult getLocationResult = HttpProxy.getInstance().getWebService()
                .getLocation(userId,
                        sessionId, null, null);

        reloadDeviceInfo();

        getLocationResult.setRequestId(RequestID.USER_LOGIN);
        if (getLocationResult.isResult()) {
            getLocationResult.setResponseData(loginResult.getResponseData());
        }
        getUnreadMessage();

        return getLocationResult;
    }


    /**
     * //     * get worse device from device list
     * //     * @return
     * //
     */
    private void getWorstDeviceAsDefaultDevice(UserLocationData userLocationData) {

        HomeDevice worstAirtouchDevice = getWorsePMDevice(userLocationData);
        if (worstAirtouchDevice != null) {
            userLocationData.setDefaultPMDevice((AirTouchDeviceObject) worstAirtouchDevice);
        }
        if (userLocationData.getAirTouchSeriesList().size() == 0) {
            userLocationData.setDefaultPMDevice(null);
        }

        HomeDevice worseTvocDevice = getWorseTVOCDevice(userLocationData);
        if (worseTvocDevice != null) {
            userLocationData.setDefaultTVOCDevice((AirTouchDeviceObject) worseTvocDevice);
        }
        if (userLocationData.getAirTouchSeriesList().size() == 0) {
            userLocationData.setDefaultTVOCDevice(null);
        }

        HomeDevice worseWaterDevice = getWorseWaterDevice(userLocationData);
        if (worseWaterDevice != null) {
            userLocationData.setDefaultWaterDevice((WaterDeviceObject) worseWaterDevice);
        }
        if (userLocationData.getWaterDeviceList().size() == 0) {
            userLocationData.setDefaultWaterDevice(null);
        }

        if ((worstAirtouchDevice == null && worseTvocDevice == null && worseWaterDevice == null) && userLocationData.getHomeDevicesList().size() > 0) {
            userLocationData.setUnSupportDevice(new UnSupportDeviceObject(userLocationData.getHomeDevicesList().get(0).getDeviceInfo()));
        }

    }


    private HomeDevice getWorsePMDevice(UserLocationData userLocationData) {
        int worstPM25 = HPlusConstants.DEVICE_INIT_VALUE;
        HomeDevice worstDevice = null;
        for (HomeDevice homeDevice : userLocationData.getAirTouchSeriesList()) {
            userLocationData.setUnSupportDevice(null);
            AirTouchDeviceObject airTouchSeriesDevice = ((AirTouchDeviceObject) homeDevice);
            if (airTouchSeriesDevice.getAirtouchDeviceRunStatus() != null) {
                int pmValue = airTouchSeriesDevice.getAirtouchDeviceRunStatus().getmPM25Value();
                if (pmValue == HPlusConstants.ERROR_MAX_VALUE) {
                    pmValue = -1;
                } else if (pmValue == HPlusConstants.ERROR_SENSOR || airTouchSeriesDevice.getiDeviceStatusFeature().isOffline()) {
                    pmValue = -2;
                }
                if (worstPM25 < pmValue) {
                    worstPM25 = pmValue;
                    worstDevice = airTouchSeriesDevice;
                }
            }
        }

        if (userLocationData.getAirTouchSeriesList().size() > 0 && worstDevice == null) {
            worstDevice = userLocationData.getAirTouchSeriesList().get(0);
        }
        return worstDevice;
    }

    private HomeDevice getWorseTVOCDevice(UserLocationData userLocationData) {
        int worstTVOCValue = HPlusConstants.DEVICE_INIT_VALUE;
        int worseTVOCLevel = HPlusConstants.DEVICE_INIT_VALUE;
        HomeDevice worstTVOCDevice = null;
        for (HomeDevice homeDevice : userLocationData.getAirTouchSeriesList()) {
            AirTouchDeviceObject airTouchSeriesDevice = ((AirTouchDeviceObject) homeDevice);
            userLocationData.setUnSupportDevice(null);

            if (airTouchSeriesDevice.canShowTvoc()) {
                if (worseTVOCLevel < airTouchSeriesDevice.getTvocFeature().getTvocLevel()
                        || (worseTVOCLevel == airTouchSeriesDevice.getTvocFeature().getTvocLevel()
                        && worstTVOCValue < (airTouchSeriesDevice.getTVOCOriginalNumber()))) {
                    worstTVOCValue = airTouchSeriesDevice.getTVOCOriginalNumber();
                    worseTVOCLevel = airTouchSeriesDevice.getTvocFeature().getTvocLevel();
                    worstTVOCDevice = homeDevice;
                }
            }

        }
        return worstTVOCDevice;
    }

    private HomeDevice getWorseWaterDevice(UserLocationData userLocationData) {
        int worseQuality = HPlusConstants.DEVICE_INIT_VALUE;
        HomeDevice worstDevice = null;
        //获取水的最差
        for (HomeDevice homeDevice : userLocationData.getWaterDeviceList()) {
            if (DeviceType.isWaterSeries(homeDevice.getDeviceType())) {
                userLocationData.setUnSupportDevice(null);

                WaterDeviceObject waterDeviceObject = ((WaterDeviceObject) homeDevice);
                if (waterDeviceObject.getAquaTouchRunstatus() != null) {
                    int waterQualityLevel = waterDeviceObject.getAquaTouchRunstatus().getWaterQualityLevel();
                    if (waterQualityLevel == DeviceConstant.INIT_STATUS) {
                        waterQualityLevel = -1;
                    } else if (waterQualityLevel == DeviceConstant.SENSOR_ERROR || waterDeviceObject.getiDeviceStatusFeature().isOffline()) {
                        waterQualityLevel = -2;
                    }
                    if (worseQuality < waterQualityLevel) {
                        worseQuality = waterQualityLevel;
                        worstDevice = waterDeviceObject;
                    }
                }
            }
        }

        if (worstDevice == null && userLocationData.getWaterDeviceList().size() > 0) {
            worstDevice = userLocationData.getWaterDeviceList().get(0);
        }
        return worstDevice;
    }


    public void getUnreadMessage() {
        HttpProxy.getInstance().getWebService()
                .getUnreadMessages(UserInfoSharePreference.getSessionId(), null, null);
    }
}
