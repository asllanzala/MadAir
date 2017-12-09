package com.honeywell.hch.airtouch.plateform.http.task;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.multi.ScenarioMultiResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.UUID;

/**
 * Created by Vincent on 6/5/16.
 */
public class ControlHomeDeviceTask extends BaseRequestTask {
    private int mLocationId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private int mScenarioMode ;
    private String mTaskUuid = "";

    public ControlHomeDeviceTask(int locationId, IRequestParams requestParams,
                                 IActivityReceive iReceiveResponse, int scenarioModel) {

        mLocationId = locationId;
        mSessionId = UserInfoSharePreference.getSessionId();
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mScenarioMode = scenarioModel;
        mTaskUuid = UUID.randomUUID().toString();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.CONTROL_HOME_DEVICE);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .controlHomeDevice(mLocationId, mSessionId, mRequestParams, mIReceiveResponse);
            return result;
        }

        return reLoginResult;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        Bundle bundle = new Bundle();
        if (responseResult.getResponseData() != null){
            bundle.putSerializable(ScenarioMultiResponse.SCENARIO_DATA,responseResult.getResponseData()
                    .getSerializable(ScenarioMultiResponse.SCENARIO_DATA));
        }
        bundle.putString(HPlusConstants.ARG_CONTROL_TASK_UUID,mTaskUuid);
        responseResult.setResponseData(bundle);
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }

    public String getmTaskUuid() {
        return mTaskUuid;
    }

}
