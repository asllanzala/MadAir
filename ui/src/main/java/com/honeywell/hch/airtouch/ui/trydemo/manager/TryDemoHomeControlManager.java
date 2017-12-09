package com.honeywell.hch.airtouch.ui.trydemo.manager;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.home.request.HomeControlRequest;
import com.honeywell.hch.airtouch.plateform.http.task.ControlHomeDeviceTask;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlBaseManager;

import java.util.List;

/**
 * Created by honeywell on 26/12/2016.
 */

public class TryDemoHomeControlManager extends ControlBaseManager {

    private static final int AWAY_SCENARIO_MODEL = 0;
    private static final int AT_HOME_SCENARIO_MODEL = 1;
    private static final int SLEEP_SCENARIO_MODEL = 2;
    private static final int AWAKE_SCENARIO_MODEL = 3;

    private static final int END_HOME_CONTROL = 1002;
    private int mLocationId;
    private int mMultiResult;
    private int mScenario;
    private boolean isFinishTryDemo = false;

    public void controlHomeDevice(int locationId, int scenario) {
        mScenario = scenario;
        mLocationId = locationId;

        setHomeScenarioIsFlashing(scenario);
        mockRequestAction();
    }

    /**
     * 移除所有的callback
     */
    public void removeAllMessage() {
        isFinishTryDemo = true;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null)
                return;
            switch (msg.what) {
                case END_HOME_CONTROL:
                    if (!isFinishTryDemo) {
                        setHomeScenarioIsFlashing(DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
                        if (TryDemoHomeListContructor.getInstance().getUserLocationDataList() != null
                                && TryDemoHomeListContructor.getInstance().getUserLocationDataList().size() > 0) {
                            TryDemoHomeListContructor.getInstance().getUserLocationDataList().get(0).setOperationModel(mScenario);
                            setAllDeviceInThisScenario();
                            sendHomeStopFlashTask();
                        }
                    }

                default:
                    break;
            }
        }
    };


    private void setAllDeviceInThisScenario() {
        UserLocationData userLocationData = TryDemoHomeListContructor.getInstance().getUserLocationDataList().get(0);
        List<HomeDevice> homeDeviceList = userLocationData.getHomeDevicesList();
        for (HomeDevice homeDevice : homeDeviceList) {
            if (homeDevice instanceof AirTouchDeviceObject) {
                ((AirTouchDeviceObject) homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(getAirtouchScenarioModel());

            } else if (homeDevice instanceof WaterDeviceObject) {
                ((WaterDeviceObject) homeDevice).getAquaTouchRunstatus().setScenarioMode(getWaterDeviceScenario());
            }
        }
    }

    private int getAirtouchScenarioModel() {
        if (mScenario == AWAKE_SCENARIO_MODEL) {
            return HPlusConstants.MODE_AUTO_INT;
        }
        return mScenario;
    }

    private int getWaterDeviceScenario() {
        if (mScenario > AWAY_SCENARIO_MODEL) {
            return HPlusConstants.WATER_MODE_HOME;
        }
        return HPlusConstants.WATER_MODE_AWAY;
    }

    /**
     * 用3秒钟模拟网络请求的时间
     */
    private void mockRequestAction() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }

                Message message = Message.obtain();
                message.what = END_HOME_CONTROL;
                message.obj = mScenario;
                handler.sendMessage(message);

            }
        }).start();
    }

    public void setHomeScenarioIsFlashing(int scenarioMode) {
        SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH,
                String.valueOf(mLocationId), scenarioMode);
    }

    private void sendHomeStopFlashTask() {
        Intent intent = new Intent(HPlusConstants.HOME_CONTROL_STOP_FLASHINGTASK);
        intent.putExtra(DashBoadConstant.ARG_RESULT_CODE, HPlusConstants.COMM_TASK_SUCCEED);
        intent.putExtra(HPlusConstants.LOCAL_LOCATION_ID, mLocationId);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }

}
