package com.honeywell.hch.airtouch.plateform.http.task;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.device.DeviceTypeListRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class GetConfigTask extends BaseRequestTask {

    private String mSessionId;
    private IActivityReceive mIReceiveResponse;


    public GetConfigTask(IActivityReceive iReceiveResponse) {
        this.mIReceiveResponse = iReceiveResponse;

        mSessionId = UserInfoSharePreference.getSessionId();

    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_ALL_DEVICE_TYPE_CONFIG);
        if (reLoginResult.isResult()) {

            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getDevicesConfig(mSessionId,
                            new DeviceTypeListRequest(), null);
            return result;
        }
        return  reLoginResult;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
