package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.manager;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.SHA1Util;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.VirtualUserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.weather.WeatherPageData;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response.GetReportResponse;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.view.MadAirScrollView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashMap;

/**
 * Created by Qian Jin on 11/27/16.
 */

public class MadAirDashboardUIManager {

    private String mBatteryPercent;

    private String mBatteryRemain;

    private String mFilterPercent;

    private String mFilterRemain;

    private String mBatteryUnit;

    private String mFilterUnit;

    private String mBreathFreq;

    private String mBreathDesc;


    private boolean mPurchaseVisible;

    private static final int SCROLL_VIEW_STOP_DELAY = 500;
    private ScrollViewCallback mScrollViewCallback;
    private int mLastY = 0;

    private static int testCount = 0; // later

    private Context mContext;

    public MadAirDashboardUIManager(Context context) {
        this.mContext = context;

    }

    public String getBatteryPercent() {
        return mBatteryPercent;
    }

    public String getBatteryRemain() {
        return mBatteryRemain;
    }

    public String getFilterPercent() {
        return mFilterPercent;
    }

    public String getFilterRemain() {
        return mFilterRemain;
    }

    public String getFilterUnit() {
        return mFilterUnit;
    }

    public String getBatteryUnit() {
        return mBatteryUnit;
    }

    public boolean getPurchaseVisible() {
        return mPurchaseVisible;
    }

    public String getBreathFreq() {
        return mBreathFreq;
    }

    public String getBreathDesc() {
        return mBreathDesc;
    }

    /*
     * Battery and Filter
     */
    public void calculateBatteryFilterFreq(int freq, int batteryPercent, int batteryRemaining, int filterDuration, int alarmInfo, TextView disconnectDescTextView) {
        int filterPercent = MadAirDeviceModel.calculateUsagePercent(filterDuration);
        int filterRemaining = MadAirDeviceModel.calculateUsageRemaining(filterDuration);

        // Battery percent
        if (batteryPercent == MadAirDeviceModel.ERROR_DATA || batteryPercent == 0) {
            mBatteryPercent = mContext.getString(R.string.mad_air_dashboard_no_data);
            mBatteryUnit = "";
        } else {
            mBatteryPercent = "" + batteryPercent;
            mBatteryUnit = mContext.getString(R.string.mad_air_dashboard_percent_unit);
        }

        // Battery remaining
        if (batteryRemaining == MadAirDeviceModel.ERROR_DATA ||
                batteryPercent == MadAirDeviceModel.ERROR_DATA || batteryPercent == 0) {
            mBatteryRemain = "";
        } else if (batteryPercent < 20) {
            mBatteryRemain = mContext.getString(R.string.mad_air_dashboard_battery_charge);
        } else {
            if (batteryRemaining >= 60) {
                String hour = MadAirDeviceModel.round((float) batteryRemaining / 60); // 四舍五入
                mBatteryRemain = mContext.getResources().getQuantityString(R.plurals.mad_air_dashboard_battery_remain_hour, Integer.valueOf(hour), hour);
            } else
                mBatteryRemain = mContext.getResources().getQuantityString(R.plurals.mad_air_dashboard_battery_remain_min, batteryRemaining, batteryRemaining);
        }

        // Filter percent
        if (filterPercent == MadAirDeviceModel.ERROR_DATA) {
            mFilterPercent = mContext.getString(R.string.mad_air_dashboard_no_data);
            mFilterUnit = "";
        } else {
            mFilterPercent = "" + filterPercent;
            mFilterUnit = mContext.getString(R.string.mad_air_dashboard_percent_unit);
        }

        // Filter remaining
        if (filterRemaining == MadAirDeviceModel.ERROR_DATA || filterPercent == MadAirDeviceModel.ERROR_DATA) {
            mFilterRemain = "";
            mPurchaseVisible = false;
        } else if (filterPercent <= 20) {
            mFilterRemain = mContext.getString(R.string.mad_air_dashboard_filter_buy);
            mPurchaseVisible = true;
        } else if (filterPercent <= 100) {
            mFilterRemain = mContext.getResources().getQuantityString(R.plurals.mad_air_dashboard_filter_remain_hour, filterRemaining, filterRemaining);
            mPurchaseVisible = false;
        }

        // Filter not install alarm
        if (GetReportResponse.isFilterNotInstalled((byte) alarmInfo)) {
            mFilterUnit = "";
            mPurchaseVisible = false;
            mFilterPercent = mContext.getString(R.string.mad_air_dashboard_no_data);
            mFilterRemain = mContext.getString(R.string.mad_air_dashboard_filter_install);
        }

        // breath freq
        if (freq > 0) {
            mBreathFreq = "" + freq;
            mBreathDesc = mContext.getString(R.string.mad_air_dashboard_breath_current);
            disconnectDescTextView.setVisibility(View.GONE);
        } else {
            mBreathFreq = mContext.getString(R.string.mad_air_dashboard_no_data);
            mBreathDesc = mContext.getString(R.string.mad_air_dashboard_freq_calculating);
            disconnectDescTextView.setVisibility(View.VISIBLE);
            disconnectDescTextView.setText(mContext.getString(R.string.confirm_filter_status));

        }
    }

    /*
     * City and Weather
     */
    public int getPm25(VirtualUserLocationData locationData) {
        if (locationData == null)
            return 0;

        HashMap<String, WeatherPageData> weatherDataHashMap = UserAllDataContainer.shareInstance().
                getWeatherRefreshManager().getWeatherPageDataHashMap();

        if (weatherDataHashMap == null || weatherDataHashMap.get(locationData.getCity()) == null)
            return 0;

        String pm25 = weatherDataHashMap.get(locationData.getCity()).
                getWeather().getNow().getAirQuality().getAirQualityIndex().getPm25();

        if (pm25 == null || pm25.equals(""))
            return 0;

        return Integer.valueOf(pm25);
    }

    public int getBackground(int pm25) {
        if (pm25 <= 75) {
            return R.drawable.mad_air_dashboard_pm25_good;
        } else if (pm25 <= 150) {
            return R.drawable.mad_air_dashboard_pm25_normal;
        } else {
            return R.drawable.mad_air_dashboard_pm25_poor;
        }
    }

    public int getPm25Color(int pm25) {
        if (pm25 <= 75) {
            return R.color.white;
        } else if (pm25 <= 150) {
            return R.color.mad_air_weather_normal;
        } else {
            return R.color.mad_air_weather_bad;
        }
    }

    public String getNeedMask(int pm25) {
        if (pm25 <= 75) {
            return "";
        } else if (pm25 <= 150) {
            return mContext.getString(R.string.mad_air_dashboard_wear_mask);
        } else {
            return mContext.getString(R.string.mad_air_dashboard_need_mask);
        }
    }

    /*
     * Device status bar
     */
    public void displayStatusBar(MadAirDeviceStatus status, ImageView deviceStatusImageView, TextView deviceStatusTextView,
                                 TextView disconnectDescTextView) {
        switch (status) {
            case DISCONNECT:
                disconnectDescTextView.setVisibility(View.VISIBLE);
                disconnectDescTextView.setText(mContext.getString(R.string.mad_air_dashboard_open_mask));
                deviceStatusImageView.setBackgroundResource(R.drawable.mad_air_dashboard_disconnect);
                deviceStatusTextView.setText(mContext.getString(R.string.mad_air_dashboard_status_disconnect));
                break;

            case CONNECT:
                disconnectDescTextView.setVisibility(View.GONE);
                deviceStatusImageView.setBackgroundResource(R.drawable.mad_air_dashboard_connect);
                deviceStatusTextView.setText(mContext.getString(R.string.mad_air_dashboard_status_connected));
                break;

            case USING:
                disconnectDescTextView.setVisibility(View.GONE);
                deviceStatusImageView.setBackgroundResource(R.drawable.mad_air_dashboard_connect);
                deviceStatusTextView.setText(mContext.getString(R.string.mad_air_dashboard_status_using));
                break;

            case BLE_DISABLE:
                disconnectDescTextView.setVisibility(View.VISIBLE);
                disconnectDescTextView.setText(mContext.getString(R.string.mad_air_dashboard_open_bluetooth));
                deviceStatusImageView.setBackgroundResource(R.drawable.mad_air_dashboard_disconnect);
                deviceStatusTextView.setText(mContext.getString(R.string.mad_air_dashboard_status_disconnect));
                break;

            default:
                break;
        }
    }

    /*
     * Scroll view
     */
    public interface ScrollViewCallback {
        void onShow();
    }

    public void setScrollViewCallback(ScrollViewCallback scrollViewCallback) {
        this.mScrollViewCallback = scrollViewCallback;
    }

    public void startScrollViewAnimation(final MadAirScrollView scrollView, final int isUp) {

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    scrollView.smoothScrollTo(0, DensityUtil.getScreenHeight() * 9 / 22 * isUp);
                else
                    scrollView.smoothScrollTo(0, DensityUtil.getScreenHeight() * 10 / 23 * isUp);
            }
        });
    }

    public void setScrollListener(MadAirScrollView scrollView, final RelativeLayout topTitleBar) {
        final int touchEventId = -9983761;

        scrollView.setScrollViewListener(new MadAirScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(MadAirScrollView scrollView, int x, int y, int oldX, int oldY) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (y < DensityUtil.getScreenHeight() * 9 / 22)
                        topTitleBar.setVisibility(View.INVISIBLE);
                    else
                        mScrollViewCallback.onShow();
                } else {
                    if (y < DensityUtil.getScreenHeight() * 10 / 23)
                        topTitleBar.setVisibility(View.INVISIBLE);
                    else
                        mScrollViewCallback.onShow();
                }
            }
        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    View scroller = (View) msg.obj;
                    if (msg.what == touchEventId) {
                        if (mLastY == scroller.getScrollY()) {
                            handleStop((MadAirScrollView) scroller);
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 5);
                            mLastY = scroller.getScrollY();
                        }
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), SCROLL_VIEW_STOP_DELAY);
                }
                return false;
            }

        });
    }

    private void handleStop(MadAirScrollView scrollView) {
        int delta;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            delta = mLastY - DensityUtil.getScreenHeight() * 9 / 22;
        else
            delta = mLastY - DensityUtil.getScreenHeight() * 10 / 23;

        if ((delta > 0 && delta < DensityUtil.getScreenHeight() / 5)
                || (delta < 0 && -delta < DensityUtil.getScreenHeight() / 5))
            scrollView.smoothScrollTo(0, mLastY - delta);
    }

    public void test(MadAirDeviceModel device) {
        if (device == null)
            return;

        /*
         * 添加设备
         */
//        MadAirDeviceModel device = new MadAirDeviceModel("1234", "jin");
//        MadAirDeviceModelSharedPreference.addDevice(device);
//        MadAirDeviceModelSharedPreference.saveDeviceName("1234", "mask1");

        /*
         * 添加实时数据
         */
//        if (testCount % 5 == 0) {
//            MadAirDeviceModelSharedPreference.saveStatus(device.getMacAddress(), MadAirDeviceStatus.CONNECT);
//            MadAirDeviceModelSharedPreference.saveRealTimeData(device.getMacAddress(),
//                    120, 119, 1, 0, 0, 1);
//        } else if (testCount % 5 == 1) {
//            MadAirDeviceModelSharedPreference.saveRealTimeData(device.getMacAddress(),
//                    20, 20, 576, 0, 9, 0);
//        } else if (testCount % 5 == 2) {
//            MadAirDeviceModelSharedPreference.saveRealTimeData(device.getMacAddress(),
//                    25, 91, 121, 27, 1, 0);
//        } else if (testCount % 5 == 3) {
//            MadAirDeviceModelSharedPreference.saveRealTimeData(device.getMacAddress(),
//                    80, 66, 179, 43, 0, 0);
//        } else if (testCount % 5 == 4) {
//            MadAirDeviceModelSharedPreference.saveStatus(device.getMacAddress(), MadAirDeviceStatus.DISCONNECT);
//        }
//
//        testCount++;

        /*
         * 颗粒物计算公式：Particle weight (ug) = [time (min) * PM2.5 (ug/m3) *18] /1000
         */
//        HashMap<String, Integer> pm25Map = new HashMap<>();
//        pm25Map.put("2016/11/09", 20);
//        pm25Map.put("2016/11/10", 117);
//        pm25Map.put("2016/11/11", 21);
//        pm25Map.put("2016/11/12", 114);
//        pm25Map.put("2016/11/13", 23);
//        pm25Map.put("2016/11/14", 180);
//        pm25Map.put("2016/11/15", 77);
//        pm25Map.put("2016/11/16", 21);
//        pm25Map.put("2016/11/17", 96);
//        pm25Map.put("2016/11/18", 0);
//        pm25Map.put("2016/11/19", 44);
//        pm25Map.put("2016/11/20", 99);
//        pm25Map.put("2016/11/21", 44);
//        pm25Map.put("2016/11/22", 109);
//        pm25Map.put("2016/11/23", 60);
//        pm25Map.put("2016/11/24", 77);
//        pm25Map.put("2016/11/25", 158);
//        pm25Map.put("2016/11/26", 14);
//        pm25Map.put("2016/11/27", 13);
//        pm25Map.put("2016/11/28", 29);
//        pm25Map.put("2016/11/29", 99);
//        pm25Map.put("2016/11/30", 147);
//        pm25Map.put("2016/12/01", 29);
//        pm25Map.put("2016/12/02", 155);
//        pm25Map.put("2016/12/03", 277);
//
//        MadAirDeviceModelSharedPreference.savePm25Mock(device.getMacAddress(), pm25Map);
//        HashMap<String, Float> particleMap = new HashMap();
//        particleMap.put("2016/11/20", 56.2f);
//        particleMap.put("2016/11/21", 156.2f);
//        particleMap.put("2016/11/22", 156.2f);
//        particleMap.put("2016/11/23", 256.2f);
//        particleMap.put("2016/11/24", 456.2f);
//        particleMap.put("2016/11/25", 356.2f);
//        particleMap.put("2016/11/26", 256.2f);
//        particleMap.put("2016/11/27", 256.2f);
//        particleMap.put("2016/11/28", 566.2f);
//        particleMap.put("2016/12/02", 333.3f);
//        particleMap.put("2016/12/03", 233.3f);
//        particleMap.put("2016/12/04", 133.3f);
//        particleMap.put("2016/12/05", 33.3f);
//        particleMap.put("2016/12/06", 23.3f);
//        particleMap.put("2016/12/07", 13.3f);
//        particleMap.put("2016/12/08", 353.3f);
//
//        HashMap<String, byte[]> flashDataMap = new HashMap();
//        flashDataMap.put("2016/11/20", new byte[]{1, 2, 10, 4});
//        flashDataMap.put("2016/11/21", new byte[]{1, 2, 10, 4});
//        flashDataMap.put("2016/11/22", new byte[]{1, 2, 10, 4});
//        flashDataMap.put("2016/11/22", new byte[]{1, 2, 10, 4});
//        flashDataMap.put("2016/11/23", new byte[]{1, 2, 20, 4});
//        flashDataMap.put("2016/11/24", new byte[]{1, 2, 30, 4});
//        flashDataMap.put("2016/11/25", new byte[]{1, 2, 40, 4});
//        flashDataMap.put("2016/11/26", new byte[]{1, 2, 0, 4});
//        flashDataMap.put("2016/11/27", new byte[]{1, 2, 15, 4});
//        flashDataMap.put("2016/11/28", new byte[]{1, 2, 25, 4});
//        flashDataMap.put("2016/12/02", new byte[]{1, 2, 35, 4});
//        flashDataMap.put("2016/12/03", new byte[]{1, 2, 22, 4});
//        flashDataMap.put("2016/12/04", new byte[]{1, 2, 11, 4});
//        flashDataMap.put("2016/12/05", new byte[]{1, 2, 19, 4});
//        flashDataMap.put("2016/12/06", new byte[]{1, 2, 19, 4});
//        flashDataMap.put("2016/12/07", new byte[]{1, 2, 19, 4});
//        flashDataMap.put("2016/12/08", new byte[]{1, 2, 19, 4});
//        MadAirHistoryRecord record = new MadAirHistoryRecord(flashDataMap);
//        MadAirDeviceModelSharedPreference.saveHistoryRecordMock(device.getMacAddress(), record, particleMap);
    }

}
