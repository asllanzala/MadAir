package com.honeywell.hch.airtouch.ui.trydemo.manager;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;

/**
 * Created by h127856 on 16/11/2.
 */
public class TryDemoConstant {

    public static final String TRY_DEMO_SP = "try_demo_sp";

    public static final String ARRIVE_TIME_KEY = "arrive_home_time";


    public static final String IS_FROM_TRY_DEMO = "is_from_trydemo";

    public static final int AIRTOUCH_P_DEVICEID = -1;
    public static final int AQUA_TOUCH_RO_600S_DEVICEID = -2;
    public static final int MADAIR_DEVICEID = -3;

    public static final int PM_DEFAULT_VAULE = 200;

    public static final int PM_MIN_VAULE = 20;

    public static final int TVOC_DEFAULT_VALUE = HPlusConstants.POOR;
    public static final int AIRTOUCH_DEFAULT_MODE = HPlusConstants.MODE_AUTO_INT;

    public static final int WATER_DEFAULT_MODE = HPlusConstants.WATER_MODE_HOME;
    public static final int WATER_DEFAULT_QUALITY = DeviceConstant.WATER_QUALITY_GOOD;

    public static final int DURING_TIME = 20;
    public static final int MAX_DIFF_PM_VALUE = PM_DEFAULT_VAULE - PM_MIN_VAULE;
    public static final int EVERY_SPEED_PM_VALUE = MAX_DIFF_PM_VALUE/(HPlusConstants.SPEED7 * DURING_TIME);


    public static final int EVERY_SPEED_TVOC_VAULE = (int)(1.2 * (HPlusConstants.TVOC_HIGH_LIMIT_FOR_450 - HPlusConstants.TVOC_LOW_LIMIT_FOR_450))/ (HPlusConstants.SPEED7 * DURING_TIME);
}
