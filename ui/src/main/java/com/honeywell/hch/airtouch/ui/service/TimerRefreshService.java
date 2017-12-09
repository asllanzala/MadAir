package com.honeywell.hch.airtouch.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.task.ShortTimerRefreshTask;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen(H127856) on 12/10/2015.
 */
public class TimerRefreshService extends Service {
    private static final String TAG = "AirTouchService";

    private static final int SHOR_FRESH = 10001;
    private static final int LONG_FRESH = 10002;

    private static final int POLLING_GAP = 20 * 1000;

    private int sleepCount = 0;

    private boolean isThreadRunning = true;

    private boolean isNeedRefresh = true;

    private final IBinder binder = new MyBinder();

    private Thread refreshThread;

    private ShortTimerRefreshTask mShortTimerRefreshTask;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOR_FRESH:
                    if (mShortTimerRefreshTask == null || !mShortTimerRefreshTask.isRefreshTaskRunning){
                        mShortTimerRefreshTask = new ShortTimerRefreshTask();
                        AsyncTaskExecutorUtil.executeAsyncTask(mShortTimerRefreshTask);
                    }
                    break;
                case LONG_FRESH:
                    UserAllDataContainer.shareInstance().getWeatherRefreshManager().addCityListRefresh(getCurrentHomeCityList(),true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public class MyBinder extends Binder {
        public TimerRefreshService getService() {
            return TimerRefreshService.this;
        }
    }
    private String mSessionId;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mSessionId = UserInfoSharePreference.getSessionId();
        
        refreshThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRunning) {
                    try {
                        if (isNeedRefresh){
                            //get location data and device data
                            Message message = Message.obtain();
                            message.what = SHOR_FRESH;
                            mHandler.sendMessage(message);

                            if (sleepCount == 60 || sleepCount == 0){
                                if (AppManager.getLocalProtocol().canShowWeatherView()){
                                    Message message2 = Message.obtain();
                                    message2.what = LONG_FRESH;
                                    mHandler.sendMessage(message2);
                                }
                                sleepCount = 0;
                            }

                            sleepCount++;
                        }
                        Thread.sleep(POLLING_GAP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        refreshThread.start();
    }


    private List<String> getCurrentHomeCityList(){
        List<String> cityList = new ArrayList<>();
        List<UserLocationData> userLocationDatas = UserDataOperator.getHomePageUserLocationDataList(UserAllDataContainer.shareInstance().getUserLocationDataList(),
                UserAllDataContainer.shareInstance().getmVirtualUserLocationDataList());
        for (UserLocationData userLocationData : userLocationDatas){
            cityList.add(userLocationData.getCity());
        }
        return cityList;
    }

    /**
     * stop the refreshMadAirWeather
     */
    public void stopRefreshThread(){
        isNeedRefresh = false;
        sleepCount = 0;
    }

    /**
     * start the refreshMadAirWeather
     */
    public void startRefreshThread(){
        isNeedRefresh = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isThreadRunning = false;
    }


}
