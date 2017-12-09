package com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.permission.HPlusPermission;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.view.CenterTextView;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

/**
 * Created by h127856 on 16/10/13.
 */
public class EnrollResultActivity extends EnrollBaseActivity {

    private int mEnrollResult;

    private ImageView mResultIcon;
    private Button mActionOneBtn;
    private Button mActionTwoBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll_result);
        initStatusBar();

        mEnrollResult = getIntent().getIntExtra(EnrollConstants.ENROLL_RESULT, EnrollConstants.ENROLL_RESULT_OTHER_FAILED);
        initView();
    }

    private void initView() {
        mResultIcon = (ImageView) findViewById(R.id.result_icon);
        mActionOneBtn = (Button) findViewById(R.id.action_one);
        mActionTwoBtn = (Button) findViewById(R.id.action_two);

        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mCenterTextView = (CenterTextView) findViewById(R.id.input_tip_id);

        if (mEnrollResult == EnrollConstants.ENROLL_RESULT_ADDDEVICE_SUCCESS) {
            mResultIcon.setImageResource(R.drawable.done);
            super.initTitleView(false, getString(R.string.enroll_finish), EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_FOUR, getString(R.string.enroll_success_msg),true);
            showOneBtn(getString(R.string.done), new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    quiteSmartlinkProcess();
                }
            });
        } else if (mEnrollResult == EnrollConstants.ENROLL_RESULT_UPDATE_WIFI_SUCCESS) {
            mResultIcon.setImageResource(R.drawable.done);

            super.initTitleView(false, getString(R.string.enroll_finish), EnrollConstants.TOTAL_TWO_STEP, EnrollConstants.STEP_TWO, getString(R.string.device_update_wifi_success),true );
            showOneBtn(getString(R.string.done), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quiteSmartlinkProcess();
                }
            });
        } else if (mEnrollResult == EnrollConstants.ENROLL_RESULT_ADDDEVICE_TIMEOUT) {
            mResultIcon.setImageResource(R.drawable.failed_icon);

            mTitleTextView.setText(getString(R.string.enroll_failed_title));
            initEnrollStepView(false, true, EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_FOUR);
            initCenterTextView(R.string.enroll_failed_timeout,R.string.custom_care_string);

            showTwoBtn(getString(R.string.enroll_exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quiteSmartlinkProcess();

                }
            },getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToOtherActivity();
                }
            });
        } else if (mEnrollResult == EnrollConstants.ENROLL_RESULT_REGISTER_BY_OTHER) {

            mResultIcon.setImageResource(R.drawable.failed_icon);

            mTitleTextView.setText(getString(R.string.enroll_failed_title));
            initEnrollStepView(false, true, EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_FOUR);
            initCenterTextView(R.string.enroll_failed_register_by_other,R.string.custom_care_string);

            showOneBtn(getString(R.string.enroll_exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quiteSmartlinkProcess();
                }
            });
        } else if (mEnrollResult == EnrollConstants.ENROLL_RESULT_NOT_GET_COMMTASK_RESULT_FAILED) {
            mResultIcon.setImageResource(R.drawable.failed_icon);
            super.initTitleView(false, getString(R.string.enroll_failed_title), EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_FOUR, getString(R.string.enroll_failed_commtask_failed),false);
            showOneBtn(getString(R.string.enroll_exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quiteSmartlinkProcess();
                }
            });
        } else if (mEnrollResult == EnrollConstants.ENROLL_RESULT_OTHER_FAILED) {

            mResultIcon.setImageResource(R.drawable.failed_icon);
            mTitleTextView.setText(getString(R.string.enroll_failed_title));
            initEnrollStepView(false, true, EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_FOUR);
            initCenterTextView(R.string.enroll_failed_other_failed,R.string.custom_care_string);

            showOneBtn(getString(R.string.enroll_exit), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quiteSmartlinkProcess();
                }
            });
        }
    }

    private void initCenterTextView(int centerTextStrId,int spannableStrId){
        String msgStr = getString(centerTextStrId);
        String spanableStr = getString(spannableStrId);
        initClickSpanableStringView(msgStr,msgStr.indexOf(spanableStr),msgStr.indexOf(spanableStr) + spanableStr.length());
    }

    private void initClickSpanableStringView( String str, int start, int eEnd) {
        SpannableString ssTitle = new SpannableString(str);
        mCenterTextView.setMovementMethod(LinkMovementMethod.getInstance());
        mCenterTextView.setVisibility(View.VISIBLE);
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);    //去除超链接的下划线
            }

            @Override
            public void onClick(View widget) {
                //call customer service
                requstCallPermission();
            }
        };

        ssTitle.setSpan(new StyleSpan(Typeface.BOLD),start, eEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssTitle.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_one)), start, eEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssTitle.setSpan(clickableSpan, start, eEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mCenterTextView.setText(ssTitle);
    }

    private void showOneBtn(String btnStr, View.OnClickListener clickListener) {
        mActionTwoBtn.setVisibility(View.GONE);
        mActionOneBtn.setVisibility(View.VISIBLE);
        mActionOneBtn.setText(btnStr);
        mActionOneBtn.setOnClickListener(clickListener);
    }

    private void showTwoBtn(String btnOneStr, View.OnClickListener oneClickListener, String btnTwoStr, View.OnClickListener twoClickListener) {
        mActionOneBtn.setVisibility(View.VISIBLE);
        mActionOneBtn.setText(btnOneStr);
        mActionOneBtn.setOnClickListener(oneClickListener);

        mActionTwoBtn.setVisibility(View.VISIBLE);
        mActionTwoBtn.setText(btnTwoStr);
        mActionTwoBtn.setOnClickListener(twoClickListener);
    }

    private void goToOtherActivity() {
        Intent intent = new Intent();
        intent.setClass(this, EnrollRegiterDeviceActivity.class);
        gotoActivityWithIntent(intent, true);
    }



    private void requstCallPermission() {
        mHPlusPermission = new HPlusPermission(this);
        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE, this);

    }

    private void checkPermissionAndAction(boolean isHasPermission) {

        if (isHasPermission) {
            callCustomerServicePhone();
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.phone_call_permission_deny), Toast.LENGTH_LONG).show();
        }
    }


    private void callCustomerServicePhone() {
        Intent intent;
        if (AppConfig.shareInstance().isIndiaAccount()) {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.INDIA_CONTACT_PHONE_NUMBER));
        } else {
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + HPlusConstants.CONTACT_PHONE_NUMBER));
        }
        startActivity(intent);
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        if (permissionCode == Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE) {
            checkPermissionAndAction(true);
        }

    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permission, permissionCode);
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
        checkPermissionAndAction(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.CALL_PHONE_REQUEST_CODE:
                checkPermissionAndAction(mHPlusPermission.verifyPermissions(grantResults));
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
