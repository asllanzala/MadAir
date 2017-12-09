package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.permission.HPlusPermission;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by h127856 on 16/10/17.
 */
public class ApSendWifiInfoErrorActivity extends EnrollBaseActivity implements View.OnClickListener{

    private final String TAG = "RetryCheckDeviceIsOnlineActivity";

    private final String CONTACT_PHONE_NUMBER = "4007204321";

    private final int TEXT_CLICKABLE_SIZE = 15;

    private TextView mTitleNoUserTextView;

    private TextView mContactServicerText;

    private TextView mUserApModuleText;

    private TextView mTitleTextView;

    private Button mRetryBtn;

    private Button mExitEnrollBtn;

    private HPlusPermission mHPlusPermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.TAG = TAG;
        setContentView(R.layout.activity_ap_sendwifi_info_error);
        initStatusBar();
        initView();
        initDragDownManager(R.id.root_view_id);

        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;

        initEnrollStepView(false,true,totalStep,EnrollConstants.STEP_TWO);
    }

    private void initView() {
        mTitleNoUserTextView = (TextView) findViewById(R.id.input_tip_id);
        mTitleNoUserTextView.setVisibility(View.GONE);

        mContactServicerText = (TextView) findViewById(R.id.title_content1_id);

        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.connecting_failed_title);

        mUserApModuleText = (TextView) findViewById(R.id.content3_1);
        initClickView(mUserApModuleText, getString(R.string.connect_timeout_content3), 7, 11, 16, 37, new ClickOperator() {
            @Override
            public void dealClick() {
                //call customer service
                if (checkPhoneCallPermission()) {
                    callPhone();
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.phone_call_permission_deny), Toast.LENGTH_LONG).show();
                }
            }
        });


        mRetryBtn = (Button) findViewById(R.id.retrybtn_id);
        mRetryBtn.setOnClickListener(this);

        mExitEnrollBtn = (Button) findViewById(R.id.exit_btn);
        mExitEnrollBtn.setOnClickListener(this);

        mHPlusPermission = new HPlusPermission(this);
        mHPlusPermission.requestCallPhonePermission(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callPhone() {
        Intent intent;
        if (AppConfig.shareInstance().isIndiaAccount()) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.INDIA_CONTACT_PHONE_NUMBER));
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.CONTACT_PHONE_NUMBER));
        }
        startActivity(intent);
    }

    private boolean checkPhoneCallPermission() {
        boolean currentPhoneCallPermission = mHPlusPermission.isCallPhonePermissionGranted(this);
        if (currentPhoneCallPermission) {
            return true;
        }
        return false;
    }


    private void initClickView(TextView v, String str, int chineseStart, int chineseEnd, int englishStart, int englishEnd, final ClickOperator clickOperator) {
        SpannableString ssTitle = new SpannableString(str);
        v.setMovementMethod(LinkMovementMethod.getInstance());

        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.enroll_blue2));
                ds.setTextSize(DensityUtil.sp2px(TEXT_CLICKABLE_SIZE));
                ds.setUnderlineText(false);    //去除超链接的下划线
            }

            @Override
            public void onClick(View widget) {
                clickOperator.dealClick();
            }
        };

        if (AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH)) {
            ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 0, chineseStart - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTitle.setSpan(clickableSpan, chineseStart, chineseEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (chineseEnd + 1 < str.length()) {
                ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), chineseEnd, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


        } else {
            ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 0, englishStart + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssTitle.setSpan(clickableSpan, englishStart, englishEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (englishEnd + 1 < str.length()) {
                ssTitle.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), englishStart + 1, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        v.setText(ssTitle);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.exit_btn) {
            quitAPEnrollProcess();
        } else if (id == R.id.retrybtn_id) {
            startIntent(ApEnrollConnectWifiActivity.class);
        }

    }

    interface ClickOperator {
        public void dealClick();
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }

    }
}
