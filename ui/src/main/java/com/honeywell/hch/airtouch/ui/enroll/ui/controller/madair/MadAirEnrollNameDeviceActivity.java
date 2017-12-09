package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager.MadAirBleDataManager;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Jin on 11/23/16.
 */
public class MadAirEnrollNameDeviceActivity extends EnrollBaseActivity {

    private HPlusEditText mDeviceNameEditText;
    private Button mConfirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_mad_air_name_device);
        setupUI(findViewById(R.id.root_view_id));
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);
    }

    private void initView() {
        super.initTitleView(false, getString(R.string.mad_air_enroll_name_device_title), EnrollConstants.TOTAL_THREE_STEP,
                EnrollConstants.STEP_THREE, getString(R.string.mad_air_enroll_name_device_desc), false);

        mConfirmButton = (Button) findViewById(R.id.mad_air_confirm_btn);
        mDeviceNameEditText = (HPlusEditText) findViewById(R.id.mad_air_device_name_et);
        mDeviceNameEditText.setEditTextImageViewVisible(View.GONE);
        mDeviceNameEditText.setEditorHint(getResources().getString(R.string.mad_air_enroll_name_device_hint));
        mDeviceNameEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maxCharacterFilter(mDeviceNameEditText);
                addDeviceFilter(mDeviceNameEditText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnClickable();
            }
        });

        setBtnClickable();

    }

    public void doClick(View v) {
        if (v.getId() == R.id.mad_air_confirm_btn) {

            if (isSameDeviceName(mDeviceNameEditText.getEditorText())) {
                mDropDownAnimationManager.showDragDownLayout(getString(R.string.mad_air_enroll_same_name), true);
                return;
            }

            // get firmware version
            MadAirBleDataManager.getInstance().readDeviceInfoCharacteristic
                    (EnrollScanEntity.getEntityInstance().getmMacID());

            // save device model into SharedPreference
            MadAirDeviceModelSharedPreference.saveStatus(EnrollScanEntity.getEntityInstance()
                    .getmMacID(), MadAirDeviceStatus.CONNECT);
            MadAirDeviceModelSharedPreference.saveDeviceName(EnrollScanEntity.getEntityInstance()
                    .getmMacID(), mDeviceNameEditText.getEditorText());

            startIntent(MadAirEnrollFinishActivity.class);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setBtnClickable() {
        boolean isEnable = !StringUtil.isEmpty(mDeviceNameEditText.getEditorText());

        mConfirmButton.setEnabled(isEnable);
        mConfirmButton.setClickable(isEnable);
    }

    private void maxCharacterFilter(HPlusEditText et) {
        try {
            String strTransfer = new String(et.getEditorText().getBytes("GBK"), "ISO8859_1");
            if (strTransfer.length() > HPlusConstants.MAX_HOME_CHAR_EDITTEXT) {

                for (int i = 0; i <= et.getEditorText().length(); i++) {
                    String s = et.getEditorText().substring(0, i);
                    String st = new String(s.getBytes("GBK"), "ISO8859_1");
                    if (st.length() == HPlusConstants.MAX_HOME_CHAR_EDITTEXT) {
                        String str = et.getEditorText().substring(0, i);
                        et.getEditText().setText(str);
                        et.getEditText().setSelection(str.length());
                        return;
                    }
                    if (st.length() > HPlusConstants.MAX_HOME_CHAR_EDITTEXT) {
                        String str = et.getEditorText().substring(0, i - 1);
                        et.getEditText().setText(str);
                        et.getEditText().setSelection(str.length());
                        return;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void addDeviceFilter(HPlusEditText et) {
        String editable = et.getEditorText();
        String str = StringUtil.stringFilterAddHome(editable); //filter special character

        if (!editable.equals(str)) {
            et.getEditText().setText(str);
        }
        et.getEditText().setSelection(et.getEditText().length());
    }

    private boolean isSameDeviceName(String name) {
        for (MadAirDeviceModel device : MadAirDeviceModelSharedPreference.getDeviceList()) {
            if (device.getDeviceName().equals(name)) {
                return true;

            }
        }

        return false;
    }

}
