package com.honeywell.hch.airtouch.ui.debug;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.storage.SwitchSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;

/**
 * Created by Vincent on 9/11/16.
 */
public class SwitchEnvActivity extends BaseActivity {
    private Button mQaBtn;
    private Button mStageBtn;
    private Button mProductBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        init();
        initData();
    }

    private void init() {
        mQaBtn = (Button) findViewById(R.id.me_qa);
        mStageBtn = (Button) findViewById(R.id.me_stage);
        mProductBtn = (Button) findViewById(R.id.me_product);
    }

    private void initData() {
        //设置网络切换
        switch (AppConfig.urlEnv) {
            case AppConfig.QA_ENV:
                buttonStatus(mQaBtn, false);
                break;
            case AppConfig.STAGE_ENV:
                buttonStatus(mStageBtn, false);
                break;
            case AppConfig.PRODUCT_ENV:
                buttonStatus(mProductBtn, false);
                break;
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.me_qa) {
            SwitchSharePreference.saveDevelopEnv(AppConfig.QA_ENV);
            logout();
        } else if (v.getId() == R.id.me_stage) {
            SwitchSharePreference.saveDevelopEnv(AppConfig.STAGE_ENV);
            logout();
        } else if (v.getId() == R.id.me_product) {
            SwitchSharePreference.saveDevelopEnv(AppConfig.PRODUCT_ENV);
            logout();
        }
    }

    private void logout() {
        AppConfig.shareInstance().loadAppInfo();
        Intent logOutIntent = new Intent(HPlusConstants.LOGOUT_ACTION);
        logOutIntent.putExtra(HPlusConstants.NEED_UPDATE, false);
        AppManager.getInstance().getApplication().getApplicationContext().sendOrderedBroadcast(logOutIntent, null);
    }

    private void buttonStatus(Button button, boolean isClickable) {
        button.setClickable(isClickable);
        button.setEnabled(isClickable);
    }
}
