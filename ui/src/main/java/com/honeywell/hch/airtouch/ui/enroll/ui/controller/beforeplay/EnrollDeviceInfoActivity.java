package com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap.ApActivateDeviceWifiActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair.MadAirEnrollTurnOnActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.smartlink.SmartLinkActivateDeviceWifiActivity;

/**
 * Created by Vincent on 9/10/16.
 */
public class EnrollDeviceInfoActivity extends EnrollBaseActivity {
    private String TAG = "EnrollDeviceInfoActivity";
    private TextView mTitleTv;
    private ImageView mDeviceInfoIv;
    private TextView mProductNameTv;
    private TextView mDeviecTypeTv;
    private TextView mModelTv;
    private EnrollScanEntity mEnrollScanEntity;
    private LinearLayout mEnrollModelLl;

    private final String wifi2Hz = "2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_before_play_device_info);
        initStatusBar();
        initView();
        initData();
        initDragDownManager(R.id.root_view_id);
    }

    private void initView() {
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mTitleTv.setText(R.string.enroll_device_info);
        mProductNameTv = (TextView) findViewById(R.id.enroll_device_product_name_tv);
        mDeviecTypeTv = (TextView) findViewById(R.id.enroll_device_type_tv);
        mModelTv = (TextView) findViewById(R.id.enroll_device_model_tv);
        mDeviceInfoIv = (ImageView) findViewById(R.id.enroll_device_iv);
        mEnrollModelLl = (LinearLayout) findViewById(R.id.enroll_device_model_ll);
    }

    private void initData() {
        mEnrollScanEntity = EnrollScanEntity.getEntityInstance();
        mProductNameTv.setText(getString(mEnrollScanEntity.getDeviceName()));
        mDeviecTypeTv.setText(mEnrollScanEntity.getDeviceTypeStr());
        //手动选择不显示model
        if (mEnrollScanEntity.getmEnrollType() == null) {
            mEnrollModelLl.setVisibility(View.GONE);
        } else {
            mModelTv.setText(mEnrollScanEntity.getmModel());
        }
        mDeviceInfoIv.setImageResource(mEnrollScanEntity.getEnrollChoiceDeivceImage());
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_device_btn) {

            if (DeviceType.isMadAir(mEnrollScanEntity.getmDeviceType())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    startIntent(MadAirEnrollTurnOnActivity.class);
                } else {
                    MessageBox.createSimpleDialog(EnrollDeviceInfoActivity.this, "",
                            getString(R.string.mad_air_enroll_not_support), getString(R.string.ok), quitEnroll);
                }
            } else if (!NetWorkUtil.isWifiAvailable(this) || mEnrollScanEntity.getmEnrollType() == null
                    || EnrollScanEntity.AP_MODE.equals((mEnrollScanEntity.getmEnrollType())[0])) {
                startIntent(ApActivateDeviceWifiActivity.class);
            } else {
                String frequency = NetWorkUtil.getNetWorkFrequency(this);
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "wifiFrequency: " + frequency);
                if (wifi2Hz.equals(frequency.substring(0, 1))) {
                    startIntent(SmartLinkActivateDeviceWifiActivity.class);
                } else {
                    startIntent(ApActivateDeviceWifiActivity.class);
                }
            }
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }


}
