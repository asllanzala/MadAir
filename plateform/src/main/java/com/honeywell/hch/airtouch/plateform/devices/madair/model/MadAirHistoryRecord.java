package com.honeywell.hch.airtouch.plateform.devices.madair.model;


import com.honeywell.hch.airtouch.library.util.DateTimeUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Qian Jin on 11/15/16.
 *
 * MadAirHistoryRecord 由两部分组成：mFlashDataMap 和 mParticleMap
 * mFlashDataMap：设备连接后，发送requestFlashData命令得到的response
 * mParticleMap: 得到response后，根据pm2.5计算出昨天的颗粒物
 */

public class MadAirHistoryRecord implements Serializable {

    public static final String DATE_FORMAT2 = "yyyy/MM/dd";
    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    private static final String PREFERENCE_FILE_NAME = "mad_air_history_record";

    private static final int INDEX_AVERAGE_FREQ = 0;
    private static final int INDEX_MAX_FREQ = 1;
    private static final int INDEX_USING_TIME = 2;
    private static final int INDEX_IS_NEW_FILTER = 3;
    private static final int FLASH_DATA_LENGTH = 4;

    private HashMap<String, byte[]> mFlashDataMap = new HashMap();

    private HashMap<String, Float> mParticleMap = new HashMap();


    public MadAirHistoryRecord(HashMap<String, byte[]> flashDataMap) {
        this.mFlashDataMap = flashDataMap;
    }

    public MadAirHistoryRecord() {
    }

    public HashMap<String, byte[]> getFlashDataMap() {
        return mFlashDataMap;
    }

    public HashMap<String, Float> getParticleMap() {
        return mParticleMap;
    }

    public void setParticleMap(HashMap<String, Float> particleMap) {
        this.mParticleMap = particleMap;
    }

    private float calculateAccumulation(int usingTime, int pm25) {
        return (usingTime * pm25 * 18) / 1000;
    }

    public void savePm25IntoParticleMap(HashMap<String, Integer> pm25Map, HashMap<String, byte[]> flashDataMap,
                                        HashMap<String, Float> localParticleMap) {

        float accumulation;

        if (pm25Map == null || localParticleMap == null)
            return;

        mFlashDataMap = flashDataMap;

        for (String dateString : mFlashDataMap.keySet()) {
            if (isToday(dateString))
                continue;

            if (pm25Map.containsKey(dateString)) {
                accumulation = calculateAccumulation(getUsingTime(dateString), pm25Map.get(dateString));
                localParticleMap.put(dateString, accumulation);
            }
        }

        mParticleMap = localParticleMap;
    }

    private int getUsingTime(String dateString) {
        if (mFlashDataMap.get(dateString).length == FLASH_DATA_LENGTH)
            return mFlashDataMap.get(dateString)[INDEX_USING_TIME] * 5; // firmware issue * 5
        else
            return 0;
    }

    private boolean isToday(String dateString) {
        return dateString.equals(DateTimeUtil.getNowDateTimeString(DATE_FORMAT2));
    }

}
