package com.honeywell.hch.airtouch.plateform.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.storage.SwitchSharePreference;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * configuration for app
 * Created by nan.liu on 1/15/15.
 */
public class AppConfig {

    public static final String UPDATE_VERSION_TIME = "updateVersionTime";

    private Context mContext;

    public static final String LOCATION_FAIL = "fail";

    /**
     * 个人账号和企业账号家的个数不限制，由于去除这个逻辑要改动较大，所以把当前的家都设置为5000个
     * 作为不受限制的处理逻辑。
     */
    public static final int ENTERPRISE_MAX_HOME_COUNT = 5000;

    public static final int PERSONAL_MAX_HOME_COUNT = 5000;

    /**
     * 1.如果所有家的设备总和超过了20，就停止alldevice界面的设备点点动画,并停止alldevice 界面接收自动刷新数据
     */
    public static final int DEVICE_NUMBER_THRESHOLD = 20;

    /**
     * 个人账号的家个数超过10个就取消所有的天气chart的显示和天气效果显示
     */
    public static final int WEATHER_CHART_EFFECT_NUMBER_THRESHOLD = 10;

    public static boolean isTestMode;
    public static boolean isDebugMode;
    public static boolean isChangeEnv = false;

    public static final int PRODUCT_ENV = 0;
    public static final int STAGE_ENV = 1;
    public static final int DEV_ENV = 2;
    public static final int QA_ENV = 3;
    public static int urlEnv;

    public static Boolean isLauchendFirstTime;
    public static Boolean isHouseTutorial;
    public static Boolean isControlTutorial;
    public static Boolean isFilterTutorial;
    public static Boolean isHomeTutorial;
    public static Boolean isWeatherTutorial;
    private static String language = null;
    private static String mGpsCityCode = "";
    private static String mLastGpsCityCode = null;

    public static Boolean canFilterScrollPage = false;
    private static boolean isHomePageCover = false;

    public static final String LANGUAGE_ZH = "zh";
    public static final String LANGUAGE_XINZHI = "zh-chs";
    public static final String LANGUAGE_EN = "en";
    public static final String APPLICATION_ID = "1237b42b-0ce7-4582-830c-34d930b1fd52";


    public static int mInitTime = 0;

    private static AppConfig appConfig = null;

    private boolean isFirstLogin = true;

    private boolean isDifferent = false;


    private CityChinaDBService cityChinaDBService = null;
    private CityIndiaDBService cityIndiaDBService = null;

    public AppConfig(Context context) {
        mContext = context;
    }

    public static final String URLVERSION = "4";

    public static AppConfig shareInstance() {
        if (appConfig == null) {
            appConfig = new AppConfig(AppManager.getInstance().getApplication());
        }
        return appConfig;
    }

    // load sharedPreference data to CurrentApp
    public void loadAppInfo() {
        isDebugMode = isDebugEnvironment();

        if (isDebugMode) {
            urlEnv = SwitchSharePreference.getDevelopEnv();
        } else {
            urlEnv = PRODUCT_ENV;
        }

        isLauchendFirstTime = true;
        isHouseTutorial = SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isHouseTutorial", false);
        isControlTutorial = SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isControlTutorial", false);
        isWeatherTutorial = SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isWeatherTutorial", false);
        isFilterTutorial = SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isFilterTutorial", false);
        isHomeTutorial = SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isHomeTutorial", false);
        mLastGpsCityCode = SharePreferenceUtil.getPrefString(HPlusConstants.PREFERENCE_USER_CONFIG,
                "gpsCityCode", "");
        mInitTime = SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_USER_CONFIG,
                UPDATE_VERSION_TIME, 0);

    }

    public void setUpdateVersionTime(int time) {
        mInitTime = time;
        SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_USER_CONFIG, UPDATE_VERSION_TIME, time);
    }

    public void setIsControlTutorial(Boolean isTutorial) {
        isControlTutorial = isTutorial;
        SharePreferenceUtil.setPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isControlTutorial", isTutorial);
    }

    public void setIsWeatherTutorial(Boolean isWeatherTutorial) {
        AppConfig.isWeatherTutorial = isWeatherTutorial;
        SharePreferenceUtil.setPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isWeatherTutorial", isWeatherTutorial);
    }


    public void setIsFilterTutorial(Boolean isTutorial) {
        isFilterTutorial = isTutorial;
        SharePreferenceUtil.setPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                "isFilterTutorial", isTutorial);
    }

    public String getLanguage() {
        Configuration config = mContext.getResources().getConfiguration();
        language = config.locale.getLanguage();
        return language;
    }

    public static String getLanguageXinzhi() {
        Configuration config = AppManager.getInstance().getApplication().getApplicationContext().getResources().getConfiguration();
        language = config.locale.getLanguage();
        String language_xinzhi = language;
        if (LANGUAGE_ZH.equals(language))
            language_xinzhi = LANGUAGE_XINZHI;
        return language_xinzhi;
    }

    public String getGpsCityCode() {
        return mGpsCityCode;
    }

    public void setGpsCityCode(String gpsCity) {
        if (!StringUtil.isEmpty(gpsCity) && !AppConfig.LOCATION_FAIL.equals(gpsCity)
                && !gpsCity.equals(mLastGpsCityCode)) {
            isDifferent = true;
            mLastGpsCityCode = gpsCity;
        } else {
            isDifferent = false;

        }
        mGpsCityCode = gpsCity;
        UserInfoSharePreference.saveGpsCityCode(gpsCity);
        SharePreferenceUtil.setPrefString(HPlusConstants.PREFERENCE_USER_CONFIG,
                "gpsCityCode", gpsCity);
    }


    public boolean isHomePageCover() {
        return isHomePageCover;
    }

    public void setHomePageCover(boolean isHomePageCover) {
        appConfig.isHomePageCover = isHomePageCover;
    }

    public String getLastGpsCityCode() {
        return mLastGpsCityCode;
    }

    public boolean isDifferent() {
        return isDifferent;
    }

    public void setIsDifferent(boolean isDifferent) {
        this.isDifferent = isDifferent;
    }

    public void resetLastGpsWithNowVaule() {
        mLastGpsCityCode = mGpsCityCode;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public boolean isIndiaAccount() {
        if (UserInfoSharePreference.getCountryCode().equals(HPlusConstants.INDIA_CODE)) {
            return true;
        }

        return false;
    }


    public City getCityFromDatabase(String cityCode) {

        City city = getCityChinaDBService().getCityByCode(cityCode);
        if (city.getNameEn() == null) {
            city = getCityIndiaDBService().getCityByCode(cityCode);
        }

        return city;
    }

    private String getDevEnvironmentFromManifest() {
        try {
            ApplicationInfo appInfo = AppManager.getInstance().getApplication().getPackageManager()
                    .getApplicationInfo(AppManager.getInstance().getApplication().getPackageName(),
                            PackageManager.GET_META_DATA);

            return appInfo.metaData.getString("DEV_ENVIRONMENT");

        } catch (Exception e) {
            return HPlusConstants.RELEASE_ENVIRONMENT;
        }
    }

    public boolean isDebugEnvironment() {
        if (HPlusConstants.RELEASE_ENVIRONMENT.equalsIgnoreCase(getDevEnvironmentFromManifest())) {
            return false;
        }
        return true;
    }

    public CityChinaDBService getCityChinaDBService() {
        if (cityChinaDBService == null) {
            cityChinaDBService = new CityChinaDBService(mContext);
        }
        return cityChinaDBService;
    }


    public CityIndiaDBService getCityIndiaDBService() {
        if (cityIndiaDBService == null) {
            cityIndiaDBService = new CityIndiaDBService(mContext);
        }
        return cityIndiaDBService;
    }

    public String getBasePurchaseUrl() {
        if (AppConfig.isDebugMode) {
            return HPlusConstants.BASE_QA_PURCHASE_URL;
        } else {
            return HPlusConstants.BASE_PURCHASE_URL;
        }
    }

    public String getDevicePurchaseUrl(String version,String model,String product){
        return getBasePurchaseUrl() + mContext.getString(R.string.purchase_url_suffix,version,model,product);
    }

    public String getIntroductionURL() {
        if (AppConfig.isDebugMode) {
            return HPlusConstants.WEB_URL_INTRODUCT_QA;
        } else {
            return HPlusConstants.WEB_URL_INTRODUCT;
        }
    }

    public String getUserManualUrl() {
        if (AppConfig.isDebugMode) {
            return HPlusConstants.WEB_URL_USERMANUAL_QA;
        } else {
            return HPlusConstants.WEB_URL_USERMANUAL;
        }
    }

    public String getEULAUrl() {
        if (AppConfig.isDebugMode) {
            return HPlusConstants.WEB_URL_EULA_QA;
        } else {
            return HPlusConstants.WEB_URL_EULA;
        }
    }

}
