package com.honeywell.hch.airtouch.ui.help;

import android.app.Activity;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.http.manager.model.DeviceInfo;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;

/**
 * Created by Vincent on 17/8/16.
 */
public class ManualActivity extends BaseActivity {
    private static final String TAG = "ManualActivity";
    private WebView mWebView;
    private TextView mTitleText;
    public static final String MANUAL_PARAMETER = "manual_parameters";
    public static final String MANUALTYPE = "manual_type";
    public static final String MADAIR_PARAMETER = "madair_parameters";

    private DeviceInfo mDeviceInfo;
    private WebSettings mWebSettings;
    private String mManualType = "";
    private String mDeviceType, mUserType, mCountry, mLanguage, mVersion;
    private String mLoadUrl;
    private final String USERMANUALINDEXURL = "index.htm";
    public static final String NOT_FOUND_INDEX = "404";
    private MadAirDeviceModel mMadAirDeviceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        initStatusBar();
        init();
        initData();

    }

    private void init() {
        mTitleText = (TextView) findViewById(R.id.title_textview_id);
        mWebView = (WebView) findViewById(R.id.webView);
        initDragDownManager(R.id.root_view_id);
        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        //设置支持缩放
        mWebSettings.setBuiltInZoomControls(false);
        //设置Web视图
        mWebView.setWebViewClient(new webViewClient());
    }

    private void initData() {
        mDeviceInfo = (DeviceInfo) getIntent().getSerializableExtra(MANUAL_PARAMETER);
        mManualType = getIntent().getStringExtra(MANUALTYPE);
        mMadAirDeviceModel = (MadAirDeviceModel) getIntent().getSerializableExtra(MADAIR_PARAMETER);
        if (mDeviceInfo != null) {
            mDeviceType = String.valueOf(mDeviceInfo.getDeviceType());
        } else if (mMadAirDeviceModel != null) {
//            mDeviceType = String.valueOf(mMadAirDeviceModel.getDeviceType());
            mDeviceType = String.valueOf(HPlusConstants.MAD_AIR_TYPE);
        }

        mUserType = String.valueOf(UserInfoSharePreference.getUserType());
        //ula 传过来的
        mCountry = getIntent().getStringExtra("countryCode");
        if (StringUtil.isEmpty(mCountry)) {
            mCountry = UserInfoSharePreference.getCountryCode();
        }

        mLanguage = AppConfig.shareInstance().getLanguage();
        mVersion = AppConfig.URLVERSION;

        if (mLanguage.equals(HPlusConstants.CHINA_LANGUAGE_CODE.toLowerCase())) {
            mLanguage = HPlusConstants.CHINA_LANGUAGE;
        }
        if (StringUtil.isEmpty(mCountry) || mCountry.equals(HPlusConstants.CHINA_CODE)) {
            mCountry = HPlusConstants.CHINA_STRING;
        } else if (mCountry.equals(HPlusConstants.INDIA_CODE)) {
            mCountry = HPlusConstants.INDIA_STRING;
        }
        if (HPlusConstants.INTRODUCTION_TYPE.equals(mManualType)) {
            loadIntroduction();
        } else if (HPlusConstants.USERMANUAL_TYPE.equals(mManualType)) {
            loadUserManual();
        } else if (getIntent().getBooleanExtra("eula", false)) {
            loadEULA();
        }
        mDialog = LoadingProgressDialog.show(mContext, getString(R.string.enroll_loading));
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "url: " + mLoadUrl);
        mWebView.loadUrl(mLoadUrl);
    }

    private void loadEnrollHelp() {

    }

    private void loadUserManual() {
        mTitleText.setText(getString(R.string.device_control_user_nanual));
        mLoadUrl = getLocalUrl(AppConfig.shareInstance().getUserManualUrl(), mDeviceType, mUserType, mCountry, mLanguage, mVersion);

    }

    private void loadIntroduction() {
        mTitleText.setText(getString(R.string.device_control_introduction));
        mLoadUrl = getLocalUrl(AppConfig.shareInstance().getIntroductionURL(), mDeviceType, mUserType, mCountry, mLanguage, mVersion);
    }

    private void loadEULA() {
        mTitleText.setText(getString(R.string.device_control_user_eula));
        mLoadUrl = getLocalUrl(AppConfig.shareInstance().getEULAUrl(), mCountry, mLanguage, mVersion);
    }

    //Web视图
    private class webViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:")) {
                MailTo mt = MailTo.parse(url);
                Intent i = newEmailIntent(mt.getTo(), mt.getSubject(), mt.getBody(), mt.getCc());
                ManualActivity.this.startActivity(i);
                view.reload();
            } else if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
            else {
//                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            //这段代码是为了解决 googleplay安全问题，否则apk在googleplay审核不通过
            MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.enroll_error),
                    getString(R.string.yes), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            handler.proceed();
                        }
                    }, getString(R.string.no), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            handler.cancel();
                        }
                    });
        }

        public void onPageFinished(WebView view, String url) {
            //  TODO Auto-generated method stub
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, " onPageFinished " + url);
            disMissDialog();
            super.onPageFinished(view, url);
        }

    }

    private Intent newEmailIntent(String address, String subject, String body, String cc) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_CC, cc);
        intent.setType("message/rfc822");
        return intent;
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mWebView.getUrl(): " + mWebView.getUrl());
            backAction();
        }
    }

    private String getLocalUrl(String baseUrl, Object... params) {
        if (params == null || params.length == 0) {
            return baseUrl;
        }
        return String.format(baseUrl, params);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "mWebView.getUrl(): " + mWebView.getUrl());
            return backAction();
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean backAction() {
        if (HPlusConstants.USERMANUAL_TYPE.equals(mManualType)) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return false;
            }
        }
        backIntent();
        return false;
    }
}
