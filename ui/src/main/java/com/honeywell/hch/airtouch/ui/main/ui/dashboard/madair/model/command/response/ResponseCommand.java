package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;


import android.os.Bundle;

import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.Command;


/**
 * Created by Qian Jin on 9/7/16.
 */
public abstract class ResponseCommand extends Command {

    public static final String BUNDLE_RESPONSE_TYPE = "bundle_type";
    public static final String BUNDLE_RESPONSE_RUN_SPEED = "bundle_run_speed";
    public static final String BUNDLE_RESPONSE_CHANGED_SPEED = "bundle_changed_speed";
    public static final String BUNDLE_RESPONSE_MOTOR_TYPE = "bundle_motor_type";
    public static final String BUNDLE_RESPONSE_BATTERY_PERCENT = "bundle_battery_percent";
    public static final String BUNDLE_RESPONSE_BATTERY_REMAIN = "bundle_battery_remain";
    public static final String BUNDLE_RESPONSE_BREATH_FREQ = "bundle_breath_freq";
    public static final String BUNDLE_RESPONSE_FILTER_DURATION = "bundle_filter";
    public static final String BUNDLE_RESPONSE_ALARM = "bundle_alarm";
    public static final String BUNDLE_RESPONSE_FLASH_DATA = "bundle_flash_data";

    public ResponseCommand(byte[] responseData) {
        super(responseData);
    }

    public abstract Bundle readData();

}
