package com.honeywell.hch.airtouch.ui.enroll.ui.controller.madair;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by Jin on 11/23/16.
 */
public class MadAirEnrollFinishActivity extends EnrollBaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enroll_mad_air_finish);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);
    }

    private void initView() {
        super.initTitleView(false, getString(R.string.enroll_finish), EnrollConstants.TOTAL_THREE_STEP, EnrollConstants.STEP_THREE,
                getString(R.string.enroll_success_msg), true);
        Intent boradIntent = new Intent(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
        AppManager.getInstance().getApplication().sendBroadcast(boradIntent);

        Intent intent = new Intent(HPlusConstants.ADD_DEVICE_OR_HOME_ACTION);
        intent.putExtra(HPlusConstants.IS_ADD_HOME, true);
        intent.putExtra(HPlusConstants.LOCAL_LOCATION_ID, DIYInstallationState.getLocationId());
        AppManager.getInstance().getApplication().sendBroadcast(intent);
    }

    public void doClick(View v) {
        if (v.getId() == R.id.mad_air_confirm_btn) {
            CloseActivityUtil.exitEnrollClient(mContext);
            backIntent();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
