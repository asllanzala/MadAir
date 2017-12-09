package com.honeywell.hch.airtouch.plateform.devices.common;


import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;

/**
 * Created by Qian Jin on 4/27/16.
 */
public class AirTouchControl {

    public static int getMode(AirtouchRunStatus runStatus) {
        return runStatus.getScenarioMode();
    }

    public static boolean isModeOn(AirtouchRunStatus runStatus) {
        if (isOffline(runStatus)) {
            return false;
        }

        return getMode(runStatus) == HPlusConstants.MODE_AUTO_INT
                || getMode(runStatus) == HPlusConstants.MODE_SLEEP_INT
                || getMode(runStatus) == HPlusConstants.MODE_QUICK_INT
                || getMode(runStatus) == HPlusConstants.MODE_SILENT_INT;

    }

    public static boolean isPowerOff(AirtouchRunStatus runStatus) {
        if (isOffline(runStatus)) {
            return true;
        }

        return getMode(runStatus) == HPlusConstants.MODE_OFF_INT;
    }

    public static boolean canRemoteControl(AirtouchRunStatus runStatus) {
        return  HPlusConstants.ENABLE_CONTROL.equalsIgnoreCase(runStatus.getMobileCtrlFlags());
    }

    public static boolean isOffline(AirtouchRunStatus runStatus) {
        return !runStatus.getIsAlive();
    }

}
