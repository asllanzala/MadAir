package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.library.util.WifiUtil;
import com.honeywell.hch.airtouch.plateform.ap.WAPIRouter;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.plateform.ap.PasswordUtil;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.ap.ApConnectDeviceToInternetPresenter;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.adapter.HomeSpinnerAdapter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollResultActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay.EnrollSelectedLocationActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetPresenter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.IConnectDeviceToInternetView;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.RetryCheckDeviceIsOnlineActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;


public class ApConnectDeviceToInternetActivity extends EnrollBaseActivity implements IConnectDeviceToInternetView {

    private static final String TAG = "AirTouchEnrollWifiPassword";

    private EnrollLoadingButton mNextButton;

    private TextView mOtherWifiTextView;

    private LinearLayout mSelectedWifiPasswordTextView;

    private LinearLayout mOtherWifiLinearLayout;
    private LinearLayout mOtherWifiPasswordLinearLayout;
    private HPlusEditText mSSIDEditText;

    private HPlusEditText mNetworkEditText;
    private HPlusEditText mPasswordEditText;

    protected String mUserPassword;
    protected String mUserSSID;
    private WAPIRouter mWAPIRouter;
    private DropEditText mDropEditText;
    private HomeSpinnerAdapter<DropTextModel> homeSpinnerTypeAdapter;

    private Boolean isLoading = false;
    private final int INTERVAL = 1000;
    private long mClickTime = 0;

    protected IConnectDeviceToInternetPresenter mPresenter;

    private RelativeLayout mConnectingCenterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollwifipassword);
        initStatusBar();
        setupUI(findViewById(R.id.root_view_id));
        initView();
        initEditText();
        initData();

        if (mWAPIRouter != null) {
            if (mWAPIRouter.getSSID().equals(getString(R.string.enroll_other))) {
                mDropEditText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        securityData();
                    }
                }, 300);
            }
        }
        mPresenter = new ApConnectDeviceToInternetPresenter(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isLoading) {
                backIntent();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (!mDropEditText.isTouchInThisDropEditText(event.getX(), event.getY())) {
            mDropEditText.closeDropPopWindow();
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void sendDeviceWifiInfoError() {
        enableComponent();
        Intent intent = new Intent();
        intent.setClass(this, ApSendWifiInfoErrorActivity.class);
        gotoActivityWithIntent(intent, false);
    }

    @Override
    public void deviceIsNotOnline() {
        enableComponent();
        showRetryActionWithReconnectWifi(getString(R.string.device_not_connect_phone));
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
        EnrollScanEntity.getEntityInstance().setIsApMode(true);
    }

    private void startConnect() throws Exception {
        if (mOtherWifiLinearLayout.getVisibility() == View.VISIBLE) {
            if (!isSSIDEmpty()) {
                mWAPIRouter.setSSID(mUserSSID);
                mWAPIRouter.setSecurity(mDropEditText.getText());
            } else {
                return;
            }
        }
        mWAPIRouter.setPassword(PasswordUtil.encryptString(mUserPassword, DIYInstallationState.getWAPIKeyResponse()));

        UmengUtil.onEvent(this,UmengUtil.INPUT_WIFI_PASSWORD_SSID_STR_ID,mSSIDEditText.getEditorText());
        mPresenter.connectDeviceToInternet();
    }


    OnClickListener nextOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            clickNextBtn();
        }

    };

    private void clickNextBtn() {
        try {

            if (!WifiUtil.getCurrentWifiSSID(this).contains(EnrollScanEntity.getEntityInstance().getDevicePrefixWifiName())) {
                //手机没有和设备wifi连接
                showRetryActionWithReconnectWifi(getString(R.string.device_not_connect_phone));
            } else if ((!isPasswordEmpty() || isSSIDSecurityOpen()) && canClcikable()) {
                isLoading = true;
                disableComponent();

                setViewGetFocus(mNextButton.getmRootRl());
                startConnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
            MessageBox.createSimpleDialog(ApConnectDeviceToInternetActivity.this, null,
                    getResources().getString(R.string.enroll_error), null, null);
        }
    }

    private void initView() {
        mOtherWifiTextView = (TextView) findViewById(R.id.other_wifi_title);
        mSSIDEditText = (HPlusEditText) findViewById(R.id.ssidEt);
        mSSIDEditText.setEditorHint(getString(R.string.enroll_hint_ssid));
        mSSIDEditText.setEditTextImageViewVisible(View.GONE);
        mSSIDEditText.getEditText().addTextChangedListener(mEditTextWatch);
        mSSIDEditText.getEditText().setOnFocusChangeListener(onSSIDFocusChanged);
        mNextButton = (EnrollLoadingButton) findViewById(R.id.nextBtn_id);
        mNextButton.getmRootRl().setOnClickListener(nextOnClick);
        mNextButton.initLoadingText(getString(R.string.enroll_next), getString(R.string.enroll_connecting));
        mNextButton.setButtonStatus(false, false);

        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;

        initTitleView(true, getString(R.string.enroll_title_input_password), totalStep, EnrollConstants.STEP_TWO, getString(R.string.connect_device_to_device_title_content),false);
        mSelectedWifiPasswordTextView = (LinearLayout) findViewById(R.id.selectedWifiLl);
        mNetworkEditText = (HPlusEditText) findViewById(R.id.networkEt);
        mNetworkEditText.showImageButton(false);
        mNetworkEditText.setEditorHint(null);


        mPasswordEditText = (HPlusEditText) findViewById(R.id.passwordEt);
        mPasswordEditText.setEditTextGravity(Gravity.LEFT | Gravity.CENTER);
        mPasswordEditText.getEditText().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mOtherWifiLinearLayout = (LinearLayout) findViewById(R.id.otherWifiLl);
        mOtherWifiPasswordLinearLayout = (LinearLayout) findViewById(R.id.otherWifiPasswordLl);

        mConnectingCenterView = (RelativeLayout) findViewById(R.id.connecting_msg_tip);
    }

    private void initData() {
        mBackFrameLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable back icon
                if (isLoading) return;
                backIntent();
            }
        });

        mPasswordEditText.setPasswordImage(true);
        mPasswordEditText.setTextChangedListener(mEditTextWatch);

        mWAPIRouter = DIYInstallationState.getWAPIRouter();
        if (mWAPIRouter != null) {
            if (mWAPIRouter.getSSID().equals(getString(R.string.enroll_other))) {
                mOtherWifiTextView.setVisibility(View.VISIBLE);
                //mContentTv.setVisibility(View.GONE);
                mSelectedWifiPasswordTextView.setVisibility(View.GONE);
                mOtherWifiLinearLayout.setVisibility(View.VISIBLE);
            } else {
                //mContentTv.setVisibility(View.VISIBLE);
                mSelectedWifiPasswordTextView.setVisibility(View.VISIBLE);
                mOtherWifiTextView.setVisibility(View.INVISIBLE);
                mOtherWifiLinearLayout.setVisibility(View.GONE);
            }

            //mContentTv.setText(getString(R.string.enroll_password_title1, mWAPIRouter.getSSID()));
            mNetworkEditText.setEditorText(mWAPIRouter.getSSID());
            //set can not edit
            mNetworkEditText.getEditText().setEnabled(false);
            mNetworkEditText.getEditText().setFocusable(false);
            mNetworkEditText.getEditText().setFocusableInTouchMode(false);
        }


        if (DIYInstallationState.getErrorCode() == 0x10)
            MessageBox.createSimpleDialog(ApConnectDeviceToInternetActivity.this, null,
                    getString(R.string.error_code_10), null, null);
        if (DIYInstallationState.getErrorCode() == 0x20)
            MessageBox.createSimpleDialog(ApConnectDeviceToInternetActivity.this, null,
                    getString(R.string.error_code_20), null, null);
        if (DIYInstallationState.getErrorCode() == 0x30)
            MessageBox.createSimpleDialog(ApConnectDeviceToInternetActivity.this, null,
                    getString(R.string.error_code_30), null, null);
//        initDragDownManager(R.id.wifi_pass_root_rl);
        disableNextButton();
    }

    private void disableNextButton() {
        if (mNextButton.getmRootRl().isClickable()) {
            mNextButton.setButtonStatus(false, false);
        }
    }

    private void enableNextButton() {
        mNextButton.setButtonStatus(true, false);
    }

    private void disableComponent() {
        mNextButton.setButtonStatus(false, true);
        mConnectingCenterView.setVisibility(View.VISIBLE);
        mPasswordEditText.getEditText().setEnabled(false);
        mPasswordEditText.getEditText().setFocusableInTouchMode(false);
        mPasswordEditText.getImageView().setEnabled(false);
    }

    private void enableComponent() {
        mNextButton.setButtonStatus(true, false);
        mConnectingCenterView.setVisibility(View.GONE);
        //enable other text view
        setTextState(true);
        mPasswordEditText.getEditText().setEnabled(true);
        mPasswordEditText.getEditText().setFocusableInTouchMode(true);
        mPasswordEditText.getImageView().setEnabled(true);
    }

    private void setTextState(boolean flag) {
        mSSIDEditText.getEditText().setEnabled(flag);
        mPasswordEditText.getEditText().setEnabled(flag);
        mPasswordEditText.getImageView().setEnabled(flag);

        mDropEditText.setViewEnable(flag);

        mSSIDEditText.getEditText().setFocusable(flag);
        mSSIDEditText.getEditText().setFocusableInTouchMode(flag);
        mPasswordEditText.getEditText().setFocusable(flag);
        mPasswordEditText.getEditText().setFocusableInTouchMode(flag);

    }


    private void securityData() {
        String[] securityTypes = WAPIRouter.RouterSecurity.getStringArray(mContext);

        homeSpinnerTypeAdapter = new HomeSpinnerAdapter<>(this, changeStringToDropTextModel(securityTypes));
        mDropEditText.setAdapter(homeSpinnerTypeAdapter, mDropEditText.getWidth());
        if (securityTypes.length == 0) {
            mDropEditText.getmDropImage().setVisibility(View.GONE);
        } else {
            mDropEditText.getmDropImage().setVisibility(View.VISIBLE);
        }
    }

    private DropTextModel[] changeStringToDropTextModel(String[] securityTypes) {
        DropTextModel[] dropTextModels = new DropTextModel[securityTypes.length];

        for (int i = 0; i < securityTypes.length; i++) {
            DropTextModel dropTextModel = new DropTextModel(securityTypes[i]);
            dropTextModels[i] = dropTextModel;
        }
        return dropTextModels;
    }


    private TextWatcher mEditTextWatch = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            decideNextButtonStatus();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    View.OnFocusChangeListener onSSIDFocusChanged = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && !isSSIDEmpty()) {
                mSSIDEditText.setEditorHint("");
            } else {
                mSSIDEditText.setEditorHint(getString(R.string.enroll_hint_ssid));
            }
        }
    };

    View.OnFocusChangeListener onPassFocusChanged = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus && !isPasswordEmpty()) {
                mPasswordEditText.setEditorHint("");
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "hasFocus is called!");
            } else {
                mPasswordEditText.setEditorHint(getString(R.string.smartlink_password_str));
            }
        }
    };

    private boolean isSSIDEmpty() {
        mUserSSID = mSSIDEditText.getEditorText();
        return mOtherWifiLinearLayout.isShown() && mUserSSID.isEmpty();

    }

    private boolean isPasswordEmpty() {
        mUserPassword = mPasswordEditText.getEditorText();
        return StringUtil.isEmpty(mUserPassword);
    }

    private void initEditText() {
        mDropEditText = (DropEditText) findViewById(R.id.enroll_dropview_edit);
        mDropEditText.getmEditText().setFocusable(false);
        mDropEditText.getmEditText().setEnabled(false);
        mDropEditText.getmEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                dropEditTextChange();
                decideNextButtonStatus();
            }
        });
        mDropEditText.getmEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
            }
        });
    }

    private void dropEditTextChange() {
        String value = mDropEditText.getText();
        if (value.equals(getString(R.string.wapi_router_security_open)) || value.isEmpty()) {
            mOtherWifiPasswordLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            mOtherWifiPasswordLinearLayout.setVisibility(View.VISIBLE);

            mPasswordEditText = (HPlusEditText) findViewById(R.id.securityPasswordEt);
            mPasswordEditText.setEditTextImageViewVisible(View.VISIBLE);
            mPasswordEditText.getEditText().setEnabled(true);
            mPasswordEditText.getImageView().setEnabled(true);
            mPasswordEditText.getEditText().setOnFocusChangeListener(onPassFocusChanged);
            mPasswordEditText.setTextChangedListener(mEditTextWatch);

            mPasswordEditText.setPasswordImage(true);

        }
    }

    private boolean isSSIDSecurityOpen() {
        return mOtherWifiLinearLayout.isShown() && !mOtherWifiPasswordLinearLayout.isShown();
    }

    private void decideNextButtonStatus() {
        if (!isPasswordEmpty() && !isSSIDEmpty()) {
            enableNextButton();
        } else if (isSSIDSecurityOpen() && !isSSIDEmpty()) {
            enableNextButton();
        } else {
            disableNextButton();
        }
    }


    private boolean canClcikable() {
        if (System.currentTimeMillis() - mClickTime > INTERVAL) {
            mClickTime = System.currentTimeMillis();
            return true;
        }
        mClickTime = System.currentTimeMillis();
        return false;
    }

}