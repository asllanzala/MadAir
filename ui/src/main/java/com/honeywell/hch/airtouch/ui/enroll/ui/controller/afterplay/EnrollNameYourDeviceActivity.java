package com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

import java.io.UnsupportedEncodingException;

/**
 * Created by h127856 on 16/10/12.
 */
public class EnrollNameYourDeviceActivity extends EnrollBaseActivity {


    private Button mButton;
    private HPlusEditText mNameDeviceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name_your_devices);
        initStatusBar();
        setupUI(findViewById(R.id.root_view_id));
        initView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            goToBackActivity();
        }

        return false;
    }


    private void initView() {

        super.initTitleView(true, getString(R.string.enroll_title4), EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_THREE, getString(R.string.name_device_msg),false);

        //覆盖父类里的点击事件
        mBackFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBackActivity();
            }
        });

        mButton = (Button) findViewById(R.id.buttom_btn_id);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DIYInstallationState.setDeviceName(mNameDeviceTextView.getEditorText());
                Intent intent = new Intent();
                intent.setClass(EnrollNameYourDeviceActivity.this, EnrollRegiterDeviceActivity.class);
                gotoActivityWithIntent(intent, false);
            }
        });

        mNameDeviceTextView = (HPlusEditText) findViewById(R.id.enroll_device_et);
        mNameDeviceTextView.setEditTextImageViewVisible(View.GONE);
        mNameDeviceTextView.setEditorHint(getResources().getString(R.string.my_device));
        mNameDeviceTextView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maxCharacterFilter(mNameDeviceTextView);
                addDeviceFilter(mNameDeviceTextView);
            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnClickable();
            }
        });

        mNameDeviceTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setBtnClickable();
            }
        });

    }


    private void goToBackActivity(){
        Intent intent = new Intent();
        intent.putExtra(EnrollSelectedLocationActivity.FROM_BACK_ACTION,true);
        intent.setClass(EnrollNameYourDeviceActivity.this, EnrollSelectedLocationActivity.class);
        startActivity(intent);
        onBackIconAction();
    }

    private void setBtnClickable() {
        boolean isEnable = !StringUtil.isEmpty(mNameDeviceTextView.getEditorText().toString());

        mButton.setEnabled(isEnable);
        mButton.setClickable(isEnable);
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


}
