package com.honeywell.hch.airtouch.ui.control.ui.group.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseFragmentActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.HplusNetworkErrorLayout;
import com.honeywell.hch.airtouch.ui.control.ui.group.view.GroupControlFragment;

/**
 * Created by Vincent on 20/7/16.
 */
public class GroupControlActivity extends BaseFragmentActivity {
    private final String TAG = "GroupControlActivity";
    private GroupControlFragment mGroupControlFragment;

    private BroadcastReceiver mRefreshGroupScenarioBc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_control_activity);
        initStatusBar();
        loadFragment();
        registerScenarioFrefesh();
    }

    public void loadFragment() {
        mGroupControlFragment = GroupControlFragment.newInstance(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.all_device_pale, mGroupControlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            if (mGroupControlFragment.setEndTipBackEvent()) {
                return false;
            }
            backIntent();
            return false;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unrgisterBc();
    }
    @Override
    public void dealWithNoNetWork(){
        mGroupControlFragment.getNetWorkErrorLayout().setVisibility(View.VISIBLE);
        mGroupControlFragment.getNetWorkErrorLayout().setErrorMsg(HplusNetworkErrorLayout.NETWORK_OFF);
        mGroupControlFragment.dealNetworkoffAndOnAction();
    }

    @Override
    public void dealWithNetworkError(){
        mGroupControlFragment.getNetWorkErrorLayout().setVisibility(View.VISIBLE);
        mGroupControlFragment.getNetWorkErrorLayout().setErrorMsg(HplusNetworkErrorLayout.CONNECT_SERVER_ERROR);
        mGroupControlFragment.dealNetworkoffAndOnAction();
    }

    @Override
    public void dealNetworkConnected(){
        mGroupControlFragment.getNetWorkErrorLayout().setVisibility(View.GONE);
        mGroupControlFragment.dealNetworkoffAndOnAction();
    }

    private void registerScenarioFrefesh() {
        mRefreshGroupScenarioBc = new ScenarioChangeBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(HPlusConstants.GROUP_SCENARIO_REFRESH);
        this.registerReceiver(mRefreshGroupScenarioBc, filter);
    }

    private class ScenarioChangeBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "action: " + action);
            if (HPlusConstants.GROUP_SCENARIO_REFRESH.equals(action)) {
                mGroupControlFragment.reGetGroupInfoFromServer();
            }

        }
    }

    private void unrgisterBc(){
        if (mRefreshGroupScenarioBc != null){
            unregisterReceiver(mRefreshGroupScenarioBc);
        }
    }

}
