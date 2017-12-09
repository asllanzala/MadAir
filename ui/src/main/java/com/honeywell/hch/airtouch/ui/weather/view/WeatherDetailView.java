package com.honeywell.hch.airtouch.ui.weather.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;

/**
 * Created by h127856 on 16/8/9.
 */
public class WeatherDetailView extends RelativeLayout {

    private Context mContext;

    private TextView mHightTemperatureTextView;
    private TextView mLowTemperatureTextView;
    private ImageView mWeatherIcon;
    private TextView mDayTimeTextView;

    public WeatherDetailView(Context context) {
        super(context);
        mContext = context;
        initView();

    }

    public WeatherDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.weather_detail_view, this);

        mHightTemperatureTextView = (TextView) findViewById(R.id.high_temperature);
        mLowTemperatureTextView = (TextView) findViewById(R.id.low_temperature);
        mWeatherIcon = (ImageView) findViewById(R.id.weather_icon);
        mDayTimeTextView = (TextView) findViewById(R.id.day_text);
    }

    public void initTodayWeatherDetailView(int highTemperature, int weatherCode, String time) {
        setHightTemperatureText(highTemperature);
        setLowTemperatureTextViewVisible(false);
        setWeatherIcon(weatherCode);
        setDayTimeTextView(time);
    }

    public void initSevenDayWeatherDetailView(int highTemperature,int lowTemperature, int weatherCode, String time) {
        setHightTemperatureText(highTemperature);
        setLowTemperatureTextViewVisible(true);
        setLowTemperatureText(lowTemperature);
        setWeatherIcon(weatherCode);
        setDayTimeTextView(time);
    }

    public void setHightTemperatureText(int highTemperature) {
        mHightTemperatureTextView.setText(String.valueOf(highTemperature) + AppManager.getInstance().getApplication().getString(R.string.temp_unit_c));
    }

    public void setLowTemperatureText(int lowTemperature) {
        mLowTemperatureTextView.setText(String.valueOf(lowTemperature) + AppManager.getInstance().getApplication().getString(R.string.temp_unit_c));
    }

    public void setWeatherIcon(int weatherCode) {
        mWeatherIcon.setImageResource(weatherCode);
    }


    public void setDayTimeTextView(String day) {
        mDayTimeTextView.setText(day);
    }

    public void setLowTemperatureTextViewVisible(boolean isVisible) {
        int viewStatus = isVisible ? View.VISIBLE : View.INVISIBLE;
        mLowTemperatureTextView.setVisibility(viewStatus);
    }
}
