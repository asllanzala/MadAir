package com.honeywell.hch.airtouch.ui.enroll.manager;

import android.util.Log;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.feature.enroll.IEnrollFeature;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.common.EnrollmentConstant;
import com.honeywell.hch.airtouch.plateform.http.model.enroll.EnrollDeviceTypeModel;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollChoiceModel;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 14/4/16.
 */
public class SmartLinkUiManager {
    private final String TAG = "SmartLinkUiManager";
    private final int STARTINDEX = 0;
    private final int ENDINDEX = 6;
    private SmartLinkManager mSmartLinkManager;

    public SmartLinkUiManager() {
        mSmartLinkManager = new SmartLinkManager();
    }

    public EnrollScanEntity parseCheckTypeResponse(String result, EnrollScanEntity scanEntity) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
            Log.i(TAG, "jsonArry: " + jsonArray.toString());
            String[] enrollType = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                enrollType[i] = jsonArray.getString(i);
                Log.i("SmartLinkEnroll", "macID" + i + ": " + enrollType[i]);
            }
            scanEntity.setmEnrollType(enrollType);
        } catch (JSONException ex) {
            ex.printStackTrace();
            scanEntity.setmEnrollType(null);
            return scanEntity;
        }
        return scanEntity;
    }

    public ScanDeviceType parseURL(String recode, EnrollScanEntity mEnrollScanEntity) {

        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "recode: " + recode);
        if (recode.contains(EnrollmentConstant.JDBASEURL)) {
            return splitJDUrl(recode, mEnrollScanEntity);
        } else if (recode.contains(EnrollmentConstant.HONEYURLURL)) {
            String[] honeyCode = recode.split("\\?");
            if (honeyCode.length == 1) {
                // http://hch.honeywell.com.cn/landingpage/air-touch-install.html
                mEnrollScanEntity.setData("", "", HPlusConstants.AIR_TOUCH_S_SILVER, null, "", false);
                Log.i(TAG, "model: " + HPlusConstants.AIR_TOUCH_S_SILVER);
                return ScanDeviceType.HPLUS_SUCCESS;
            }
        } else if (HPlusConstants.AIR_TOUCH_P_MODEL.equals(recode) || HPlusConstants.AIR_TOUCH_S_SILVER.equals
                (recode)) {
            //PAC45M1022W
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "TOUCH_P_MODEL: " + recode);
            mEnrollScanEntity.setData("", "", HPlusConstants.AIR_TOUCH_P_MODEL, null, "", false);
            return ScanDeviceType.HPLUS_SUCCESS;
        }
        return splitBaseUrl(recode, mEnrollScanEntity);
    }

    public ScanDeviceType splitJDUrl(String recode, EnrollScanEntity mEnrollScanEntity) {
        String[] JD = recode.split("\\?");
        Log.i(TAG, "JD.length: " + JD.length);
        if (JD.length >= 2) {
            String jdBase64 = JD[1];
            String[] jdValue = jdBase64.split("\\=");
            if ("g".equals(jdValue[0])) {
                String base64url = "";
                try {
                    base64url = StringUtil.decodeURL(jdValue[1]);
                } catch (UnsupportedEncodingException ex) {
                    return ScanDeviceType.INVALID;
                }
                String mProductUUID = StringUtil.parseJDURL(base64url, STARTINDEX, ENDINDEX);
                if (!StringUtil.isInvalidQrCode(mProductUUID)) {
                    return ScanDeviceType.INVALID;
                }
                String mMacID = StringUtil.parseJDURL(base64url, ENDINDEX, base64url.length());
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mProductUUID: " + mProductUUID);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mMacID: " + mMacID);
                mEnrollScanEntity.setData(mProductUUID, mMacID, mProductUUID, null, "", false);
                return ScanDeviceType.HPLUS_SUCCESS;
            }
        }
        return ScanDeviceType.FAIL;
    }

    public ScanDeviceType splitBaseUrl(String recode, EnrollScanEntity mEnrollScanEntity) {
        String[] modelCode = recode.split("model=");
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "modelCode.length: " + modelCode.length);
        if (modelCode.length == 1) {
            String model = modelCode[0];
            mEnrollScanEntity.setData("", "", model, null, "", false);
            return ScanDeviceType.HPLUS_SUCCESS;
        } else {
            String modelParse = modelCode[1];
            String[] macIdurlCode = modelParse.split("&h=");
            String[] countryCode = modelParse.split("&country=");
            if (macIdurlCode.length >= 2) {
                String model = macIdurlCode[0];
                String base64url = "";
                try {
                    base64url = StringUtil.decodeURL(macIdurlCode[1]);
                } catch (UnsupportedEncodingException ex) {
                    return ScanDeviceType.INVALID;
                }
                String mMacID = StringUtil.parseJDURL(base64url, STARTINDEX, base64url.length());
                if (!StringUtil.isInvalidQrCode(mMacID)) {
                    return ScanDeviceType.INVALID;
                }
                String mProductUUID = mMacID;
                mEnrollScanEntity.setData(mProductUUID, mMacID, model, null, "", false);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mProductUUID: " + mProductUUID);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mMacID: " + mMacID);
            } else if (countryCode.length >= 2) {
                String model = countryCode[0];
                String county = countryCode[1];
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "country: " + county);
                mEnrollScanEntity.setData("", "", model, null, county, false);
            } else {
                String model = modelParse;
                mEnrollScanEntity.setData("", "", model, null, "", false);
            }
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "model: " + mEnrollScanEntity.getmModel());
            if (mEnrollScanEntity.isMadAirModle(mEnrollScanEntity.getmModel())) {
                mEnrollScanEntity.setmEnrollType(new String[]{"-2"});
                mEnrollScanEntity.setmDeviceType(HPlusConstants.MAD_AIR_TYPE);
                return ScanDeviceType.MADAIR_SUCCESS;
            }
            return ScanDeviceType.HPLUS_SUCCESS;
        }
    }

    public List<EnrollDeviceTypeModel> handleEnrollDeviceResponse(ResponseResult responseResult) {
        if (responseResult.getResponseData() != null) {
            List<EnrollDeviceTypeModel> enrollDeviceTypeModelList = (List<EnrollDeviceTypeModel>) responseResult.getResponseData()
                    .getSerializable(EnrollDeviceTypeModel.ENROLL_DEVICE_TYPE_DATA);
            EnrollScanEntity.getEntityInstance().setmEnrollDeviceTypeModelList(enrollDeviceTypeModelList);
            return enrollDeviceTypeModelList;
        }
        return null;
    }

    public SelfServerEnrollType checkDeviceType(EnrollScanEntity enrollScanEntity, List<EnrollDeviceTypeModel> enrollDeviceTypeModelList) {
        for (EnrollDeviceTypeModel enrollDeviceTypeModel : enrollDeviceTypeModelList) {
            List<String> modelList = enrollDeviceTypeModel.getmModels();
            if (modelList != null && modelList.size() > 0) {
                for (String model : modelList) {
                    if (enrollScanEntity.getmModel().equals(model)) {
                        LogUtil.log(LogUtil.LogLevel.INFO, "SmartLinkEnroll", "model: " + model);
                        LogUtil.log(LogUtil.LogLevel.INFO, "SmartLinkEnroll", "deviceType: " + enrollDeviceTypeModel.getmType());
                        enrollScanEntity.setmDeviceType(enrollDeviceTypeModel.getmType());
                        if (DeviceType.isHplusSeries(enrollDeviceTypeModel.getmType())) {
                            return SelfServerEnrollType.SUCCESS;
                        } else {
                            return SelfServerEnrollType.UPDATE;
                        }
                    }
                }
            }

        }
        return SelfServerEnrollType.UNKNOWDEVICE;

    }

    public void parseEnrollChoiceModle(List<EnrollChoiceModel> mEnrollChoiceModelList) {
        for (int i = 0; i < AppManager.getLocalProtocol().getEnrollDeviceType().length; i++) {
            int deviceType = AppManager.getLocalProtocol().getEnrollDeviceType()[i];
            IEnrollFeature ihPlusDeviceFeature = DeviceType.getEnrollFeatureByDeviceType(deviceType);
            EnrollChoiceModel enrollChoiceModel = new EnrollChoiceModel(ihPlusDeviceFeature.getEnrollChoiceDeivceImage(), ihPlusDeviceFeature.getEnrollDeviceName(), deviceType);
            mEnrollChoiceModelList.add(enrollChoiceModel);
        }
    }

    public void enrollChoiceImageClick(EnrollChoiceModel enrollChoiceModel, List<EnrollChoiceModel> mEnrollChoiceModelList) {
        enrollChoiceModel.setAlpha(1.0f);
        EnrollScanEntity.getEntityInstance().setmDeviceType(enrollChoiceModel.getDeviceType());
        for (int i = 0; i < mEnrollChoiceModelList.size(); i++) {
            EnrollChoiceModel enrollChoiceModelTemple = mEnrollChoiceModelList.get(i);
            if (enrollChoiceModelTemple != enrollChoiceModel) {
                enrollChoiceModelTemple.setAlpha(0.5f);
            }
        }
    }

    public class DownLoadType {


        public int mResource;

        public int getResource() {
            return mResource;
        }

        public void setResource(int resource) {
            mResource = resource;
        }


        public DownLoadType(int resource) {
            mResource = resource;
        }

        public int getResource(DownLoadType clickType) {
            return clickType.mResource;
        }
    }

    public class GetEnrollType {

        public int resource;

        public int getResource() {
            return resource;
        }

        public void setResource(int resource) {
            this.resource = resource;
        }

        public GetEnrollType(int resource) {
            this.resource = resource;
        }

        public int getResource(GetEnrollType clickType) {
            return clickType.resource;
        }
    }

    public enum SelfServerEnrollType {
        SUCCESS,
        UNKNOWDEVICE,
        UPDATE,
    }

    public enum ScanDeviceType {
        HPLUS_SUCCESS,
        FAIL,
        INVALID,
        MADAIR_SUCCESS;

    }

    //检查设备是否是授权过来的设备并在当前家中
    public boolean checkIfAuthDeviceAlreadyEnrolled(String macId) {
        List<UserLocationData> userLocations = UserAllDataContainer.shareInstance().getUserLocationDataList();
        if (userLocations != null) {
            for (int i = 0; i < userLocations.size(); i++) {
                ArrayList<HomeDevice> homeDevicesList = userLocations.get(i).getHomeDevicesList();
                for (int j = 0; j < homeDevicesList.size(); j++) {
                    if (homeDevicesList.get(j).getDeviceInfo().getMacID().equalsIgnoreCase(macId)) {
                        DIYInstallationState.setIsDeviceAlreadyEnrolled(true);
                        if (!userLocations.get(i).isIsLocationOwner()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //检查设备是否在当前家中
    public boolean checkIfSelfDeviceAlreadyEnrolled(String macId) {
        LogUtil.log(LogUtil.LogLevel.INFO, "AirTouchEnrollWelcome", "checkIfSelfDeviceAlreadyEnrolled");
        List<UserLocationData> userLocations = UserAllDataContainer.shareInstance().getUserLocationDataList();
        if (userLocations != null) {
            for (int i = 0; i < userLocations.size(); i++) {
                ArrayList<HomeDevice> homeDevicesList = userLocations.get(i).getHomeDevicesList();
                for (int j = 0; j < homeDevicesList.size(); j++) {
                    LogUtil.log(LogUtil.LogLevel.INFO, "AirTouchEnrollWelcome", "home macId: " + homeDevicesList.get(j).getDeviceInfo().getMacID());
                    if (homeDevicesList.get(j).getDeviceInfo().getMacID().equalsIgnoreCase(macId)) {
                        LogUtil.log(LogUtil.LogLevel.INFO, "AirTouchEnrollWelcome", "device macId: " + homeDevicesList.get(j).getDeviceInfo().getMacID());
                        DIYInstallationState.setIsDeviceAlreadyEnrolled(true);
                        if (userLocations.get(i).isIsLocationOwner()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void executeDownLoadDeviceType() {
        mSmartLinkManager.executeDownLoadDeviceType();
    }

    public void executeGetDeviceType(String deviceModel) {
        mSmartLinkManager.executeGetDeviceType(deviceModel);
    }

    public void setSuccessCallback(SmartLinkManager.SuccessCallback successCallback) {
        mSmartLinkManager.setSuccessCallback(successCallback);
    }

    public void setErrorCallback(SmartLinkManager.ErrorCallback errorCallback) {
        mSmartLinkManager.setErrorCallback(errorCallback);
    }
}



