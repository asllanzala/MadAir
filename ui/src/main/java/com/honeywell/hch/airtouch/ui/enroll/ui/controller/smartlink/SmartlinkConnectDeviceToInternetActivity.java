package com.honeywell.hch.airtouch.ui.enroll.ui.controller.smartlink;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.smartlink.SmarlinkConnectDeviceToInternetPersenter;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollResultActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollSelectedLocationActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetView;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.RetryCheckDeviceIsOnlineActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;


/**
 * Created by wuyuan on 11/23/15.
 */
public class SmartlinkConnectDeviceToInternetActivity extends EnrollBaseActivity implements IConnectDeviceToInternetView {

    private HPlusEditText mWifiPassword;

    private EnrollLoadingButton mNextButton;

    private String mSsid;

    private String mUserPassword;

    private HPlusEditText mSsidTextView;

    private boolean isResume = false;

    private int mLocalIp;


    private boolean mWholeFindingProcessIsRunning = false;

    private TextView mTitleTextView;

    private SmarlinkConnectDeviceToInternetPersenter mPersenter;

    private RelativeLayout mConnectingCenterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartlink_inputpassword);

        initStatusBar();
        setupUI(findViewById(R.id.root_view_id));

        initView();
        initDragDownManager(R.id.root_view_id);

        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;

        initTitleView(true, getString(R.string.enroll_title_input_password), totalStep, EnrollConstants.STEP_TWO, getString(R.string.connect_device_to_internet_tipmsg),false);
    }


    @Override
    public void sendDeviceWifiInfoError() {
        enableComponent();
        Intent intent = new Intent();
        intent.setClass(this, SmartlinkSendWifiInfoFailedActivity.class);
        gotoActivityWithIntent(intent, false);
    }

    @Override
    public void deviceIsNotOnline() {
        enableComponent();

        showRetryAction(getString(R.string.device_not_connect_phone));
    }

    @Override
    public void deviceHasConnectToInternet() {
        enableComponent();
        Intent intent = new Intent();
        intent.setClass(this, EnrollSelectedLocationActivity.class);
        gotoActivityWithIntent(intent, false);
    }

    @Override
    public void updateDeviceWifiSuccess() {
        enableComponent();
        Intent intent = new Intent();
        intent.putExtra(EnrollConstants.ENROLL_RESULT, EnrollConstants.ENROLL_RESULT_UPDATE_WIFI_SUCCESS);
        intent.setClass(this, EnrollResultActivity.class);
        gotoActivityWithIntent(intent, false);
    }


    @Override
    public void phoneNotConnectWifi() {
        enableComponent();
        Intent intent = new Intent();
        intent.setClass(this, RetryCheckDeviceIsOnlineActivity.class);
        gotoActivityWithIntent(intent, false);
        EnrollScanEntity.getEntityInstance().setIsApMode(false);
    }


    @Override
    public void onBackIconAction() {
        if (!mWholeFindingProcessIsRunning) {
            goBackActivity();
        }
    }

    private void initView() {
        mWifiPassword = (HPlusEditText) findViewById(com.honeywell.hch.airtouch.ui.R.id.ssid_password_id);

        mWifiPassword.getEditText().setTextColor(getResources().getColor(R.color.black));
        mWifiPassword.setEditorHint(getString(R.string.smartlink_password_str));
        mWifiPassword.setEditTextGravity(Gravity.LEFT | Gravity.CENTER);
        mWifiPassword.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mWifiPassword.getEditText().addTextChangedListener(mEditTextWatch2);
        mWifiPassword.setPasswordImage(true);

        mWifiPassword.getEditText().setOnFocusChangeListener(onPassFocusChanged);

        mNextButton = (EnrollLoadingButton) findViewById(R.id.nextBtn_id);
        mNextButton.initLoadingText(getString(R.string.enroll_next), getString(R.string.enroll_connecting));
        mNextButton.setButtonStatus(false, false);
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.enroll_title_input_password);

        disableNextButton();
        mNextButton.getmRootRl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isHasWifi = NetWorkUtil.isWifiAvailable(SmartlinkConnectDeviceToInternetActivity.this);

                if (isHasWifi) {
                    startFindingProcess();
                } else {
                    goToSettingDialog(R.string.enroll_wifi_unavaiable);
                }

            }
        });
        mSsidTextView = (HPlusEditText) findViewById(R.id.wifi_ssid_id);
        mSsidTextView.setEditorHint(getString(R.string.enroll_hint_ssid));
        mSsidTextView.showImageButton(false);
        mConnectingCenterView = (RelativeLayout) findViewById(R.id.connecting_msg_tip);
    }

    private void startFindingProcess() {
        mWholeFindingProcessIsRunning = true;
        disableComponent();
        //show animation
        showConnecting();

        if (mPersenter == null) {
            mPersenter = new SmarlinkConnectDeviceToInternetPersenter(this, mSsid, mUserPassword);
        }
        mPersenter.connectDeviceToInternet();

        UmengUtil.onEvent(this,UmengUtil.INPUT_WIFI_PASSWORD_SSID_STR_ID,mSsidTextView.getEditorText());

    }


    private TextWatcher mEditTextWatch2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtil.isEmpty(mSsid) && mLocalIp != 0) {
                decideNextButtonStatus();
            }
        }
    };


    private void decideNextButtonStatus() {
        if (!isPasswordEmpty() && !isSSIDEmpty()) {
            enableNextButton();
        } else {
            disableNextButton();
        }
    }

    private boolean isSSIDEmpty() {
        return StringUtil.isEmpty(mSsid);
    }

    private boolean isPasswordEmpty() {
        mUserPassword = mWifiPassword.getEditorText();
        return StringUtil.isEmpty(mUserPassword);
    }

    View.OnFocusChangeListener onPassFocusChanged = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && !isPasswordEmpty()) {
                mWifiPassword.setEditorHint("");
            } else {
                mWifiPassword.setEditorHint(getString(R.string.smartlink_password_str));
            }
        }
    };

    private void disableNextButton() {
        mNextButton.setButtonStatus(false, false);
    }

    private void enableNextButton() {
        mNextButton.setButtonStatus(true, false);
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkWifiStatusAndUpdate();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isResume = false;
        hideConnecting();

    }

    @Override
    protected void dealNetworkConnect() {
        if (isResume) {
            String mNewSid = NetWorkUtil.updateWifiInfo(this);
            if (!StringUtil.isEmpty(mNewSid) && !mNewSid.equals(mSsid)) {
                updateSsidAndLocalIp(mNewSid);
            }

            disMissDialog();
        }

    }

    @Override
    protected void dealNoNetwork() {
        if (isResume && (mAlertDialog == null || !mAlertDialog.isShowing())) {
            goToSettingDialog(R.string.enroll_wifi_unavaiable);
        }
    }

    private void checkWifiStatusAndUpdate() {
        if (!mWholeFindingProcessIsRunning) {
            String mNewSid = NetWorkUtil.updateWifiInfo(this);
            if (!StringUtil.isEmpty(mNewSid) && !mNewSid.equals(mSsid)) {
                updateSsidAndLocalIp(mNewSid);
            } else if (StringUtil.isEmpty(mNewSid)) {
                goToSettingDialog(R.string.enroll_wifi_unavaiable);
            }
        }

    }


    private void updateSsidAndLocalIp(String mNewSid) {

        disMissDialog();
        mSsid = mNewSid;
        mSsidTextView.setEditorText(mNewSid);
        //set can not edit
        mSsidTextView.getEditText().setEnabled(false);
        mSsidTextView.setEnabled(false);

        mWifiPassword.setEditorText(null);
        mWifiPassword.setEditorHint(getString(R.string.smartlink_password_str));

        mLocalIp = NetWorkUtil.getNetworkIp(this);
        if (mLocalIp == 0) {
            goToSettingDialog(R.string.enroll_wifi_unavaiable);
        }
    }


    private void showConnecting() {
        //disable other text view
        setTextState(false);
    }

    private void hideConnecting() {
        //enable other text view
        setTextState(true);
    }

    public void setTextState(boolean flag) {
        mSsidTextView.getEditText().setEnabled(flag);
        mWifiPassword.getEditText().setEnabled(flag);
        mWifiPassword.getImageView().setEnabled(flag);

        mSsidTextView.getEditText().setFocusable(flag);
        mSsidTextView.getEditText().setFocusableInTouchMode(flag);
        mWifiPassword.getEditText().setFocusable(flag);
        mWifiPassword.getEditText().setFocusableInTouchMode(flag);
    }

    /**
     * when finding the device,we should disable all the component
     */
    private void disableComponent() {
        mNextButton.setButtonStatus(false, true);
        mConnectingCenterView.setVisibility(View.VISIBLE);
        mWifiPassword.getEditText().setEnabled(false);
        mWifiPassword.getEditText().setFocusableInTouchMode(false);
        mWifiPassword.getImageView().setEnabled(false);
    }

    public void enableComponent() {

        //the finding process is done
        mWholeFindingProcessIsRunning = false;

        mNextButton.setButtonStatus(true, false);
        mConnectingCenterView.setVisibility(View.GONE);

        mWifiPassword.getEditText().setEnabled(true);
        mWifiPassword.getEditText().setFocusableInTouchMode(true);
        mWifiPassword.getImageView().setEnabled(true);

        hideConnecting();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK && mWholeFindingProcessIsRunning) {
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && !mWholeFindingProcessIsRunning) {
            goBackActivity();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void goBackActivity() {
        //disable back icon
        backIntent();
    }

    @Override
    protected void initTitleBackLayout(boolean isVisible) {
        super.initTitleBackLayout(isVisible);
        if (isVisible) {
            mBackFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mWholeFindingProcessIsRunning) {
                        goBackActivity();
                    }
                }
            });
        }
    }

}
