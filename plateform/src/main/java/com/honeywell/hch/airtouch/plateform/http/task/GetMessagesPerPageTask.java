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
public class GetMessagesPerPageTask extends BaseRequestTask {
    public static final String ISREFLASH = "isreflash";
    private String mSessionId;
    private int mPageSize;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;
    private int mLoadMode;
    private int mFromRecordNo;
    private String mLanguage;
    private int mCompareMsgPoolId;

    public GetMessagesPerPageTask(int fromRecordNo, int pageSize, String language, int compareMsgPoolId, int loadMode, IRequestParams requestParams, IActivityReceive
            iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();

        mFromRecordNo = fromRecordNo;
        mPageSize = pageSize;
        mLanguage = language;
        mCompareMsgPoolId = compareMsgPoolId;
        mLoadMode = loadMode;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_MESSAGES_PER_PAGE);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .GetMessagesPerPage(mSessionId, mFromRecordNo, mPageSize, mLanguage, mCompareMsgPoolId, mRequestParams,
                            mIReceiveResponse);
            result.getResponseData().putInt(ISREFLASH, mLoadMode);
            getUnreadMessage();
            return result;
        } else {
            reLoginResult.getResponseData().putInt(ISREFLASH, mLoadMode);
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
