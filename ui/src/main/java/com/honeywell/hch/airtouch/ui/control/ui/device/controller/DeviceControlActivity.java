package com.honeywell.hch.airtouch.ui.control.ui.device.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseFragmentActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIBaseManager;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIManager;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.fragment.AirTouchControlFragment;
import com.honeywell.hch.airtouch.ui.control.ui.device.fragment.AquaTouchFragment;
import com.honeywell.hch.airtouch.ui.control.ui.device.fragment.DeviceControlBaseFragment;
import com.honeywell.hch.airtouch.ui.control.ui.device.fragment.MadAirDetailInfoFragment;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoControlUIManager;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoIndicatorValueManager;

import java.util.ArrayList;

/**
 * The Vertical Fragment for Control and AirTouchFilter
 * Created by Jesse on 22/7/16.
 */

public class DeviceControlActivity extends BaseFragmentActivity {
    public static final String ARG_DEVICE = "device";
    public static final String ARG_LOCATION = "location";
    public static final String ARG_DEVICE_ID = "deviceId";

    public static final String ARG_DEVICE_RUNSTATUS = "device_runstatus";
    private static final String TAG = "DeviceControlActivity";
    private UserLocationData mUserLocation = null; // this location may not be current location
    private HomeDevice mThisHomeDevice;
    private DeviceControlBaseFragment mDeviceControlFragment;
    private int mLocationId = 0;
    private int mDeviceId = 0;

    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();
    public static final String ARG_FROM_TYPE = "arg_from_type";

    private int mFromType;
    private ControlUIBaseManager mControlUIManager;

    private TryDemoIndicatorValueManager mTryDemoIndicatorValueManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control_new);
        initStatusBar();
        initLocationAndDevice();
        if (mThisHomeDevice == null) {
            showMessageBox();
            return;
        }


        mTryDemoIndicatorValueManager = new TryDemoIndicatorValueManager();
        loadDeviceControlFragment();

    }

    private void loadDeviceControlFragment() {
        if (DeviceType.isAirTouchSeries(mThisHomeDevice.getDeviceType())) {
            loadAirTouchFragment();
        } else if (DeviceType.isWaterSeries(mThisHomeDevice.getDeviceType())) {
            loadAquaTouchFragment();
        } else if (DeviceType.isMadAIrSeries(mThisHomeDevice.getDeviceType())) {
            loadMadAirDetailInfoFragment();
        }
        mTryDemoIndicatorValueManager.addRefreshListener(mDeviceControlFragment);
    }

    private void loadMadAirDetailInfoFragment() {
        mDeviceControlFragment = MadAirDetailInfoFragment.newInstance(mThisHomeDevice, this, mFromType);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.device_control_panel, mDeviceControlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadAirTouchFragment() {
        mDeviceControlFragment = AirTouchControlFragment.newInstance(mThisHomeDevice, this, mFromType);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.device_control_panel, mDeviceControlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void loadAquaTouchFragment() {
        mDeviceControlFragment = AquaTouchFragment.newInstance(mThisHomeDevice, this, mFromType);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.device_control_panel, mDeviceControlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initLocationAndDevice() {
        PushMessageModel pushMessageModel = (PushMessageModel) getIntent().getSerializableExtra(PushMessageModel.PUSHPARAMETER);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mLocationId = bundle.getInt(ARG_LOCATION);
            mDeviceId = bundle.getInt(ARG_DEVICE_ID);
            mFromType = getIntent().getIntExtra(ARG_FROM_TYPE, ControlConstant.FROM_NORMAL_CONTROL);
        }
        if (pushMessageModel != null) {
            mLocationId = pushMessageModel.getmLocationId();
            mDeviceId = pushMessageModel.getmDeviceId();
        }
        initControlUIManager();
        mUserLocation = mControlUIManager.getLocationWithId(mLocationId);
        if (mUserLocation == null) {
            showMessageBox();
            return;
        }
        mThisHomeDevice = mControlUIManager.getHomeDeviceByDeviceId(mDeviceId);
    }

    private void initControlUIManager() {
        if (mFromType == ControlConstant.FROM_NORMAL_CONTROL) {
            mControlUIManager = new ControlUIManager();
        } else {
            mControlUIManager = new TryDemoControlUIManager();
        }
    }

    private void showMessageBox() {
        MessageBox.createSimpleDialog(this, "", getString(R.string.device_deleted_already), getString(R.string.get_it), new MessageBox.MyOnClick() {
            @Override
            public void onClick(View v) {
                backIntent();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CloseActivityUtil.exitEnrollClient(mContext);
        //enroll finish 启动刷新广播
        Intent intent = new Intent(HPlusConstants.EXIT_ENROLL_PROCESS);
        sendBroadcast(intent);
    }

    public UserLocationData getmUserLocation() {
        return mUserLocation;
    }


    public ControlUIBaseManager getControlUIManager() {
        return mControlUIManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTryDemoIndicatorValueManager != null) {
            mTryDemoIndicatorValueManager.removeRefreshListener(mDeviceControlFragment);
        }
    }

    public static Boolean hasNullDataInAirtouchDevice(HomeDevice homeDevice) {
        return homeDevice == null || homeDevice.getDeviceInfo() == null
                || ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus() == null;
    }

    public static Boolean hasNullDataInSmartRODevice(WaterDeviceObject homeDevice) {
        return homeDevice == null || homeDevice.getDeviceInfo() == null
                || homeDevice.getAquaTouchRunstatus() == null;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface MyTouchListener {
        public void onTouchEvent(MotionEvent event);
    }

    /**
     * register touch listener for fragment
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * unregister touch listener for fragment
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setQuickActionResult();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setQuickActionResult() {
        Intent intent = new Intent();
        intent.putExtra(ARG_DEVICE, mDeviceId);
        intent.putExtra(DashBoadConstant.ARG_LOCATION_ID, mLocationId);

        //返回alldevice 或者 group
        intent.putExtra(DashBoadConstant.ARG_LAST_RUNSTATUS, mDeviceControlFragment.getmLatestRunStatus());

        //返回quick action
        intent.putExtra(DashBoadConstant.ARG_QUICK_ACTION_DEVICE, mThisHomeDevice.getDeviceInfo());
        setResult(DashBoadConstant.QUICK_ACTION_RESULT_CODE, intent);
        backIntent();
    }


    @Override
    public void dealWithNoNetWork() {
        if (mDeviceControlFragment != null) {
            mDeviceControlFragment.setNoNetWorkView();
        }
    }

    @Override
    public void dealWithNetworkError() {
        if (mDeviceControlFragment != null) {
            mDeviceControlFragment.setNetWorkErrorView();
        }
    }

    @Override
    public void dealNetworkConnected() {
        if (mDeviceControlFragment != null) {
            mDeviceControlFragment.setNetWorkViewGone();
        }
    }


    public int getFromType() {
        return mFromType;
    }
}
