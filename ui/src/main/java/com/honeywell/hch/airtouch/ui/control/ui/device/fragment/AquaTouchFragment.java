package com.honeywell.hch.airtouch.ui.control.ui.device.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.plateform.http.model.device.AquaTouchRunstatus;
import com.honeywell.hch.airtouch.plateform.http.model.device.SmartROCapability;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.TypeTextView;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;

import java.util.Map;

/**
 * Created by Vincent on 25/7/16.
 */
public class AquaTouchFragment extends DeviceControlBaseFragment {
    private final String TAG = "AquaTouchFragment";
    private int mCommand = 1;
    private RadioButton homeRadioButton, awayRadioButton;
    private RadioGroup modeRadioGroup;
    private TypeTextView mAquaQualityTv;
    private AquaTouchRunstatus mLatestRunStatus;
    private boolean isAfterCreateView = false;
    private Drawable mRegularGreyDb;
    private Drawable mHolidayGreyDb;
    private Drawable mRegularLightDb;
    private Drawable mHolidayLightDb;


    public static AquaTouchFragment newInstance(HomeDevice homeDevice, Activity activity, int fromType) {
        AquaTouchFragment fragment = new AquaTouchFragment();
        fragment.initActivity(activity);
        fragment.setCurrentDevice(homeDevice, fromType);
        return fragment;
    }

    protected void setCurrentDevice(HomeDevice device, int fromType) {

        mCurrentDevice = device;
        mDeviceId = mCurrentDevice.getDeviceInfo().getDeviceID();

        if (mParentActivity != null) {
            mDeviceProductName = mParentActivity.getString(mCurrentDevice.getiEnrollFeature().getEnrollDeviceName());
        }
        initControlUIManager(mCurrentDevice, fromType);
        mControlUIManager.createFilters(mCurrentDevice, mFilters);

        registerRunStatusChangedReceiver();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControlModeDrawable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aqua_touch, container, false);
        initView(view);
        initAdapter();
        showLayout();
        initListener();
        initRadioButtonListener();
        return view;
    }

    protected void initView(View view) {
        super.initView(view, R.string.device_control_auqa_filter_status);
        mAquaQualityTv = (TypeTextView) view.findViewById(R.id.aqua_quality_value);
        modeRadioGroup = (RadioGroup) view.findViewById(R.id.command_radio_group);
        modeRadioGroup.setClickable(true);
        homeRadioButton = (RadioButton) view.findViewById(R.id.home_radio);
        awayRadioButton = (RadioButton) view.findViewById(R.id.away_radio);
    }

    private void initControlModeDrawable() {
        mRegularGreyDb = getResources().getDrawable(R.drawable.ic_regular_grey);
        mRegularLightDb = getResources().getDrawable(R.drawable.ic_regular_blue);
        mHolidayGreyDb = getResources().getDrawable(R.drawable.ic_holiday_grey);
        mHolidayLightDb = getResources().getDrawable(R.drawable.ic_holiday_blue);
        mRegularGreyDb.setBounds(0, 0, mRegularGreyDb.getMinimumWidth(), mRegularGreyDb.getMinimumHeight());
        mRegularLightDb.setBounds(0, 0, mRegularLightDb.getMinimumWidth(), mRegularLightDb.getMinimumHeight());
        mHolidayGreyDb.setBounds(0, 0, mHolidayGreyDb.getMinimumWidth(), mHolidayGreyDb.getMinimumHeight());
        mHolidayLightDb.setBounds(0, 0, mHolidayLightDb.getMinimumWidth(), mHolidayLightDb.getMinimumHeight());
    }


    private void showLayout() {
        mDeviceNameTv.setText(mCurrentDevice.getDeviceInfo().getName());
        displayStatus(((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus());
        isFlashingView();
    }

    private void isFlashingView() {
        if (mControlUIManager.getIsFlashing(mDeviceId)) {
            String mode = mControlUIManager.getControlModePre(mDeviceId);
            isControlling = true;
            clearAllRadioButton();
            if (mode.equals(String.valueOf(HPlusConstants.WATER_MODE_HOME))) {
                homeRaidoClick();
                homeRadioButton.startAnimation(alphaOffAnimation);
            } else if (mode.equals(String.valueOf(HPlusConstants.WATER_MODE_AWAY))) {
                holidayRadioClick();
                awayRadioButton.startAnimation(alphaOffAnimation);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initFilter();
    }

    private void initFilter() {
        if (!DeviceControlActivity.hasNullDataInSmartRODevice((WaterDeviceObject) mCurrentDevice)
                && AppManager.getLocalProtocol().getRole().canShowFilter(mCurrentDevice.getDeviceInfo())) {
            isAfterCreateView = true;
            Map<Integer, SmartROCapability> capabilityMap = mControlUIManager.getSmartRoCapabilityMap();
            SmartROCapability smartROCapability = capabilityMap.get(mCurrentDevice.getDeviceType());
            if (smartROCapability != null) {
                updateFilterRemain(smartROCapability);
            } else {
                mControlUIManager.getConfigFromServer();
            }
        }
    }

    private void initRadioButtonListener() {
        homeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canControl()) {
                    homeRaidoClick();
                    homeRadioButton.startAnimation(alphaOffAnimation);
                    mCommand = HPlusConstants.WATER_MODE_HOME;
                    controlDevice();
                }
            }
        });
        awayRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canControl()) {
                    holidayRadioClick();
                    awayRadioButton.startAnimation(alphaOffAnimation);
                    mCommand = HPlusConstants.WATER_MODE_AWAY;
                    controlDevice();
                }
            }
        });
    }

    private boolean canControl() {
        if (!mCurrentDevice.getiDeviceStatusFeature().isNormal()) {
            return false;
        }

        if (DeviceControlActivity.hasNullDataInSmartRODevice((WaterDeviceObject) mCurrentDevice)) {
            return false;
        }

        if (ControlUIManager.isErrorBeforeControlDevice(mCurrentDevice, ControlUIManager.PRESS_MODE,
                mDropDownAnimationManager, getContext())) {
            return false;
        }
        clearAllAnimation();
        clearAllRadioButton();
        return true;
    }

    private void updateFilterRemain(SmartROCapability smartROCapability) {
        for (int i = 0; i < ((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus()
                .getmFilterInfoList().size(); i++) {
            mControlUIManager.setmWaterRunTimeLife(i, mCurrentDevice, mFilters,smartROCapability);
            mDeviceFilterAdapter.changeData(mFilters);
            mDeviceFilterAdapter.notifyDataSetChanged();
        }
    }

    private void clearAllAnimation() {
        homeRadioButton.clearAnimation();
        awayRadioButton.clearAnimation();
    }

    private void clearAllRadioButton() {
        homeRadioButton.setClickable(true);
        homeRadioButton.setTextColor(mModeOffTextColor);
        awayRadioButton.setClickable(true);
        awayRadioButton.setTextColor(mModeOffTextColor);
        homeRadioButton.setCompoundDrawables(null, mRegularGreyDb, null, null);
        awayRadioButton.setCompoundDrawables(null, mHolidayGreyDb, null, null);
    }

    private void displayStatus(AquaTouchRunstatus runStatusResponse) {
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "displayStatus----");
        isControlling = false;
        clearAllRadioButton();
        clearAllAnimation();

        if (runStatusResponse == null) {
            return;
        }

        showNormalOrErrorLayout(mCurrentDevice.getiDeviceStatusFeature().isNormal());
        if (((WaterDeviceObject) mCurrentDevice).getWaterQualityFeature().showQualityLevel() != null) {
            mAquaQualityTv.setTypeText(((WaterDeviceObject) mCurrentDevice).getWaterQualityFeature().showQualityLevel());
            mAquaQualityTv.setTextColor(((WaterDeviceObject) mCurrentDevice).getWaterQualityFeature().showQualityColor());
        } else {
            mAquaQualityTv.setTextColor(((WaterDeviceObject) mCurrentDevice).getWaterQualityFeature().showQualityColor());
            mAquaQualityTv.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
        }
        mLatestRunStatus = runStatusResponse;

        if (!mLatestRunStatus.isAlive()) {
            return;
        }
        switch (mLatestRunStatus.getScenarioMode()) {
            case HPlusConstants.WATER_MODE_HOME:
                homeRaidoClick();
                break;
            case HPlusConstants.WATER_MODE_AWAY:
                holidayRadioClick();
                break;
            default:
                homeRadioButton.setTextColor(mModeOffTextColor);
                awayRadioButton.setTextColor(mModeOffTextColor);
                homeRadioButton.setCompoundDrawables(null, mRegularGreyDb, null, null);
                awayRadioButton.setCompoundDrawables(null, mHolidayGreyDb, null, null);
                break;
        }
    }

    private void homeRaidoClick() {
        homeRadioButton.setTextColor(mModeOnTextColor);
        homeRadioButton.setCompoundDrawables(null, mRegularLightDb, null, null);
        homeRadioButton.setClickable(false);
    }

    private void holidayRadioClick() {
        awayRadioButton.setTextColor(mModeOnTextColor);
        awayRadioButton.setCompoundDrawables(null, mHolidayLightDb, null, null);
        awayRadioButton.setClickable(false);
    }

    private void controlDevice() {
        isControlling = true;
        mControlUIManager.controlWaterDevice(mDeviceId, mCommand, mDeviceProductName);
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "water_controll fail: " + responseResult.getRequestId());
        switch (responseResult.getRequestId()) {
            case COMM_TASK:
                displayStatus(mLatestRunStatus);
                errorHandle(responseResult, getString(id));
                break;
            default:
                super.dealErrorCallBack(responseResult, id);
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "water_controll successfull: " + responseResult.getRequestId());
        switch (responseResult.getRequestId()) {
            case COMM_TASK:
                clearAllAnimation();
                mLatestRunStatus = ((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus();
                ControlUIManager.updateModeToWaterRunStatus(mCommand, mLatestRunStatus);
                displayStatus(mLatestRunStatus);
                break;
            case GET_ALL_DEVICE_TYPE_CONFIG:
                if (isAfterCreateView && responseResult != null && responseResult.isResult() && !DeviceControlActivity.hasNullDataInSmartRODevice((WaterDeviceObject) mCurrentDevice)) {
                    initFilter();
                }
                break;
        }

    }

    protected void dealWithBroadCast() {
        if (mCurrentDevice != null && ((DeviceControlActivity)mParentActivity).getFromType() == ControlConstant.FROM_NORMAL_CONTROL) {
            if (!isControlling) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "dealWithBroadCast----");
                displayStatus(((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus());
            }
            mControlUIManager.createFilters(mCurrentDevice, mFilters);
            initFilter();
        }
    }


    /**
     *这个只是用于try demo的数据刷新，不需要处理网络请求
     */
    @Override
    protected void doRefreshUI(){
        if (mCurrentDevice != null) {
            if (!isControlling){
                displayStatus(((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus());
            }
        }
    }

    @Override
    public Bundle getmLatestRunStatus() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DeviceControlActivity.ARG_DEVICE_RUNSTATUS, mLatestRunStatus);
        return bundle;
    }

    @Override
    public void doRefreshUIOpr() {
        if (mCurrentDevice != null) {
            if (!isControlling) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "dealWithBroadCast----");
                displayStatus(((WaterDeviceObject) mCurrentDevice).getAquaTouchRunstatus());
            }
        }
    }
}
