package com.honeywell.hch.airtouch.ui.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.util.BitmapUtil;
import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Future;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.FutureHour;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Now;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.Weather;
import com.honeywell.hch.airtouch.plateform.http.task.DownloadBackgroundTask;
import com.honeywell.hch.airtouch.plateform.timereceive.MorningAlarmReceiver;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.weather.view.WeatherDetailView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;


/**
 * Created by h127856 on 16/8/8.
 */
public class WeatherActivity extends BaseActivity {

    private static final int MORNING_TIME = 6;
    private static final int NIGHT_TIME = 18;
    private static final int MAX_SEVEN_DAY = 6;

    private static final int WEATHER_ITEM_WIDTH = DensityUtil.getScreenWidth() / 7;

    private final static int PM25AQI_MEDIUM_LIMIT = 75;
    private final static int PM25AQI_HIGH_LIMIT = 150;

    private FrameLayout mBackIconLayout;
    private TextView mTitleText;
    private TextView mPmValueText;
    private TextView mAqiValueText;
    private LinearLayout mWeatherTitle;
    private TextView mTodayTemperatureTextView;
    private LinearLayout mTodayWeatherListLayout;
    private LinearLayout mSevenDayWeatherListLayout;

    private RelativeLayout mTodayTitleLayout;
    private TextView mSevenDayTextView;
    private TextView mTodayTextView;
    private LinearLayout mPmAndAqiValueLayout;
    private RelativeLayout mErrorTextView;
    private HorizontalScrollView mHorizonScrollView;
    private ImageView mBackgroundImageView;

    private String mCityName;
    private String mCityCode;
    private Now mThisNowWeather;
    private HashMap<String, WeatherPageData> mWeatherDataHashMap;
    private int mTodayLowTemperature;
    private int mTodayHighTemperature;
    private ArrayList<String> mCityBackgroundList = new ArrayList<>();

    private DownloadBackgroundTask mDownloadBackgroundTask;
    private Bitmap mBackgroundBitmap;
    private WeatherActivityReceiver mBroadcastReceiver;

    private int mLocationId;

    private boolean isHasError = false;
    private boolean isDaylight = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Calendar calendar = Calendar.getInstance();
        isDaylight = calendar.get(Calendar.HOUR_OF_DAY) >= MORNING_TIME && calendar.get(Calendar
                .HOUR_OF_DAY) < NIGHT_TIME;

        initView();
        initStatusBar();
        initData();
        registerBroadcastReceiver();

        mLocationId = getIntent().getIntExtra(DashBoadConstant.ARG_LOCATION_ID,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
            mBackgroundBitmap = null;
        }
        unregisterBroadcastReceiver();
    }

    private void initView() {
        mBackIconLayout = (FrameLayout) findViewById(R.id.enroll_back_layout);
        mBackIconLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        });
        mTitleText = (TextView) findViewById(R.id.title_textview_id);
        mPmValueText = (TextView) findViewById(R.id.pm_value);
        mAqiValueText = (TextView) findViewById(R.id.aqi_value);
        mWeatherTitle = (LinearLayout) findViewById(R.id.weather_title);
        mTodayTemperatureTextView = (TextView) findViewById(R.id.today_weather_temperature);
        mTodayWeatherListLayout = (LinearLayout) findViewById(R.id.today_hour_list_weather);
        mSevenDayWeatherListLayout = (LinearLayout) findViewById(R.id.seven_day_list_weather);
        mTodayTitleLayout = (RelativeLayout) findViewById(R.id.today_layout);
        mTodayTextView = (TextView) findViewById(R.id.today_text);
        mPmAndAqiValueLayout = (LinearLayout) findViewById(R.id.pm_aqi_layout);
        mBackgroundImageView = (ImageView) findViewById(R.id.background_id);

        mErrorTextView = (RelativeLayout) findViewById(R.id.error_msg);
        mHorizonScrollView = (HorizontalScrollView) findViewById(R.id.horizon_scrollview);

        mTodayTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTodayWeatherListLayout.setVisibility(View.VISIBLE);
                mSevenDayWeatherListLayout.setVisibility(View.GONE);
                mPmAndAqiValueLayout.setVisibility(View.VISIBLE);
                mTodayTextView.setTextColor(getResources().getColor(R.color.white));
                mTodayTemperatureTextView.setTextColor(getResources().getColor(R.color.white));
                mSevenDayTextView.setTextColor(getResources().getColor(R.color.white_30));
            }
        });
        mSevenDayTextView = (TextView) findViewById(R.id.seven_days);
        mSevenDayTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPmAndAqiValueLayout.setVisibility(View.GONE);
                mTodayWeatherListLayout.setVisibility(View.GONE);
                mSevenDayWeatherListLayout.setVisibility(View.VISIBLE);
                mTodayTextView.setTextColor(getResources().getColor(R.color.white_30));
                mTodayTemperatureTextView.setTextColor(getResources().getColor(R.color.white_30));
                mSevenDayTextView.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }


    private void initData() {

        mCityCode = getIntent().getStringExtra(DashBoadConstant.ARG_CITY_NAME);
        mCityName = getIntent().getStringExtra(DashBoadConstant.ARG_LOCATION_NAME);


        setRandomBackgroundFromList(true);

        mTitleText.setText(mCityName);

        mWeatherDataHashMap = UserAllDataContainer.shareInstance().getWeatherRefreshManager().getWeatherPageDataHashMap();
        mThisNowWeather = mWeatherDataHashMap.get(mCityCode).getWeather().getNow();

        initPmValueAndAqiValue();
        initTodayTemperatureViewList();
        initSevenDaysTemperatureViewList();

        mTodayTemperatureTextView.setText(getString(R.string.weather_temperature, mTodayLowTemperature, mTodayHighTemperature));

        isShowErrorTextView();


    }

    private void downloadBackgroundList() {
        if (mDownloadBackgroundTask == null || (mDownloadBackgroundTask != null && !mDownloadBackgroundTask.isRunning())) {
            mDownloadBackgroundTask = new DownloadBackgroundTask();
            AsyncTaskExecutorUtil.executeAsyncTask(mDownloadBackgroundTask);
        }
    }


    private void setRandomBackgroundFromList(boolean isNeedDownLoad) {
        ArrayList<String> cityBackgroundList = getIntent().getStringArrayListExtra(DashBoadConstant.ARG_CITY_BACKGROUD_LIST);
        mCityBackgroundList.clear();
        if (cityBackgroundList != null && cityBackgroundList.size() > 0) {
            for (String path : cityBackgroundList) {
                if (isContainCondition(path)) {
                    mCityBackgroundList.add(path);
                }
            }
        }

        if (mCityBackgroundList != null && mCityBackgroundList.size() > 0) {

            Random random = new Random();
            int index = random.nextInt(mCityBackgroundList.size());

            mBackgroundBitmap = BitmapUtil.createBitmapEffectlyFromPath(this, mCityBackgroundList.get(index));
            mBackgroundImageView.setImageBitmap(mBackgroundBitmap);
        } else {
            if (isDaylight) {
                mBackgroundImageView.setImageResource(R.drawable.default_city_day_clear);
            } else {
                mBackgroundImageView.setImageResource(R.drawable.default_city_night_clear);
            }

            //如果是download成功或是时间变更后进入的，不需要再次下载，否则会出现死循环
            if (isNeedDownLoad) {
                downloadBackgroundList();
            }
        }
    }

    private void initPmValueAndAqiValue() {
        if (mThisNowWeather != null && mThisNowWeather.getAirQuality() != null) {
            String pmValue = mThisNowWeather.getAirQuality().getAirQualityIndex().getPm25();
            String aqiValue = mThisNowWeather.getAirQuality().getAirQualityIndex().getAqi();
            try {
                mPmValueText.setText(pmValue);
                mAqiValueText.setText(aqiValue);
                mPmValueText.setTextColor(getPMAQIValueColor(Integer.valueOf(pmValue)));
                mAqiValueText.setTextColor(getPMAQIValueColor(Integer.valueOf(aqiValue)));
            } catch (Exception e) {
                //避免服务器返回数据错误，导致显示错误
                mPmValueText.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
                mAqiValueText.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
            }

        } else {
            mPmValueText.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
            mAqiValueText.setText(HPlusConstants.DATA_LOADING_FAILED_STATUS);
        }

    }

    private void initTodayTemperatureViewList() {
        if (mWeatherDataHashMap != null && mWeatherDataHashMap.get(mCityCode) != null) {
            String currentDate = DateTimeUtil.getDateTimeString(new Date(), DateTimeUtil.WEATHER_TODAY_TO_FORMAT);
            FutureHour[] hourlyData = mWeatherDataHashMap.get(mCityCode).getHourlyData();
            if (hourlyData != null && hourlyData.length > 0) {
                for (int i = 0; i < hourlyData.length; i++) {
                    int nowTemperature = hourlyData[i].getTemperature();
                    int weatherIcon = weatherIconId(hourlyData[i].getCode());

                    Date date = hourlyData[i].getDate();
                    String currentDayTime = DateTimeUtil.getDateTimeString(date, DateTimeUtil.WEATHER_TODAY_TO_FORMAT);
                    String time;
                    if (currentDate.equals(currentDayTime)) {
                        time = getString(R.string.weathr_now);
                    } else {
                        time = DateTimeUtil.getDateTimeString(date, DateTimeUtil.WEATHER_CHART_TIME_FORMAT);
                    }
                    WeatherDetailView weatherDetailView = new WeatherDetailView(this);
                    weatherDetailView.initTodayWeatherDetailView(nowTemperature, weatherIcon, time);
                    if (i < hourlyData.length - 1) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WEATHER_ITEM_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
                        weatherDetailView.setLayoutParams(params);
                    }
                    mTodayWeatherListLayout.addView(weatherDetailView);
                }
            } else {
                isHasError = true;
            }
        } else {
            isHasError = true;
        }


    }

    private void initSevenDaysTemperatureViewList() {
        if (mWeatherDataHashMap != null && mWeatherDataHashMap.get(mCityCode) != null) {
            Weather weather = mWeatherDataHashMap.get(mCityCode).getWeather();
            if (weather != null && weather.getFutureList() != null && weather.getFutureList().size() > 0) {
                int index = 0;
                String currentDate = DateTimeUtil.getDateTimeString(new Date(), DateTimeUtil.THINKPAGE_DATE_FORMAT);
                for (Future future : weather.getFutureList()) {
                    int highTemperature = future.getHigh();
                    int lowTemperature = future.getLow();
                    int weatherIcon = weatherIconId(future.getCode1());
                    String time = "";
                    if (index == 0) {
                        //初始化
                        mTodayLowTemperature = future.getLow();
                        mTodayHighTemperature = future.getHigh();
                    }
                    if (currentDate.equals(future.getDate())) {
                        time = getString(R.string.weather_today);
                        mTodayLowTemperature = future.getLow();
                        mTodayHighTemperature = future.getHigh();
                    } else {
                        time = future.getDay();
                    }
                    WeatherDetailView weatherDetailView = new WeatherDetailView(this);
                    weatherDetailView.initSevenDayWeatherDetailView(highTemperature, lowTemperature, weatherIcon, time);
                    if (index < MAX_SEVEN_DAY) {
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(WEATHER_ITEM_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
                        weatherDetailView.setLayoutParams(params);
                    }
                    mSevenDayWeatherListLayout.addView(weatherDetailView);
                    index++;
                    if (index > MAX_SEVEN_DAY) {
                        return;
                    }
                }
            } else {
                isHasError = true;
            }

        } else {
            isHasError = true;
        }

    }

    private int weatherIconId(int weatherCode) {
        if (weatherCode == 99) {
            weatherCode = DashBoadConstant.WEATHER_ICON.length - 1;
        }
        return DashBoadConstant.WEATHER_ICON[weatherCode];
    }

    private void isShowErrorTextView() {
        int errorShowStatus = isHasError ? View.VISIBLE : View.GONE;
        int scrollShowStatus = isHasError ? View.GONE : View.VISIBLE;
        mErrorTextView.setVisibility(errorShowStatus);
        mHorizonScrollView.setVisibility(scrollShowStatus);
        mWeatherTitle.setVisibility(scrollShowStatus);
    }

    private boolean isContainCondition(String pathItem) {
        if (isDaylight) {
            return pathItem.contains("day");
        } else {
            return pathItem.contains("night");
        }
    }

    private class WeatherActivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (HPlusConstants.DOWN_LOAD_BG_END.equals(action)) {
                setRandomBackgroundFromList(false);
            } else if (MorningAlarmReceiver.TIME_CHANGE_ACTION.equals(action)) {
                setDayOrNight();
                setRandomBackgroundFromList(false);
            }

        }
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.DOWN_LOAD_BG_END);
        intentFilter.addAction(MorningAlarmReceiver.TIME_CHANGE_ACTION);

        mBroadcastReceiver = new WeatherActivityReceiver();
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void unregisterBroadcastReceiver() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
    }


    private void setDayOrNight() {
        Calendar calendar = Calendar.getInstance();
        isDaylight = calendar.get(Calendar.HOUR_OF_DAY) >= MORNING_TIME && calendar.get(Calendar
                .HOUR_OF_DAY) < NIGHT_TIME;
    }


    private int getPMAQIValueColor(int value) {
        if (value <= PM25AQI_MEDIUM_LIMIT) {
            return mContext.getResources().getColor(R.color.pm_25_good);
        } else if (value <= PM25AQI_HIGH_LIMIT) {
            return mContext.getResources().getColor(R.color.pm_25_bad);
        } else {
            return mContext.getResources().getColor(R.color.pm_25_worst);
        }
    }
}
