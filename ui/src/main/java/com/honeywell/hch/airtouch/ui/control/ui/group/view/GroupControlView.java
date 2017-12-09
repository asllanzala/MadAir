package com.honeywell.hch.airtouch.ui.control.ui.group.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.manager.model.DeviceMode;

/**
 * Created by Vincent on 14/6/16.
 */
public class GroupControlView extends RelativeLayout {
    private final String TAG = "GroupControlView";
    private Context mContext;

    private final static float ALPHA_DISABLE = 0.5f;
    private final static float ALPHA_ENABLE = 1.0f;

    private ImageView mHeaderIv;
    private TextView mHeaderTv;

    private ImageView mPowerIv;
    private TextView mPowerTv;
    private ImageView mHomeIv;
    private TextView mHomeTv;
    private ImageView mSleepIv;
    private TextView mSleepTv;
    private ImageView mAwayIv;
    private TextView mAwayTv;
    private ImageView mAwakeIv;
    private TextView mAwakeTv;

    private RelativeLayout mPowerRl;
    private RelativeLayout mHomeRl;
    private RelativeLayout mSleepRl;
    private RelativeLayout mAwayRl;
    private RelativeLayout mAwakeRl;
    private TextView mOperationTv;

    /**
     * 不能进入下一级界面的时候，箭头要设置为透明度30%
     */
    private final static float ALPHA_ARROR_CANNOT_GO = 0.3f;

    public GroupControlView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public GroupControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.group_control_view, this);
        mHeaderIv = (ImageView) v.findViewById(R.id.group_control_header_img);
        mHeaderTv = (TextView) v.findViewById(R.id.group_control_header_tv);
        mOperationTv = (TextView) findViewById(R.id.group_control_operation);

        mPowerIv = (ImageView) v.findViewById(R.id.group_control_power_on_iv);
        mPowerTv = (TextView) v.findViewById(R.id.group_control_power_on_tv);
        mHomeIv = (ImageView) v.findViewById(R.id.group_control_home_iv);
        mHomeTv = (TextView) v.findViewById(R.id.group_control_home_tv);
        mSleepIv = (ImageView) v.findViewById(R.id.group_control_sleep_iv);
        mSleepTv = (TextView) v.findViewById(R.id.group_control_sleep_tv);
        mAwayIv = (ImageView) v.findViewById(R.id.group_control_away_iv);
        mAwayTv = (TextView) v.findViewById(R.id.group_control_away_tv);
        mAwakeIv = (ImageView) v.findViewById(R.id.group_control_awake_iv);
        mAwakeTv = (TextView) v.findViewById(R.id.group_control_awake_tv);

        mPowerRl = (RelativeLayout) v.findViewById(R.id.group_control_power_on_rl);
        mHomeRl = (RelativeLayout) v.findViewById(R.id.group_control_home_rl);
        mSleepRl = (RelativeLayout) v.findViewById(R.id.group_control_sleep_rl);
        mAwayRl = (RelativeLayout) v.findViewById(R.id.group_control_away_rl);
        mAwakeRl = (RelativeLayout) v.findViewById(R.id.group_control_awake_rl);
    }

    public void clickHomeBtn() {
        stopAllFlick();
        startFlick(mHomeRl);
        setControlButton(DeviceMode.MODE_HOME);
    }

    public void clickSleepBtn() {
        stopAllFlick();
        startFlick(mSleepRl);
        setControlButton(DeviceMode.MODE_SLEEP);
    }

    public void clickAwayBtn() {
        stopAllFlick();
        startFlick(mAwayRl);
        setControlButton(DeviceMode.MODE_AWAY);
    }

    public void clickAwakeBtn() {
        stopAllFlick();
        startFlick(mAwakeRl);
        setControlButton(DeviceMode.MODE_AWAKE);
    }

    public void setHeadLayout(int mode) {
        switch (mode) {
            case DeviceMode.MODE_HOME:
                setControlButton(DeviceMode.MODE_HOME);
                mHeaderIv.setImageResource(R.drawable.home_active_big);
                mHeaderTv.setText(getResources().getString(R.string.group_control_home));
                mHeaderTv.setTextColor(getResources().getColor(R.color.home_on));
                mOperationTv.setText(getResources().getString(R.string.last_operation));
                break;
            case DeviceMode.MODE_SLEEP:
                setControlButton(DeviceMode.MODE_SLEEP);
                mHeaderIv.setImageResource(R.drawable.sleep_big);
                mHeaderTv.setText(getResources().getString(R.string.group_control_sleep));
                mHeaderTv.setTextColor(getResources().getColor(R.color.sleep_on));
                mOperationTv.setText(getResources().getString(R.string.last_operation));
                break;
            case DeviceMode.MODE_AWAY:
                setControlButton(DeviceMode.MODE_AWAY);
                mHeaderIv.setImageResource(R.drawable.away_big);
                mHeaderTv.setText(getResources().getString(R.string.group_control_away));
                mHeaderTv.setTextColor(getResources().getColor(R.color.away_on));
                mOperationTv.setText(getResources().getString(R.string.last_operation));
                break;
            case DeviceMode.MODE_AWAKE:
                setControlButton(DeviceMode.MODE_AWAKE);
                mHeaderIv.setImageResource(R.drawable.awake_big);
                mHeaderTv.setText(getResources().getString(R.string.group_control_awake));
                mHeaderTv.setTextColor(getResources().getColor(R.color.yellow_one));
                mOperationTv.setText(getResources().getString(R.string.last_operation));
                break;
            case DeviceMode.MODE_UNDEFINE:
            default:
                setControlButton(DeviceMode.MODE_UNDEFINE);
                mHeaderIv.setImageResource(R.drawable.undefined_big);
                mHeaderTv.setText(getResources().getString(R.string.group_control_na));
                mHeaderTv.setTextColor(getResources().getColor(R.color.text_common));
                mOperationTv.setText(getResources().getString(R.string.no_last_operation));
                break;
        }
    }

    public void setControlButton(int mode) {
        switch (mode) {
            case DeviceMode.MODE_HOME:
                enableClick();
                mHomeRl.setClickable(false);
                mHomeIv.setImageResource(R.drawable.home_active);
                mHomeTv.setTextColor(getResources().getColor(R.color.home_on));

                mAwakeIv.setImageResource(R.drawable.awake_mode_off);
                mAwakeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mSleepIv.setImageResource(R.drawable.group_sleepoff_btn);
                mSleepTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mAwayIv.setImageResource(R.drawable.group_awayoff_btn);
                mAwayTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                break;
            case DeviceMode.MODE_SLEEP:
                enableClick();
                mSleepRl.setClickable(false);
                mSleepIv.setImageResource(R.drawable.group_sleepon_btn);
                mSleepTv.setTextColor(getResources().getColor(R.color.sleep_on));

                mAwakeIv.setImageResource(R.drawable.awake_mode_off);
                mAwakeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mHomeIv.setImageResource(R.drawable.home_inactive);
                mHomeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mAwayIv.setImageResource(R.drawable.group_awayoff_btn);
                mAwayTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                break;
            case DeviceMode.MODE_AWAY:
                enableClick();
                mAwayRl.setClickable(false);
                mAwayIv.setImageResource(R.drawable.group_awayon_btn);
                mAwayTv.setTextColor(getResources().getColor(R.color.away_on));

                mAwakeIv.setImageResource(R.drawable.awake_mode_off);
                mAwakeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mHomeIv.setImageResource(R.drawable.home_inactive);
                mHomeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mSleepIv.setImageResource(R.drawable.group_sleepoff_btn);
                mSleepTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                break;
            case DeviceMode.MODE_AWAKE:
                enableClick();
                mAwakeRl.setClickable(false);
                mAwakeIv.setImageResource(R.drawable.awake_mode_on);
                mAwakeTv.setTextColor(getResources().getColor(R.color.yellow_one));

                mAwayIv.setImageResource(R.drawable.group_awayoff_btn);
                mAwayTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mHomeIv.setImageResource(R.drawable.home_inactive);
                mHomeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mSleepIv.setImageResource(R.drawable.group_sleepoff_btn);
                mSleepTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                break;

            case DeviceMode.MODE_DISABLE_CLICK:
                disableClick();
                mAwayIv.setImageResource(R.drawable.group_awayoff_btn);
                mAwayTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mHomeIv.setImageResource(R.drawable.home_inactive);
                mHomeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mSleepIv.setImageResource(R.drawable.group_sleepoff_btn);
                mSleepTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mAwakeIv.setImageResource(R.drawable.awake_mode_off);
                mAwakeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                break;
            case DeviceMode.MODE_UNDEFINE:
            default:
                enableClick();
                mAwayIv.setImageResource(R.drawable.group_awayoff_btn);
                mAwayTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mHomeIv.setImageResource(R.drawable.home_inactive);
                mHomeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mSleepIv.setImageResource(R.drawable.group_sleepoff_btn);
                mSleepTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                mAwakeIv.setImageResource(R.drawable.awake_mode_off);
                mAwakeTv.setTextColor(getResources().getColor(R.color.all_device_head_item));
                break;
        }
    }

    public void startFlickMode(int mode) {
        switch (mode) {
            case DeviceMode.MODE_HOME:
                clickHomeBtn();
                break;
            case DeviceMode.MODE_SLEEP:
                clickSleepBtn();
                break;
            case DeviceMode.MODE_AWAY:
                clickAwayBtn();
                break;
            case DeviceMode.MODE_AWAKE:
                clickAwakeBtn();
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

    public boolean isStarting() {
        if (mSleepRl.getAnimation() != null) {
            return true;
        } else if (mHomeRl.getAnimation() != null) {
            return true;
        } else if (mAwayRl.getAnimation() != null) {
            return true;
        } else if (mAwakeRl.getAnimation() != null) {
            return true;
        }
        return false;
    }

    public void stopAllFlick() {
        stopFlick(mSleepRl);
        stopFlick(mHomeRl);
        stopFlick(mAwayRl);
        stopFlick(mAwakeRl);
    }

    public void disableClick() {
        mSleepRl.setClickable(false);
        mHomeRl.setClickable(false);
        mAwayRl.setClickable(false);
        mAwakeRl.setClickable(false);
    }

    public void enableClick() {
        mSleepRl.setClickable(true);
        mHomeRl.setClickable(true);
        mAwayRl.setClickable(true);
        mAwakeRl.setClickable(true);
    }

}
