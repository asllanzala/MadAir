package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.plateform.devices.DeviceConstant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wuyuan on 4/27/16.
 */
public class AquaTouchRunstatus implements IRequestParams, Serializable {


    @SerializedName("scenarioMode")
    private int mScenarioMode;

    @SerializedName("errFlags")
    private int[] mErrFlags;

    @SerializedName("waterQualityLevel")
    private int mWaterQualityLevel;
    /*0 =   WATER QUALITY INITIALIZING
    1 = LEVEL_1 (Best Water Quality)
    2 = LEVEL_2
    3 = LEVEL_3
    14 = SENSOR_ERROR15 = NOT_ACTIVE
    */

    //制水时间，占时没用
    @SerializedName("singleWaterMakingTime")
    private int mSingleWaterMakingTime;

    //入水质量
    @SerializedName("inflowTDS")
    private int mInflowTDS;

    //出水质量
    @SerializedName("outflowTDS")
    private int mOutflowTDS;

    //出水体积
    @SerializedName("outflowVolume")
    private int mOutflowVolume;

    @SerializedName("isAlive")
    private boolean isAlive;

    @SerializedName("mobileCtrlFlags")
    private int mMoblieCtrlFlags;

    @SerializedName("filters")
    private List<SmartROFilterInfo> mFilterInfoList;


    public int getScenarioMode() {
        return mScenarioMode;
    }

    public void setScenarioMode(int mScenarioMode) {
        this.mScenarioMode = mScenarioMode;
    }

    public int[] getErrFlags() {
        return mErrFlags;
    }

    public void setErrFlags(int[] mErrFlags) {
        this.mErrFlags = mErrFlags;
    }

    public int getWaterQualityLevel() {
        return mWaterQualityLevel;
    }

    public void setWaterQualityLevel(int mWaterQualityLevel) {
        this.mWaterQualityLevel = mWaterQualityLevel;
    }

    public int getSingleWaterMakingTime() {
        return mSingleWaterMakingTime;
    }

    public void setSingleWaterMakingTime(int mSingleWaterMakingTime) {
        this.mSingleWaterMakingTime = mSingleWaterMakingTime;
    }

    public int getInflowTDS() {
        return mInflowTDS;
    }

    public void setInflowTDS(int mInflowTDS) {
        this.mInflowTDS = mInflowTDS;
    }

    public int getOutflowTDS() {
        return mOutflowTDS;
    }

    public void setOutflowTDS(int mOutflowTDS) {
        this.mOutflowTDS = mOutflowTDS;
    }

    public int getOutflowVolume() {
        return mOutflowVolume;
    }

    public void setOutflowVolume(int mOutflowVolume) {
        this.mOutflowVolume = mOutflowVolume;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public int ismMoblieCtrlFlags() {
        return mMoblieCtrlFlags;
    }

    public void setmMoblieCtrlFlags(int mMoblieCtrlFlags) {
        this.mMoblieCtrlFlags = mMoblieCtrlFlags;
    }

    public List<SmartROFilterInfo> getmFilterInfoList() {
        return mFilterInfoList;
    }

    public void setmFilterInfoList(List<SmartROFilterInfo> mFilterInfoList) {
        this.mFilterInfoList = mFilterInfoList;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

    public boolean isNormal() {
        return mErrFlags.length == 0 || mWaterQualityLevel == DeviceConstant.SENSOR_ERROR;
    }

}
