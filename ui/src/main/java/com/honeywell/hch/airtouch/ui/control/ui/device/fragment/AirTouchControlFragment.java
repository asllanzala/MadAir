package com.honeywell.hch.airtouch.ui.control.ui.device.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.model.device.AirtouchCapability;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.CustomFontTextView;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.control.ui.device.view.AirTouchLedView;
import com.honeywell.hch.airtouch.ui.control.ui.device.view.DeviceControlView;
import com.honeywell.hch.airtouch.ui.control.ui.device.view.LedSetting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by Vincent on 25/7/16.
 */
public class AirTouchControlFragment extends DeviceControlBaseFragment {

    private final String TAG = "AirTouchControlFragment";
    private AirtouchRunStatus mLatestRunStatus;
    private String mCommand = ""; // this command combines speed and mode
    private DeviceControlView mDeviceControlView;
    private TextView mPowerTv;
    private TextView mSleepTv;
    private TextView mAutoTv;
    private TextView mFastTv;
    private TextView mQuietTv;
    private CustomFontTextView mPM25Tv;
    private CustomFontTextView mTVOCTv;
    private TextView mTVOCHintTv;
    private TextView mGetHomeTimeTv, mCleanUpTimeTv;
    private TextView mGetHomeTimePrefixTv, mCleanUpTimePrefixTv;
    private RelativeLayout mTVOCRl;

    private ArrayList<CheckBox> ledCheckBox = new ArrayList<>();
    private int[] mCleanTimeArray;
    private int mLedTotalPoints;
    private int mMaxSpeed;
    private LedSetting ledSetting;

    private AirTouchLedView mAirTouchLedView;
    private int radius;
    private int yPiexl;

    // device view status
    private boolean isFirstLaunch = true;
    private boolean isLedOnMove = false;
    private boolean isLedOnDown = false;
    private int mSpeed;


    //default value of fan speed two dots as a level, only three when FFAC
    private int mDots = 2;

    private RelativeLayout mCircle;


    public static AirTouchControlFragment newInstance(HomeDevice homeDevice, Activity activity, int fromType) {
        AirTouchControlFragment fragment = new AirTouchControlFragment();
        fragment.initActivity(activity);
        fragment.setCurrentDevice(homeDevice, fromType);
        return fragment;
    }

    private void setCurrentDevice(HomeDevice device, int fromType) {

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
        ((DeviceControlActivity) getFragmentActivity()).registerMyTouchListener(mTouchListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_air_touch, container, false);
        initView(view);
        initLedView();
        initAdapter();
        showLayout();
        initListener();
        return view;
    }

    protected void initView(View view) {
        super.initView(view, R.string.device_control_filter_status);
        mDeviceControlView = (DeviceControlView) view.findViewById(R.id.device_control_view);
        mPowerTv = (TextView) view.findViewById(R.id.device_control_power_tv);
        mPM25Tv = (CustomFontTextView) view.findViewById(R.id.device_control_first_value_tv);
        mTVOCTv = (CustomFontTextView) view.findViewById(R.id.device_control_second_value_tv);
        mTVOCHintTv = (TextView) view.findViewById(R.id.device_control_second_hint_tv);
        mGetHomeTimeTv = (TextView) view.findViewById(R.id.device_home_time_first_hint_tv);
        mCleanUpTimeTv = (TextView) view.findViewById(R.id.device_home_time_second_hint_tv);
        mGetHomeTimePrefixTv = (TextView) view.findViewById(R.id.device_home_time_first_value_tv);
        mCleanUpTimePrefixTv = (TextView) view.findViewById(R.id.device_home_time_second_value_tv);
        mSleepTv = (TextView) view.findViewById(R.id.device_control_sleep_tv);
        mAutoTv = (TextView) view.findViewById(R.id.device_control_auto_tv);
        mFastTv = (TextView) view.findViewById(R.id.device_control_fast_tv);
        mQuietTv = (TextView) view.findViewById(R.id.device_control_quiet_tv);
        mCircle = (RelativeLayout) view.findViewById(R.id.device_control_fan_header_rl);
        mTVOCRl = (RelativeLayout) view.findViewById(R.id.device_control_second_value_rl);
        mAirTouchLedView = (AirTouchLedView) view.findViewById(R.id.device_control_panel_circle);

    }

    private void initLedView() {

        int totalPointNum = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getDeviceControlPoint();
        int unitNumber = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getPointsPerSpeed();
        mLedTotalPoints = totalPointNum * unitNumber;
        //in relative layout, measure again to get the correct value of width and height
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mCircle.measure(w, h);
        int width = mCircle.getMeasuredWidth();
        int height = mCircle.getMeasuredHeight();

        //the marginTop of circle is 40dp, the marginTop of LED layout is 20dp
        yPiexl = DensityUtil.dip2px(25) + height / 2;
        radius = width / 2 + 55;
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "radius----" + radius);
        mAirTouchLedView.initLedPosition(mLedTotalPoints, radius, yPiexl);
        ledCheckBox = mAirTouchLedView.getLedCheckBox();
        mMaxSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getMaxSpeed();
    }

    /**
     * get device running status and display.
     */
    private void initDevice() {
        ledSetting = new LedSetting();
        mLatestRunStatus = ((AirTouchDeviceObject) mCurrentDevice).getAirtouchDeviceRunStatus();
        mCleanTimeArray = mLatestRunStatus.getCleanTime();
        displayStatus(mLatestRunStatus);
    }

    private void showLayout() {
        mDeviceNameTv.setText(mCurrentDevice.getDeviceInfo().getName());
        if (!DeviceControlActivity.hasNullDataInAirtouchDevice(mCurrentDevice)) {
            initDevice();
            isFlashingView();
        }
    }

    private void isFlashingView() {
        if (mControlUIManager.getIsFlashing(mDeviceId)) {
            String mode = mControlUIManager.getControlModePre(mDeviceId);
            isControlling = true;
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "isFlashingView----");

            mDeviceControlView.setControlTv(mode);
            mDeviceControlView.startFlick(mode);
            switch (mode) {
                case HPlusConstants.MODE_OFF:
                    showLedView(0);
                    break;
                case HPlusConstants.MODE_SLEEP:
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getSleepSpeed();
                    showLedView(mSpeed);
                    break;
                case HPlusConstants.MODE_AUTO:
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getAutoSpeedFeature().getAutoSpeed();
                    showLedView(mSpeed);
                    break;
                case HPlusConstants.MODE_QUICK:
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getMaxSpeed();
                    showLedView(mSpeed);
                    break;
                case HPlusConstants.MODE_SILENT:
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getSilentSpeed();
                    showLedView(mSpeed);
                    break;
                default:
                    break;
            }
//            if(mode.contains("Speed_")){
//                int led = Integer.parseInt(mode.substring(6)) * mDots - 1;
//                if(led <= 0) return;
//                mAirTouchLedView.showLedfanPosition(led);
//                mAirTouchLedView.getLedfan().startAnimation(alphaOffAnimation);
//                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "isFlashingView----" + led);
//            }
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mPowerTv.setOnClickListener(this);
        mSleepTv.setOnClickListener(this);
        mAutoTv.setOnClickListener(this);
        mFastTv.setOnClickListener(this);
        mQuietTv.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (DeviceType.isAirTouchSeries(mCurrentDevice.getDeviceType())
                && !DeviceControlActivity.hasNullDataInAirtouchDevice(mCurrentDevice)) {
            initFilter();
        }

    }

    private void initFilter() {
        if (AppManager.getLocalProtocol().getRole().canShowFilter(mCurrentDevice.getDeviceInfo())) {
            Map<Integer, AirtouchCapability> capabilityMap = mControlUIManager.getDeviceCapabilityMap();
            AirtouchCapability airtouchCapability = capabilityMap.get(mCurrentDevice.getDeviceType());
            LogUtil.log(LogUtil.LogLevel.DEBUG, "fsfssf", "airtouchCapability: " + airtouchCapability);
            if (airtouchCapability != null) {
                updateFilterRemain(airtouchCapability);
            } else {
                mControlUIManager.getConfigFromServer();
            }
        }
    }


    /**
     * Parse device running status to speed and mode.
     * Display all status including led speed, mode, power, offline, control.
     * Display clean time for back home.
     * Display arriving home time.
     * Clear all animation showing at the present.
     * Set mode and power button clickable.
     * Save latest running status.
     */
    private void displayStatus(AirtouchRunStatus runStatusResponse) {
//        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "displayStatus----");
        isControlling = false;
        clearLedAnimation();
        mDeviceControlView.stopAllFlick();
        stopFlick(mPowerOffTv);

        mPM25Tv.setCustomText(String.valueOf(((AirTouchDeviceObject) mCurrentDevice).getPmSensorFeature().getPm25Value()));
        mPM25Tv.setTextColor(((AirTouchDeviceObject) mCurrentDevice).getPmSensorFeature().getPm25Color());
        if (((AirTouchDeviceObject) mCurrentDevice).canShowTvoc()) {
            mTVOCTv.setCustomText(String.valueOf(((AirTouchDeviceObject) mCurrentDevice).getTvocFeature().getTVOC()));
            mTVOCTv.setTextColor(((AirTouchDeviceObject) mCurrentDevice).getTvocFeature().getTVOCColor());
        } else {
            mTVOCTv.setVisibility(View.INVISIBLE);
            mTVOCHintTv.setVisibility(View.INVISIBLE);
            mTVOCRl.setVisibility(View.GONE);
        }
        showNormalOrErrorLayout(true);
        mLatestRunStatus = runStatusResponse;
        if (runStatusResponse == null)
            return;


        mSpeed = ControlUIManager.parseSpeed(runStatusResponse);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "speed: " + mSpeed);
        showLedView(mSpeed);
        if (!runStatusResponse.getIsAlive()) {
            ledSetting.setLastSettingLed(0);
            showLedView(0);
        }
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mode int: " + ControlUIManager.parseMode(runStatusResponse));

        parseModeAndSpeed();
    }


    private void parseModeAndSpeed() {
        // show mode view
        switch (ControlUIManager.parseMode(mLatestRunStatus)) {
            case HPlusConstants.MODE_AUTO_INT:
                mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getAutoSpeedFeature().getAutoSpeed();
                showLedView(mSpeed);
                mDeviceControlView.setControlTv(HPlusConstants.MODE_AUTO);
                break;

            case HPlusConstants.MODE_SLEEP_INT:
                mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getSleepSpeed();
                showLedView(mSpeed);
                mDeviceControlView.setControlTv(HPlusConstants.MODE_SLEEP);
                break;

            case HPlusConstants.MODE_QUICK_INT:
                mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getMaxSpeed();
                showLedView(mSpeed);
                mDeviceControlView.setControlTv(HPlusConstants.MODE_QUICK);
                break;

            case HPlusConstants.MODE_SILENT_INT:
                mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getSilentSpeed();
                showLedView(mSpeed);
                mDeviceControlView.setControlTv(HPlusConstants.MODE_SILENT);
                break;
            //手动时，模式致为初始状态
            case HPlusConstants.MODE_DEFAULT_INT:
            case HPlusConstants.MODE_MANUAL_INT:
                mDeviceControlView.setControlTv(HPlusConstants.MODE_MANUAL);
                break;

            default:
                break;
        }

        mDeviceControlView.setmFanSpeedTv(String.valueOf(mSpeed));
        displayCleanTime(mSpeed);
        displayArriveHome();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (mControlUIManager.canClcikable() &&
                v.getId() != R.id.enroll_back_layout
                && v.getId() != R.id.all_device_select_iv) {
            if (canControl()) {
                clearLedAnimation();
                if (v.getId() == R.id.device_control_power_tv) {
                    mCommand = HPlusConstants.MODE_OFF;
                    mDeviceControlView.setControlTv(mCommand);
                    mDeviceControlView.startFlick(mCommand);
                    controlDevice();
                } else if (v.getId() == R.id.device_control_sleep_tv) {
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getSleepSpeed();
                    showLedView(mSpeed);
                    mCommand = HPlusConstants.MODE_SLEEP;
                    mDeviceControlView.setControlTv(mCommand);
                    mDeviceControlView.startFlick(mCommand);
                    controlDevice();
                } else if (v.getId() == R.id.device_control_auto_tv) {
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getAutoSpeedFeature().getAutoSpeed();
                    showLedView(mSpeed);
                    mCommand = HPlusConstants.MODE_AUTO;
                    mDeviceControlView.setControlTv(mCommand);
                    mDeviceControlView.startFlick(mCommand);
                    controlDevice();
                } else if (v.getId() == R.id.device_control_fast_tv) {
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getMaxSpeed();
                    showLedView(mSpeed);
                    mCommand = HPlusConstants.MODE_QUICK;
                    mDeviceControlView.setControlTv(mCommand);
                    mDeviceControlView.startFlick(mCommand);
                    controlDevice();
                } else if (v.getId() == R.id.device_control_quiet_tv) {
                    mSpeed = ((AirTouchDeviceObject) mCurrentDevice).getSpeedStatusFeature().getSilentSpeed();
                    showLedView(mSpeed);
                    mCommand = HPlusConstants.MODE_SILENT;
                    mDeviceControlView.setControlTv(mCommand);
                    mDeviceControlView.startFlick(mCommand);
                    controlDevice();
                } else if (v.getId() == R.id.device_control_power_off_tv) {
                    mCommand = HPlusConstants.MODE_AUTO;
                    startFlick(mPowerOffTv);
                    controlDevice();
                }
            }
        }
    }

    private boolean canControl() {
        if (!mCurrentDevice.getiDeviceStatusFeature().isNormal()) {
            return false;
        }
        if (DeviceControlActivity.hasNullDataInAirtouchDevice(mCurrentDevice)) {
            return false;
        }
        if (ControlUIManager.isErrorBeforeControlDevice(mCurrentDevice, ControlUIManager.PRESS_MODE,
                mDropDownAnimationManager, getContext())) {
            return false;
        }
        return true;
    }

    private void controlDevice() {
        isControlling = true;
        if (!mCommand.equals("")) {
            mControlUIManager.controlDevice(mDeviceId, mCommand, mDeviceProductName);
        }
    }

    private void updateFilterRemain(AirtouchCapability airtouchCapability) {
        mControlUIManager.setupMaxLife(airtouchCapability, mFilters);
        mControlUIManager.setUnAuthFilter(mLatestRunStatus, mFilters);
        mDeviceFilterAdapter.changeData(mFilters);
        mDeviceFilterAdapter.notifyDataSetChanged();
    }

    @Override
    protected void dealErrorCallBack(ResponseResult responseResult, int id) {
        super.dealErrorCallBack(responseResult, id);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "air_controll fail");
        switch (responseResult.getRequestId()) {
            case COMM_TASK:
                displayStatus(mLatestRunStatus);
                if (isAdded()) {
                    errorHandle(responseResult, (getFragmentActivity().getString(id)));
                }
                break;
            default:
                super.dealErrorCallBack(responseResult, id);
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case GET_ALL_DEVICE_TYPE_CONFIG:
                if (responseResult != null && responseResult.isResult()) {
                    initFilter();
                }
                break;
            case COMM_TASK:
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "dealSuccessCallBack");
                mLatestRunStatus = ((AirTouchDeviceObject) mCurrentDevice).getAirtouchDeviceRunStatus();

                ControlUIManager.updateModeToRunStatus(mCommand, mLatestRunStatus);
                ControlUIManager.updateRunStatusToModel((AirTouchDeviceObject) mCurrentDevice, mLatestRunStatus,
                        ((DeviceControlActivity) getFragmentActivity()).getmUserLocation());

                displayStatus(mLatestRunStatus);
                break;
        }
    }

    @Override
    protected void dealWithBroadCast() {
        //try demo里在网络变化后是是不需要进行更新的。
        if (mCurrentDevice != null && ((DeviceControlActivity) mParentActivity).getFromType() == ControlConstant.FROM_NORMAL_CONTROL) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "dealWithBroadCast isControlling: " + isControlling);
            if (!isControlling) {
                displayStatus(((AirTouchDeviceObject) mCurrentDevice).getAirtouchDeviceRunStatus());
            }

            mControlUIManager.createFilters(mCurrentDevice, mFilters);
            initFilter();
        }
    }

    /**
     * 这个只是用于try demo的数据刷新，不需要处理网络请求
     */
    @Override
    protected void doRefreshUI() {
        if (mCurrentDevice != null) {
            if (!isControlling) {
                displayStatus(((AirTouchDeviceObject) mCurrentDevice).getAirtouchDeviceRunStatus());

            }
        }
    }

    /**
     * Led speed control
     */
    private DeviceControlActivity.MyTouchListener mTouchListener = new DeviceControlActivity.MyTouchListener() {
        @Override
        public void onTouchEvent(MotionEvent ev) {
            if (DeviceControlActivity.hasNullDataInAirtouchDevice(mCurrentDevice))
                return;

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!AppConfig.canFilterScrollPage) {
                        // Show led rising or falling when onTouch
                        //LogUtil.log(LogUtil.LogLevel.INFO, TAG, "ev.getX = " + ev.getX() + " ev.getY = " + ev.getY());
                        for (int i = 0; i < mLedTotalPoints; i++) {
                            int[] location = new int[2];
                            ledCheckBox.get(i).getLocationInWindow(location); //获取在当前窗口内的绝对坐标
                            if (Math.abs(ev.getX() - location[0]) < 30 && Math.abs(ev.getY() - location[1]) < 50) {
                                if (canControl()) {
                                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "i = " + i);
                                    isLedOnDown = true;
                                    //i+1 means the number of LED checked
                                    ledSetting.setSettingLed(i + 1);

                                    // show led view
                                    if (ledSetting.getSettingLed() > ledSetting.getLastSettingLed()) {
                                        ledRising();
                                    } else if (ledSetting.getSettingLed() < ledSetting.getLastSettingLed()) {
                                        ledFalling();
                                    }
                                    ledSetting.setLastSettingLed(ledSetting.getSettingLed());
                                }
                            }
                        }
                    }
                    break;

                case MotionEvent.ACTION_UP:
                    if (!AppConfig.canFilterScrollPage) {
                        // After showing, control speed when UP.
                        if (isLedOnDown) {
                            isLedOnDown = false;
                            clearLedAnimation();
                            if (ControlUIManager.isErrorBeforeControlDevice(mCurrentDevice, ControlUIManager.PRESS_SPEED,
                                    mDropDownAnimationManager, getContext())) {
                                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "MotionEvent.ACTION_UP---");
                                displayStatus(mLatestRunStatus);
                                return;
                            }

                            /**
                             * (1)when mDots == 3,
                             * For example, if led is 4/5, add to 6.
                             * If led is 3, do nothing
                             *
                             * (2)when mDots == 2,
                             * For example, if led is 3, add to 4.
                             * If led is 6, do nothing.
                             */
                            int led = ledSetting.getSettingLed();
                            LogUtil.log(LogUtil.LogLevel.INFO, "cgh", "led = " + led);
                            if (led % mDots == 0 && led > 0) {
                                ledSetting.setSettingSpeed(led / mDots);
                                if (ledSetting.getSettingSpeed() != ledSetting.getLastSettingSpeed() && canControl()) {
                                    //ledCheckBox.get(led - 1).startAnimation(alphaOffAnimation);
                                    mAirTouchLedView.showLedfanPosition(led - 1);
                                    mAirTouchLedView.getLedfan().startAnimation(alphaOffAnimation);
                                    ledSetting.setLastSettingSpeed(led / mDots);
                                    mCommand = "Speed_" + led / mDots;
                                    mDeviceControlView.setControlTv(HPlusConstants.MODE_MANUAL); //将模式置为灰色
                                    controlDevice();

                                    // for a special case: mode switch to manual
                                    if (ledSetting.getSettingSpeed() == ledSetting.getLastSettingSpeed()
                                            && ((AirTouchDeviceObject) mCurrentDevice).isModeOn() && canControl()) {
                                        ledCheckBox.get(led - 1).startAnimation(alphaOffAnimation);
                                        ledSetting.setLastSettingSpeed(led / mDots);
                                        mCommand = "Speed_" + led / mDots;
                                        mDeviceControlView.setControlTv(HPlusConstants.MODE_MANUAL); //将模式置为灰色
                                        controlDevice();
                                    }
                                }
                            } else if (led % mDots != 0) {
                                ledSetting.setSettingSpeed((led + mDots - (led % mDots)) / mDots);
                                //update last setting led after add to even number led
                                ledSetting.setLastSettingLed(led + mDots - (led % mDots));
                                ledCheckBox.get(led - 2 + mDots - (led % mDots)).setChecked(true);
                                ledCheckBox.get(led - 1 + mDots - (led % mDots)).setChecked(true);

                                if (ledSetting.getSettingSpeed() != ledSetting.getLastSettingSpeed() && canControl()) {
                                    ledCheckBox.get(led - 1 + mDots - (led % mDots)).startAnimation(alphaOffAnimation);
                                    mAirTouchLedView.showLedfanPosition(led - 1 + mDots - (led % mDots));
                                    mAirTouchLedView.getLedfan().startAnimation(alphaOffAnimation);

                                    ledSetting.setLastSettingSpeed((led + mDots - (led % mDots)) / mDots);
                                    mCommand = "Speed_" + (led + mDots - (led % mDots)) / mDots;
                                    mDeviceControlView.setControlTv(HPlusConstants.MODE_MANUAL); //将模式置为灰色
                                    controlDevice();
                                }
                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void ledRising() {
        if (!mCurrentDevice.getiDeviceStatusFeature().isPowerOff()) {
            for (int i = 0; i < mLedTotalPoints; i++) {
                if (((i + 1) > ledSetting.getLastSettingLed())
                        && ((i + 1) <= ledSetting.getSettingLed())) {
                    ledCheckBox.get(i).setChecked(true);
                }
            }
        }
    }

    private void ledFalling() {
        if (!mCurrentDevice.getiDeviceStatusFeature().isPowerOff()) {
            for (int i = mLedTotalPoints - 1; i > 0; i--) {
                if (((i + 1) <= ledSetting.getLastSettingLed())
                        && ((i + 1) > ledSetting.getSettingLed())) {
                    ledCheckBox.get(i).setChecked(false);
                }
            }
        }
    }

    private void clearLedAnimation() {
        for (int i = 0; i < mLedTotalPoints; i++) {
            ledCheckBox.get(i).clearAnimation();
            mAirTouchLedView.getLedfan().clearAnimation();
        }
    }

    /**
     * Display clean time for back home.
     *
     * @param speed - setting speed or running speed
     */
    private void displayCleanTime(int speed) {
        mCleanTimeArray = mLatestRunStatus.getCleanTime();
        if (mCleanTimeArray == null || mCurrentDevice.getiDeviceStatusFeature().isPowerOff()) {
            mCleanUpTimeTv.setVisibility(View.GONE);
            mCleanUpTimePrefixTv.setVisibility(View.GONE);
            return;
        }

        if (mCleanTimeArray.length == mMaxSpeed) {
            if (speed > 0) {
                mCleanUpTimePrefixTv.setVisibility(View.VISIBLE);
                mCleanUpTimeTv.setVisibility(View.VISIBLE);
                int cleanTime = mCleanTimeArray[speed - 1];
                if (cleanTime > 60) {
                    cleanTime /= 60;
                    mCleanUpTimeTv.setText(String.format
                            (getFragmentActivity().getString(R.string.clean_time_hour), cleanTime));
                } else if (cleanTime > 0) {
                    mCleanUpTimeTv.setText(String.format
                            (getFragmentActivity().getString(R.string.clean_time_minute), cleanTime));
                }
                return;
            }
        } else {
            mCleanUpTimeTv.setVisibility(View.GONE);
            mCleanUpTimePrefixTv.setVisibility(View.GONE);
        }

    }

    /**
     * Display arrive home time for back home.
     */
    private void displayArriveHome() {
        if (mLatestRunStatus == null || mCurrentDevice.getiDeviceStatusFeature().isOffline()) {
            mGetHomeTimeTv.setVisibility(View.GONE);
            mGetHomeTimePrefixTv.setVisibility(View.GONE);
            return;
        }

        if (mLatestRunStatus.isCleanBeforeHomeEnable()) {
            mGetHomeTimeTv.setVisibility(View.VISIBLE);
            mGetHomeTimePrefixTv.setVisibility(View.VISIBLE);
            Calendar calendar = Calendar.getInstance();
            int hour = Integer.parseInt(mLatestRunStatus.getTimeToHome().substring(11, 13));
            int minute = Integer.parseInt(mLatestRunStatus.getTimeToHome().substring(14, 16));
            int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
            int dstOffset = calendar.get(Calendar.DST_OFFSET);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.add(Calendar.MILLISECOND, zoneOffset + dstOffset);
            SimpleDateFormat sdf = new SimpleDateFormat("HH':'mm");
            String settingTime = sdf.format(calendar.getTime());
            mGetHomeTimeTv.setText(settingTime);
        } else {
            mGetHomeTimeTv.setVisibility(View.GONE);
            mGetHomeTimePrefixTv.setVisibility(View.GONE);
        }
    }

    private void showLedView(int speed) {
        if (DeviceType.isAirTouchFFAC(mCurrentDevice.getDeviceType())) {
            mDots = 3;
        }
        if (speed > 0) {
            for (int i = 0; i < mLedTotalPoints; i++) {
                if (i <= (speed * mDots - 1)) {
                    ledCheckBox.get(i).setChecked(true);
                } else {
                    ledCheckBox.get(i).setChecked(false);
                }
            }
        } else if (speed == 0) {
            for (int i = 0; i < mLedTotalPoints; i++) {
                ledCheckBox.get(i).setChecked(false);
                mAirTouchLedView.hideLedfanPosition();
            }
        }
        showLedfan(speed);
        ledSetting.setLastSettingSpeed(speed);
        ledSetting.setLastSettingLed(speed * mDots);
    }

    public void showLedfan(int speed) {
        if (speed > 0) {
            mAirTouchLedView.showLedfanPosition(speed * mDots - 1);
        }
    }

    public Bundle getmLatestRunStatus() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(DeviceControlActivity.ARG_DEVICE_RUNSTATUS, mLatestRunStatus);
        return bundle;
    }
}
