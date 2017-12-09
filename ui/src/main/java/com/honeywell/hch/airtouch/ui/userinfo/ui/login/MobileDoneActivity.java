package com.honeywell.hch.airtouch.ui.userinfo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.AndroidBug5497Workaround;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.UserAccountBaseActivity;

/**
 * Created by H127856 .
 */
public class MobileDoneActivity extends UserAccountBaseActivity implements TextView.OnEditorActionListener {
    private static final int MAX_NICK_CODE_LENGTH = 9;
    private HPlusEditText mNickNameEditTextView;
    private HPlusEditText mConfirmEditTextView;
    private RelativeLayout mConfirmLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_done);
        AndroidBug5497Workaround.assistActivity(this);

        initView();
        initDragDownManager(R.id.root_view_id);
        setupUI(findViewById(R.id.root_view_id));
        setListenerToRootView(R.id.root_view_id,mBottomBtn);

        mCountryCode = getIntent().getStringExtra(MobileGetSmsActivity.COUNTRY_CODE);
        mPhoneNumber = getIntent().getStringExtra(MobileGetSmsActivity.PHONE_NUMBER);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtonStatus();
    }

    @Override
    protected void initView() {
        super.initView();
        mNickNameEditTextView = (HPlusEditText) findViewById(R.id.nick_name_edittext);
        mNickNameEditTextView.getEditText().setOnEditorActionListener(this);
        mNickNameEditTextView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_NICK_CODE_LENGTH)});

        mPasswordTextView = (HPlusEditText) findViewById(R.id.password_edittext);
        mPasswordTextView.getEditText().setOnEditorActionListener(this);
        mPasswordTextView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PASSWORD_LENGTH)});

        mConfirmEditTextView = (HPlusEditText) findViewById(R.id.comfirm_password_edittext);
        mConfirmEditTextView.getEditText().setOnEditorActionListener(this);
        mConfirmEditTextView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PASSWORD_LENGTH)});

        mConfirmLayout = (RelativeLayout) findViewById(R.id.comfirm_layout);
        initNicknameEditText();
        initPasswordEditText();
        initConfirmEditText();
        mActionBarTitleText.setText(getString(R.string.profile_title));
        mBottomBtn.setText(getString(R.string.register));
    }


    private void initPasswordEditText() {
        mPasswordTextView.setPasswordImage(false);
//        mPasswordTextView.setEditTextImageViewVisible(View.GONE);
        mPasswordTextView.setEditorHint(getString(R.string.user_account_password_hint));
        mPasswordTextView.setTextChangedListener(mPasswordTextWatch);
        mPasswordTextView.setChangeEyeStatusInterface(new HPlusEditText.IChangeEyeStatus() {
            @Override
            public void afterChangeEyeStatus(boolean isEyeOn) {
                if (isEyeOn) {
                    mConfirmLayout.setVisibility(View.VISIBLE);
                }
                int visible = isEyeOn ? View.GONE : View.VISIBLE;
                mConfirmLayout.setVisibility(visible);
                setButtonStatus();
            }
        });
    }

    private void initConfirmEditText() {
        mConfirmEditTextView.setPasswordCleanImage();
        mConfirmEditTextView.setEditorHint(getString(R.string.confirm_ps_hint));
        mConfirmEditTextView.setTextChangedListener(mConfirmTextWatcher);
    }

    private void initNicknameEditText() {
        mNickNameEditTextView.setCleanImage();
        mNickNameEditTextView.setEditorHint(getString(R.string.nickname_hint));
        mNickNameEditTextView.setTextChangedListener(mNickNameTextWatcher);
    }

    private TextWatcher mConfirmTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.specialCharacterAndChineseFilter(mConfirmEditTextView.getEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {
            setEditImageVisible(mConfirmEditTextView);
            setButtonStatus();
        }
    };

    private TextWatcher mNickNameTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.specialCharactFilter(mNickNameEditTextView.getEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {
            setEditImageVisible(mNickNameEditTextView);
            setButtonStatus();
        }
    };

    @Override
    public void setButtonStatus() {
        if ((mConfirmLayout.getVisibility() == View.VISIBLE
                && mUserAccountRelatedUIManager.validPassword(mPasswordTextView.getEditorText())
                && mUserAccountRelatedUIManager.validPassword(mConfirmEditTextView.getEditorText())
                && !StringUtil.isEmpty(mNickNameEditTextView.getEditorText()))
                || (mConfirmLayout.getVisibility() != View.VISIBLE
                && mUserAccountRelatedUIManager.validPassword(mPasswordTextView.getEditorText())
                && !StringUtil.isEmpty(mNickNameEditTextView.getEditorText()))) {
            setButtonEnable(true);
        } else {
            setButtonEnable(false);
        }
    }

    @Override
    public void dealBottomBtnClickEven() {
        if (!isNetworkOff()){
            if (mConfirmLayout.getVisibility() == View.VISIBLE &&
                    !mPasswordTextView.getEditorText().equals(mConfirmEditTextView.getEditorText())) {
                mDropDownAnimationManager.showDragDownLayout(getString(R.string.password_not_match), true);
                return;
            }
            mDialog = LoadingProgressDialog.show(MobileDoneActivity.this, getString(R.string.registering));
            mUserAccountRelatedUIManager.registerNewUser(mNickNameEditTextView.getEditorText(), mPhoneNumber
                    , mPasswordTextView.getEditorText(), mCountryCode);
        }

    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case USER_REGISTER:
                mDropDownAnimationManager.showDragDownLayout(getString(R.string.register_success), false);
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                mDialog = LoadingProgressDialog.show(MobileDoneActivity.this, getString(R.string.log_in));
                mUserAccountRelatedUIManager.loginEvent(mPhoneNumber, mPasswordTextView.getEditorText());
                break;
            case USER_LOGIN:
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                mUserAccountRelatedUIManager.dealWithLoginSuccess(responseResult);

                Intent intent = new Intent();
                intent.setClass(MobileDoneActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);

                finish();
                break;
        }
    }


    public void doClick(View v) {
        finishThisActivity();
    }

    private void finishThisActivity() {
        if (mDialog != null) {
            mDialog.cancel();
        }
        Intent i = new Intent();
        i.setClass(this, MobileGetSmsActivity.class);
        i.putExtra(HPlusConstants.NEW_USER, true);
        i.putExtra(MobileGetSmsActivity.COUNTRY_CODE, mCountryCode);
        i.putExtra(MobileGetSmsActivity.PHONE_NUMBER, mPhoneNumber);
        i.putExtra(MobileGetSmsActivity.FROM_BACK, true);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            finishThisActivity();
        }

        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getHint() != null && v.getHint().equals(mNickNameEditTextView.getEditText().getHint())
                && (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE)) {
            setEditTextViewGetFocus(mPasswordTextView);
        } else if (v.getHint() != null && v.getHint().equals(mPasswordTextView.getEditText().getHint())
                && (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE)) {
            setEditTextViewGetFocus(mConfirmEditTextView);
        } else {
            hideInputKeyBoard();
        }

        return true;
    }


}