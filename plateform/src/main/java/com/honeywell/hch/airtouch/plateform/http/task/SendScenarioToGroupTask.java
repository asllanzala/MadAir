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
 * Created by Jin Qian on 10/13/15.
 */
public class SendScenarioToGroupTask extends BaseRequestTask {
    private String mSessionId;
    private int mGroupId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private String mTaskUuid = "";

    public SendScenarioToGroupTask(int groupId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mGroupId = groupId;
        mTaskUuid = UUID.randomUUID().toString();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.SEND_SCENARIO_TO_GROUP);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .sendScenarioToGroup(mSessionId, mGroupId,
                            mRequestParams, mIReceiveResponse);
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
