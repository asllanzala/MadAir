package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class AddDeviceTask extends BaseRequestTask {
    private int mLocationId;
    private String mSessionId;
    private String mUserId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public AddDeviceTask(int locationId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;

        mLocationId = locationId;
        mSessionId = UserInfoSharePreference.getSessionId();
        mUserId =  UserInfoSharePreference.getUserId();
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.DELETE_DEVICE);
        if (reLoginResult.isResult()) {
            ResponseResult addResult = HttpProxy.getInstance().getWebService()
                    .addDevice(mLocationId, mSessionId,
                            mRequestParams, mIReceiveResponse);
            return addResult;
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
