package com.honeywell.hch.airtouch.plateform.http.task;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.group.CreateGroupResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;


/**
 * Created by Jin Qian on 10/13/15.
 */
public class CreateGroupTask extends BaseRequestTask {
    private String mSessionId;
    private String mGroupName;
    private int mMasterDeviceId;
    private int mLocationId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public CreateGroupTask(String groupName, int masterDeviceId, int locationId,
                           IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mGroupName = groupName;
        mMasterDeviceId = masterDeviceId;
        mLocationId = locationId;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.CREATE_GROUP);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .createGroup(mSessionId, mGroupName, mMasterDeviceId, mLocationId,
                            mRequestParams, mIReceiveResponse);

            if (result.isResult()) {
                Bundle bundle = result.getResponseData();
                if (bundle.getInt(CreateGroupResponse.CODE_ID) == 200)
                    return result;
            }
            return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", RequestID.CREATE_GROUP);

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
