package com.honeywell.hch.airtouch.ui.enroll.ui.controller.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.library.util.WifiUtil;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.CenterTextView;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.EnrollDeviceManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkManager;
import com.honeywell.hch.airtouch.ui.enroll.manager.SmartLinkUiManager;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap.ApActivateDeviceWifiActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollScanActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.smartlink.SmartLinkActivateDeviceWifiActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollIndicatorView;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;

/**
 * Special base activity for enrollment
 * Created by nan.liu on 1/26/15.
 */
public class EnrollBaseActivity extends BaseActivity {



    private static final String ONE_TIME_CHINESE = "一";
    private static final String THREE_TIME_CHINESE = "3";

    private static final String ONE_TIME_en = "one";
    private static final String THREE_TIME_en = "3";

    private static final String TWO_TIME_CHINESE = "两";
    private static final String TEN_TIME_CHINESE = "10";

    private static final String TWO_TIME_en = "two";
    private static final String TEN_TIME_en = "10";

    protected boolean isHasWifi = false;
    protected SmartLinkManager mSmartLinkManager;
    protected SmartLinkUiManager mSmartLinkUiManager;
    protected FrameLayout mBackFrameLayout;
    protected TextView mTitleTextView;
    protected EnrollIndicatorView mEnrollIndicatorView;
    protected CenterTextView mCenterTextView;

    //用于记录H+进程是否被杀掉（在6.0上，把权限关闭后，会把Activity里保存过的东西都删除。所以在enroll的时候，如果这个标志位为0.说明被清空了
    // 所以需要结束enroll流程）,所以只要在enroll前赋值为1就可以。
    public static int mAliveFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
        if (mAliveFlag == 0){
            quiteSmartlinkProcess();
            finish();
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            gotoActivityWithIntent(intent, false);
        }

    }


    /**
     * 设置enroll步骤indicator view的显示,不带有提示的
     *
     * @param isVisible  是否需要显示
     * @param totalStep  总共几步
     * @param stepNumber 当前处于第几步
     */
    protected void initEnrollStepView(boolean isNeedBack,boolean isVisible, int totalStep, int stepNumber) {
        initTitleBackLayout(isNeedBack);
        if (isVisible) {
            EnrollIndicatorView enrollIndicatorView = (EnrollIndicatorView) findViewById(R.id.indicator_id);
            enrollIndicatorView.setVisibility(View.VISIBLE);
            enrollIndicatorView.setEnrollStepParams(totalStep, stepNumber);
        }
    }


    protected void initTitleView(boolean isbackArrowShow,String titleStr,int totalStep,int currentStep,String centerTextStr,boolean isAllProcessSuccess) {
        initTitleBackLayout(isbackArrowShow);
        initTitleString(titleStr);
        initEnrollIndicatorView(totalStep,currentStep, isAllProcessSuccess);
        initTitleCenterView(centerTextStr);
    }


    protected void onBackIconAction(){
        backIntent();
    }

    protected void initTitleBackLayout(boolean isVisible) {
        mBackFrameLayout = (FrameLayout) findViewById(R.id.enroll_back_layout);
        int visible = isVisible ? View.VISIBLE : View.GONE;
        mBackFrameLayout.setVisibility(visible);

        if (isVisible) {
            mBackFrameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackIconAction();
                }
            });
        }
    }

    private void initTitleString(String titleStr){
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(titleStr);
    }

    private void initEnrollIndicatorView(int totalStep,int currentStep,boolean isAllProcessSuccess){
        if (mEnrollIndicatorView == null){
            mEnrollIndicatorView = (EnrollIndicatorView) findViewById(R.id.indicator_id);

            if (isAllProcessSuccess){
                mEnrollIndicatorView.setEnrollStepAllSuccess(totalStep);
            }else{
                mEnrollIndicatorView.setEnrollStepParams(totalStep, currentStep);

            }
        }
    }

    protected void initTitleCenterView(String centerStr){
        mCenterTextView = (CenterTextView) findViewById(R.id.input_tip_id);
        mCenterTextView.setVisibility(View.VISIBLE);
        mCenterTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mCenterTextView.setText(centerStr);

    }

    private void init() {
        /*
         * Umeng statistic
         */
        mContext = this;
        //enroll 后 都删除
        CloseActivityUtil.enrollActivityList.add(this);

        registerNetworkChangeBroadcast();
        mSmartLinkManager = new SmartLinkManager();
        mSmartLinkUiManager = new SmartLinkUiManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        disMissDialog();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkBroadcast();
    }


    public BroadcastReceiver networkChangeBroadcastRec = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                if (!NetWorkUtil.isWifiAvailable(context)) {
                    isHasWifi = false;
                    dealNoNetwork();
                } else {
                    isHasWifi = true;
                    dealNetworkConnect();
                }
            }
        }
    };

    protected void dealNoNetwork() {

    }

    protected void dealNetworkConnect() {

    }

    private void registerNetworkChangeBroadcast() {
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeBroadcastRec, mFilter);
    }

    private void unregisterNetworkBroadcast() {
        if (networkChangeBroadcastRec != null) {
            unregisterReceiver(networkChangeBroadcastRec);
        }
    }

    protected MessageBox.MyOnClick messageBoxClick = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            disMissDialog();
        }
    };

    protected MessageBox.MyOnClick quitEnroll = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {

            quiteSmartlinkProcess();
        }
    };

    protected MessageBox.MyOnClick quitAPEnroll = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {

            quitAPEnrollProcess();
        }
    };


    protected void quitAPEnrollProcess(){
        disMissDialog();
        mAlertDialog = MessageBox.createSimpleDialog(this, null, getString(R.string.quit_enroll_confirm_msg), getString(R.string.ok), new MessageBox.MyOnClick() {
            @Override
            public void onClick(View v) {
                String wifiSSid = WifiUtil.getCurrentWifiSSID(EnrollBaseActivity.this);
                if (StringUtil.isEmpty(wifiSSid) || !wifiSSid.equals(DIYInstallationState.getmHomeConnectedSsid())){
                    //如果退出enroll时候，手机没有连上网络或是手机连接的网络和enroll前的wifi不一样，需要重新连接wifi
                    EnrollDeviceManager enrollDeviceManager = new EnrollDeviceManager();
                    enrollDeviceManager.reconnectHomeWifi();
                }

                UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_CANCEL, "");
                CloseActivityUtil.exitEnrollClient(mContext);
                backIntent();
            }
        });

    }

    protected MessageBox.MyOnClick quitSmartlinkEnroll = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {

            quiteSmartlinkProcess();
        }
    };

    protected void quiteSmartlinkProcess(){
        disMissDialog();
        UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_CANCEL, "");
        CloseActivityUtil.exitEnrollClient(mContext);
        backIntent();
    }


    /**
     * AP的重试，点击重试按钮需要弹框提示用户wifi切换，并且跳转到ap得连接wifi界面
     * @param message
     */
    public void showRetryActionWithReconnectWifi(String message){
        mAlertDialog = MessageBox.createTwoButtonDialog(this, null, message
                , getString(R.string.enroll_exit), quitAPEnroll, getString(R.string.retry), new MessageBox.MyOnClick() {
            @Override
            public void onClick(View v) {
               disMissDialog();
                UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(), UmengUtil.EnrollEventType.ENROLL_CANCEL, "");

                BackActivityWithNoFinishIntent(ApActivateDeviceWifiActivity.class);

            }
        });
    }

    /**
     * 用于smartlink的重试，不需要提示用户网络已经切换，并且跳转到smartlink的界面
     * @param message
     */
    public void showRetryAction(String message){
        mAlertDialog = MessageBox.createTwoButtonDialog(this, null, message
                , getString(R.string.enroll_exit), quitSmartlinkEnroll, getString(R.string.retry), new MessageBox.MyOnClick() {
            @Override
            public void onClick(View v) {
                disMissDialog();
                BackActivityWithNoFinishIntent(SmartLinkActivateDeviceWifiActivity.class);
            }
        });
    }


    /**
     * 获取焦点
     *
     * @param view
     */
    public void setViewGetFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    /**
     * enroll除了第一个界面外，其他的界面都不需要对网络和networkerro进行显示
     */
    @Override
    protected void initNetWorkLayout() {
    }


    /**
     * 去设置界面的对话框
     * @param messageStrId
     */
    public void goToSettingDialog(int messageStrId) {
        if (mAlertDialog == null) {
            mAlertDialog = MessageBox.createSimpleDialog(this, "", getResources().getString(messageStrId),
                    getResources().getString(R.string.go_to_setting), new MessageBox.MyOnClick() {
                        @Override
                        public void onClick(View v) {
                            if (mAlertDialog != null) {
                                mAlertDialog.cancel();
                                mAlertDialog = null;
                            }

                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    });
            try {
                mAlertDialog.show();
            } catch (Exception e) {

            }

        }

    }



}


