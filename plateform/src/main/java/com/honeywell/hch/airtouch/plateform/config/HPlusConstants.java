package com.honeywell.hch.airtouch.plateform.config;

/**
 * Created by wuyuan on 15/4/28.
 */
public class HPlusConstants {

    /**
     * device wifi  prefix
     */
    public static final String AIR_TOUCH_S_SSID = "AirTouch S";
    public static final String AIR_TOUCH_X_SSID = "AirTouch X";
    public static final String AIR_TOUCH_P_SSID = "AirTouch P";
    public static final String AIR_TOUCH_FFAC_SSID = "AIRBIG2W";
    public static final String AQUA_TOUCH_600_SSID = "ATRO600S";
    public static final String AQUA_TOUCH_400_SSID = "ATRO400S";
    public static final String AQUA_TOUCH_100_SSID = "ATRO100S";
    public static final String AQUA_TOUCH_75_SSID = "ATRO75S";
    public static final String AQUA_TOUCH_50_SSID = "ATRO50S";
    public static final String AIR_TOUCH_XCOMPACT_SSID = "AirTouch X2";
    public static final String AIR_TOUCH_X3COMPACT_SSID = "AirTouch X3";

    /**
     * display HouseActivity
     */
    public static final String ADD_DEVICE_OR_HOME_ACTION = "add_device_or_home_action";
    public static final String IS_ADD_HOME = "is_add_home";
    // for enrollment last step, location id of selected home
    public static final String LOCAL_LOCATION_ID = "local_location_id";

    public static final int DEFAULT_TIME = -1;



    public static String downLoadUrl() {
        if (AppConfig.isDebugMode) {
            return "https://acscloud.honeywell.com.cn/hplus/qa/model_type_qa/Hplus_DeviceType.txt";
        } else {
            return "https://acscloud.honeywell.com.cn/hplus/model_type/Hplus_DeviceType.txt";
        }
    }

    public static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=com.honeywell.hch.airtouch&hl=en";
//    public static final String ENROLL_DEVICE_TYPE_URL_DEBUG = "http://hch.blob.core.chinacloudapi.cn/testsite/Hplus_DeviceType.txt";
//
//    public static final String ENROLL_DEVICE_TYPE_URL = "http://hch.blob.core.chinacloudapi.cn/hplus-model-type/Hplus_DeviceType.txt";
    /**
     * for init emotion bubble content of cigeratte or car fume
     */
    public static final String INIT_STR_VALUE = "0.00";

    public static final int AIRTOUCH_S_TYPE = 1048577;

    public static final int AIRTOUCH_X_TYPE = 1048592;

    public static final int AIRTOUCH_JD_TYPE = 1048578;

    public static final int AIRTOUCH_450_TYPE = 1048579;

    public static final int AIRTOUCH_FFAC_TYPE = 1048580;

    public static final int WATER_SMART_RO_600_TYPE = 1048608;

    public static final int WATER_SMART_RO_400_TYPE = 1114114;

    public static final int AIRTOUCH_XCOMPACT_TYPE = 1048581;

    public static final int AIRTOUCH_X3COMPACT_TYPE = 1048582;
    public static final int AIRTOUCH_X3COMPACT_UPDATE_TYPE = 1048584;

    public static final int AIRTOUCH_S_UPDATE_TYPE = 1048585;
    public static final int AIRTOUCH_450_UPDATE_TYPE = 1048586;

    public static final int WATER_SMART_RO_100_TYPE = 1114115;
    public static final int WATER_SMART_RO_75_TYPE = 1114116;
    public static final int WATER_SMART_RO_50_TYPE = 1114117;

    public static final int MAD_AIR_TYPE = 1000000;
    public static final int MAD_AIR_AUTH = 1000001;
    /**
     * broadcast
     */

    public static final String AUTH_UNREAD_REFLASH_MESSAGE = "auth_unread_reflash_message";
    public static final String AUTH_NOTIFY_MESSAGE = "auth_notify_message";

    public static final String DOWN_LOAD_BG_END = "down_load_bg_end";

    public static final String SET_DEFALUT_HOME = "set_defalut_home";

    public static final String RENAME_HOME = "rename_home";

    public static final String AFTER_USER_LOGIN = "after_user_login";

    public static final String SHORTTIME_REFRESH_END_ACTION = "short_time_refresh_task";

    public static final String LONG_REFRESH_END_ACTION = "long_time_refresh_task";

    public static final String REFRESH_SESSION_ACTION = "refresh_session_action";

    public static final String TRY_DEMO_VALUE_CHANGE = "try_demo_value_change";

    public static final String GROUP_SCENARIO_REFRESH = "group_scenario_refresh";

    public static final String GPS_RESULT = "gps_result";

    public static final String NEED_UPDATE = "need_update";

    public static final String LOGOUT_ACTION = "android.intent.action.logout_action";

    public static final String ENTER_ENROLL_PROCESS = "enter_enroll_process";
    public static final String EXIT_ENROLL_PROCESS = "exit_enroll_process";

    public static final String NET_WORK_ERROR = "network_error";

    public static final String NETWORK_CONNECT_SERVER_WELL = "network_connect_server_well";

    public static final String HAS_WIFI_CONNECTED = "has_wifi_connected";

    public static final String UPDATE_ME_RED_DOT_ACTION = "update_me_red_dot";

    public static final String REFRESH_MADAIR_DATA = "refresh_madair_data";
    public static final String TRY_DEMO_REFRESH_MADAIR_DATA = "try_demo_refresh_madair_data";
    /**
     * Max/Min limit
     */
    public static final int MAX_HOME_CHAR_EDITTEXT = 14;
    public static final int MIN_USER_PASSWORD = 6;

    /**
     * HTTP response bundle data key
     */
    public static final String COMM_TASK_BUNDLE_KEY = "taskId";
    public static final String LOCATION_ID_BUNDLE_KEY = "locationId";

    public static final String DEVICE_CAPABILITY_KEY = "device_capability_key";

    public static final String WEATHER_DATA_KEY = "weather_data_key";

    /**
     * HTTP response flag for cases
     */
    public static final int CHECK_MAC_ALIVE = 2000;
    public static final int CHECK_MAC_AGAIN = 2001;
    public static final int CHECK_MAC_OFFLINE = 2002;
    public static final int COMM_TASK_SUCCEED = 3000;
    public static final int COMM_TASK_FAILED = 3001;
    public static final int COMM_TASK_RUNNING = 3002;
    public static final int COMM_TASK_TIMEOUT = 3003;
    public static final int COMM_TASK_END = 3004;
    public static final int COMM_TASK_PART_FAILED = 3005;
    public static final int COMM_TASK_ALL_FAILED = 3006;

    public static final int HOME_CONTROL_ERROR = 3007;

    /**
     * the max value of low pm value
     */
    public static int MAX_PMVALUE_LOW = 75;
    public static final int OUTDOOR_PM25_MAX = 110;
    /**
     * the middle value of middle pm value
     */
    public static final int MAX_PMVALUE_MIDDLE = 150;

    /**
     * location id
     */
    public static final String LOCATION_ID = "location_id";

    public static final String DEVICE_ID_LIST = "open_device_id_list";

    /**
     * country code
     */
    public static final String CHINA_CODE = "86";
    public static final String INDIA_CODE = "91";

    public static final String CHINA_STRING = "CN";
    public static final String INDIA_STRING = "IN";

    public static final String CHINA_LANGUAGE_CODE = "zh";
    public static final String CHINA_LANGUAGE = "CN";

    public static final int FROM_HOME_PAGE = 0;
    public static final int FROM_DEVICE_PAGE = 1;

    /**
     * intent putExtra
     */
    public static final String FORGET_PASSWORD = "forgetPassword";
    public static final String GET_SMS_RESULT = "get_sms_result";
    public static final String SMS_CODE_VALID_RESULT = "sms_code_valid_result";
    public static final String MOBILE_DONE_BACK = "mobileDoneBack";
    public static final String NEW_USER = "newUser";

    public static final String SMART_ENROLL_ENRTRANCE = "smartenrollentranch";
    public static final String IMG_UPLOAD_CALLBACK = "img_upload_callback";

    public static final String GET_TOTAL_DUST_PARAMETER= "get_total_dust_parameter";
    public static final String GET_TOTAL_VOLUME_PARAMETER= "get_total_volume_parameter";

    public static int COMM_TASK_TIME_GAP = 1000;

    public static final int ERROR_FILTER_RUNTIME = -1;


    public static final String YINGYONGBAO_URL = "http://android.myapp.com/myapp/detail.htm?apkName=com.honeywell.hch.airtouch";
    public static final String ENTERPRISE_TYPE = "EnterpriseApp";

    /**
     * URL for introduction
     */
    public static final String WEB_URL_INTRODUCT = "https://acscloud.honeywell.com.cn/hplus/device_intro/index.html?deviceType=%1$s&userType=%2$s&country=%3$s&language=%4$s&version=%5$s";
    public static final String WEB_URL_INTRODUCT_QA = "https://acscloud.honeywell.com.cn/hplus/qa/device_intro_qa/index.html?deviceType=%1$s&userType=%2$s&country=%3$s&language=%4$s&version=%5$s";


    /**
     * user manual new
     */
    public static final String WEB_URL_USERMANUAL = "https://acscloud.honeywell.com.cn/hplus/user_manual/home/Usermanual.htm?deviceType=%1$s&userType=%2$s&country=%3$s&language=%4$s&version=%5$s";
    public static final String WEB_URL_USERMANUAL_QA = "https://acscloud.honeywell.com.cn/hplus/qa/user_manual_qa/home/Usermanual.htm?deviceType=%1$s&userType=%2$s&country=%3$s&language=%4$s&version=%5$s";

    /**
     * URL for EULA
     */
    public static final String WEB_URL_EULA = "https://acscloud.honeywell.com.cn/hplus/eula/index.html?country=%1$s&language=%2$s&version=%3$s";
    public static final String WEB_URL_EULA_QA = "https://acscloud.honeywell.com.cn/hplus/qa/eula_qa/index.html?country=%1$s&language=%2$s&version=%3$s";

    /*
        filter purchase
     */
    public static final String BASE_PURCHASE_URL = "https://acscloud.honeywell.com.cn/hplus/filter/filterpurchase.html?";
    public static final String BASE_QA_PURCHASE_URL = "https://acscloud.honeywell.com.cn/hplus/qa/filter_qa/filterpurchase.html?";
    /**
     * AirTouch Device Control & AirTouchFilter panel
     */
    public static final int AIR_TOUCH_2_POINT_PER_SPEED = 2;
    public static final int AIR_TOUCH_3_POINT_PER_SPEED = 3;
    public static final int AIR_TOUCH_S_CONTROL_POINT_TOTAL = 7;
    public static final int AIR_TOUCH_450_CONTROL_POINT_TOTAL = 7;
    public static final int AIR_TOUCH_X_CONTROL_POINT_TOTAL = 9;
    public static final int AIR_TOUCH_FFAC_CONTROL_POINT_TOTAL = 7;
    public static final int SMART_RO_FILTER_NUMBER = 3;
    public static final int AIR_TOUCH_S_FILTER_NUMBER = 3;
    public static final int AIR_TOUCH_X_FILTER_NUMBER = 1;
    public static final int AIR_TOUCH_450_FILTER_NUMBER = 2;
    public static final int AIR_TOUCH_FFAC_FILTER_NUMBER = 2;
    public static final int AIR_TOUCH_XCOMPACT_FILTER_NUMBER = 2;
    public static final int MAD_AIR_FILTER_NUMBER = 1;
    public final static int SPEED1 = 1;
    public final static int SPEED2 = 2;
    public final static int SPEED3 = 3;
    public final static int SPEED4 = 4;
    public final static int SPEED5 = 5;
    public final static int SPEED6 = 6;
    public final static int SPEED7 = 7;
    public final static int SPEED8 = 8;
    public final static int SPEED9 = 9;
    public final static String SPEED_1 = "Speed_1";
    public final static String SPEED_2 = "Speed_2";
    public final static String SPEED_3 = "Speed_3";
    public final static String SPEED_4 = "Speed_4";
    public final static String SPEED_5 = "Speed_5";
    public final static String SPEED_6 = "Speed_6";
    public final static String SPEED_7 = "Speed_7";
    public final static String SPEED_8 = "Speed_8";
    public final static String SPEED_9 = "Speed_9";

    /**
     * Off   --- 0
     *         Auto ---  1
     *         Sleep ----- 2
     *         Silent  ------ 3
     *         QuickClean ----- 4
     *         Manual  ------ 5
     */
    public final static int MODE_AUTO_INT = 1;
    public final static int MODE_SLEEP_INT = 2;
    public final static int MODE_QUICK_INT = 4;
    public final static int MODE_SILENT_INT = 3;
    public final static int MODE_OFF_INT = 0;
    public final static int MODE_MANUAL_INT = 5;
    public final static int MODE_DEFAULT_INT = -1;
    public final static int MODE_DEFAULT_NONE_MODE = -2;

    public final static String MODE_AUTO = "Auto";
    public final static String MODE_SLEEP = "Sleep";
    public final static String MODE_QUICK = "QuickClean";
    public final static String MODE_SILENT = "Silent";
    public final static String MODE_OFF = "Off";
    public final static String MODE_MANUAL = "Manual";


    public final static int WATER_MODE_HOME = 0;
    public final static int WATER_MODE_AWAY = 1;

    public final static int PM25_LOW_LIMIT = 35;
    public final static int PM25_MEDIUM_LIMIT = 75;
    public final static int PM25_MEDIUMER_LIMIT = 115;
    public final static int PM25_HIGH_LIMIT = 150;
    public final static int PM25_MAX_VALUE = 999;
    public final static String MAX_TVOC_VALUE = "1.50";
    public final static String MIN_TVOC_VALUE = "0.13";
    public final static double TVOC_LOW_LIMIT_FOR_450 = 200;
    public final static double TVOC_HIGH_LIMIT_FOR_450 = 600;
    public final static double TVOC_LOW_LIMIT_FOR_PREMIUM = 0.35;
    public final static double TVOC_HIGH_LIMIT_FOR_PREMIUM = 0.45;

    public final static float TVOC_HIGH_LIMIT_FOR_PREMIUM_Level = 0.85f;
    public final static float TVOC_LOW_LIMIT_FOR_PREMIUM_Level = 0.6f;
    public final static double TVOC_MAX_FOR_PREMIUM_Level = 1500;
    public final static double TVOC_MIN_FOR_PREMIUM_Level = 130;
    public final static int ERROR_VALUE = -1;
    public final static int ERROR_MAX_VALUE = 65535;

    public final static int ERROR_SENSOR = 65534;
    public final static int GOOD = 200;

    public final static int AVERAGE = 400;

    public final static int POOR = 600;

    /**
     * Call Service Phone Number
     */
    public static final String CONTACT_PHONE_NUMBER = "4007204321";
    public static final String INDIA_CONTACT_PHONE_NUMBER = "1800-103-4761";

    /**
     * AirTouchDeviceObject model
     */
    public static final String AIR_TOUCH_P_MODEL = "PAC45M1022W";
    public static final String AIR_TOUCH_P_WIFI = "KJ450F-PAC2022S";

    public static final String AIR_TOUCH_X_1G_MODEL = "KJ700G-PAC2127W";
    public static final String AIR_TOUCH_X_2G_MODEL = "KJ700F-PAC2127W";

    public static final String AIR_TOUCH_S_SILVER = "PAC35M2101S";
    public static final String AIR_TOUCH_S_INDIA = "HAC35M2101S";
    public static final String AIR_TOUCH_S_LIGHT = "PAC35M2101T1";
    public static final String AIR_TOUCH_S_DARK = "PAC35M2101T2";
    public static final String AIR_TOUCH_S_PECAN = "KJ300F-PAC2101S/T1/T2";

    public static final String AIR_TOUCH_S_JD = "KSN95Y";
    public static final String AIR_TOUCH_P_JD = "KHN6YM";

    public static final String AIR_TOUCH_FFAC_MODEL_G = "SKJ900G-PAC3454W";
    public static final String AIR_TOUCH_FFAC_MODEL_W = "SKJ900G-PAC3454A";

    public static final String AQUA_TOUCH_600G_MODEL = "WTE-P-D-(FST)-90-HRO-600-S";

    public static final String AQUA_TOUCH_400G_MODEL = "YCZ-CT60-002-S";

    public static final String AIR_TOUCH_X3_COMPACT_MODEL = "KJ600F-PAC2158A";

    public static final String AIR_TOUCH_X_COMPACT_MODEL = "KJ550F-PAC2156W";

    //MadAIr 四种类型
    public static final String MAD_AIR_MODEL_WHITE = "MA50100PW";
    public static final String MAD_AIR_MODEL_BLACK = "MA50100PB";
    public static final String MAD_AIR_MODEL_PINK = "MA50100PI";
    public static final String MAD_AIR_MODEL_SKULL = "MA50100SK";

    /**
     * Personal/Enterprise account
     */
    public static final int PERSONAL_ACCOUNT = 1;
    public static final int ENTERPRISE_ACCOUNT = 2;

    public static final String DEBUG_ENVIRONMENT = "debug";
    public static final String RELEASE_ENVIRONMENT = "release";

    /**
     * SharedPreference name (must not change name due to old app version)
     */
    public static final String PREFERENCE_SEND_GROUP_CONTROL_MODE = "group_control_mode";
    public static final String PREFERENCE_SUCCESS_GROUP_CONTROL_MODE = "group_previous_control_mode";
    public static final String PREFERENCE_GROUP_CONTROL_FLASH = "group_control_flash";
    public static final String PREFERENCE_USER_CONFIG = "user_config";
    public static final String PREFERENCE_DEVICE_CONTROL_MODE = "device_control_mode";

    public static final String DEVICE_CONTROL_NORMAL = "control_normal";
    public static final String PREFERENCE_DEVICE_CONTROL_FLASH = "device_control_flash";

    public static final String FIRST_RUN = "first";

    /**
     * 远程控制的值
     */
    public static final String ENABLE_CONTROL = "ENABLED";
    public static final String DISABLE_CONTROL = "DISABLE";


    public static final int WATER_ENABLE_CONTROL = 1;

    public static final int WATER_INIT_QUALITY_LEVEL = 0;


    public static final int EMOTION_TYPE_AIRTOUCH = 1;
    public static final int EMOTION_TYPE_WATER = 2;

    public final static int OWN_UPDATE = 0;
    public final static int NO_UPDATE = -1;
    public final static int NORMAL_UPDATE = 1;
    public final static int FORCE_UPDATE = 2;
    public final static int UNSUPPORT_MESSAGE_UPDATE = 3;
    public final static int NOTIFICATION_UPDATE = 4;


    public final static int DEFAULT_COMD_TASK_KEY = -1;

    public static final String COMM_TASK_MAP_BUNDLE_KEY = "comm_map_bundle_key";


    public static final String USER_LOGIN_INFO = "user_login_info";

    /*
    broad cast
     */
    public static final String BROADCAST_ACTION_STOP_FLASHINGTASK = "broadcast_stop_task";

    public static final String HOME_CONTROL_STOP_FLASHINGTASK = "home_control_stop_flashing";

    public static final String DEVICE_CONTROL_BROADCAST_ACTION_STOP_FLASHINGTASK = "device_control_broadcast_stop_task";

    public static final String BROADCAST_ACTION_GROUP_REFLASH = "group_reflash";


    /**
     *
     */
    public static final String DATA_LOADING_STATUS = "···";
    public static final String DATA_LOADING_FAILED_STATUS = "---";

    public static final String INTRODUCTION_TYPE = "introduction_type";
    public static final String USERMANUAL_TYPE = "usermanual_type";
    public static final String FLOATFORMAT = "0.00";

    public static final String BUNDLE_DEVICES_IDS = "device_ids";


    /**
     * 用于TVOC的等级的比较
     */
    public static final int TVOC_GOOD_LEVEL = 1;
    public static final int TVOC_MID_LEVEL = 2;
    public static final int TVOC_BAD_LEVEL = 3;
    //tvoc的读数为65534或是65535的时候
    public static final int TVOC_ERROR_LEVEL = -1;
    public static final int TVOC_SENSOR_ERROR_LEVEL = -2;
    public static final int DEVICE_INIT_VALUE = -3;


    public static final int DEFAULT_PM25_VALUE = 65535;
    public static final String ARG_CONTROL_TASK_UUID = "control_task_uuid";

    public static final int DECLINEACTON = 0;
    public static final int ACCEPTACTION = 1;
    public static final int GOTITACTION = 3;


    public static final int FROM_GROUP_SCENARIO_CONTROL = 1;
    public static final int FROM_HOME_SCENARIO_CONTROL = 2;
}
