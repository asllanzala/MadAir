package com.honeywell.hch.airtouch.ui.schedule.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.wheelView.ArrayWheelAdapter;
import com.honeywell.hch.airtouch.library.wheelView.NumericWheelAdapter;
import com.honeywell.hch.airtouch.library.wheelView.WheelView;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.device.BackHomeRequest;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.schedule.controller.presenter.ArriveHomePresenter;
import com.honeywell.hch.airtouch.ui.schedule.controller.presenter.IArriveHomePresenter;
import com.honeywell.hch.airtouch.ui.trydemo.manager.TryDemoConstant;
import com.honeywell.hch.airtouch.ui.trydemo.presenter.TryDemoArriveHomePresenter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by wuyuan on 10/23/15.
 */
public class ArriveHomeActivity extends BaseActivity implements IArriveHomeView {

    private static final String TAG = "ArriveHomeActivity";
    private WheelView mHourWheel;
    private WheelView mMinuteWheel;
    private Button mTellAirTouchTextView;
    private ImageView mClockImageView;
    private String[] mMinuteArray = {"00", "30"};
    private int isCleanTimeEnabled = -1;
    private Animation mAlphaOffAnimation;
    private TextView mArriveHomeTxt;
    private Dialog mDialog;
    private RelativeLayout mHasDeviceLayout;
    private List<HomeDevice> homeDeviceList;
    private ImageView mArriveHomeBackgroundLeft, mArriveHomeBackgroundRight;
    private TextView mTimeColon;
    protected UserLocationData mUserLocationData;
    protected int mLocationId = 0;
    private TextView mTitleTextView;
    private IArriveHomePresenter iArriveHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrive_home);
        initStatusBar();
        initLocation();
        initView();
    }

    public void initLocation() {
        mLocationId = getIntent().getIntExtra(HPlusConstants.LOCATION_ID, 0);
        if (getIntent().getBooleanExtra(TryDemoConstant.IS_FROM_TRY_DEMO, false)) {
            iArriveHomePresenter = new TryDemoArriveHomePresenter();
        } else {
            iArriveHomePresenter = new ArriveHomePresenter();
        }
        mUserLocationData = iArriveHomePresenter.getLocationDataByLocationId(mLocationId);
        iArriveHomePresenter.initPresenter(mUserLocationData, this);
    }

    private void initView() {
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.time_arrive_home);
        mHasDeviceLayout = (RelativeLayout) findViewById(R.id.has_deivce_layout_id);
        mHasDeviceLayout.setVisibility(View.VISIBLE);
        initHasDeviceLayout();
    }

    private void initHasDeviceLayout() {
        mArriveHomeTxt = (TextView) findViewById(R.id.clock_tv);
        mHourWheel = (WheelView) findViewById(R.id.hour_wheel);
        mMinuteWheel = (WheelView) findViewById(R.id.minute_wheel);
        mTellAirTouchTextView = (Button) findViewById(R.id.tell_air_touch_tv);

        mTimeColon = (TextView) findViewById(R.id.time_colon);
        mTimeColon.setVisibility(View.INVISIBLE);
        mArriveHomeBackgroundLeft = (ImageView) findViewById(R.id.arrive_home_background_left);
        mArriveHomeBackgroundRight = (ImageView) findViewById(R.id.arrive_home_background_right);

        mTellAirTouchTextView.setOnClickListener(tellAirTouchOnClick);

        //default time is 17:30
        mHourWheel.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
        mHourWheel.setCurrentItem(17);
        mHourWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCleanTimeEnabled != 1) {
                    mHourWheel.showItem();
                }
            }
        });
        mMinuteWheel.setAdapter(new ArrayWheelAdapter<>(mMinuteArray, 2));
        mMinuteWheel.setCurrentItem(1);
        mMinuteWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCleanTimeEnabled != 1) {
                    mMinuteWheel.showItem();
                }
            }
        });
        mClockImageView = (ImageView) findViewById(R.id.clock_iv);
        mArriveHomeTxt.setText(getString(R.string.arrive_home_unsetted));
        homeDeviceList = mUserLocationData.getHomeDevicesList();
        iArriveHomePresenter.showTimeAfterGetRunstatus();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    /**
     * Back home
     */
    View.OnClickListener tellAirTouchOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isNetworkOff()) {
                return;
            }
            mHourWheel.hideItem();
            mMinuteWheel.hideItem();

            Calendar calendar = Calendar.getInstance();
            int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
            int dstOffset = calendar.get(Calendar.DST_OFFSET);
            calendar.set(Calendar.HOUR_OF_DAY, mHourWheel.getCurrentItem());
            calendar.set(Calendar.MINUTE, Integer.parseInt(mMinuteArray[mMinuteWheel.getCurrentItem()]));
            calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
            boolean isBeforeNow = calendar.getTime().before(currentCalendar.getTime());

            //判断设置时间是否早于当前
            if (isBeforeNow) {
                calendar.add(Calendar.DATE, 1);
            }
            String settingTime = sdf.format(calendar.getTime());
            BackHomeRequest backHomeRequest = new BackHomeRequest();
            backHomeRequest.setTimeToHome(settingTime);
            backHomeRequest.setDeviceString("");

            if (isCleanTimeEnabled == 1) {
                backHomeRequest.setIsEnableCleanBeforeHome(false);
                isCleanTimeEnabled = 0;
                mDialog = LoadingProgressDialog.show(ArriveHomeActivity.this, getResources().getString(R.string.canceling_clock));
            } else if (isCleanTimeEnabled == 0) {
                long time = calendar.getTime().getTime() - currentCalendar.getTime().getTime();
                long second = time / 1000 / 60;
                if (second < 30) {
                    MessageBox.createSimpleDialog(ArriveHomeActivity.this, null, getString(R.string.arrive_time_in30min), null, null);
                    mHourWheel.showItem();
                    mMinuteWheel.showItem();
                    return;
                }
                backHomeRequest.setIsEnableCleanBeforeHome(true);
                isCleanTimeEnabled = 1;
                mDialog = LoadingProgressDialog.show(ArriveHomeActivity.this, getResources().getString(R.string.setting_clock));
                //check is all offline,true pop dialog.
                if (mUserLocationData.isAllDeviceOffline()) {
                    MessageBox.createSimpleDialog(ArriveHomeActivity.this, null, getString(R.string.arrive_home_all_device_offline), getString(R.string.ok), null);
                }
            }
            mTellAirTouchTextView.setClickable(false);

//            SetArrvieHomeTimeTask arrvieHomeTimeTask = new SetArrvieHomeTimeTask(mLocationId, backHomeRequest, backHomeResponse);
//            AsyncTaskExecutorUtil.executeAsyncTask(arrvieHomeTimeTask);

            mAlphaOffAnimation = AnimationUtils.loadAnimation(ArriveHomeActivity.this,
                    R.anim.control_alpha);
            mTellAirTouchTextView.startAnimation(mAlphaOffAnimation);

            iArriveHomePresenter.setArrvieHomeTimeTask(mLocationId, backHomeRequest, backHomeResponse);

        }
    };


    final IActivityReceive backHomeResponse = new IActivityReceive() {

        @Override
        public void onReceive(ResponseResult resultResponse) {
            mTellAirTouchTextView.clearAnimation();
            mTellAirTouchTextView.setClickable(true);
            if (mDialog != null) {
                mDialog.cancel();
            }
            if (resultResponse == null) {
                return;
            }
            switch (resultResponse.getRequestId()) {
                case ARRIVE_HOME_TIME:
                    if (resultResponse.isResult()) {
                        startGetDeviceDetailInfo();
                        if (isCleanTimeEnabled == 1) {
                            setIconVisible(false);
                            mArriveHomeTxt.setText(getString(R.string.arrive_home_settled));
                            mClockImageView.setImageResource(R.drawable.clock_blue);
                            mTellAirTouchTextView.setText(getString(R.string.cancel));
                        } else if (isCleanTimeEnabled == 0) {
                            setIconVisible(true);
                            mArriveHomeTxt.setText(getString(R.string.arrive_home_unsetted));
                            setPerformClick();
                            mClockImageView.setImageResource(R.drawable.clock_white);
                            mTellAirTouchTextView.setText(getString(R.string.tell_air_touch));
                        }
                    } else {
                        if (isCleanTimeEnabled == 1) {
                            showToast(getString(R.string.tell_fail));
                            isCleanTimeEnabled = 0;
                        } else if (isCleanTimeEnabled == 0) {
                            showToast(getString(R.string.cancel_fail));
                            isCleanTimeEnabled = 1;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };

    private void startGetDeviceDetailInfo() {
//        if (!GetDeviceDetailInfoTask.isTaskRunning()) {
//            GetDeviceDetailInfoTask requestTask
//                    = new GetDeviceDetailInfoTask();
//            AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
//        }
        iArriveHomePresenter.startGetDeviceDetailInfo();
    }

    private void saveTimeToHome(String timeToHome) {
        if (timeToHome.equals(""))
            return;

        Calendar calendar = Calendar.getInstance();
        int hour = Integer.parseInt(timeToHome.substring(11, 13));
        int minute = Integer.parseInt(timeToHome.substring(14, 16));
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        int dstOffset = calendar.get(Calendar.DST_OFFSET);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.add(Calendar.MILLISECOND, (zoneOffset + dstOffset));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss");
        String settingTime = sdf.format(calendar.getTime());
        hour = Integer.parseInt(settingTime.substring(11, 13));
        minute = Integer.parseInt(settingTime.substring(14, 16));
        minute = (minute == 0 ? 0 : 1);
        mHourWheel.setCurrentItem(hour);
        mMinuteWheel.setCurrentItem(minute);
    }

    @Override
    public void showTimeAfterGetRunstatus(String arriveTime) {
//        initAlpha();
        saveTimeToHome(arriveTime);
    }

    @Override
    public void hasArriveHomeTimeLayout() {
        isCleanTimeEnabled = 1;
        mArriveHomeTxt.setText(getString(R.string.arrive_home_settled));
        mClockImageView.setImageResource(R.drawable.clock_blue);
        setIconVisible(false);
        mTellAirTouchTextView.setText(getString(R.string.cancel));
    }

    @Override
    public void noArriveHomeTimeLayout() {
        isCleanTimeEnabled = 0;
        mArriveHomeTxt.setText(getString(R.string.arrive_home_unsetted));
        setPerformClick();
        mClockImageView.setImageResource(R.drawable.clock_white);
        setIconVisible(true);
        mTellAirTouchTextView.setText(getString(R.string.tell_air_touch));
    }


    public void setIconVisible(boolean flag) {
        if (flag) {
            mArriveHomeBackgroundLeft.setVisibility(View.VISIBLE);
            mArriveHomeBackgroundRight.setVisibility(View.VISIBLE);
            mTimeColon.setVisibility(View.INVISIBLE);
            WheelView.VALUE_PAINT_COLOR = getApplicationContext().getResources().getColor(R.color.white);
        } else {
            mArriveHomeBackgroundLeft.setVisibility(View.INVISIBLE);
            mArriveHomeBackgroundRight.setVisibility(View.INVISIBLE);
            mTimeColon.setVisibility(View.VISIBLE);
            WheelView.VALUE_PAINT_COLOR = getApplicationContext().getResources().getColor(R.color.black);
        }
        mHourWheel.invalidate();
        mMinuteWheel.invalidate();
    }


    public void setPerformClick() {
        mHourWheel.performClick();
        mMinuteWheel.performClick();
    }

    //set layout halfAlpha
    private void initAlpha() {
        if (!hasDeviceOnlineHome()) {
            //mHasDeviceLayout.setAlpha(0.35f);
            mHourWheel.setClickable(false);
            mMinuteWheel.setClickable(false);
            mTellAirTouchTextView.setClickable(false);
            mTellAirTouchTextView.setEnabled(false);
        }
    }

    private boolean hasDeviceOnlineHome() {
        if (homeDeviceList != null && homeDeviceList.size() > 0) {
            for (int i = 0; i < homeDeviceList.size(); i++) {
                HomeDevice homeDeviceItem = homeDeviceList.get(i);
                if (homeDeviceItem != null && homeDeviceItem instanceof AirTouchDeviceObject) {
                    if (homeDeviceItem.getDeviceInfo().getIsAlive()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void finish() {
        if (mDialog != null) {
            mDialog.cancel();
        }
        super.finish();
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }
}
