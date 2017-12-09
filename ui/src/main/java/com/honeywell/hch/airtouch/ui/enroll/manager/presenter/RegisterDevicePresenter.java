package com.honeywell.hch.airtouch.ui.enroll.manager.presenter;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.ByteUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.DeviceRegisterRequest;
import com.honeywell.hch.airtouch.plateform.http.task.AddDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.CommTask;
import com.honeywell.hch.airtouch.plateform.http.task.ShortTimerRefreshTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.control.manager.umeng.UmengUiManager;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;

import com.honeywell.hch.airtouch.ui.enroll.interfacefile.IRegisterDeviceView;


/**
 * Created by h127856 on 16/10/13.
 *
 */
public class RegisterDevicePresenter  {

    private final String TAG = "RegisterDevicePresenter";

    private int mTaskId;
    private IRegisterDeviceView iRegisterDeviceView;

    public RegisterDevicePresenter(IRegisterDeviceView iRegisterDeviceView){
        this.iRegisterDeviceView = iRegisterDeviceView;
    }

    public void addDevice(){
        String macId = EnrollScanEntity.getEntityInstance().getmMacID();
        String  crcId =  ByteUtil.calculateCrc(macId);
        String deviceName = DIYInstallationState.getDeviceName();
        int locationId = DIYInstallationState.getLocationId();

        IActivityReceive addDeviceResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                if (responseResult.isResult()) {
                    switch (responseResult.getRequestId()) {
                        case ADD_DEVICE:
                            if (responseResult.getResponseCode() == StatusCode.OK) {
                                mTaskId = responseResult.getResponseData()
                                        .getInt(HPlusConstants.COMM_TASK_BUNDLE_KEY);
                                runCommTask();
                            }
                            return;
                        default:
                            break;
                    }
                } else if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
                    iRegisterDeviceView.registerByOtherError();
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL, "add_device_response_code_" + StatusCode.BAD_REQUEST);

                }else if (responseResult.getResponseCode() == StatusCode.NETWORK_TIMEOUT
                           || responseResult.getResponseCode() ==  StatusCode.NETWORK_ERROR){
                    iRegisterDeviceView.registerTimeoutError();
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL, "add_device_response_code_" + StatusCode.BAD_REQUEST);

                } else {
                    iRegisterDeviceView.otherFailed();
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL, "add_device_" + responseResult.getResponseCode() + "_" + responseResult.getExeptionMsg());

                }
            }
        };

//        mLoadingCallback.onLoad(mContext.getString(R.string.adding_device));
        DeviceRegisterRequest deviceRegisterRequest
                = new DeviceRegisterRequest(macId, crcId, deviceName);
        AddDeviceTask requestTask
                = new AddDeviceTask(locationId, deviceRegisterRequest, addDeviceResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    private void runCommTask() {
        final IActivityReceive runCommTaskResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                if (responseResult.isResult()) {
                    switch (responseResult.getRequestId()) {
                        case COMM_TASK:
                            switch (responseResult.getFlag()) {
                                case HPlusConstants.COMM_TASK_SUCCEED:
                                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "runCommTask--");
                                    getAllLocationAndAllDevice();
                                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_SUCCESS, "");
                                    break;
                                case HPlusConstants.COMM_TASK_TIMEOUT:
                                    iRegisterDeviceView.commNotGetResultFailed();
                                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_SUCCESS, "");

                                    break;
                                default:
                                    iRegisterDeviceView.commNotGetResultFailed();
                                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL, "add_device_runCommTask_getFlag_" + responseResult.getFlag());

                                    break;
                            }
                            break;

                        default:
                            break;
                    }
                } else {
                    iRegisterDeviceView.otherFailed();
                    UmengUtil.enrollEvent(UmengUiManager.getEnrollProductName(),UmengUtil.EnrollEventType.ENROLL_FAIL, "add_device_runCommTask_response_" + responseResult.getResponseCode() + "_" + responseResult.getExeptionMsg());

                }
            }
        };

        CommTask commTask = new CommTask(mTaskId, null, runCommTaskResponse, true);
        AsyncTaskExecutorUtil.executeAsyncTask(commTask);
    }


    private void getAllLocationAndAllDevice() {
        IActivityReceive getAllDeviceResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                switch (responseResult.getRequestId()) {
                    case SHORT_TIMER_REFRESH:
                        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "SHORT_TIMER_REFRESH--");
                        sendBroadcastToMainPage(false);
                        iRegisterDeviceView.addDeviceSuccess();
                        break;
                }
            }
        };

        ShortTimerRefreshTask shortTimerRefreshTask = new ShortTimerRefreshTask(getAllDeviceResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(shortTimerRefreshTask);
    }


    private void sendBroadcastToMainPage(boolean isAddHome) {
        Intent intent = new Intent(HPlusConstants.ADD_DEVICE_OR_HOME_ACTION);
        intent.putExtra(HPlusConstants.IS_ADD_HOME, isAddHome);
        intent.putExtra(HPlusConstants.LOCAL_LOCATION_ID, DIYInstallationState.getLocationId());
        AppManager.getInstance().getApplication().sendBroadcast(intent);
    }
}


