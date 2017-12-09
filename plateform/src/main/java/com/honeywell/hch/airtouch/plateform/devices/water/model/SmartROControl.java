package com.honeywell.hch.airtouch.plateform.devices.water.model;


import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;

/**
 * Created by Vincent on 10/5/16.
 */
public class SmartROControl {
    public static int getMode(AquaTouchRunstatus runStatus) {
        return runStatus.getScenarioMode();
    }

    public static boolean isModeOn(AquaTouchRunstatus runStatus) {
        if (isOffline(runStatus)) {
            return false;
        }

        return getMode(runStatus) == HPlusConstants.WATER_MODE_HOME
                || getMode(runStatus) == HPlusConstants.WATER_MODE_AWAY;

    }

    public static boolean isPowerOff(AquaTouchRunstatus runStatus) {
        return false;
    }

    public static boolean canRemoteControl(AquaTouchRunstatus runStatus) {
        return runStatus.ismMoblieCtrlFlags() == HPlusConstants.WATER_ENABLE_CONTROL;

    }

    public static boolean isOffline(AquaTouchRunstatus runStatus) {
        return !runStatus.isAlive();
    }
}
