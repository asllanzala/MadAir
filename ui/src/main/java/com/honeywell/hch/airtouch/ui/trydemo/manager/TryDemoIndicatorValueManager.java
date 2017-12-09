package com.honeywell.hch.airtouch.ui.trydemo.manager;

import android.os.Handler;
import android.os.Message;

import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlUIBaseManager;
import com.honeywell.hch.airtouch.ui.trydemo.ui.IRefreshOpr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 16/11/8.
 */
public class TryDemoIndicatorValueManager {

    private static final int TRY_DEMO_REFRSH_MSG = 9898;

    private  boolean isThreadRun = true;

    private static boolean isNeedChangeValue = true;

    public static List<IRefreshOpr> mRefrshFragment = new ArrayList<>();

    public static void addRefreshListener(IRefreshOpr iRefreshOpr){
        mRefrshFragment.add(iRefreshOpr);
    }

    public static void removeRefreshListener(IRefreshOpr iRefreshOpr){
        mRefrshFragment.remove(iRefreshOpr);
    }

    public void setThreadRunStatus(boolean isRunning){
        isThreadRun = isRunning;
    }

    public void exitTryDemoProcess(){
        isThreadRun = false;
        isNeedChangeValue = true;
        mRefrshFragment.clear();
    }


     Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg == null)
                return;
            switch (msg.what) {
                //GPS find the city
                case TRY_DEMO_REFRSH_MSG:
                    if (mRefrshFragment.size() > 0){
                        for (IRefreshOpr iRefreshOpr : mRefrshFragment){
                            iRefreshOpr.doRefreshUIOpr();
                        }
                    }

                    break;

                default:
                    break;
            }
        }
    };

    /**
     * 改变pm value的值
     * 每间3秒钟去修改一次数据。同时通知界面去更新
     */
    public  void startChangePMValue() {
        isThreadRun = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRun) {
                    if (isNeedChangeValue){
                        try{
                            Thread.sleep(3000);
                        }catch (Exception e){

                        }

                        synchronized (TryDemoDataConstructor.mLockObj){
                            if(TryDemoDataConstructor.getTryDemoHomeDeviceList().size() > 0){
                                constructAirtouchDeviceValue();
                            }
                        }
                    }
                }
            }
        }).start();
    }


    private  void constructAirtouchDeviceValue(){
        AirTouchDeviceObject airTouchDeviceObject = (AirTouchDeviceObject)TryDemoDataConstructor.getTryDemoHomeDeviceList().get(0);
        int mSpeed  = 0;
        switch (airTouchDeviceObject.getAirtouchDeviceRunStatus().getScenarioMode()) {
            case HPlusConstants.MODE_AUTO_INT:
                mSpeed = airTouchDeviceObject.getAutoSpeedFeature().getAutoSpeed();
                break;

            case HPlusConstants.MODE_SLEEP_INT:
                mSpeed = airTouchDeviceObject.getSpeedStatusFeature().getSleepSpeed();

                break;

            case HPlusConstants.MODE_QUICK_INT:
                mSpeed = airTouchDeviceObject.getSpeedStatusFeature().getMaxSpeed();

                break;

            case HPlusConstants.MODE_SILENT_INT:
                mSpeed = airTouchDeviceObject.getSpeedStatusFeature().getSilentSpeed();

                break;
            //手动时，模式致为初始状态
            case HPlusConstants.MODE_DEFAULT_INT:
            case HPlusConstants.MODE_MANUAL_INT:
                mSpeed = ControlUIBaseManager.parseSpeed(airTouchDeviceObject.getAirtouchDeviceRunStatus());
                break;

            default:
                break;
        }


        int pmValue =  airTouchDeviceObject.getAirtouchDeviceRunStatus().getmPM25Value();
        int value = pmValue - mSpeed * TryDemoConstant.EVERY_SPEED_PM_VALUE * 3;
        int afterpmValue = value > TryDemoConstant.PM_MIN_VAULE ? value : TryDemoConstant.PM_MIN_VAULE;
        airTouchDeviceObject.getAirtouchDeviceRunStatus().setmPM25Value(afterpmValue);
        if (afterpmValue <= TryDemoConstant.PM_MIN_VAULE){
            isNeedChangeValue = false;
        }
        changeTvoc(airTouchDeviceObject.getAirtouchDeviceRunStatus().getTvocValue(),mSpeed,airTouchDeviceObject);


        Message message = Message.obtain();
        message.what = TRY_DEMO_REFRSH_MSG;
        handler.sendMessage(message);

    }

    private void changeTvoc(int orignalTvoc,int speed,AirTouchDeviceObject airTouchDeviceObject){
        int afterChangeTvoc = HPlusConstants.GOOD;
        if (orignalTvoc > HPlusConstants.GOOD){
            afterChangeTvoc = orignalTvoc -  speed * TryDemoConstant.EVERY_SPEED_TVOC_VAULE * 3;
        }
        airTouchDeviceObject.getAirtouchDeviceRunStatus().setTvocValue(afterChangeTvoc);
    }
}
