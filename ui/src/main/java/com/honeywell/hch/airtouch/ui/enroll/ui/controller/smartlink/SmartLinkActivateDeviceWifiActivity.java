package com.honeywell.hch.airtouch.ui.enroll.ui.controller.smartlink;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by Vincent on 25/11/15.
 */
public class SmartLinkActivateDeviceWifiActivity extends EnrollBaseActivity {
    private TextView mTitleVIewCn;
    private TextView mTitleTextView;
    private ImageView mMachineView;
    private Button mNextButton;
    private final long delayTime = 3800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartlink_choose);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);

        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;
        initEnrollStepView(true, true, totalStep, EnrollConstants.STEP_ONE);
    }

    private void initView() {
        mContext = this;
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.enroll_title_activie);
        mTitleVIewCn = (TextView) findViewById(R.id.input_tip_id);
        mMachineView = (ImageView) findViewById(R.id.enroll_choose_machine_image);
        mNextButton = (Button) findViewById(R.id.enroll_choose_nextBtn);
        mNextButton.setClickable(false);
        mNextButton.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mNextButton.setClickable(true);
                mNextButton.setEnabled(true);
            }
        }, delayTime);
        initTextandImage();
    }

    private void initTextandImage() {
        mTitleVIewCn.setText(getSpanable(R.string.enroll_smartlink_hear_time,new int[]{R.string.spannable_string_3,R.string.spannable_string_1}));
        mTitleVIewCn.setVisibility(View.VISIBLE);
        mTitleTextView.setVisibility(View.VISIBLE);
        mMachineView.setImageResource(EnrollScanEntity.getEntityInstance().getDeviceImage());
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_choose_nextBtn) {
            startIntent(SmartlinkConnectDeviceToInternetActivity.class);
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

    @Override
    protected void dealNetworkConnect() {
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }

    }

    @Override
    protected void dealNoNetwork() {
        if (mAlertDialog == null || !mAlertDialog.isShowing()) {
            goToSettingDialog(R.string.enroll_wifi_unavaiable);
        }
    }

}
