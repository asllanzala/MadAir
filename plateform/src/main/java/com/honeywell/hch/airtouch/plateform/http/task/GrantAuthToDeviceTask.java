package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 2/14/16.
 */
public class GrantAuthToDeviceTask extends BaseRequestTask {
    private String mSessionId;
    private int mDeviceId;
    private int mAssignRole;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public GrantAuthToDeviceTask(int deviceId, int assignRole,
                                 IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        mDeviceId = deviceId;
        mAssignRole = assignRole;
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GRANT_AUTH_TO_DEVICE);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .grantAuthToDevice(mDeviceId, mAssignRole, mSessionId, mRequestParams, mIReceiveResponse);
            return result;
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
