package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Vincent on 30/3/16.
 */
public class GetAuthMessageByIdTask extends BaseRequestTask {
    private String mSessionId;
    private int mMessageId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    public GetAuthMessageByIdTask(int messageId, IRequestParams requestParams, IActivityReceive
            iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        mMessageId = messageId;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_AUTH_MESSAGE_BY_ID);
        if (reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .getAuthMessageById(mMessageId, mSessionId, mRequestParams, mIReceiveResponse);

            if (result != null && result.isResult()) {
                MessageRequest messageListRequest = new MessageRequest(new Integer[]{mMessageId});
                HttpProxy.getInstance().getWebService().updateMessageStatus(mSessionId, messageListRequest, mIReceiveResponse);
                getUnreadMessage();
            }
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
