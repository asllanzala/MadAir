package com.honeywell.hch.airtouch.ui.main.ui.me;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.storage.UpdateVersionPreference;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.tutorial.controller.TutorialActivity;
import com.honeywell.hch.airtouch.ui.update.ui.UpdateVersionMinderActivity;

/**
 * Created by Vincent on 4/8/16.
 */
public class AboutActivity extends BaseActivity {

    private TextView mAboutTitleTv;
    private TextView mVersionTv;
    private PackageInfo mInfo;
    private LinearLayout mNewVersionLl;
    private TextView mNewVersionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initStatusBar();
        init();
        initData();
    }

    private void init() {
        mAboutTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mVersionTv = (TextView) findViewById(R.id.me_about_version_detail);
        mNewVersionLl = (LinearLayout) findViewById(R.id.me_about_new_version_ll);
        mNewVersionTv = (TextView) findViewById(R.id.me_about_new_version_detail);
    }

    private void initData() {
        mAboutTitleTv.setText(getString(R.string.me_about));
        try {
            mInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mVersionTv.setText(mInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mVersionTv.setText(getString(R.string.group_control_na));
        }
        if (UpdateVersionPreference.getOwnUpdate()) {
            mNewVersionTv.setText(UpdateManager.getInstance().getVersionCollector().getVersionName());
            mNewVersionLl.setVisibility(View.VISIBLE);
            updateMeRedDot();
        }
    }

    private void updateMeRedDot() {
        UpdateVersionPreference.saveMeRedDot(false);
        Intent boradIntent = new Intent(HPlusConstants.UPDATE_ME_RED_DOT_ACTION);
        sendBroadcast(boradIntent);
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.me_new_features_tv) {
            startIntent(TutorialActivity.class);
        } else if (v.getId() == R.id.me_new_install_tv) {
            goToUpdateVersionMinderActivity();
        }
    }

    private void goToUpdateVersionMinderActivity() {
        Intent intent = new Intent(mContext, UpdateVersionMinderActivity.class);
        intent.putExtra(UpdateManager.VERSION_DATA, UpdateManager.getInstance().getVersionCollector());
        intent.putExtra(UpdateManager.UPDATE_TYPE, HPlusConstants.NORMAL_UPDATE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppManager.getInstance().getApplication().startActivity(intent);
    }

}
