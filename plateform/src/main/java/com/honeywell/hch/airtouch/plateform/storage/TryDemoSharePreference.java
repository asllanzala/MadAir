package com.honeywell.hch.airtouch.plateform.storage;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;

/**
 * Created by Vincent on 14/11/16.
 */
public class TryDemoSharePreference {
    private final static String TRYDEMO_KEY = "trydemo_key";
    private final static String TRYDEMO_INFO_SHAREPREFERENCE = "trydemo_info_sharepreference";

    public static void saveTryDemoEntrance(boolean isShown) {
        AppConfig.isChangeEnv = true;
        SharePreferenceUtil.setPrefBoolean(TRYDEMO_INFO_SHAREPREFERENCE,
                TRYDEMO_KEY, isShown);
    }

    public static boolean isShownTryDemoEntrance() {
        return SharePreferenceUtil.getPrefBoolean(TRYDEMO_INFO_SHAREPREFERENCE, TRYDEMO_KEY, true);
    }
}
