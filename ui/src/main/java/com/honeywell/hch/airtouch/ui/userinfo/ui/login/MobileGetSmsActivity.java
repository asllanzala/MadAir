package com.honeywell.hch.airtouch.ui.userinfo.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.AndroidBug5497Workaround;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.help.ManualActivity;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.UserAccountBaseActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.forgetpassword.ForgetPasswordActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class MobileGetSmsActivity extends UserAccountBaseActivity implements TextView.OnEditorActionListener {

    public static final String COUNTRY_CODE = "country_code";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String FROM_BACK = "from_back";

    private static final int MAX_VERIFICATION_CODE_LENGTH = 4;
    private static final int TIME_COUNT = 60;

    private TextView mPolityTextView;
    private RelativeLayout mVerificationLayout;
    private HPlusEditText mVerificationCodeEditText;
    private boolean isNewUser = false;

    private volatile int mTimerCount;
    private Timer mTimer;
    private TextView mSendTimeTextView;
    private boolean isFromBack = false;
    private TextView mSendCodeTipTextView;

    private boolean isFromStartActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_get_sms);
        AndroidBug5497Workaround.assistActivity(this);

        isNewUser = getIntent().getBooleanExtra(HPlusConstants.NEW_USER, false);
        isFromStartActivity = getIntent().getBooleanExtra(StartActivity.FROM_START_ACTIVITY, false);

        mCountryCode = getIntent().getStringExtra(COUNTRY_CODE);
        mPhoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
        isFromBack = getIntent().getBooleanExtra(FROM_BACK, false);
        initView();
        initDragDownManager(R.id.root_view_id);
        setupUI(findViewById(R.id.root_view_id));
        setListenerToRootView(R.id.root_view_id, mBottomBtn);
        if (isFromBack) {
            mPhoneNumberTextView.getEditText().setText(mPhoneNumber);
            showNormalViewFromBack();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonStatus();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            finishThisActivity();
        }

        return false;
    }

    @Override
    public void initView() {
        super.initView();
        setActionBarTitleText(getString(R.string.contact_info));
        initPhoneNumberEditText();
        initDropTextView();
        mPhoneNumberTextView.requestFocus();

        mPhoneNumberTextView.getEditText().setOnEditorActionListener(this);

        mPolityTextView = (TextView) findViewById(R.id.polity_text);
        mVerificationLayout = (RelativeLayout) findViewById(R.id.verify_code_layout);

        mSendTimeTextView = (TextView) findViewById(R.id.send_time_text);

        mSendCodeTipTextView = (TextView) findViewById(R.id.send_code_tip);

        initVerificationEditTextView();

        initUserPolityText();

        setSendCodeTipTextView();

    }


    public void doClick(View v) {
        finishThisActivity();
    }


    @Override
    protected void dealBottomBtnClickEven() {
        if (!isNetworkOff()){
            if (mVerificationLayout.getVisibility() != View.VISIBLE || StringUtil.isEmpty(mVerificationCodeEditText.getEditorText())) {
                mDialog = LoadingProgressDialog.show(MobileGetSmsActivity.this, getString(R.string.sending_sms));
                mUserAccountRelatedUIManager.checkAuthUser(mPhoneNumberTextView.getEditorText());
            } else {
                mDialog = LoadingProgressDialog.show(MobileGetSmsActivity.this, getString(R.string.is_verifying));
                mUserAccountRelatedUIManager.verifySmsCode(mPhoneNumberTextView.getEditorText(), mVerificationCodeEditText.getEditorText());
            }
        }

    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        if (mNetWorkErrorLayout != null) {
            mNetWorkErrorLayout.setVisibility(View.GONE);
        }
        switch (responseResult.getRequestId()) {
            case CHECK_AUTH_USER:

                //如果是企业账号，并且是进行忘记密码操作，提示企业账号不支持
                if (mUserAccountRelatedUIManager.isEnterpriseAccount(responseResult) && !isNewUser) {
                    dismissDialog();
                    mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.enterprise_not_support_tip)
                            , getString(R.string.cancel), null, getString(R.string.sideitem_customer_care), new MessageBox.MyOnClick() {
                                @Override
                                public void onClick(View v) {

                                    callServicePhone(false);
                                }
                            });
                    return;
                }

                boolean isHpluser = mUserAccountRelatedUIManager.isHplusUser(responseResult);
                //如果注册时候，输入的手机号是HPlus用户
                if (isHpluser && isNewUser) {
                    dismissDialog();
                    mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.already_registed)
                            , getString(R.string.cancel), null, getString(R.string.login), new MessageBox.MyOnClick() {
                                @Override
                                public void onClick(View v) {
                                    finishThisActivity();
                                }
                            });
                    return;
                } else if (!isHpluser && !isNewUser) {
                    dismissDialog();
                    //如果忘记密码时候，输入的手机号不是HPlus用户
                    mAlertDialog = MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.not_register_user_tip)
                            , getString(R.string.cancel), null, getString(R.string.login_new_user), new MessageBox.MyOnClick() {
                                @Override
                                public void onClick(View v) {
                                    changeToSignUpActivity();
                                }
                            });
                    return;
                }
                mUserAccountRelatedUIManager.getSmsVerifyCode(mPhoneNumberTextView.getEditorText(), mCountryCode);
                break;

            case GET_SMS_CODE:
                dismissDialog();
                //发送成功，但是计数器还不到0.只是让验证码可以输入,并且获取到焦点
                showVerificationCodeLayout();
                mVerificationCodeEditText.getEditText().setEnabled(true);
                mDropEditText.setViewEnable(false);
                setEditTextViewGetFocus(mVerificationCodeEditText);
                break;
            case VERIFY_SMS_VALID:
                dismissDialog();
                goToNextActivity();
                break;
        }
    }


    @Override
    protected void dealAfterShowErrorMesssage(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case GET_SMS_CODE:
            case VERIFY_SMS_VALID:
            case CHECK_AUTH_USER:
                dealSendError();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }
    }

    /**
     * 用户选择了一个dropEditText ITEM后
     */
    @Override
    protected void setDropTextAfterSelect() {
        mDropEditText.setfterSelectResultInterface(new DropEditText.IAfterSelectResult() {
            @Override
            public void setSelectResult(String cityCode) {
                setAfterSelectDropEdit(cityCode);
                if (mVerificationCodeEditText != null) {
                    mVerificationCodeEditText.setEditorText("");
                }
                setSendCodeTipTextView();

            }

            @Override
            public  void setSelectResult(DropTextModel dropTextModel){}
        });
    }

    private void changeToSignUpActivity() {
        isNewUser = true;
        initUserPolityText();
    }

    private void initUserPolityText() {
        if (isNewUser) {
            setSpannableString(getString(R.string.poily_text_str), 23, 32, 56, 79, mPolityTextView, new ISpanClickListener() {
                @Override
                public void clickEvent() {
                    Intent i = new Intent();
                    i.setClass(MobileGetSmsActivity.this, ManualActivity.class);
                    i.putExtra("eula", true);
                    i.putExtra("countryCode", mCountryCode);
                    startActivity(i);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
            });
            mPolityTextView.setVisibility(View.VISIBLE);
        }
    }

    private void goToNextActivity() {
        Intent i = new Intent();
        i.putExtra(COUNTRY_CODE, mCountryCode);
        i.putExtra(PHONE_NUMBER, mPhoneNumberTextView.getEditorText());
        i.putExtra(HPlusConstants.NEW_USER, isNewUser);

        if (isNewUser) {
            i.setClass(this, MobileDoneActivity.class);

        } else {
            i.setClass(this, ForgetPasswordActivity.class);
        }
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
//        finish();
    }

    private void finishThisActivity() {

        dismissDialog();

        if (isFromStartActivity) {
            Intent i = new Intent();
            i.setClass(MobileGetSmsActivity.this, StartActivity.class);
            i.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY,true);
            startActivity(i);
        }

        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    protected void setButtonStatus() {

        //如果是获取验证码，那么
        if ((mUserAccountRelatedUIManager.validPhoneNumber(mPhoneNumberTextView.getEditorText(), mCountryCode)
                && mVerificationLayout.getVisibility() != View.VISIBLE)
                || (mUserAccountRelatedUIManager.validPhoneNumber(mPhoneNumberTextView.getEditorText(), mCountryCode)
                && mVerificationLayout.getVisibility() == View.VISIBLE
                && !StringUtil.isEmpty(mVerificationCodeEditText.getEditorText())
                && mVerificationCodeEditText.getEditorText().length() == MAX_VERIFICATION_CODE_LENGTH)) {
            setButtonEnable(true);
        } else {
            setButtonEnable(false);
        }

        boolean isResendTextClick = mUserAccountRelatedUIManager.validPhoneNumber(mPhoneNumberTextView.getEditorText(), mCountryCode) ? true : false;
        setReSendTextStatus(isResendTextClick);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.getHint() != null && v.getHint().equals(mPhoneNumberTextView.getEditText().getHint())
                && (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE)
                && mVerificationLayout.getVisibility() == View.VISIBLE) {
            setEditTextViewGetFocus(mVerificationCodeEditText);
        } else {
            hideInputKeyBoard();
        }
        return true;
    }

    @Override
    public void phoneNumberAfterTextChange() {
        mVerificationCodeEditText.setEditorText("");

    }


    private void setSendCodeTipTextView() {
        String tip = getString(R.string.sms_arrive);
        if (!mCountryCode.equals(HPlusConstants.CHINA_CODE)) {
            tip = getString(R.string.sms_hint);
            setSpannableString(tip, 27, 31, 92, 106, mSendCodeTipTextView, new ISpanClickListener() {
                @Override
                public void clickEvent() {
                    callServicePhone(true);
                }
            });
        } else {
            mSendCodeTipTextView.setText(tip);
        }

    }


    private void showVerificationCodeLayout() {
        setActionBarTitleText(getString(R.string.verifiationv_str));

        //dropEdit 不可以编辑
        mDropEditText.setViewEnable(false);

        //设置phoneTextView不可编辑
        mPhoneNumberTextView.getEditText().setEnabled(false);
        setButtonEnable(false);

        if (mPolityTextView != null) {
            mPolityTextView.setVisibility(View.GONE);
        }
        mVerificationLayout.setVisibility(View.VISIBLE);
        mVerificationCodeEditText.setEditorText("");
        initTimer();
    }

    /**
     * 从上一个界面返回回来的后现实的状态
     */
    private void showNormalViewFromBack() {
        setActionBarTitleText(getString(R.string.verifiationv_str));

        //dropEdit 不可以编辑
        mDropEditText.setViewEnable(true);

        //设置phoneTextView不可编辑
        mPhoneNumberTextView.getEditText().setEnabled(true);
        setButtonEnable(false);
        setReSendTextStatus(true);

        if (mPolityTextView != null) {
            mPolityTextView.setVisibility(View.GONE);
        }
        mVerificationLayout.setVisibility(View.VISIBLE);
        mVerificationCodeEditText.requestFocus();
        setResendButton();
    }


    private void setReSendTextStatus(boolean isClick) {
        int color = isClick ? getResources().getColor(R.color.login_new_user_color) : getResources().getColor(R.color.gray_line);
        mSendTimeTextView.setClickable(isClick);
        mSendTimeTextView.setTextColor(color);

        mSendTimeTextView.getPaint().setFakeBoldText(isClick);
    }

    private void setNumberTimerTextViewStatus() {
        mSendTimeTextView.setClickable(false);
        mSendTimeTextView.getPaint().setFakeBoldText(true);
        mSendTimeTextView.setTextColor(getResources().getColor(R.color.login_new_user_color));
    }

    private void dealSendError() {

        mDropEditText.setViewEnable(true);

        mPhoneNumberTextView.getEditText().setEnabled(true);

        mVerificationCodeEditText.getEditText().setText(null);
        mVerificationCodeEditText.requestFocus();
    }


    private void initVerificationEditTextView() {
        mVerificationCodeEditText = (HPlusEditText) findViewById(R.id.verify_code_edit_id);
        mVerificationCodeEditText.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_VERIFICATION_CODE_LENGTH)});
        mVerificationCodeEditText.setCleanImage();
        mVerificationCodeEditText.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
        mVerificationCodeEditText.setEditorHint(getString(R.string.verification_code_hint));
        mVerificationCodeEditText.setTextChangedListener(verifyEditTextWatch);
        mVerificationCodeEditText.getEditText().setOnEditorActionListener(this);
    }

    private TextWatcher verifyEditTextWatch = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            StringUtil.isAlaboNumeric(mVerificationCodeEditText.getEditText());
        }

        @Override
        public void afterTextChanged(Editable s) {
            setEditImageVisible(mVerificationCodeEditText);
            setButtonStatus();
        }
    };

    private void setSpannableString(String str, int chineseStart, int chineseEnd, int englishStart, int englishEnd, TextView textView, final ISpanClickListener iSpanClickListener) {
        SpannableString ssTitle = new SpannableString(str);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.enroll_blue2));
                ds.setFakeBoldText(true);
                ds.setUnderlineText(false);
            }

            @Override
            public void onClick(View widget) {
                iSpanClickListener.clickEvent();
            }
        };

        if (AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH)) {
            ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 0, chineseStart - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTitle.setSpan(clickableSpan, chineseStart, chineseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (chineseEnd + 1 < str.length()) {
                ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), chineseEnd, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


        } else {
            ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 0, englishStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTitle.setSpan(clickableSpan, englishStart, englishEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (englishEnd + 1 < str.length()) {
                ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), englishStart + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        textView.setText(ssTitle);
    }

    private void initTimer() {
        mTimerCount = TIME_COUNT;
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                Message msg = new Message();
                mTimerCount--;
                msg.what = mTimerCount;
                handler.sendMessage(msg);
            }
        };

        mTimer = new Timer();
        mTimer.schedule(timerTask, 0, 1000);

        setNumberTimerTextViewStatus();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mTimerCount <= 0) {
                setResendButton();
                dealSendError();
            } else if (mTimerCount > 0) {
                setSendTimeTextView();
            }
        }
    };


    private void setSendTimeTextView() {
        mSendTimeTextView.setText(String.valueOf(mTimerCount) + "s");
        mSendTimeTextView.setClickable(false);
    }

    private void setResendButton() {
        //设置mTimerCount为-1,避免当前进行的TimerTask会影响显示
        mTimerCount = -1;

        if (mTimer != null) {
            mTimer.cancel();
        }
        mSendTimeTextView.setText(getString(R.string.sms_resend));
        setReSendTextStatus(true);

        mSendTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDialog = LoadingProgressDialog.show(MobileGetSmsActivity.this, getString(R.string.sending_sms));
                mUserAccountRelatedUIManager.checkAuthUser(mPhoneNumberTextView.getEditorText());
            }
        });
    }


    private interface ISpanClickListener {
        void clickEvent();
    }
}
