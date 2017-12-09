package com.honeywell.hch.airtouch.plateform.http.task;


import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.group.GroupResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 10/13/15.
 */
public class MoveOutDeviceFromGroupTask extends BaseRequestTask {
    private String mSessionId;
    private int mGroupId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public MoveOutDeviceFromGroupTask(int groupId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mGroupId = groupId;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.DELETE_DEVICE_FROM_GROUP);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .deleteDeviceFromGroup(mSessionId, mGroupId,
                            mRequestParams, mIReceiveResponse);
            if (result.isResult()) {
                Bundle bundle = result.getResponseData();
                if (bundle.getInt(GroupResponse.CODE_ID) == 200)
                    return result;
            }
            return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", RequestID.DELETE_DEVICE_FROM_GROUP);

        }
        return reLoginResult;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
