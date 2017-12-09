package com.honeywell.hch.airtouch.ui.help;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.control.manager.model.ControlConstant;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;

/**
 * Created by Vincent on 2/8/16.
 */
public class DeviceControlHelpActivity extends BaseActivity {

    public final static String HELP_PARAMETER = "help_parameter";
    public final static String MAD_AIR_HELP_PARAMETER = "mad_air_help_parameter";
    private TextView mTitleTextView;
    private TextView mProductNameTv;
    private TextView mFirmVersionTv;
    private DeviceInfo mDeviceInfo;
    private int mFromType;
    private MadAirDeviceModel mMadAirDeviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control_help);
        initStatusBar();
        initView();
        initData();
    }

    private void initView() {
        mDeviceInfo = (DeviceInfo) getIntent().getSerializableExtra(HELP_PARAMETER);
        mFromType = getIntent().getIntExtra(DeviceControlActivity.ARG_FROM_TYPE, ControlConstant.FROM_NORMAL_CONTROL);
        mMadAirDeviceModel = (MadAirDeviceModel) getIntent().getSerializableExtra(MAD_AIR_HELP_PARAMETER);
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mProductNameTv = (TextView) findViewById(R.id.device_firmware_name_tv);
        mFirmVersionTv = (TextView) findViewById(R.id.device_firmware_version_tv);
    }

    private void initData() {
        mTitleTextView.setText(R.string.device_control_help);
        if (mDeviceInfo != null) {
            mProductNameTv.setText(DeviceType.getEnrollFeatureByDeviceType(mDeviceInfo.getDeviceType()).getEnrollDeviceName());
            if (mFromType == ControlConstant.FROM_NORMAL_CONTROL) {
                mFirmVersionTv.setText(mDeviceInfo.getFirmwareVersion());
            } else {
                mFirmVersionTv.setText(getString(R.string.try_demo_version_name));
            }
        } else if (mMadAirDeviceModel != null) {
            mProductNameTv.setText(getString(R.string.mad_air_enroll_name_device));
            mFirmVersionTv.setText(mMadAirDeviceModel.getFirmwareVersion());
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.device_control_introduce_rl) {
            startIntent(ManualActivity.MANUAL_PARAMETER, ManualActivity.MANUALTYPE, HPlusConstants.INTRODUCTION_TYPE, ManualActivity.class);
        } else if (v.getId() == R.id.device_control_user_manual_rl) {
            startIntent(ManualActivity.MANUAL_PARAMETER, ManualActivity.MANUALTYPE, HPlusConstants.USERMANUAL_TYPE, ManualActivity.class);
        }
    }

    private void startIntent(String deviceKey, String typeKey, String type, Class mClass) {
        Intent intent = new Intent((Activity) mContext, mClass);
        intent.putExtra(deviceKey, mDeviceInfo);
        intent.putExtra(typeKey, type);
        intent.putExtra(ManualActivity.MADAIR_PARAMETER, mMadAirDeviceModel);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }
}
