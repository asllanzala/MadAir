package com.honeywell.hch.airtouch.ui.control.ui.device.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;

/**
 * Created by Jesse on 22/7/16.
 */
public class DeviceControlView extends RelativeLayout {
    private final String TAG = "DeviceControlView";
    private Context mContext;

    private final static float ALPHA_DISABLE = 0.5f;
    private final static float ALPHA_ENABLE = 1.0f;
    private final int INTERVAL = 1000;
    private long mClickTime = 0;


    private TextView mPowerTv;
    private TextView mSleepTv;
    private TextView mAutoTv;
    private TextView mFastTv;
    private TextView mQuietTv;
    private TextView mFanSpeedTv;

    private Drawable mSleepGreyDb;
    private Drawable mSleepLightDb;
    private Drawable mAutoGreyDb;
    private Drawable mAutoLightDb;
    private Drawable mFastLightDb;
    private Drawable mFastGreyDb;
    private Drawable mQuietLightDb;
    private Drawable mQuietGreytDb;
    private Drawable mPowerGreyDb;
    private Drawable mPowerLightDb;
    private ColorStateList mModeOnTextColor, mModeOffTextColor;

    /**
     * 不能进入下一级界面的时候，箭头要设置为透明度30%
     */
    private final static float ALPHA_ARROR_CANNOT_GO = 0.3f;

    public DeviceControlView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public DeviceControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.device_control_view, this);
        initControlModeDrawable();
        mPowerTv = (TextView) v.findViewById(R.id.device_control_power_tv);
        mSleepTv = (TextView) v.findViewById(R.id.device_control_sleep_tv);
        mAutoTv = (TextView) v.findViewById(R.id.device_control_auto_tv);
        mFastTv = (TextView) v.findViewById(R.id.device_control_fast_tv);
        mQuietTv = (TextView) v.findViewById(R.id.device_control_quiet_tv);
        mFanSpeedTv = (TextView) v.findViewById(R.id.device_control_fanspeed_tv);
    }

    private void initControlModeDrawable() {
        mSleepGreyDb = getResources().getDrawable(R.drawable.device_sleepoff_btn);
        mSleepLightDb = getResources().getDrawable(R.drawable.device_sleepon_btn);
        mAutoGreyDb = getResources().getDrawable(R.drawable.device_autooff_btn);
        mAutoLightDb = getResources().getDrawable(R.drawable.device_autoon_btn);
        mFastGreyDb = getResources().getDrawable(R.drawable.device_fastoff_btn);
        mFastLightDb = getResources().getDrawable(R.drawable.device_faston_btn);
        mQuietGreytDb = getResources().getDrawable(R.drawable.device_quietoff_btn);
        mQuietLightDb = getResources().getDrawable(R.drawable.device_quieton_btn);
        mPowerGreyDb = getResources().getDrawable(R.drawable.device_poweroff_btn);
        mPowerLightDb = getResources().getDrawable(R.drawable.device_poweron_btn);

        mSleepGreyDb.setBounds(0, 0, mSleepGreyDb.getMinimumWidth(), mSleepGreyDb.getMinimumHeight());
        mSleepLightDb.setBounds(0, 0, mSleepLightDb.getMinimumWidth(), mSleepLightDb.getMinimumHeight());
        mAutoGreyDb.setBounds(0, 0, mAutoGreyDb.getMinimumWidth(), mAutoGreyDb.getMinimumHeight());
        mAutoLightDb.setBounds(0, 0, mAutoLightDb.getMinimumWidth(), mAutoLightDb.getMinimumHeight());
        mFastGreyDb.setBounds(0, 0, mFastGreyDb.getMinimumWidth(), mFastGreyDb.getMinimumHeight());
        mFastLightDb.setBounds(0, 0, mFastLightDb.getMinimumWidth(), mFastLightDb.getMinimumHeight());
        mQuietGreytDb.setBounds(0, 0, mQuietGreytDb.getMinimumWidth(), mQuietGreytDb.getMinimumHeight());
        mQuietLightDb.setBounds(0, 0, mQuietLightDb.getMinimumWidth(), mQuietLightDb.getMinimumHeight());
        mPowerGreyDb.setBounds(0, 0, mPowerGreyDb.getMinimumWidth(), mPowerGreyDb.getMinimumHeight());
        mPowerLightDb.setBounds(0, 0, mPowerLightDb.getMinimumWidth(), mPowerLightDb.getMinimumHeight());
        mModeOnTextColor = getResources().getColorStateList(R.color.auth_cancel_text_color);
        mModeOffTextColor = getResources().getColorStateList(R.color.text_common);
    }

    public void setmFanSpeedTv(String speed) {
        mFanSpeedTv.setText(speed);
    }

    //设置颜色
    public void setControlTv(String mode) {
        stopAllFlick();
        enableClick();
        resetTextColor();
        switch (mode) {
            case HPlusConstants.MODE_SLEEP:
                mSleepTv.setClickable(false);
                mSleepTv.setTextColor(mModeOnTextColor);
                mSleepTv.setCompoundDrawables(null, mSleepLightDb, null, null);
                showPower(true);
                break;
            case HPlusConstants.MODE_AUTO:
                mAutoTv.setClickable(false);
                mAutoTv.setTextColor(mModeOnTextColor);
                mAutoTv.setCompoundDrawables(null, mAutoLightDb, null, null);
                showPower(true);
                break;
            case HPlusConstants.MODE_QUICK:
                mFastTv.setClickable(false);
                mFastTv.setTextColor(mModeOnTextColor);
                mFastTv.setCompoundDrawables(null, mFastLightDb, null, null);
                showPower(true);
                break;
            case HPlusConstants.MODE_SILENT:
                mQuietTv.setClickable(false);
                mQuietTv.setTextColor(mModeOnTextColor);
                mQuietTv.setCompoundDrawables(null, mQuietLightDb, null, null);
                showPower(true);
                break;
            case HPlusConstants.MODE_OFF:
                mPowerTv.setClickable(false);
                mPowerTv.setTextColor(mModeOnTextColor);
                mPowerTv.setCompoundDrawables(null, mPowerLightDb, null, null);
                break;
            default:
                break;

        }
    }

    public void showPower(boolean on) {
        if (on) {
            mPowerTv.setTextColor(mModeOnTextColor);
            mPowerTv.setClickable(true);
            mPowerTv.setCompoundDrawables(null, mPowerLightDb, null, null);
        } else {
            mPowerTv.setTextColor(mModeOffTextColor);
            mPowerTv.setClickable(true);
            mPowerTv.setCompoundDrawables(null, mPowerGreyDb, null, null);
        }
    }

    //设置动画
    public void startFlick(String mode) {
        switch (mode) {
            case HPlusConstants.MODE_SLEEP:
                startFlick(mSleepTv);
                break;
            case HPlusConstants.MODE_AUTO:
                startFlick(mAutoTv);
                break;
            case HPlusConstants.MODE_QUICK:
                startFlick(mFastTv);
                break;
            case HPlusConstants.MODE_SILENT:
                startFlick(mQuietTv);
                break;
            case HPlusConstants.MODE_OFF:
                startFlick(mPowerTv);
                break;
            default:
                break;
        }
    }

    private void startFlick(View view) {
        if (null == view) {
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(1, 0.2f);

        alphaAnimation.setDuration(800);

        alphaAnimation.setInterpolator(new LinearInterpolator());

        alphaAnimation.setRepeatCount(Animation.INFINITE);

        alphaAnimation.setRepeatMode(Animation.REVERSE);

        view.startAnimation(alphaAnimation);
        view.setClickable(false);
    }

    /**
     * 取消View闪烁效果
     */

    private void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
        view.setClickable(true);
    }

    public void stopAllFlick() {
        stopFlick(mPowerTv);
        stopFlick(mSleepTv);
        stopFlick(mAutoTv);
        stopFlick(mFastTv);
        stopFlick(mQuietTv);
    }

    public void disableClick() {
        mPowerTv.setClickable(false);
        mSleepTv.setClickable(false);
        mAutoTv.setClickable(false);
        mFastTv.setClickable(false);
        mQuietTv.setClickable(false);
    }

    public void enableClick() {
        mPowerTv.setClickable(true);
        mSleepTv.setClickable(true);
        mAutoTv.setClickable(true);
        mFastTv.setClickable(true);
        mQuietTv.setClickable(true);
    }


    public void resetTextColor() {
        mSleepTv.setTextColor(mModeOffTextColor);
        mAutoTv.setTextColor(mModeOffTextColor);
        mFastTv.setTextColor(mModeOffTextColor);
        mQuietTv.setTextColor(mModeOffTextColor);
        mSleepTv.setCompoundDrawables(null, mSleepGreyDb, null, null);
        mAutoTv.setCompoundDrawables(null, mAutoGreyDb, null, null);
        mFastTv.setCompoundDrawables(null, mFastGreyDb, null, null);
        mQuietTv.setCompoundDrawables(null, mQuietGreytDb, null, null);

    }


}
