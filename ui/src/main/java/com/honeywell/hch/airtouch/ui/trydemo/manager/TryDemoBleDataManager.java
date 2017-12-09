package com.honeywell.hch.airtouch.ui.trydemo.manager;

import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirMotorSpeed;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.IMadAirBLEDataManager;

/**
 * Created by honeywell on 23/12/2016.
 */

public class TryDemoBleDataManager implements IMadAirBLEDataManager {

    @Override
    public void changeMotorSpeed(String macAddress, MadAirMotorSpeed madAirMotorSpeed) {

    }

    @Override
    public  void saveTodayPm25(String macAddress, int pm25){
    }
}
