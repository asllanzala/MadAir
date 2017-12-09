package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 10/13/15.
 */
public class GetGroupByGroupIdTask extends BaseRequestTask {
    private String mSessionId;
    private int mGroupId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private boolean isNeedRefreshOpr;

    public GetGroupByGroupIdTask(boolean isRefreshOpr,int groupId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mGroupId = groupId;
        isNeedRefreshOpr = isRefreshOpr;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_GROUP_BY_GROUP_ID);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getGroupByGroupId(mSessionId, mGroupId,
                            mRequestParams, mIReceiveResponse);
            return result;
        }
        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        responseResult.setIsAutoRefresh(isNeedRefreshOpr);
        if (mIReceiveResponse != null && ((isNeedRefreshOpr && responseResult.isResult()
                && responseResult.getResponseCode() == StatusCode.OK) || !isNeedRefreshOpr)) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
