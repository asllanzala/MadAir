package com.honeywell.hch.airtouch.ui.userinfo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.AndroidBug5497Workaround;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.UserAccountBaseActivity;

/**
 * <p/>
 * change by Stephen(H127856)
 * call another login task for login action
 */
public class UserLoginActivity extends UserAccountBaseActivity implements TextView.OnEditorActionListener {

    private TextView mForgotPassword;
    private TextView mNewUserText;
    private boolean isFromStartActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        AndroidBug5497Workaround.assistActivity(this);
        initView();
        initDragDownManager(R.id.root_view_id);
        setupUI(findViewById(R.id.root_view_id));
        setListenerToRootView(R.id.root_view_id, mBottomBtn);
        isFromStartActivity = getIntent().getBooleanExtra(StartActivity.FROM_START_ACTIVITY, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            finishThisActivity();
        }

        return false;
    }

    @Override
    protected void initView() {
        super.initView();
        setActionBarTitleText(getString(R.string.login));
        setButtonText(getString(R.string.login));

        initPhoneNumberEditText();

        mPasswordTextView = (HPlusEditText) findViewById(R.id.password_txt);
        isNeedHideEye = true;
        mPasswordTextView.setPasswordImage(false);
        mPasswordTextView.setEditTextImageViewVisible(View.GONE);
        mPasswordTextView.setEditorHint(getString(R.string.user_account_password_hint));
        mPasswordTextView.setTextChangedListener(mPasswordTextWatch);
        mPasswordTextView.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_PASSWORD_LENGTH)});
        mPasswordTextView.getEditText().setOnEditorActionListener(this);

        initDropTextView();

        mPhoneNumberTextView.requestFocus();

        mForgotPassword = (TextView) findViewById(R.id.forgot_password);

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGetSmsActivity(false);
            }
        });

        mNewUserText = (TextView) findViewById(R.id.new_user);
        mNewUserText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGetSmsActivity(true);
            }
        });

        mPhoneNumberTextView.getEditText().setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mPhoneNumberTextView.getEditText().setOnEditorActionListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void dealBottomBtnClickEven() {

        //有网络的时候才去加载
        if (!isNetworkOff()){
            if (mDialog == null || !mDialog.isShowing()) {
                mDialog = LoadingProgressDialog.show(this, getString(R.string.log_in));
            }
            mUserAccountRelatedUIManager.loginEvent(mPhoneNumberTextView.getEditorText(), mPasswordTextView.getEditorText());

        }

    }


    @Override
    protected void setButtonStatus() {
        if (mUserAccountRelatedUIManager.validPassword(mPasswordTextView.getEditorText())
                && mUserAccountRelatedUIManager.validPhoneNumber(mPhoneNumberTextView.getEditorText(), mCountryCode)) {
            setButtonEnable(true);
        } else {
            setButtonEnable(false);
        }
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case USER_LOGIN:
                UserAllDataContainer.shareInstance().setmPushMessageModel(null);
                mUserAccountRelatedUIManager.dealWithLoginSuccess(responseResult);

                Intent intent = new Intent();
                intent.setClass(UserLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        }
        super.dealSuccessCallBack(responseResult);
    }


    @Override
    protected void dealErrorCallBack(ResponseResult responseResult) {
        super.dealErrorCallBack(responseResult, 0);
        switch (responseResult.getRequestId()) {
            case USER_LOGIN:
                UserAllDataContainer.shareInstance().setmPushMessageModel(null);
                dealLoginError(responseResult);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        if (v.getId() == mPhoneNumberTextView.getEditText().getId() && actionId == EditorInfo.IME_ACTION_NEXT) {
            setEditTextViewGetFocus(mPasswordTextView);
        } else {
            hideInputKeyBoard();
        }
        return true;
    }


    public void doClick(View v) {
        finishThisActivity();
    }

    private void finishThisActivity() {
        if (isFromStartActivity) {
            Intent i = new Intent();
            i.setClass(UserLoginActivity.this, StartActivity.class);
            i.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY,true);
            startActivity(i);
        }
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }


    private void goToGetSmsActivity(boolean isNewUser) {

        Intent i = new Intent();
        i.putExtra(HPlusConstants.NEW_USER, isNewUser);
        i.setClass(UserLoginActivity.this, MobileGetSmsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }


}