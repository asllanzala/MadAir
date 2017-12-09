package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Vincent on 30/3/16.
 */
public class GetDeviceListByGroupIdTask extends BaseRequestTask {
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private boolean isAutoOperation = false;

    public GetDeviceListByGroupIdTask(IRequestParams requestParams, IActivityReceive
            iReceiveResponse,boolean isAutoRefresh) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        isAutoOperation = isAutoRefresh;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_DEVICE_LIST_BY_GROUP_ID);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getDeviceListByGroupId(mSessionId, mRequestParams, mIReceiveResponse);
            return result;
        }

        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        responseResult.setIsAutoRefresh(isAutoOperation);
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
