package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 10/13/15.
 */
public class GetGroupByLocationIdTask extends BaseRequestTask {
    private String mSessionId;
    private int mLocationId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private boolean isRefresh = false;

    public GetGroupByLocationIdTask(boolean refresh,int locationId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mLocationId = locationId;
        isRefresh = refresh;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_GROUP_BY_LOCATION_ID);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getGroupByLocationId(mSessionId, mLocationId,
                            mRequestParams, mIReceiveResponse);

            return result;
        }
        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        responseResult.setIsAutoRefresh(isRefresh);

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
