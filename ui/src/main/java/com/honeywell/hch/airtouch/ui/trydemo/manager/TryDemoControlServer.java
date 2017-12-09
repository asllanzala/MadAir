package com.honeywell.hch.airtouch.ui.trydemo.manager;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.water.model.WaterDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.AirTouchDeviceObject;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.ui.control.manager.device.IControlManager;

/**
 * Created by h127856 on 16/11/3.
 * 这个类是模拟网络数据，进行try demo的网络数据的设置
 */
public class TryDemoControlServer implements IControlManager {

    private static final int CONTROL_AIRTOUCH_DEVICE = 1;
    private static final int CONTROL_WATER_DEVICE = 2;

    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg == null)
                return;
            switch (msg.what) {
                //GPS find the city
                case CONTROL_AIRTOUCH_DEVICE:
                    String scenarioOrSpeed = (String)msg.obj;
                    parseScenarioOrSpeed(scenarioOrSpeed);
                    sendSuccessCallBack();
                    break;

                case CONTROL_WATER_DEVICE:
                    try{
                        if (TryDemoDataConstructor.getSelectStutasHomeDevicesList().size() > 1){
                            int scenarioMode = (int)msg.obj;
                            HomeDevice homeDevice = TryDemoDataConstructor.getSelectStutasHomeDevicesList().get(1).getDeviceItem();
                            ((WaterDeviceObject)homeDevice).getAquaTouchRunstatus().setScenarioMode(scenarioMode);
                            sendSuccessCallBack();
                        }
                    }catch (Exception e){

                    }

                    break;

                default:
                    break;
            }
        }
    };

    @Override
    public void setSuccessCallback(SuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }


    @Override
    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }

    @Override
    public void controlDevice(int deviceId, String scenarioOrSpeed, String productName) {
        mockRequestAction(CONTROL_AIRTOUCH_DEVICE,scenarioOrSpeed);
    }

    @Override
    public void controlWaterDevice(int deviceId, int scenario, String productName) {
        mockRequestAction(CONTROL_WATER_DEVICE,scenario);
    }

    @Override
    public void getConfigFromServer() {

    }

    @Override
    public void runCommTaskThread(int taskId) {

    }

    private void sendSuccessCallBack(){
        ResponseResult responseResult = new ResponseResult();
        responseResult.setRequestId(RequestID.COMM_TASK);
        if (mSuccessCallback != null) {
            mSuccessCallback.onSuccess(responseResult);
        }

        Intent intent = new Intent(HPlusConstants.TRY_DEMO_REFRESH_MADAIR_DATA);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }

    private void parseScenarioOrSpeed(String scenarioOrSpeed){
        //TODO: when user exitTryDemo,the TryDemoDataConstructor will be cleared.So it will occur java.lang.IndexOutOfBoundsException: Invalid index 0, size is 0
        try {
            HomeDevice homeDevice = TryDemoDataConstructor.getSelectStutasHomeDevicesList().get(0).getDeviceItem();
            if (scenarioOrSpeed.contains("Speed")){
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_MANUAL_INT);
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setFanSpeedStatus(scenarioOrSpeed);
            }else{
                parseScenarioMode(scenarioOrSpeed,homeDevice);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * 根据用户选择的模式，把字符串解析成runstatus里的int
     * @param scenarioModel
     * @param homeDevice
     */
    private void parseScenarioMode(String scenarioModel,HomeDevice homeDevice){
        switch (scenarioModel) {
            case HPlusConstants.MODE_SLEEP:
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_SLEEP_INT);
                break;
            case HPlusConstants.MODE_AUTO:
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_AUTO_INT);

                break;
            case HPlusConstants.MODE_QUICK:
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_QUICK_INT);

                break;
            case HPlusConstants.MODE_SILENT:
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_SILENT_INT);

                break;
            case HPlusConstants.MODE_OFF:
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_OFF_INT);
                break;

            case HPlusConstants.MODE_MANUAL:
                ((AirTouchDeviceObject)homeDevice).getAirtouchDeviceRunStatus().setScenarioMode(HPlusConstants.MODE_MANUAL_INT);
                break;
            default:
                break;

        }
    }


    /**
     * 用3秒钟模拟网络请求的时间
     * @param action
     * @param scenarioModeOrSpeed
     */
    private void mockRequestAction(final int action,final Object scenarioModeOrSpeed ) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }

                Message message = Message.obtain();
                message.what = action;
                message.obj = scenarioModeOrSpeed;
                handler.sendMessage(message);

            }
        }).start();
    }
}
