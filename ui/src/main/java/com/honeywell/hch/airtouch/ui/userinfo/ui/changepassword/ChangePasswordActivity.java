package com.honeywell.hch.airtouch.ui.userinfo.ui.changepassword;

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
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.main.ui.me.ProfileActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.UserAccountBaseActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.login.MobileGetSmsActivity;

/**
 * Created by H127856 .
 */
public class ChangePasswordActivity extends UserAccountBaseActivity implements TextView.OnEditorActionListener {
    private final String TAG = "ChangePasswordActivity";

    private HPlusEditText mOldPasswordEditTextView;
    private HPlusEditText mConfirmEditTextView;
    private RelativeLayout mConfirmLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        AndroidBug5497Workaround.assistActivity(this);

        initView();
        initDragDownManager(R.id.root_view_id);
        setupUI(findViewById(R.id.root_view_id));
        setListenerToRootView(R.id.root_view_id, mBottomBtn);

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
        mOldPasswordEditTextView = (HPlusEditText) findViewById(R.id.old_password_edittext);
        mOldPasswordEditTextView.getEditText().setOnEditorActionListener(this);

        mPasswordTextView = (HPlusEditText) findViewById(R.id.new_password_edittext);
        mPasswordTextView.getEditText().setOnEditorActionListener(this);
        mPasswordTextView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PASSWORD_LENGTH)});

        mConfirmEditTextView = (HPlusEditText) findViewById(R.id.comfirm_password_edittext);
        mConfirmEditTextView.getEditText().setOnEditorActionListener(this);
        mConfirmEditTextView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PASSWORD_LENGTH)});

        mConfirmLayout = (RelativeLayout) findViewById(R.id.comfirm_layout);
        initNicknameEditText();
        initPasswordEditText();
        initConfirmEditText();
        mActionBarTitleText.setText(getString(R.string.change_password));
        mBottomBtn.setText(getString(R.string.change_pw_btn));
    }


    private void initPasswordEditText() {
        mPasswordTextView.setPasswordImage(false);
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
        mConfirmEditTextView.setEditorHint(getString(R.string.change_password_confirm_pw_hint));
        mConfirmEditTextView.setTextChangedListener(mConfirmTextWatcher);
    }

    private void initNicknameEditText() {
        mOldPasswordEditTextView.setPasswordCleanImage();
        mOldPasswordEditTextView.setEditorHint(getString(R.string.old_password_hint));
        mOldPasswordEditTextView.setTextChangedListener(mOldPasswordTextWatcher);
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

    private TextWatcher mOldPasswordTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.stringOnlyAcsiiFilter(mOldPasswordEditTextView.getEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {
            setEditImageVisible(mOldPasswordEditTextView);
            setButtonStatus();
        }
    };

    @Override
    public void setButtonStatus() {
        if ((mConfirmLayout.getVisibility() == View.VISIBLE
                && !StringUtil.isEmpty(mOldPasswordEditTextView.getEditorText())
                && mUserAccountRelatedUIManager.validPassword(mConfirmEditTextView.getEditorText())
                && mUserAccountRelatedUIManager.validPassword(mPasswordTextView.getEditorText()))
                || (mConfirmLayout.getVisibility() != View.VISIBLE
                && mUserAccountRelatedUIManager.validPassword(mPasswordTextView.getEditorText())
                && !StringUtil.isEmpty(mOldPasswordEditTextView.getEditorText()))) {
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
            mDialog = LoadingProgressDialog.show(ChangePasswordActivity.this, getString(R.string.changing_password));
            mUserAccountRelatedUIManager.changePassword(mOldPasswordEditTextView.getEditorText(), mPasswordTextView.getEditorText());
        }

    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case CHANGE_PASSWORD:
                mUserAccountRelatedUIManager.dealWithUpatePasswordSuccess(mPasswordTextView.getEditorText());
                if (mDialog != null) {
                    mDialog.cancel();
                }
                Intent intent = new Intent();
                intent.putExtra(ProfileActivity.CHANGE_PASSWORD_SUCCESS, true);
                setResult(DashBoadConstant.CHANGE_PASSWORD_CODE, intent);
                finishThisActivity();
//                mDropDownAnimationManager.showDragDownLayout(getString(R.string.change_password_success), false);

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
        if (v.getHint() != null && v.getHint().equals(mOldPasswordEditTextView.getEditText().getHint())
                && (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE)) {
            setEditTextViewGetFocus(mPasswordTextView);
        } else if (v.getHint() != null && v.getHint().equals(mPasswordTextView.getEditText().getHint())
                && mConfirmLayout.getVisibility() == View.VISIBLE && (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE)) {
            setEditTextViewGetFocus(mConfirmEditTextView);
        } else {
            hideInputKeyBoard();
        }

        return true;
    }
}