package com.honeywell.hch.airtouch.ui.common.ui;

import com.honeywell.hch.airtouch.ui.R;

/**
 * Created by h127856 on 7/21/16.
 */
public class DashBoadConstant {


    public static int[] WEATHER_ICON = {R.drawable.sunny, R.drawable.sunny, R.drawable.sunny,
            R.drawable.sunny, R.drawable.heavycloudy, R.drawable.lightcloudy,
            R.drawable.lightcloudy, R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy,
            R.drawable.rainy, R.drawable.rainy, R.drawable.rainy, R.drawable.rainy,
            R.drawable.rainy, R.drawable.rainy, R.drawable.rainy, R.drawable.rainy,
            R.drawable.rainy, R.drawable.rainy, R.drawable.rainandsnow, R.drawable.snow,
            R.drawable.snow, R.drawable.snow, R.drawable.snow, R.drawable.snow,
            R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy,
            R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy,
            R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy, R.drawable.cloudy,
            R.drawable.cloudy, R.drawable.cloudy};


    public static final int DEFAULT_PM25_VALUE = 65535;
    public static final int DEFAULT_WEATHER_CODE = -1;
    public static final int DEFAULT_TEMPERATURE = -99;


    public static final int WATER_QUALITY_DEFAULT = -1;
    public static final int WATER_QUALITY_GOOD = 1;
    public static final int WATER_QUALITY_MIDDLE = 2;
    public static final int WATER_QUALITY_BAD = 3;


    public static final int ONLY_AITOUCH_WORSE = 0;
    public static final int ONLY_WATER_WORSE = 1;
    public static final int BOTH_AITOUCH_WATER_WORSE = 2;


    public static final int NO_ERROR_IN_HOME = 0;


    public static final int GROUP_CONTROL_REQUEST_CODE = 11;
    public static final int DEVICE_CONTROL_REQUEST_CODE = 12;
    public static final int QUICK_ACTION_RESULT_CODE = 13;
    public static final int CHANGE_PASSWORD_CODE = 14;

    public static final String ARG_QUICK_ACTION_DEVICE = "quick_action_device";

    public static final String ARG_HANDLE_MESSAGE = "arg_handle_message";

    public static final String ARG_READ_MESSAGE = "arg_read_message";

    public static final int RESULT_READ = 1001;

    public static final String ARG_GROUP_ID = "group_id";
    public static final String ARG_HOME_INDEX = "homeIndex";
    public static final String ARG_LOCATION_ID = "location_id";
    public static final String ARG_LAST_RUNSTATUS = "last_runstatus";
    public static final String ARG_CITY_NAME = "city_name";
    public static final String ARG_LOCATION_NAME = "location_name";
    public static final String ARG_CITY_BACKGROUD_LIST = "city_background_list";

    public static final String ARG_CONTROL_TASK_UUID = "control_task_uuid";
    public static final String ARG_MULTI_TASK_RESULT = "multi_result";
    public static final String ARG_RESULT_CODE = "result_code";

    public static final int DEAFAULT_SCENARIO_MODEL = -1;
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

    public static int[] HOME_SCENARIO_INACTVIE_SRC = {R.drawable.gohome_inactive, R.drawable.home_sleep_inactive, R.drawable.home_awake_inactive, R.drawable.away_inactive};
    public static int[] HOME_SCENARIO_ACTVIE_SRC = {R.drawable.home_athome_active, R.drawable.sleep_active, R.drawable.home_awake_active, R.drawable.home_away_active};
    public static int[] HOME_SCENARIO_ACTVIE_TEXTCOLOR = {R.color.blue_one, R.color.purple_one,R.color.yellow_one, R.color.green_one};


    public static final int DECLINEACTON = 0;
    public static final int ACCEPTACTION = 1;
    public static final int GOTITACTION = 3;
}
