package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager;

import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirMotorSpeed;

/**
 * Created by honeywell on 23/12/2016.
 */

public interface IMadAirBLEDataManager {

    void saveTodayPm25(String macAddress, int pm25);

    void changeMotorSpeed(String macAddress, MadAirMotorSpeed madAirMotorSpeed);
}
