package com.honeywell.hch.airtouch.plateform.http.task;

import android.content.Intent;
import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskListResponse;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskRequest;
import com.honeywell.hch.airtouch.plateform.http.model.multicommtask.MultiCommTaskResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin Qian on 10/30/15.
 */
public class MultiCommTask extends BaseRequestTask {
    private final String TAG = "MultiCommTask";
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private int checkMacPollingTime = 0;
    private final int CHECK_COMM_TASK_TIMES = 30;
    private final String RUNNINGSTATE = "Running";
    private final String CREATEDSTATE = "Created";
    private final String SUCCESSSTATE = "Succeeded";
    private final String FAILSTATE = "Failed";

    private final int ALL_SUCCESS = 0;
    private final int PART_FAILED = 1;
    private final int ALL_FAILED = 2;

    private String mControlTaskId = "";
    private final int GROUPCACHEID = 100;

    private ArrayList<Integer> mSuccessCmmIdTask = new ArrayList<>();


    private int mTotalTaskIdCount = 0;

    private int mCommType = HPlusConstants.FROM_GROUP_SCENARIO_CONTROL;

    /**
     *
     * @param requestParams
     * @param iReceiveResponse
     * @param controlTaskId
     * @param commType 是homeControl 还是group control
     */
    public MultiCommTask(IRequestParams requestParams, IActivityReceive iReceiveResponse, String controlTaskId,int commType) {

        this.mSessionId = UserInfoSharePreference.getSessionId();
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mControlTaskId = controlTaskId;
        mTotalTaskIdCount = ((MultiCommTaskRequest)requestParams).getDeviceListLength();
        mCommType = commType;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {
        while (checkMacPollingTime <= CHECK_COMM_TASK_TIMES) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "checkMacPollingTime: " + checkMacPollingTime);
            ResponseResult taskResult = HttpProxy.getInstance().getWebService()
                    .multiCommTask(mSessionId, mRequestParams, mIReceiveResponse);

            if (taskResult.isResult()) {
                MultiCommTaskListResponse multiTaskData = (MultiCommTaskListResponse) taskResult.getResponseData()
                        .getSerializable(MultiCommTaskListResponse.MUTLICOMMTASK);
                if (multiTaskData == null)
                    return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, AppManager.getInstance().getApplication().getString(R.string.group_control_all_fail), RequestID.MULTI_COMM_TASK);

                boolean isNeedContinue = false;
                // If there exists task not finished, that means at least one task "Running" or "Created"
                List<MultiCommTaskResponse> tasks = multiTaskData.getMultiCommTaskResponses();
                for (MultiCommTaskResponse task : tasks) {
                    LogUtil.log(LogUtil.LogLevel.INFO, TAG, "task.getState(): " + task.getState());
                    if (RUNNINGSTATE.equals(task.getState()) || CREATEDSTATE.equals(task.getState())) {
                        if (checkMacPollingTime >= CHECK_COMM_TASK_TIMES) {
                            checkMacPollingTime = 0;

                            return getResponseResult(tasks, taskResult);
                        } else {
                            isNeedContinue = true;
                            break;
                        }
                    }
                }

                if (!isNeedContinue) {
                    return getResponseResult(tasks, taskResult);
                }
            } else {
                taskResult.setFlag(HPlusConstants.COMM_TASK_ALL_FAILED);
                return taskResult;
            }
            checkMacPollingTime++;
            try {
                Thread.sleep(HPlusConstants.COMM_TASK_TIME_GAP);
            } catch (Exception e) {

            }

        }
        return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL,
                AppManager.getInstance().getApplication().getString(R.string.group_control_time_out), RequestID.MULTI_COMM_TASK);

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        Bundle bundle = new Bundle();
        bundle.putString(HPlusConstants.ARG_CONTROL_TASK_UUID, mControlTaskId);
        bundle.putIntegerArrayList(HPlusConstants.BUNDLE_DEVICES_IDS, mSuccessCmmIdTask);
        responseResult.setResponseData(bundle);
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }


    private int getCommTaskFinalResult(List<MultiCommTaskResponse> tasks) {

        boolean isHasSuccess = false;
        boolean isHasFailed = false;

        for (MultiCommTaskResponse task : tasks) {
            if (SUCCESSSTATE.equals(task.getState())) {
                mSuccessCmmIdTask.add(task.getCommTaskId());
                isHasSuccess = true;
            } else {
                isHasFailed = true;
            }
        }
        if (!isHasFailed && mTotalTaskIdCount == mSuccessCmmIdTask.size()) {
            return ALL_SUCCESS;
        } else if (!isHasSuccess) {
            return ALL_FAILED;
        }

        return PART_FAILED;
    }

    private ResponseResult getResponseResult(List<MultiCommTaskResponse> tasks, ResponseResult taskResult) {
        int result = getCommTaskFinalResult(tasks);
        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "multi task result: " + result);
        if (result == ALL_SUCCESS) {
            taskResult.setFlag(HPlusConstants.COMM_TASK_SUCCEED);
        } else if (result == ALL_FAILED) {
            taskResult.setFlag(HPlusConstants.COMM_TASK_ALL_FAILED);
        } else {
            taskResult.setFlag(HPlusConstants.COMM_TASK_PART_FAILED);
        }

        if (mCommType == HPlusConstants.FROM_HOME_SCENARIO_CONTROL){
            ResponseResult getLocationResult = HttpProxy.getInstance().getWebService().getLocation(UserInfoSharePreference.getUserId(), UserInfoSharePreference.getSessionId(), null, null);
            if (getLocationResult.isResult()) {
                reloadDeviceInfo();
                Intent boradIntent = new Intent(HPlusConstants.SHORTTIME_REFRESH_END_ACTION);
                AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
            }
        }else if (mCommType == HPlusConstants.FROM_GROUP_SCENARIO_CONTROL){
            Intent boradIntent = new Intent(HPlusConstants.GROUP_SCENARIO_REFRESH);
            AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
        }

        return taskResult;
    }

}
