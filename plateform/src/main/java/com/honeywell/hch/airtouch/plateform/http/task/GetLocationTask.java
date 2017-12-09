package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Vincent on 10/3/16.
 */
public class GetLocationTask  extends BaseRequestTask {
    private String mUserId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;

    public GetLocationTask( IActivityReceive iReceiveResponse) {
        this.mIReceiveResponse = iReceiveResponse;
        mUserId = UserInfoSharePreference.getUserId();
        mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_LOCATION);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService().getLocation(mUserId, mSessionId, null, mIReceiveResponse);
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
