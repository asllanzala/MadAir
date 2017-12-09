package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.BCryptUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.ChangePasswordRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class ChangePasswordTask extends BaseRequestTask {

    private IActivityReceive mIReceiveResponse;
    private ChangePasswordRequest mRequestParams;
    private String mUserId;
    private String mSessionId;

    public ChangePasswordTask(ChangePasswordRequest requestParams, IActivityReceive iReceiveResponse) {
        this.mUserId = UserInfoSharePreference.getUserId();
        this.mSessionId = UserInfoSharePreference.getSessionId();
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {
        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.CHANGE_PASSWORD);
        if (reLoginResult.isResult()) {
            String sault = UserInfoSharePreference.getUserSault();
            String newBcryptPassword = "";
            if (!"".equals(sault)) {

                newBcryptPassword = BCryptUtil.hashpw(mRequestParams.getNewPassword(), sault);
                mRequestParams.setOldPassword(BCryptUtil.hashpw(mRequestParams.getOldPassword(), sault));
                mRequestParams.setNewPassword(newBcryptPassword);
                mRequestParams.setBCrypt(true);
            }
            ResponseResult updatePasswordResult = HttpProxy.getInstance().getWebService()
                    .changePassword(mUserId, mSessionId, mRequestParams, mIReceiveResponse);
            //TODO:保存是因为如果这时候刷新session 可能会用原来的bcrypt password，这样登录就失败了
            if (updatePasswordResult.isResult() && mRequestParams.isBCrypt() == true) {
                UserInfoSharePreference.saveBycryptPd(newBcryptPassword);
            }
            return updatePasswordResult;
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
