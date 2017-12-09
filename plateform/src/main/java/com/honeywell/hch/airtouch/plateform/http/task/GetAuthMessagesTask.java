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
public class GetAuthMessagesTask extends BaseRequestTask {
    public static final String ISREFLASH = "isreflash";
    private String mSessionId;
    private int mPageIndex;
    private int mPageSize;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private int mLoadMode;

    public GetAuthMessagesTask(int pageIndex, int pageSize, IRequestParams requestParams, IActivityReceive
            iReceiveResponse, int loadMode) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mPageIndex = pageIndex;
        mPageSize = pageSize;
        mLoadMode = loadMode;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_AUTH_MESSAGES);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getAuthMessages(mPageIndex, mPageSize, mSessionId, mRequestParams, mIReceiveResponse);
            result.getResponseData().putInt(ISREFLASH, mLoadMode);
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
