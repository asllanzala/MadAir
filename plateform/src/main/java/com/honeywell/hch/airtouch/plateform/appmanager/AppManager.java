package com.honeywell.hch.airtouch.plateform.appmanager;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.honeywell.hch.airtouch.plateform.appmanager.enterprise.ChinaEnterpriseAccountProtocol;
import com.honeywell.hch.airtouch.plateform.appmanager.personal.ChinaPersonalAccountProtocol;
import com.honeywell.hch.airtouch.plateform.appmanager.personal.IndiaPersonalAccountProtocol;
import com.honeywell.hch.airtouch.plateform.appmanager.protocol.LocalizationProtocol;
import com.honeywell.hch.airtouch.plateform.appmanager.protocol.UpdateProtocol;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.config.LibraryInitUtil;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;


/**
 * Initial mHPlusApplication
 */
public class AppManager {

    private static String INDIA_COUNTRY_CODE = "IN";

    private static String TAG = AppManager.class.getSimpleName();

    private static AppManager mAppManager;

    private Application mHPlusApplication = null;

    private static boolean isEnterPriseVersion = false;

    public Application getApplication() {
        return mHPlusApplication;
    }

    public void setHPlusApplication(Application application) {
        mHPlusApplication = application;
    }

    private static boolean mIsYingYongBao = true;

    /*
        qa env download url
     */
    private final static String WHATS_NEW_YYB_QA = "https://acscloud.honeywell.com.cn/hplus/qa/whats_new_qa/whats_new_yyb.json";
    private final static String WHATS_NEW_GOOGLEPLAY_QA = "https://acscloud.honeywell.com.cn/hplus/qa/whats_new_qa/whats_new_googleplay.json";

    /*
        env download url
     */
    private final static String WHATS_NEW_YYB = "https://acscloud.honeywell.com.cn/hplus/whats_new/whats_new_yyb.json";
    private final static String WHATS_NEW_GOOGLEPLAY = "https://acscloud.honeywell.com.cn/hplus/whats_new/whats_new_googleplay.json";

    private static String mAndroidVersionUrlPath = WHATS_NEW_YYB;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (mAppManager == null) {
            mAppManager = new AppManager();
        }
        return mAppManager;
    }

    public static void setIsEnterPriseVersion(boolean isEnterPriseVersion) {
        AppManager.isEnterPriseVersion = isEnterPriseVersion;
    }


    public static boolean isEnterPriseVersion() {
        isEnterPriseVersion = UserInfoSharePreference.getUserType() == HPlusConstants.ENTERPRISE_ACCOUNT;
        return isEnterPriseVersion;
    }

    public void init(boolean isYingYongBao) {
        LibraryInitUtil.getInstance().init(mHPlusApplication);
        mIsYingYongBao = isYingYongBao;
        setAndroidVersionUrlPath();
    }

    public boolean getmIsYingYongBao() {
        return mIsYingYongBao;
    }

    public static void setAndroidVersionUrlPath() {
        if (mIsYingYongBao) {
            if (AppConfig.isDebugMode) {
                mAndroidVersionUrlPath = WHATS_NEW_YYB_QA;
            } else {
                mAndroidVersionUrlPath = WHATS_NEW_YYB;
            }
        } else if (AppConfig.isDebugMode) {
            mAndroidVersionUrlPath = WHATS_NEW_GOOGLEPLAY_QA;
        } else {
            mAndroidVersionUrlPath = WHATS_NEW_GOOGLEPLAY;
        }
    }

    public static String getAndroidVersionUrlPath() {
        return mAndroidVersionUrlPath;
    }

    //.........................................protocol register.................

    private static ChinaEnterpriseAccountProtocol mChinaEnterpriseAccountProtocol;
    private static ChinaPersonalAccountProtocol mChinaPersonalAccountProtocol;
    private static IndiaPersonalAccountProtocol mIndiaPersonalAccountProtocol;


    private static UpdateProtocol mUpdateProtocol;


    public static LocalizationProtocol getLocalProtocol() {
        if (isEnterPriseVersion) {
            return getChinaEnterpriseAccountProtocol();
        } else {
            if (UserInfoSharePreference.isUserAccountHasData()) {
                if (isIndiaAccount()) {
                    return getIndiaPersonalAccountProtocol();
                }
                return getChinaPersonalAccountProtocol();
            } else {
                if (isLocatedInIndia()) {
                    return getIndiaPersonalAccountProtocol();
                }
                return getChinaPersonalAccountProtocol();

            }
        }

    }

    private static ChinaEnterpriseAccountProtocol getChinaEnterpriseAccountProtocol() {
        if (mChinaEnterpriseAccountProtocol == null) {
            mChinaEnterpriseAccountProtocol = new ChinaEnterpriseAccountProtocol();
        }
        return mChinaEnterpriseAccountProtocol;
    }

    private static ChinaPersonalAccountProtocol getChinaPersonalAccountProtocol() {
        if (mChinaPersonalAccountProtocol == null) {
            mChinaPersonalAccountProtocol = new ChinaPersonalAccountProtocol();
        }
        return mChinaPersonalAccountProtocol;
    }

    private static IndiaPersonalAccountProtocol getIndiaPersonalAccountProtocol() {
        if (mIndiaPersonalAccountProtocol == null) {
            mIndiaPersonalAccountProtocol = new IndiaPersonalAccountProtocol();
        }
        return mIndiaPersonalAccountProtocol;
    }

    private static boolean isIndiaAccount() {
        if (UserInfoSharePreference.getCountryCode().equals(HPlusConstants.INDIA_CODE)) {
            return true;
        }

        return false;
    }

    private static boolean isLocatedInIndia() {

        if (UserInfoSharePreference.getGpsCountryCode().equals(HPlusConstants.INDIA_CODE) || isIndiaBySIMInfo()) {
            return true;
        }

        return false;
    }


    private static boolean isIndiaBySIMInfo() {
        TelephonyManager manager = (TelephonyManager) AppManager.getInstance().getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null && manager.getSimCountryIso() != null) {
            String countryID = manager.getSimCountryIso().toUpperCase();  //CN
            if (INDIA_COUNTRY_CODE.equals(countryID)) {
                return true;
            }
        }

        return false;
    }

    public static void registerUpdateProtocol(UpdateProtocol updateProtocol) {
        mUpdateProtocol = updateProtocol;
    }

    public static UpdateProtocol getUpdateProtocol() {
        if (mUpdateProtocol == null) {
            mUpdateProtocol = new DefaultUpdateProtocol();
        }
        return mUpdateProtocol;
    }

}
