package com.honeywell.hch.airtouch;

import com.honeywell.hch.airtouch.library.LibApplication;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;

/**
 * Initial mHPlusApplication
 * Created by nan.liu on 1/14/15.
 */
public class HPlusApplication extends LibApplication {

    private static String TAG = HPlusApplication.class.getSimpleName();

    private static HPlusApplication mHPlusApplication = null;

//    private static final String ANDROID_UPGRADE_JSON_PATH = "https://hch.blob.core.chinacloudapi.cn/hplus-android-version-update/android_Hplus_update_info_yingyongbao.txt";

    @Override
    public void onCreate() {
        super.onCreate();
        mHPlusApplication = this;
        AppManager.getInstance().setHPlusApplication(mHPlusApplication);
        AppManager.getInstance().init( true);
        UmengUtil.onActivityCreate(this);
    }


}
