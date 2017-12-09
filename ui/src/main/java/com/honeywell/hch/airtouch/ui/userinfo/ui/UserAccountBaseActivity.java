package com.honeywell.hch.airtouch.ui.userinfo.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.CenterTextView;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.enroll.ui.adapter.HomeSpinnerAdapter;
import com.honeywell.hch.airtouch.ui.userinfo.manager.UserAccountManager;
import com.honeywell.hch.airtouch.ui.userinfo.manager.UserAccountRelatedUIManager;
import com.honeywell.hch.airtouch.ui.userinfo.ui.login.UserLoginActivity;

/**
 * Created by h127856 on 6/23/16.
 */
public class UserAccountBaseActivity extends BaseActivity implements View.OnClickListener {


    public static final int MAX_PASSWORD_LENGTH = 30;

    protected TextView mActionBarTitleText;
    private TextView mActionBarText;
    protected Button mBottomBtn;

    protected DropEditText mDropEditText;

    protected String mCountryCode;
    protected String mPhoneNumber;

    protected HPlusEditText mPhoneNumberTextView;
    protected HPlusEditText mPasswordTextView;
    protected UserAccountRelatedUIManager mUserAccountRelatedUIManager;

    protected AlertDialog mAlertDialog;

    protected boolean isNeedHideEye = false;

    protected void initView() {
        //登陆前 加载
        CloseActivityUtil.beforeLoginActivityList.add(this);

        initStatusBar();
        mActionBarTitleText = (TextView) findViewById(R.id.title_textview_id);
        mActionBarText = (CenterTextView) findViewById(R.id.input_tip_id);
        mActionBarText.setVisibility(View.GONE);
        mBottomBtn = (Button) findViewById(R.id.buttom_btn_id);
        mBottomBtn.setOnClickListener(this);
        setButtonEnable(false);
        initManagerCallBack();
    }

    protected void setActionBarTitleText(String title) {
        mActionBarTitleText.setText(title);
    }

    protected void setButtonText(String btnStr) {
        mBottomBtn.setText(btnStr);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttom_btn_id) {
            dealBottomBtnClickEven();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDropEditText != null) {
            mDropEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initDropTextViewAdapter(mDropEditText.getWidth());
                }
            }, 300);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
            mAlertDialog = null;
        }
    }

    /**
     * need to be override
     */
    protected void dealBottomBtnClickEven() {
    }

    protected UserAccountManager.SuccessCallback mSuccessCallback = new UserAccountManager.SuccessCallback() {
        @Override
        public void onSuccess(ResponseResult responseResult) {
            dealSuccessCallBack(responseResult);
        }
    };

    protected UserAccountManager.ErrorCallback mErrorCallBack = new UserAccountManager.ErrorCallback() {
        @Override
        public void onError(ResponseResult responseResult) {
            dealErrorCallBack(responseResult);
        }
    };

    /*
        group control update group name need overide
     */
    protected void dealErrorCallBack(ResponseResult responseResult) {
        super.dealErrorCallBack(responseResult,0);
        switch (responseResult.getRequestId()) {
            case USER_LOGIN:
            case CHECK_AUTH_USER:
                dealLoginError(responseResult);
                break;
            case USER_REGISTER:
                if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
                    int errorType = mUserAccountRelatedUIManager.getRegisterApiError(responseResult);
                    if (errorType == UserAccountRelatedUIManager.USER_ALLREADY_EXIST) {
                        mAlertDialog = MessageBox.createTwoButtonDialog(this, null, getString(R.string.already_registed),
                                getString(R.string.cancel), null, getString(R.string.login), new MessageBox.MyOnClick() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        Intent i = new Intent();
                                        i.setClass(UserAccountBaseActivity.this, UserLoginActivity.class);
                                        startActivity(i);
                                    }
                                });
                    } else if (errorType == UserAccountRelatedUIManager.USER_REGISTER_ERROR) {
                        mDropDownAnimationManager.showDragDownLayout(responseResult.getExeptionMsg(), true);
                    } else {
                        mDropDownAnimationManager.showDragDownLayout(getString(R.string.register_failed), true);
                    }

                } else {
                    errorHandle(responseResult, responseResult.getExeptionMsg());
                }
                break;
            case GET_SMS_CODE:
                if (responseResult.getResponseCode() == StatusCode.SEND_VERIFY_CODE_ERROR) {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.sms_send_fail), true);
                } else {
                    errorHandle(responseResult, responseResult.getExeptionMsg());
                }
                dealAfterShowErrorMesssage(responseResult);
                break;
            case VERIFY_SMS_VALID:
                if (responseResult.getResponseCode() == StatusCode.VERIFY_CODE_ERROR) {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.sms_code_wrong), true);
                } else {
                    errorHandle(responseResult, responseResult.getExeptionMsg());
                }
                dealAfterShowErrorMesssage(responseResult);
                break;
            case UPDATE_PASSWORD:
                int errorType = mUserAccountRelatedUIManager.getRestPasswordError(responseResult);
                if (errorType == UserAccountRelatedUIManager.USER_NOT_EXIST) {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.user_not_exist, String.valueOf(mPhoneNumber)), true);
                } else if (errorType == UserAccountRelatedUIManager.USER_PASSWORD_ERROR) {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.update_password_error), true);

                } else {
                    errorHandle(responseResult, responseResult.getExeptionMsg());
                }
                break;
            case CHANGE_PASSWORD:
                if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.change_password_invalid), true);
                } else {
                    errorHandle(responseResult, responseResult.getExeptionMsg());
                }
                break;
        }
    }


    protected void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void dealLoginError(ResponseResult responseResult) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        if (responseResult.getResponseCode() == StatusCode.UNAUTHORIZED) {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.login_password_invalid), true);
        } else if (responseResult.getResponseCode() == StatusCode.UNACTIVATED_ACCOUNT) {
            mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.non_activated)
                    , getString(R.string.cancel), null, getString(R.string.sideitem_customer_care), new MessageBox.MyOnClick() {
                @Override
                public void onClick(View v) {
                    callServicePhone(false);
                }
            });
        } else if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
            mDropDownAnimationManager.showDragDownLayout(getString(R.string.login_failed), true);
        } else {
            errorHandle(responseResult, responseResult.getExeptionMsg());
        }
    }

    protected void initManagerCallBack() {
        mUserAccountRelatedUIManager = new UserAccountRelatedUIManager(this);

        mUserAccountRelatedUIManager.setErrorCallback(mErrorCallBack);
        mUserAccountRelatedUIManager.setSuccessCallback(mSuccessCallback);
    }

    protected void initDropTextView() {
        mDropEditText = (DropEditText) findViewById(R.id.country_drop_edittext_id);
        mDropEditText.getmEditText().setFocusable(false);
        mDropEditText.getmEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDropEditText.dealWithClickEvent();

                return false;
            }
        });
        setDropTextAfterSelect();
        initDefaultDropEditText();
    }

    /**
     * 用户选择了一个dropEditText ITEM后
     */
    protected void setDropTextAfterSelect() {
        mDropEditText.setfterSelectResultInterface(new DropEditText.IAfterSelectResult() {
            @Override
            public void setSelectResult(String cityCode) {
                setAfterSelectDropEdit(cityCode);
            }

            @Override
            public  void setSelectResult(DropTextModel dropTextModel){}
        });
    }

    public void setAfterSelectDropEdit(String cityCode) {
        mCountryCode = cityCode;
        if (mPhoneNumberTextView != null) {
            mPhoneNumberTextView.setEditorText("");
            setMobileMaxLength(mCountryCode, mPhoneNumberTextView.getEditText());
        }
        if (mPasswordTextView != null) {
            mPasswordTextView.getEditText().setText("");
        }
    }

    protected void initDropTextViewAdapter(int width) {

        DropTextModel[] dropTextModels = new DropTextModel[2];
        dropTextModels[0] = new DropTextModel(getString(R.string.china_str), R.drawable.china_flag);
        dropTextModels[1] = new DropTextModel(getString(R.string.india_str), R.drawable.india_flag);

        HomeSpinnerAdapter homeSpinnerTypeAdapter = new HomeSpinnerAdapter<>(this, dropTextModels);
        mDropEditText.setAdapter(homeSpinnerTypeAdapter, width);
    }

    /**
     * 设置mobile phone输入框的最大长度限制
     *
     * @param mCountryCode
     * @param mobilePhoneEditText
     */
    protected void setMobileMaxLength(String mCountryCode, EditText mobilePhoneEditText) {
        mobilePhoneEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(mUserAccountRelatedUIManager.getPhoneNumberLength(mCountryCode))});

    }

    protected void setEditImageVisible(HPlusEditText targetTextView) {
        if (targetTextView != null && StringUtil.isEmpty(targetTextView.getEditorText())) {
            targetTextView.showImageButton(false);
        } else if (targetTextView != null) {
            targetTextView.showImageButton(true);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (mDropEditText != null && !mDropEditText.isTouchInThisDropEditText(event.getX(), event.getY())) {
            mDropEditText.closeDropPopWindow();
        }
        return super.dispatchTouchEvent(event);

    }

    public void initPhoneNumberEditText() {
        mPhoneNumberTextView = (HPlusEditText) findViewById(R.id.phone_number_txt);
        mPhoneNumberTextView.setCleanImage();
        mPhoneNumberTextView.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
        mPhoneNumberTextView.setEditorHint(getString(R.string.authorize_invite_edit_hind));
        mPhoneNumberTextView.setTextChangedListener(mPhoneNumberWatch);
    }

    public void initDefaultDropEditText() {

        if (StringUtil.isEmpty(mCountryCode)) {
            String cityCode = UserInfoSharePreference.getGpsCountryCode();
            mCountryCode = cityCode;
        }
        if (StringUtil.isEmpty(mCountryCode) || mCountryCode.equals(HPlusConstants.CHINA_CODE)) {
            mCountryCode = HPlusConstants.CHINA_CODE;
            mDropEditText.setDropTextItem(getString(R.string.china_str), R.drawable.china_flag);
        } else if (mCountryCode.equals(HPlusConstants.INDIA_CODE)) {
            mCountryCode = HPlusConstants.INDIA_CODE;
            mDropEditText.setDropTextItem(getString(R.string.india_str), R.drawable.india_flag);
        }

        if (mPhoneNumberTextView != null) {
            setMobileMaxLength(mCountryCode, mPhoneNumberTextView.getEditText());
        }
    }

    protected TextWatcher mPhoneNumberWatch = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            phoneNumberOnTextChange();
        }

        @Override
        public void afterTextChanged(Editable s) {
            setEditImageVisible(mPhoneNumberTextView);
            setButtonStatus();
            if (mPhoneNumberTextView != null && StringUtil.isEmpty(mPhoneNumberTextView.getEditorText())) {
                if (mPasswordTextView != null) {
                    mPasswordTextView.getEditText().setText("");
                }
                phoneNumberAfterTextChange();
            }
        }
    };

    public void setButtonEnable(boolean isEnable) {
        mBottomBtn.setEnabled(isEnable);
        mBottomBtn.setClickable(isEnable);
    }

    protected void phoneNumberOnTextChange() {
        StringUtil.isAlaboNumeric(mPhoneNumberTextView.getEditText());
    }

    protected TextWatcher mPasswordTextWatch = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.specialCharacterAndChineseFilter(mPasswordTextView.getEditText());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!isNeedHideEye) {
                mPasswordTextView.showImageButton(true);
            }
            setButtonStatus();
        }
    };

    protected void callServicePhone(boolean isIndiaOnly) {

        if (checkPhoneCallPermission()) {
            Intent intent = null;
            if (AppConfig.shareInstance().isIndiaAccount() || isIndiaOnly) {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.INDIA_CONTACT_PHONE_NUMBER));
            } else {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.CONTACT_PHONE_NUMBER));
            }
            try {
                startActivity(intent);
            } catch (SecurityException e) {

            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.phone_call_permission_deny), Toast.LENGTH_LONG).show();
        }

    }


    private boolean checkPhoneCallPermission() {
        boolean currentPhoneCallPermission = mHPlusPermission.isCallPhonePermissionGranted(this);
        if (currentPhoneCallPermission) {
            return true;
        }
        return false;
    }

    /**
     * 获取焦点
     *
     * @param hPlusEditText
     */
    public void setEditTextViewGetFocus(HPlusEditText hPlusEditText) {
        hPlusEditText.getEditText().setFocusable(true);
        hPlusEditText.getEditText().setFocusableInTouchMode(true);
        hPlusEditText.getEditText().requestFocus();
    }

    /**
     * 如果子类有需要，需要对这个方法进行重写
     */
    protected void phoneNumberAfterTextChange() {

    }


    /**
     * 如果子类有需要，需要对这个方法进行重写
     * 设置button的enable或是disable
     */
    protected void setButtonStatus() {

    }

    /**
     * need override this method
     * 需要子类进行重写
     *
     * @param responseResult
     */
    protected void dealAfterShowErrorMesssage(ResponseResult responseResult) {

    }

}
