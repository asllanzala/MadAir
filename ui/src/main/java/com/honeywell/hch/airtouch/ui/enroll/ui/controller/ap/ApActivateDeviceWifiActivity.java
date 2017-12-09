package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Enrollment Step 1 - SmartPhone find Air Touch's SSID.
 * Scanning surrounding every 1s.
 */
public class ApActivateDeviceWifiActivity extends EnrollBaseActivity {

    private static final String TAG = "AirTouchEnrollWelcome";

    private ImageView mMachineView;
    private Button mNextButton;
    private final long delayShortTime = 3800;
    private final long delayLongTime = 11000;
    private long delayTime = 3800;
    private TextView mFromApTextView;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartlink_choose);
        initStatusBar();
        initView();
        initTextandImage();
        initDragDownManager(R.id.root_view_id);
        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;
        initEnrollStepView(true, true, totalStep, EnrollConstants.STEP_ONE);

    }

    private void initView() {
        mContext = this;
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.enroll_title_activie);
        mFromApTextView = (TextView) findViewById(R.id.input_tip_id);
        mMachineView = (ImageView) findViewById(R.id.enroll_choose_machine_image);
        mNextButton = (Button) findViewById(R.id.enroll_choose_nextBtn);
        mNextButton.setClickable(false);
        mNextButton.setEnabled(false);
    }

    private void initTextandImage() {
        if (DeviceType.isApMode(EnrollScanEntity.getEntityInstance().getmDeviceType())) {
            delayTime = delayShortTime;
            mFromApTextView.setText(getSpanable(R.string.enroll_smartlink_hear_time,new int[]{R.string.spannable_string_3,R.string.spannable_string_1}));
        } else {
            delayTime = delayLongTime;
            mFromApTextView.setText(getSpanable(R.string.enroll_ap_hear_time,new int[]{R.string.spannable_string_2,R.string.spannable_string_4}));
        }
        mFromApTextView.setVisibility(View.VISIBLE);
        mMachineView.setImageResource(EnrollScanEntity.getEntityInstance().getDeviceImage());
        new Handler().postDelayed(new Runnable() {
            public void run() {
                mNextButton.setClickable(true);
                mNextButton.setEnabled(true);
            }
        }, delayTime);
    }




    public void doClick(View v) {
        if (v.getId() == R.id.enroll_choose_nextBtn) {
            startIntent(ApPhoneToDeviceActivity.class);
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

}
