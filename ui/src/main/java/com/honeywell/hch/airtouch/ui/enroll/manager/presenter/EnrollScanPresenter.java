package com.honeywell.hch.airtouch.ui.enroll.manager.presenter;

import android.content.Context;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.http.model.enroll.EnrollDeviceTypeModel;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IEnrollScanView;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkUiManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkUiManager.DownLoadType;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkUiManager.GetEnrollType;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkUiManager.ScanDeviceType;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollDeviceInfoActivity;

import java.util.List;

/**
 * Created by Vincent on 17/10/16.
 */
public class EnrollScanPresenter {
    private final String TAG = "EnrollScanPresenter";

    private IEnrollScanView mIEnrollScanView;
    private Context mContext;
    protected SmartLinkUiManager mSmartLinkUiManager;
    private final int AWAKE = 0;
    public static final int RUNNING = 1;
    public static final int SUCCESS = 2;
    private final int FAIL = 3;
    private DownLoadType downLoadType;
    private GetEnrollType getEnrollType;
    private EnrollScanEntity mEnrollScanEntity = null;
    private List<EnrollDeviceTypeModel> mEnrollDeviceTypeModel = null;


    public EnrollScanPresenter(Context context, IEnrollScanView enrollScanView) {
        mContext = context;
        mIEnrollScanView = enrollScanView;
        mSmartLinkUiManager = new SmartLinkUiManager();
        initManagerCallBack();
        downLoadType = mSmartLinkUiManager.new DownLoadType(AWAKE);
        getEnrollType = mSmartLinkUiManager.new GetEnrollType(AWAKE);
    }

    public void initManagerCallBack() {
        setErrorCallback(mErrorCallBack);
        setSuccessCallback(mSuccessCallback);
    }

    public void restManagerCallBack() {
        setErrorCallback(null);
        setSuccessCallback(null);
    }

    private SmartLinkManager.SuccessCallback mSuccessCallback = new SmartLinkManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            if (mEnrollScanEntity != null){
                dealSuccessCallBack(responseResult);
            }
        }
    };

    private SmartLinkManager.ErrorCallback mErrorCallBack = new SmartLinkManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult, int id) {
            if (mEnrollScanEntity != null){
                dealErrorCallBack(responseResult, id);
            }
        }
    };

    private void dealSuccessCallBack(ResponseResult responseResult) {
        mIEnrollScanView.dealSuccessCallBack();
        switch (responseResult.getRequestId()) {
            case GET_ENROLL_TYPE:
                UmengUtil.onEvent(mContext, UmengUtil.SCAN_SSID_STR_ID, NetWorkUtil.updateWifiInfo(mContext));
                getEnrollType.setResource(SUCCESS);
                if (responseResult.isResult() && responseResult.getResponseCode() == StatusCode.OK) {
                    String result = responseResult.getExeptionMsg();
                    if (!StringUtil.isEmpty(result)) {
                        mSmartLinkUiManager.parseCheckTypeResponse(result, mEnrollScanEntity);
                        if (downLoadType.getResource() == RUNNING) {
                            break;
                        }
                        isSupportSmartLink();
                    }
                } else {
                    mIEnrollScanView.unKnowDevice();
                }
                break;
            case ENROLL_DEVICE_TYPE:
                downLoadType.setResource(SUCCESS);
                mEnrollDeviceTypeModel = mSmartLinkUiManager.handleEnrollDeviceResponse(responseResult);
                if (mEnrollDeviceTypeModel != null && !StringUtil.isEmpty(mEnrollScanEntity.getmModel())
                        && getEnrollType.getResource() == SUCCESS) {
                    isSupportSmartLink();
                }
                break;
        }
    }

    private void isSupportSmartLink() {
        String[] deviceType = mEnrollScanEntity.getmEnrollType();
        if (deviceType != null) {
            if (downLoadType.getResource() == FAIL) {
                mIEnrollScanView.deviceModelErrorl();
            } else if (EnrollScanEntity.NO_WIFI_MODE.equals(deviceType[0])) {
                mIEnrollScanView.unSupportSmartLinkDevice();
            } else if (mSmartLinkUiManager.checkDeviceType(mEnrollScanEntity, mEnrollDeviceTypeModel) == SmartLinkUiManager.SelfServerEnrollType.SUCCESS) {
                //SmartLink mode need to verify network
                if (mSmartLinkUiManager.checkIfSelfDeviceAlreadyEnrolled(mEnrollScanEntity.getmMacID())) {
                    mIEnrollScanView.updateWifi();
                } else if (mSmartLinkUiManager.checkIfAuthDeviceAlreadyEnrolled(mEnrollScanEntity.getmMacID())) {
                    mIEnrollScanView.registedAuthDevice();
                } else if (!NetWorkUtil.isNetworkAvailable(mContext)) {
                    mIEnrollScanView.showNoNetWorkDialog();
                } else {
                    mIEnrollScanView.startIntent(EnrollDeviceInfoActivity.class);
                }
            } else if (mSmartLinkUiManager.checkDeviceType(mEnrollScanEntity, mEnrollDeviceTypeModel) == SmartLinkUiManager.SelfServerEnrollType.UPDATE) {
                mIEnrollScanView.updateVersion();
            } else if (mSmartLinkUiManager.checkDeviceType(mEnrollScanEntity, mEnrollDeviceTypeModel) == SmartLinkUiManager.SelfServerEnrollType.UNKNOWDEVICE) {
                mIEnrollScanView.unKnowDevice();
            }
        } else {
            mIEnrollScanView.unKnowDevice();
        }
    }

    private void dealErrorCallBack(ResponseResult responseResult, int id) {
        switch (responseResult.getRequestId()) {
            case ENROLL_DEVICE_TYPE:
                downLoadType.setResource(FAIL);
                break;
            case GET_ENROLL_TYPE:
                getEnrollType.setResource(FAIL);
                mIEnrollScanView.errorHandle(responseResult, mContext.getString(id));
                break;
        }
        mIEnrollScanView.disMissDialog();
    }

    private void setSuccessCallback(SmartLinkManager.SuccessCallback successCallback) {
        mSmartLinkUiManager.setSuccessCallback(successCallback);
    }

    private void setErrorCallback(SmartLinkManager.ErrorCallback errorCallback) {
        mSmartLinkUiManager.setErrorCallback(errorCallback);
    }


    public void initSmartEnrollScanEntity() {
        mEnrollScanEntity = EnrollScanEntity.getEntityInstance();
        mEnrollScanEntity.clearData();
        DIYInstallationState.reset();
    }

    public void downLoadEdviceType() {
        if (NetWorkUtil.isNetworkAvailable(mContext)) {
            downLoadType.setResource(RUNNING);
            mSmartLinkUiManager.executeDownLoadDeviceType();
        }
    }

    public void executeGetDeviceType() {
        mSmartLinkUiManager.executeGetDeviceType(mEnrollScanEntity.getmModel());
    }

    public void paresURL(String recode) {
        ScanDeviceType scanDeviceType = mSmartLinkUiManager.parseURL(recode, mEnrollScanEntity);
        if (scanDeviceType == ScanDeviceType.MADAIR_SUCCESS) {

            // upper case macId
            String macId = mEnrollScanEntity.getmMacID().toUpperCase();

            if (macId.length() == 12) {
                String newMacId = macId.substring(0, 2) + ":" + macId.substring(2, 4) + ":"
                        + macId.substring(4, 6) + ":" + macId.substring(6, 8) + ":"
                        + macId.substring(8, 10) + ":" + macId.substring(10, 12);

                mEnrollScanEntity.setmMacID(newMacId);
            }

            if (MadAirDeviceModelSharedPreference.hasDevice(mEnrollScanEntity.getmMacID())) {
                mIEnrollScanView.madAirDeviceAlreadyEnrolled();
            } else {
                mIEnrollScanView.startIntent(EnrollDeviceInfoActivity.class);
            }
        } else if (scanDeviceType == ScanDeviceType.HPLUS_SUCCESS) {
            if (downLoadType.getResource() == AWAKE || downLoadType.getResource() == FAIL) {
                downLoadEdviceType();
            }
            executeGetDeviceType();
        } else if (scanDeviceType == ScanDeviceType.INVALID) {
            mIEnrollScanView.invalidDevice();
        } else {
            mIEnrollScanView.unKnowDevice();
        }
    }

    public DownLoadType getDownLoadType() {
        return downLoadType;
    }

    public void setmEnrollScanEntity() {
        mEnrollScanEntity = EnrollScanEntity.getEntityInstance();
    }
}