package com.honeywell.hch.airtouch.plateform.storage;

import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;

/**
 * Created by Vincent on 10/11/16.
 */
public class UpdateVersionPreference {

    /*
      *lastversion 是服务器上最新的，每当更新更新 lastversion时 把红点设为true
     */
    private final static String ME_RED_DOT_KEY = "update_version_env_key";
    private final static String OWN_UPDATE_KEY = "own_update_key";
    private final static String LAST_VERSION_CODE_KEY = "last_version_code_key";
    private final static String UPDATE_VERSION_INFO_SHAREPREFERENCE = "update_version_info_sharepreference";
    public final static int LASTVERSIONDEFAULT = -1;

    public static void saveMeRedDot(boolean isShowRedDot) {
        SharePreferenceUtil.setPrefBoolean(UPDATE_VERSION_INFO_SHAREPREFERENCE,
                ME_RED_DOT_KEY, isShowRedDot);
    }

    public static boolean getMeRedDot() {
        return SharePreferenceUtil.getPrefBoolean(UPDATE_VERSION_INFO_SHAREPREFERENCE, ME_RED_DOT_KEY, false);
    }

    public static void saveVersionCode(int lastVersionCode) {
        SharePreferenceUtil.setPrefInt(UPDATE_VERSION_INFO_SHAREPREFERENCE,
                LAST_VERSION_CODE_KEY, lastVersionCode);
    }

    public static int getLastVersionCode() {
        return SharePreferenceUtil.getPrefInt(UPDATE_VERSION_INFO_SHAREPREFERENCE, LAST_VERSION_CODE_KEY, LASTVERSIONDEFAULT);
    }

    public static void saveOwnUpdate(boolean ownUpdate) {
        SharePreferenceUtil.setPrefBoolean(UPDATE_VERSION_INFO_SHAREPREFERENCE,
                OWN_UPDATE_KEY, ownUpdate);
    }

    public static boolean getOwnUpdate() {
        return SharePreferenceUtil.getPrefBoolean(UPDATE_VERSION_INFO_SHAREPREFERENCE, OWN_UPDATE_KEY, false);
    }

}
