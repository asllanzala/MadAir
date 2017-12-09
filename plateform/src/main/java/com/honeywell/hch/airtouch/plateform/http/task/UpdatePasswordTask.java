package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.BCryptUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UpdatePasswordRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserValidateResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class UpdatePasswordTask extends BaseRequestTask {

    private IActivityReceive mIReceiveResponse;
    private UpdatePasswordRequest mRequestParams;

    public UpdatePasswordTask(UpdatePasswordRequest requestParams, IActivityReceive iReceiveResponse) {
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
    }


    @Override
    protected ResponseResult doInBackground(Object... params) {
        UserLoginRequest userLoginRequest = new UserLoginRequest(mRequestParams.getUsername(), AppConfig.APPLICATION_ID);
        ResponseResult userValidateResult = HttpProxy.getInstance().getWebService().userValidate(userLoginRequest, null);
        if (!userValidateResult.isResult()) {
            return userValidateResult;
        }
        //TODO:userValidate 调用成功
        UserValidateResponse userValidateResponse = (UserValidateResponse) userValidateResult.getResponseData().get(UserValidateResponse.USER_VALIDATE_DATA);
        //TODO:新用户，有Bcrypt 密码 传bcrypt 密码，老用户传明码
        String bcryptPassword ="";
        if (!"".equals(userValidateResponse.getmBcryptSalt())) {
            String sault = SAULT_PREFIX + userValidateResponse.getmBcryptSalt();
            int isSuccess = userValidateResponse.getIsSuccess();
            bcryptPassword = BCryptUtil.hashpw(mRequestParams.getNewPassword(), sault);
            mRequestParams.setNewPassword(bcryptPassword);
            mRequestParams.setBCrypt(true);
        }
        ResponseResult updatePasswordResult = HttpProxy.getInstance().getWebService()
                .updatePassword(mRequestParams, mIReceiveResponse);

        return updatePasswordResult;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
