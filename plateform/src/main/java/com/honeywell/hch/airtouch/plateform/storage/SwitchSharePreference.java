package com.honeywell.hch.airtouch.plateform.storage;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;

/**
 * Created by Vincent on 9/11/16.
 */
public class SwitchSharePreference {
    private final static String DEVELOP_ENV_KEY = "develop_env_key";
    private final static String DEVELOP_INFO_SHAREPREFERENCE = "develop_info_sharepreference";
    public static void saveDevelopEnv(int id) {
        AppConfig.isChangeEnv = true;
        SharePreferenceUtil.setPrefInt(DEVELOP_INFO_SHAREPREFERENCE,
                DEVELOP_ENV_KEY, id);
    }

    public static int getDevelopEnv() {
        return SharePreferenceUtil.getPrefInt(DEVELOP_INFO_SHAREPREFERENCE, DEVELOP_ENV_KEY, AppConfig.QA_ENV);
    }
}
