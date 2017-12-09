package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/8/24.
 */
public class CommTask extends BaseRequestTask {

    private static final int COMM_TASK_TIME_GAP = 1000;

    private int mTaskId;
    private String mUserId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private int checkMacPollingTime = 0;
    private boolean mNeedRefreshHomeAndDevices;
    private int CHECK_COMM_TASK_TIMES = 40;


    public CommTask(int taskId, IRequestParams requestParams, IActivityReceive iReceiveResponse, boolean needRefreshHomeAndDevices) {
        this.mTaskId = taskId;
        this.mUserId = UserInfoSharePreference.getUserId();
        this.mSessionId = UserInfoSharePreference.getSessionId();
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        this.mNeedRefreshHomeAndDevices = needRefreshHomeAndDevices;
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        while (checkMacPollingTime <= CHECK_COMM_TASK_TIMES) {
            ResponseResult taskResult = HttpProxy.getInstance().getWebService()
                    .getCommTask(mTaskId, mSessionId, mRequestParams, mIReceiveResponse);
            if (taskResult.isResult()) {
                switch (taskResult.getFlag()) {
                    case HPlusConstants.COMM_TASK_RUNNING:
                        if (checkMacPollingTime >= CHECK_COMM_TASK_TIMES) {
                            checkMacPollingTime = 0;
                            taskResult.setFlag(HPlusConstants.COMM_TASK_TIMEOUT);
                            afterSuccess();
                            return taskResult;
                        }
                        break;

                    case HPlusConstants.COMM_TASK_SUCCEED:
                    case HPlusConstants.COMM_TASK_TIMEOUT:
                        taskResult.setFlag(taskResult.getFlag());
                        afterSuccess();
                         return taskResult;

                    case HPlusConstants.COMM_TASK_FAILED:
                        taskResult.setFlag(HPlusConstants.COMM_TASK_FAILED);
                        return taskResult;
                }

            } else {
                return taskResult;
            }
            checkMacPollingTime++;
            try {
                Thread.sleep(HPlusConstants.COMM_TASK_TIME_GAP);
            } catch (Exception e) {

            }
        }

        return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", RequestID.COMM_TASK);
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }

    private void afterSuccess() {
        if (mNeedRefreshHomeAndDevices) {
            HttpProxy.getInstance().getWebService().getLocation(mUserId, mSessionId, null, mIReceiveResponse);
            reloadDeviceInfo();
        }
    }
}
