package com.honeywell.hch.airtouch.plateform.http.task;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SwapLocationRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.util.List;

/**
 * Created by Jin Qian on 15/7/2.
 */
public class SwapLocationNameTask extends BaseRequestTask {
    private int mLocationId;
    private String mUserId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public SwapLocationNameTask(int locationId, IRequestParams requestParams, IActivityReceive
            iReceiveResponse) {
        this.mLocationId = locationId;
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;

        this.mUserId = UserInfoSharePreference.getUserId();
        this.mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.SWAP_LOCATION);
        if (reLoginResult.isResult()) {
            ResponseResult swapResult = HttpProxy.getInstance().getWebService()
                    .swapLocationName(mLocationId, mSessionId ,
                            mRequestParams, mIReceiveResponse);
            if (swapResult.isResult()) {
//                HttpWebService.sharedInstance().getLocation(mUserId, mSessionId, null, null);
                List<UserLocationData> userLocationDataList = UserAllDataContainer.shareInstance().getUserLocationDataList();
                for (UserLocationData userLocationDataItem: userLocationDataList){
                    if (userLocationDataItem.getLocationID() == mLocationId){
                        userLocationDataItem.setName(((SwapLocationRequest)mRequestParams).getName());
                    }
                }
            }

            return swapResult;
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
