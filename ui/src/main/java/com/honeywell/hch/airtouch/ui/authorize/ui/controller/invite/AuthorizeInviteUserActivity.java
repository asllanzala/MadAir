package com.honeywell.hch.airtouch.ui.authorize.ui.controller.invite;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthBaseModel;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.CheckAuthUserResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.authorize.ui.controller.list.AuthorizeBaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropDownAnimationManager;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 1/2/16.
 */
public class AuthorizeInviteUserActivity extends AuthorizeBaseActivity implements AuthorizeBaseActivity.AuthorizeClick {

    private final String TAG = "AuthorizeInviteUserActivity";
    private TextView mTitleTv;
    //    private TextView mChooseContactTv;
    private TextView mAddTv;
    private TextView mResultTv;
    private HPlusEditText mAirSearchEt;
    private RelativeLayout mInviteListRl;
    private RelativeLayout mAuthRootLayout;
    private Button mInviteBtn;
    private int INPUT_MAX_LENGTH = 11;
    private final int CHINA_MAX_LENGTH = 11;
    private final int INDIA_MAX_LENGTH = 10;
    private String inputPhoneNumber;
    private AuthBaseModel mAuthBaseModel;
    private List<CheckAuthUserResponse> mCheckAuthUserResponsesList;
    private AppConfig mAppConfig;
    private String userSelf;
    private final int ENTERPRISETYPE = 2;
    private InputMethodManager mInputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_invite_user);
        initStatusBar();
        init();
        initAirEt();
        initData();
        dealWithIntent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAppConfig.isIndiaAccount()) {
            INPUT_MAX_LENGTH = INDIA_MAX_LENGTH;
        } else {
            INPUT_MAX_LENGTH = CHINA_MAX_LENGTH;
        }
        mAirSearchEt.getEditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(INPUT_MAX_LENGTH)});
    }

    private void init() {
        mAppConfig = AppConfig.shareInstance();
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
//        mChooseContactTv = (TextView) findViewById(R.id.auth_invite_choose_tv);
        mAddTv = (TextView) findViewById(R.id.auth_invite_add_tv);
        mResultTv = (TextView) findViewById(R.id.auth_invite_result_tv);
        mAirSearchEt = (HPlusEditText) findViewById(R.id.auth_invite_search_et);
        mInviteListRl = (RelativeLayout) findViewById(R.id.auth_invite_display_ly);
        mInviteBtn = (Button) findViewById(R.id.auth_invite_btn);
        mAuthRootLayout = (RelativeLayout) findViewById(R.id.root_view_id);
        userSelf = UserInfoSharePreference.getMobilePhone();
        mDropDownAnimationManager = new DropDownAnimationManager(mAuthRootLayout, mContext);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void dealWithIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAuthBaseModel = (AuthBaseModel) bundle.get(INTENTPARAMETEROBJECT);
        }
    }

    private void initAirEt() {
        mAirSearchEt.setCleanImage();
        mAirSearchEt.setEditorHint(getString(R.string.authorize_invite_edit_hind));
        mAirSearchEt.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

        mAirSearchEt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                isShowMultiDialog();
                StringUtil.isAlaboNumeric(mAirSearchEt.getEditText());
                showInviteAddList(mAirSearchEt);
                setEditImageVisible(mAirSearchEt);
//                if (StringUtil.isEmpty(mAirSearchEt.getEditorText())) {
//                    mAirSearchEt.showClearButton(false);
////                    mChooseContactTv.setVisibility(View.INVISIBLE);
//                }
            }
        });
    }

    protected void setEditImageVisible(HPlusEditText targetTextView) {
        if (targetTextView != null && StringUtil.isEmpty(targetTextView.getEditorText())) {
            targetTextView.showImageButton(false);
        } else if (targetTextView != null) {
            targetTextView.showImageButton(true);
        }
    }

    private void initData() {
        mTitleTv.setText(R.string.authorize_to_item);
        mInviteBtn.setText(R.string.enroll_next);
        buttonStatus(false);
    }


    private void showInviteAddList(HPlusEditText et) {
        if (isShowInviteList(et)) {
            setAddListText(mAddTv, getAddListCharacter(et), 3, 4, 11);
        } else {
            mInviteListRl.setVisibility(View.GONE);
//            mChooseContactTv.setVisibility(View.VISIBLE);
        }
    }

    private void isShowMultiDialog() {
        if (mResultTv.isShown()) {
            StringUtil.filterCharacterEmpty(mAirSearchEt.getEditText());
            showSimpleDialog(getString(R.string.authorize_add_one_remind), getString(R.string.ok), null);
            return;
        }
    }

    public void doClick(View v) {
        super.doClick(v);
        if (v.getId() == R.id.auth_invite_display_ly) {
            inputPhoneNumber = mAirSearchEt.getEditorText();
            checkUserByPhone();
        } else if (/*v.getId() == R.id.auth_invite_choose_tv
                || */v.getId() == R.id.auth_invite_result_tv) {
            mResultTv.setText(null);
            mResultTv.setVisibility(View.GONE);
            buttonStatus(false);
//            mChooseContactTv.setVisibility(View.INVISIBLE);
            isShowInviteList(mAirSearchEt);
        } else if (v.getId() == R.id.auth_invite_btn) {
            mAuthorizeUiManager.encloseGrantToUserNameList(mAuthBaseModel, mCheckAuthUserResponsesList);
            startIntent(AuthorizeChoosePermissionActivity.class, mAuthBaseModel, ClickType.AUTHROIZE);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        mCheckAuthUserResponsesList = mAuthorizeUiManager.checkAuthUserResponsesList(responseResult);
        for (CheckAuthUserResponse checkAuthUserResponse : mCheckAuthUserResponsesList) {
            if (checkAuthUserResponse.isHPlusUser() == false) {
                showSimpleDialog(getString(R.string.authorize_add_remind, inputPhoneNumber), getString(R.string.ok), null);
                return;
            }
            if (checkAuthUserResponse.getmUserType() == ENTERPRISETYPE) {
                showSimpleDialog(getString(R.string.authorize_enterprise_remind), getString(R.string.ok), null);
                return;
            }
        }
        mAirSearchEt.setEditorText(null);
        mResultTv.setText(inputPhoneNumber);
        mResultTv.setVisibility(View.VISIBLE);
        buttonStatus(true);
//                mChooseContactTv.setVisibility(View.INVISIBLE);
        mInviteListRl.setVisibility(View.GONE);
    }

    private void checkUserByPhone() {
        if (isOwnerSelf(inputPhoneNumber)) {
            return;
        }
        List<String> phoneNumberList = new ArrayList<>();
        phoneNumberList.add(inputPhoneNumber);

        if (!isNetworkOff()){
            mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
            mAuthorizeUiManager.checkAuthUser(phoneNumberList);
        }
    }

    private boolean isOwnerSelf(String inputPhoneNumber) {
        if (userSelf.equals(inputPhoneNumber)) {
            showSimpleDialog(getString(R.string.authorize_authto_me_remind), getString(R.string.ok), null);
            return true;
        }
        return false;
    }

    private boolean isShowInviteList(HPlusEditText et) {
        if (et.getEditText().length() == INPUT_MAX_LENGTH) {
            mInviteListRl.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    private void setAddListText(TextView v, String str, int chineseStart, int englishStart, int End) {
        SpannableString smsHint = new SpannableString(str);
        if (AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH)) {
            smsHint.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), chineseStart, chineseStart + End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            smsHint.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), englishStart, englishStart + End, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        v.setText(smsHint);
    }

    private String getAddListCharacter(HPlusEditText et) {
        return getString(R.string.authorize_add_phone, et.getEditorText());
    }

    private void buttonStatus(boolean isClickable) {
        mInviteBtn.setClickable(isClickable);
        mInviteBtn.setEnabled(isClickable);
    }
}
