package com.honeywell.hch.airtouch.plateform.http.task;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.FeedBackDeleteImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackImgRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by zhujunyu on 2016/12/22.
 */

public class FeedbackDeleteImgTask extends BaseRequestTask {
    private IActivityReceive mIReceiveResponse;
    private FeedBackDeleteImgRequest mRequestParams;
    private String mUserId;
    private String mSessionId;


    public FeedbackDeleteImgTask( FeedBackDeleteImgRequest mRequestParams,IActivityReceive mIReceiveResponse) {
        this.mIReceiveResponse = mIReceiveResponse;
        this.mRequestParams = mRequestParams;
        this.mUserId = UserInfoSharePreference.getUserId();
        this.mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {
        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.FEED_BACK_DELETE_IMG);
        if (reLoginResult.isResult()){
            ResponseResult feedBackResult = HttpProxy.getInstance().getWebService()
                    .feedBackDeleteImg(mUserId,mSessionId, mRequestParams,mIReceiveResponse);
            return feedBackResult;
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
