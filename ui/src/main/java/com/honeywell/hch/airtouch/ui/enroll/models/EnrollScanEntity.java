
package com.honeywell.hch.airtouch.ui.enroll.models;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouch450EnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.AirTouchJDPEnrollFeatureImpl;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.IEnrollFeature;
import com.honeywell.hch.airtouch.plateform.http.model.enroll.EnrollDeviceTypeModel;
import com.honeywell.hch.airtouch.ui.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Vincent on 24/11/15.
 */
public class EnrollScanEntity {

    private static EnrollScanEntity mEnrollScanEntity;

    public static final String AP_MODE = "0";
    public static final String SMARTLINK_MODE = "1";
    public static final String NO_WIFI_MODE = "-1";

    private String mProductUUID;

    private String mMacID;

    private String mModel = "";

    private String[] mEnrollType;

    private String mCountry;

    private boolean isFromTimeout = false;

    private boolean isRegisteredByThisUser = false;

    private String smartEntrance;

    private int mDeviceType;

    private boolean isApMode;

    private boolean noQRcode;

    //本地服务器的deviceTypemodel
    private List<EnrollDeviceTypeModel> mEnrollDeviceTypeModelList;

    public String getSmartEntrance() {
        return smartEntrance;
    }

    public void setSmartEntranch(String smartEntranch) {
        this.smartEntrance = smartEntranch;
    }

    public String getmProductUUID() {
        return mProductUUID;
    }

    public void setmProductUUID(String mProductUUID) {
        this.mProductUUID = mProductUUID;
    }

    public String getmMacID() {
        return mMacID;
    }

    public void setmMacID(String mMacID) {
        this.mMacID = mMacID;
    }

    public String getmModel() {
        return mModel;
    }

    public int getmDeviceType() {
        return mDeviceType;
    }

    public void setmDeviceType(int mDeviceType) {
        this.mDeviceType = mDeviceType;
    }

    public void setmModel(String mModel) {
        this.mModel = mModel;
    }

    public String[] getmEnrollType() {
        return mEnrollType;
    }

    public void setmEnrollType(String[] mEnrollType) {
        this.mEnrollType = mEnrollType;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public boolean isFromTimeout() {
        return isFromTimeout;
    }

    public void setFromTimeout(boolean isTimeOut) {
        this.isFromTimeout = isTimeOut;
    }

    public boolean isNoQRcode() {
        return noQRcode;
    }

    public void setNoQRcode(boolean noQRcode) {
        this.noQRcode = noQRcode;
    }

    public void setData(String mProductUUID, String mMacID, String mModel, String[] mEnrollType, String mCountry, boolean isFromTimeout) {
        this.mProductUUID = mProductUUID;
        if (mMacID != null) {
            mMacID = mMacID.toLowerCase();
        }
        this.mMacID = mMacID;
        this.mModel = mModel;
        this.mEnrollType = mEnrollType;
        this.mCountry = mCountry;
        this.isFromTimeout = isFromTimeout;
    }

    public void clearData() {
        this.mProductUUID = "";
        this.mMacID = "";
        this.mModel = "";
        this.mEnrollType = null;
        this.mCountry = "";
        this.isFromTimeout = false;
        this.noQRcode = false;
    }

    public static EnrollScanEntity getEntityInstance() {
        if (mEnrollScanEntity == null) {
            mEnrollScanEntity = new EnrollScanEntity();
        }
        return mEnrollScanEntity;
    }

    public boolean isRegisteredByThisUser() {
        return isRegisteredByThisUser;
    }

    public void setIsRegisteredByThisUser(boolean isRegisteredByThisUser) {
        this.isRegisteredByThisUser = isRegisteredByThisUser;
    }

    @Override
    public String toString() {
        return "EnrollScanEntity{" +
                "mProductUUID='" + mProductUUID + '\'' +
                ", mMacID='" + mMacID + '\'' +
                ", mModel='" + mModel + '\'' +
                ", mEnrollType=" + Arrays.toString(mEnrollType) +
                ", mCountry='" + mCountry + '\'' +
                ", isFromTimeout=" + isFromTimeout +
                '}';
    }

    public int getDeviceImage() {
        return getEnrollFeature().getEnrollDeviceImage();
    }

    public int getDeviceName() {
        return getEnrollFeature().getEnrollDeviceName();
    }

    public int getEnrollChoiceDeivceImage() {
        return getEnrollFeature().getEnrollChoiceDeivceImage();
    }

    public String getDevicePrefixWifiName() {
        return getEnrollFeature().getEnrollDeviceWifiPre();
    }

    /**
     * * 为了兼容JDP设备。JDP的deviceType为450，但是model不一样，在enroll过程中显示的model不一样，所以需要对这块进行
     * 区分，这个方法也是为了兼容后续有类似的“devicetype一样，但是devicemodel不一致，需要在显示上不一致的情况”
     *
     * @return
     */
    public IEnrollFeature getEnrollFeature() {
        if (DeviceType.isAirTouch450(mDeviceType)) {
            if (HPlusConstants.AIR_TOUCH_P_JD.equals(mModel)) {
                return new AirTouchJDPEnrollFeatureImpl();
            }
            return new AirTouch450EnrollFeatureImpl();
        } else if (DeviceType.isMadAir(mDeviceType)) {
            return DeviceType.getEnrollFeatureByDeviceType(mDeviceType, mModel);
        }
        return DeviceType.getEnrollFeatureByDeviceType(mDeviceType);
    }

    public String getDeviceTypeStr() {
        if (DeviceType.isAirTouchSeries(mDeviceType)) {
            return AppManager.getInstance().getApplication().getString(R.string.enroll_air_touch_device_type);
        } else if (DeviceType.isWaterSeries(mDeviceType)) {
            return AppManager.getInstance().getApplication().getString(R.string.enroll_aqua_touch_device_type);
        } else if (DeviceType.isMadAIrSeries(mDeviceType)) {
            return AppManager.getInstance().getApplication().getString(R.string.mad_air_enroll_device_type);
        }
        return AppManager.getInstance().getApplication().getString(R.string.group_control_na);
    }

    public boolean isApMode() {
        return isApMode;
    }

    public void setIsApMode(boolean isApMode) {
        this.isApMode = isApMode;
    }

    public boolean isMadAirModle(String model) {
        switch (model) {
            case HPlusConstants.MAD_AIR_MODEL_WHITE:
            case HPlusConstants.MAD_AIR_MODEL_BLACK:
            case HPlusConstants.MAD_AIR_MODEL_PINK:
            case HPlusConstants.MAD_AIR_MODEL_SKULL:
                return true;
            default:
                return false;
        }
    }

    public List<EnrollDeviceTypeModel> getmEnrollDeviceTypeModelList() {
        return mEnrollDeviceTypeModelList;
    }

    public void setmEnrollDeviceTypeModelList(List<EnrollDeviceTypeModel> mEnrollDeviceTypeModelList) {
        this.mEnrollDeviceTypeModelList = mEnrollDeviceTypeModelList;
    }
}
