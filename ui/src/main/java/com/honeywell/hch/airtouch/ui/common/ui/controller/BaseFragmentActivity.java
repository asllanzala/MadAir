package com.honeywell.hch.airtouch.ui.common.ui.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;

/**
 * Base fragment activity, implement some common function
 * Created by Jin Qian on 1/30/15.
 */
public class BaseFragmentActivity extends FragmentActivity {
    protected String TAG = "BaseFragmentActivity";
    protected Context mContext;
    protected BroadcastReceiver networkBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        CloseActivityUtil.activityList.add(this);
        UmengUtil.visitPageEvent(this.getLocalClassName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏
            // 状态栏字体设置为深色，SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 为SDK23增加
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

//            // 部分机型的statusbar会有半透明的黑色背景
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
        }
        registerNetworkReceiver();
    }

    protected void initStatusBar() {
        StatusBarUtil.changeStatusBarTextColor(findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void onResume() {
        super.onResume();
        UmengUtil.onFragmentActivityResume(mContext, TAG);
    }

    public void onPause() {
        super.onPause();
        UmengUtil.onFragmentActivityPause(mContext, TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CloseActivityUtil.activityList.remove(this);
        unRegisterNetwork();
    }

    public void backIntent() {
        finish();
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // when the progress is finding the device , can not be back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backIntent();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class NetWorkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "action: " + action);
            if (HPlusConstants.NET_WORK_ERROR.equals(action)) {
                dealWithNetworkError();
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action) || HPlusConstants.NETWORK_CONNECT_SERVER_WELL.equals(action)) {
                if (NetWorkUtil.isNetworkAvailable(BaseFragmentActivity.this)) {
                    if (!UserAllDataContainer.shareInstance().isHasNetWorkError()){
                        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "手机有网络------");
                        dealNetworkConnected();
                    }
                } else  {
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "手机没有任何网络....");
                    dealWithNoNetWork();
                }

            }

        }
    }

    private void registerNetworkReceiver() {
        networkBroadcast = new NetWorkBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(HPlusConstants.NET_WORK_ERROR);
        filter.addAction(HPlusConstants.NETWORK_CONNECT_SERVER_WELL);
        this.registerReceiver(networkBroadcast, filter);
    }

    private void unRegisterNetwork() {
        unregisterReceiver(networkBroadcast);
    }

    protected void dealWithNoNetWork() {
    }

    protected void dealWithNetworkError() {
    }

    protected void dealNetworkConnected() {
    }

}
