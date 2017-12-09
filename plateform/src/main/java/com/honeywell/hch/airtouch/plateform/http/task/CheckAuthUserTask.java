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
public class CheckAuthUserTask extends BaseRequestTask {
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    private boolean isNeedLogin  = true;

    public CheckAuthUserTask(IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
    }

    public CheckAuthUserTask(IRequestParams requestParams, IActivityReceive iReceiveResponse,boolean isNeedLogin) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
        mSessionId = UserInfoSharePreference.getSessionId();
        this.isNeedLogin = isNeedLogin;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.CHECK_AUTH_USER);
        if (!isNeedLogin || reLoginResult.isResult()) {
            ResponseResult result = HttpProxy.getInstance().getWebService()
                    .checkAuthUser(mSessionId, mRequestParams, mIReceiveResponse);
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
