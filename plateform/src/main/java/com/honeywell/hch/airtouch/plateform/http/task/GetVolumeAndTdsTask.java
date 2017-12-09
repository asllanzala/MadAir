package com.honeywell.hch.airtouch.plateform.http.task;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Vincent on 9/2/17.
 */

public class GetVolumeAndTdsTask extends BaseRequestTask {
    private int mLocationId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private String mFromDate;
    private String mToDate;

    public GetVolumeAndTdsTask(int locationId, String fromDate, String toDate, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mLocationId = locationId;
        mFromDate = fromDate;
        mToDate = toDate;
        mSessionId = UserInfoSharePreference.getSessionId();
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_VOLUME_AND_TDS);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getVolumeAndTdsByLocationID(mLocationId, mFromDate, mToDate, mSessionId,
                            mRequestParams, mIReceiveResponse);
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
