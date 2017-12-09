package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Vincent on 6/5/16.
 */
public class ControlWaterDeviceTask extends BaseRequestTask  {
    private int mDeviceId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public ControlWaterDeviceTask(int deviceId, IRequestParams requestParams,
                             IActivityReceive iReceiveResponse) {

        mDeviceId = deviceId;
        mSessionId = UserInfoSharePreference.getSessionId();

        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;

    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.WATER_CONTROL_DEVICE);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .controlWaterDevice(mDeviceId, mSessionId, mRequestParams, mIReceiveResponse);
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
