package com.honeywell.hch.airtouch.plateform.storage;

import android.content.Context;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by h127856 on 6/23/16.
 * <p/>
 * 如果是登录成功的，该sharePreference登录账号相关的有值，否则无值
 */
public class UserInfoSharePreference {

    private final static String DEFAULT_STRING_VALUE = "";
    public final static int DEFAULT_INT_VALUE = -1;
    private final static long DEFAULT_LONG_VALUE = -1;

    /**
     * profile相关缓存
     */
    private final static String USER_INFO_SHAREPREFERENCE = "user_info_sp";
    private final static String USER_INFO_SHAREPREFERENCE_DEFAULT = "user_info_sp_default"; //default 专门用一个file
    private final static String USER_NICK_NAME_KEY = "nick_name";
    private final static String USER_PHONE_NUMBER_KEY = "phone_number";
    private final static String USER_PASSWORD_KEY = "user_password";
    private final static String USER_USER_ID_KEY = "user_id";

    private final static String USER_SESSION_ID_KEY = "session_id";
    private final static String USER_TOKEN_ID_KEY = "token_id";
    private final static String USER_TYPE_KEY = "user_type";
    private final static String GPS_CITY_CODE_KEY = "gps_city_code";
    private final static String LAST_UPDATE_SESSION = "last_update_session";
    private final static String USER_DEFAULT_HOME_ID = "user_default_home_id";
    private final static String MANUAL_CITY_CODE_KEY = "manual_city_code";
    private final static String IS_GPS_CODE_KEY = "is_gps_city_code";
    private final static String EMOTION_AIR_KEY = "emotion_air_key";
    private final static String EMOTION_WATER_KEY = "emotion_water_key";
    private final static String EMOTION_AIR_TOTAL_KEY = "emotion_air_total_key";
    private final static String EMOTION_WATER_TOTAL_KEY = "emotion_water_total_key";
    //
    private final static String USER_SAULT_KEY = "sault_id";
    private final static String USER_BYCRYPT_PD_KEY = "bycrypt_pd_id";
    /**
     * 登录成功之后的国家码
     */
    private final static String USER_COUNTRY_CODE_KEY = "country_code";

    /**
     * 定位到的国家码
     */
    private final static String GPS_COUNTRY_CODE_KEY = "gps_country_code";

    /**
     * dashbaord tab相关数据缓存
     */
    private final static String USER_LOCATION_CACH_DATA_KEY = "user_location_data_key";
    private final static String USER_DEVICE_RUNSTATUS_CACH_DATA_KEY = "user_device_runstatus_data_key";
    private final static String DASH_BOARD_LAST_UPATETIME_DATA_KEY = "dashboard_last_updatetime_data_key";

    /**
     * Devices tab相关数据缓存
     */
    private final static String GROUP_CACH_DATA_KEY = "group_cach_data_key";
    private final static String GROUP_UPDATETIME_DATA_KEY = "group_update_time_data_key";

    /**
     * message tab相关数据缓存
     */
    private final static String MESSAGE_CACH_DATA_KEY = "message_cach_data_key";
    private final static String MESSAGE_UPDATE_TIME_DATA_KEY = "message_update_time_data_key";

    private final static String DEVICE_CONFIG_CACHE_KEY = "device_config_cache_key";

    private static String mSession="";

    public static void saveUserInfoInSp(String nickname, String mobilePhone, String password,
                                        String userId, String countryCode) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_NICK_NAME_KEY, nickname);
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_PHONE_NUMBER_KEY, mobilePhone);
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_PASSWORD_KEY, StringUtil.encodeStringWithTripleDES(password));
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_USER_ID_KEY, userId);
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_COUNTRY_CODE_KEY, countryCode);
    }

    public static void saveLoginSession(String sessionId) {
//        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_SESSION_ID_KEY, sessionId);
        mSession = sessionId;
    }

    public static void saveUserTokenFromBaiduPush(String token) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_TOKEN_ID_KEY, token);
    }

    public static void saveUserType(int userType) {
        SharePreferenceUtil.setPrefInt(USER_INFO_SHAREPREFERENCE, USER_TYPE_KEY, userType);
    }

    public static void savePassword(String newPassword) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_PASSWORD_KEY, StringUtil.encodeStringWithTripleDES(newPassword));
    }

    public static void saveGpsCityCode(String gpsCityCode) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, GPS_CITY_CODE_KEY, gpsCityCode);
    }

    public static void saveGpsCountryCode(String gpsCountryCode) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, GPS_COUNTRY_CODE_KEY, gpsCountryCode);
    }

    public static void saveLastUpdateSession(long lastSession) {
        SharePreferenceUtil.setPrefLong(USER_INFO_SHAREPREFERENCE, LAST_UPDATE_SESSION, lastSession);
    }

    public static void saveCountryCode(String countryCode) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_COUNTRY_CODE_KEY, countryCode);
    }

    public static void saveDefaultHomeId(int id) {
        SharePreferenceUtil.setPrefInt(USER_INFO_SHAREPREFERENCE_DEFAULT,
                getUserId() + USER_DEFAULT_HOME_ID, id);
    }

    public static int getDefaultHomeId() {
        return SharePreferenceUtil.getPrefInt(USER_INFO_SHAREPREFERENCE_DEFAULT, getUserId() + USER_DEFAULT_HOME_ID, DEFAULT_INT_VALUE);
    }

    public static long getLastUpdateSession() {
        return SharePreferenceUtil.getPrefLong(USER_INFO_SHAREPREFERENCE, LAST_UPDATE_SESSION, DEFAULT_LONG_VALUE);
    }

    public static String getNickName() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_NICK_NAME_KEY, DEFAULT_STRING_VALUE);
    }


    public static String getMobilePhone() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_PHONE_NUMBER_KEY, DEFAULT_STRING_VALUE);
    }

    public static String getPassword() {
        String pass = SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_PASSWORD_KEY, DEFAULT_STRING_VALUE);
        return StringUtil.decryptStringWithTripleDES(pass);
    }

    public static String getUserId() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_USER_ID_KEY, DEFAULT_STRING_VALUE);
    }

    public static String getCountryCode() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_COUNTRY_CODE_KEY, DEFAULT_STRING_VALUE);
    }

    public static String getSessionId() {
        return mSession;
//        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_SESSION_ID_KEY, DEFAULT_STRING_VALUE);
    }

    public static String getTokenFromBaiduPush() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_TOKEN_ID_KEY, DEFAULT_STRING_VALUE);
    }

    public static int getUserType() {
        return SharePreferenceUtil.getPrefInt(USER_INFO_SHAREPREFERENCE, USER_TYPE_KEY, DEFAULT_INT_VALUE);
    }

    public static String getGpsCityCode() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, GPS_CITY_CODE_KEY, AppConfig.LOCATION_FAIL);
    }


    public static String getGpsCountryCode() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, GPS_COUNTRY_CODE_KEY, HPlusConstants.CHINA_CODE);
    }


    public static void clearUserSharePreferencce(Context context) {
        SharePreferenceUtil.clearPreference(context, SharePreferenceUtil.getSharedPreferenceInstanceByName(USER_INFO_SHAREPREFERENCE));
    }

    /**
     * 通过判断这个sharePreference的是否有账号相关的值来判断用户是否登录
     */
    public static boolean isUserAccountHasData() {
        if (!StringUtil.isEmpty(getMobilePhone())) {
            return true;
        }
        return false;
    }


    public static void saveLocationCachesData(String locationData) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_LOCATION_CACH_DATA_KEY, locationData);
    }

    public static String getLocationCachesData() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_LOCATION_CACH_DATA_KEY, DEFAULT_STRING_VALUE);
    }


    public static void saveDeviceRunstatusCachesData(String runstatusData) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_DEVICE_RUNSTATUS_CACH_DATA_KEY, runstatusData);
        SharePreferenceUtil.setPrefLong(USER_INFO_SHAREPREFERENCE, DASH_BOARD_LAST_UPATETIME_DATA_KEY, System.currentTimeMillis());
    }

    public static String getDeviceRunstatusCachesData() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_DEVICE_RUNSTATUS_CACH_DATA_KEY, DEFAULT_STRING_VALUE);
    }

    public static long getDashBoardLastUpatetimeData() {
        return SharePreferenceUtil.getPrefLong(USER_INFO_SHAREPREFERENCE, DASH_BOARD_LAST_UPATETIME_DATA_KEY, DEFAULT_LONG_VALUE);
    }

    public static long getMessageLastUpatetimeData() {
        return SharePreferenceUtil.getPrefLong(USER_INFO_SHAREPREFERENCE, MESSAGE_UPDATE_TIME_DATA_KEY, DEFAULT_LONG_VALUE);
    }

    public static void saveGroupCachesData(String groupData, int locationId) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, GROUP_CACH_DATA_KEY + String.valueOf(locationId), groupData);
//        SharePreferenceUtil.setPrefLong(USER_INFO_SHAREPREFERENCE, GROUP_UPDATETIME_DATA_KEY + String.valueOf(locationId), System.currentTimeMillis());
    }

//    public static void saveGroupCacheUpdateTime(int locationId,long time){
//        SharePreferenceUtil.setPrefLong(USER_INFO_SHAREPREFERENCE, GROUP_UPDATETIME_DATA_KEY + String.valueOf(locationId), time);
//    }

    public static String getGroupCachesData(int locationId) {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, GROUP_CACH_DATA_KEY + String.valueOf(locationId), DEFAULT_STRING_VALUE);
    }

    public static long getGroupUpdateTimeData(int locationId) {
        return SharePreferenceUtil.getPrefLong(USER_INFO_SHAREPREFERENCE, GROUP_UPDATETIME_DATA_KEY + String.valueOf(locationId), DEFAULT_LONG_VALUE);
    }


    public static void saveMessageCachesData(String message) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, MESSAGE_CACH_DATA_KEY, message);
        SharePreferenceUtil.setPrefLong(USER_INFO_SHAREPREFERENCE, MESSAGE_UPDATE_TIME_DATA_KEY, System.currentTimeMillis());
    }

    public static String getMessageCachesData() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, MESSAGE_CACH_DATA_KEY, DEFAULT_STRING_VALUE);
    }


    public static long getMessageUpdateTimeData() {
        return SharePreferenceUtil.getPrefLong(USER_INFO_SHAREPREFERENCE, MESSAGE_UPDATE_TIME_DATA_KEY, DEFAULT_LONG_VALUE);
    }

    public static void saveDeviceConfigCachesData(String deviceConfig) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, DEVICE_CONFIG_CACHE_KEY, deviceConfig);
    }

    public static String getDeviceConfigCacheData() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, DEVICE_CONFIG_CACHE_KEY, DEFAULT_STRING_VALUE);

    }

    //手动选择城市gps code
    public static void saveManualCityCode(String gpsCityCode) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, MANUAL_CITY_CODE_KEY, gpsCityCode);
    }

    public static String getManualCityCode() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, MANUAL_CITY_CODE_KEY, "");
    }

    //使用手动选择城市还是gps定位的城市
    public static void saveIsUsingGpsCityCode(boolean isUsingGpsCityCode) {
        SharePreferenceUtil.setPrefBoolean(USER_INFO_SHAREPREFERENCE, IS_GPS_CODE_KEY, isUsingGpsCityCode);
    }

    public static boolean getIsUsingGpsCityCode() {
        return SharePreferenceUtil.getPrefBoolean(USER_INFO_SHAREPREFERENCE, IS_GPS_CODE_KEY, true);
    }

    public static void saveEmotionAirCachesData(String locationId, String emotionAirData) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, EMOTION_AIR_KEY + locationId, emotionAirData);
    }

    public static void saveEmotionWaterCachesData(String locationId, String emotionWaterData) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, EMOTION_WATER_KEY + locationId, emotionWaterData);
    }

    public static String getEmotionAirCachesData(String locationId) {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, EMOTION_AIR_KEY + locationId, "");
    }

    public static String getEmotionWaterCachesData(String locationId) {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, EMOTION_WATER_KEY + locationId, "");
    }

    public static void saveEmotionAirCachesTotalData(String locationId, float airTotal) {
        SharePreferenceUtil.setPrefFloat(USER_INFO_SHAREPREFERENCE, EMOTION_AIR_TOTAL_KEY + locationId, airTotal);
    }

    public static void saveEmotionWaterCachesTotalData(String locationId, float waterTotal) {
        SharePreferenceUtil.setPrefFloat(USER_INFO_SHAREPREFERENCE, EMOTION_WATER_TOTAL_KEY + locationId, waterTotal);
    }

    public static float getEmotionAirCachesTotalData(String locationId) {
        return SharePreferenceUtil.getPrefFloat(USER_INFO_SHAREPREFERENCE, EMOTION_AIR_TOTAL_KEY + locationId, 0.00f);
    }

    public static float getEmotionWaterCachesTotalData(String locationId) {
        return SharePreferenceUtil.getPrefFloat(USER_INFO_SHAREPREFERENCE, EMOTION_WATER_TOTAL_KEY + locationId, 0.00f);
    }

    public static void saveUserSault(String sault) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_SAULT_KEY, sault);
    }

    public static String getUserSault() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_SAULT_KEY, "");
    }

    public static void saveBycryptPd(String bycryptPd) {
        SharePreferenceUtil.setPrefString(USER_INFO_SHAREPREFERENCE, USER_BYCRYPT_PD_KEY, bycryptPd);
    }

    public static String getBycryptPd() {
        return SharePreferenceUtil.getPrefString(USER_INFO_SHAREPREFERENCE, USER_BYCRYPT_PD_KEY, "");
    }

}
