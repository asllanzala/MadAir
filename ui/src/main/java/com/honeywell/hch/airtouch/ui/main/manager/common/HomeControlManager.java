package com.honeywell.hch.airtouch.ui.main.manager.common;

import android.content.Intent;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.home.request.HomeControlRequest;
import com.honeywell.hch.airtouch.plateform.http.model.multi.ScenarioMultiCommTaskResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multi.ScenarioMultiResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskRequest;
import com.honeywell.hch.airtouch.plateform.http.task.ControlHomeDeviceTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetLocationTask;
import com.honeywell.hch.airtouch.plateform.umeng.UmengUtil;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.control.manager.device.ControlBaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 16/8/11.
 */
public class HomeControlManager extends ControlBaseManager {

    private int mLocationId;
    private int mMultiResult;
    private int mScenario;

    public void controlHomeDevice(int locationId, int scenario) {
        mScenario = scenario;
        mLocationId = locationId;
        setHomeScenarioIsFlashing(scenario);

        HomeControlRequest homeControlRequest = new HomeControlRequest(scenario);
        ControlHomeDeviceTask requestTask
                = new ControlHomeDeviceTask(locationId, homeControlRequest, mResponse, scenario);
        mLastTaskId = requestTask.getmTaskUuid();
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    @Override
    public void handleResponseReceive(ResponseResult responseResult) {
        switch (responseResult.getRequestId()) {
            case CONTROL_HOME_DEVICE:
                String homeControlTaskId = responseResult.getResponseData().getString(DashBoadConstant.ARG_CONTROL_TASK_UUID);
                if (!StringUtil.isEmpty(mLastTaskId) && mLastTaskId.equals(homeControlTaskId)) {
                    startMultiCommTaskAfterControl(responseResult, homeControlTaskId);
                }
                break;
            case GET_LOCATION:
                if (mMultiResult != -1) {
                    sendHomeStopFlashTask(mMultiResult);
                    mMultiResult = -1;
                }
                break;
        }
    }


    /**
     * 存储multi task的对象的唯一标识和对应model的一一对应关系
     */
    @Override
    public void storageMultiTaskStatus(String commtaskId) {

    }


    @Override
    public void dealMultiCommTaskResult(ResponseResult responseResult, boolean isResultTrue) {

        String controlTaskId = responseResult.getResponseData().getString(DashBoadConstant.ARG_CONTROL_TASK_UUID);

        if (controlTaskId.equals(mLastTaskId)) {
            mLastTaskId = "";

            setHomeScenarioIsFlashing(DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
            if (isResultTrue) {
                mMultiResult = responseResult.getFlag();
                GetLocationTask requestTask
                        = new GetLocationTask(mResponse);
                AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
            } else {
                sendHomeStopFlashTask(responseResult.getFlag());
            }

            UmengUtil.HomeControlType homeCtrlStatus = responseResult.getFlag() == HPlusConstants.COMM_TASK_ALL_FAILED ? UmengUtil.HomeControlType.HOME_CONTROL_FAIL : UmengUtil.HomeControlType.HOME_CONTROL_SUCCESS;
            UmengUtil.homeControlEvent(mLocationId, mScenario, homeCtrlStatus, String.valueOf(responseResult.getFlag()));
        }
    }

    public void startMultiCommTaskAfterControl(ResponseResult responseResult, String controlHomeTaskId) {
        if (responseResult.getResponseCode() == StatusCode.OK) {
            // Return success result to UI thread.
            ScenarioMultiResponse response = (ScenarioMultiResponse) responseResult.getResponseData()
                    .getSerializable(ScenarioMultiResponse.SCENARIO_DATA);
            if (response == null) {
                mLastTaskId = "";
                setHomeScenarioIsFlashing(DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
                responseResult.setFlag(HPlusConstants.HOME_CONTROL_ERROR);
                sendErrorCallBack(responseResult, R.string.group_control_all_fail);
                UmengUtil.homeControlEvent(mLocationId, mScenario, UmengUtil.HomeControlType.HOME_CONTROL_FAIL, "startMultiCommTaskAfterControl response is null");

                return;
            }

            List<Integer> taskIds = new ArrayList<>();
            int taskCount = 0;
            for (ScenarioMultiCommTaskResponse res : response.getGroupCommTaskResponse()) {

                if (res.getCommTaskId() != 0) {
                    taskCount += res.getCommTaskId();
                    taskIds.add(res.getCommTaskId());
                }
                //TODO: 如果taskId ＝0，说明这个设备不可以被控制，所以也不需要发到云端去轮询查询
//                taskIds.add(res.getCommTaskId());
            }

            // If all task is 0, do not run multi-task.
            if (taskCount == 0) {
                mLastTaskId = "";
                setHomeScenarioIsFlashing(DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
                sendHomeStopFlashTask(HPlusConstants.COMM_TASK_ALL_FAILED);
                UmengUtil.homeControlEvent(mLocationId, mScenario, UmengUtil.HomeControlType.HOME_CONTROL_FAIL, "cmmtask count is 0");

            } else {
                runMultiCommTask(new MultiCommTaskRequest(taskIds), controlHomeTaskId, HPlusConstants.FROM_HOME_SCENARIO_CONTROL);
            }
        } else {
            mLastTaskId = "";
            setHomeScenarioIsFlashing(DashBoadConstant.DEAFAULT_SCENARIO_MODEL);
            responseResult.setFlag(HPlusConstants.HOME_CONTROL_ERROR);
            sendErrorCallBack(responseResult, R.string.group_control_all_fail);
            UmengUtil.homeControlEvent(mLocationId, mScenario, UmengUtil.HomeControlType.HOME_CONTROL_FAIL, "responseResult.getResponseCode()_" + responseResult.getResponseCode());
        }
    }

    private void sendHomeStopFlashTask(int code) {
        Intent intent = new Intent(HPlusConstants.HOME_CONTROL_STOP_FLASHINGTASK);
        intent.putExtra(DashBoadConstant.ARG_RESULT_CODE, code);
        intent.putExtra(HPlusConstants.LOCAL_LOCATION_ID, mLocationId);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(intent);
    }


    public void setHomeScenarioIsFlashing(int scenarioMode) {
        SharePreferenceUtil.setPrefInt(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH,
                String.valueOf(mLocationId), scenarioMode);
    }

    public String getLastTaskId() {
        return mLastTaskId;
    }

    public void setLastTaskId(String mLastTaskId) {
        this.mLastTaskId = mLastTaskId;
    }
}
