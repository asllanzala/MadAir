package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.BCryptUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserRegisterRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserValidateResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import static com.honeywell.hch.airtouch.library.http.model.RequestID.USER_LOGIN;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class RegisterUserTask extends BaseRequestTask {
    private IActivityReceive mIReceiveResponse;
    private UserRegisterRequest mRequestParams;

    public RegisterUserTask(UserRegisterRequest request, IActivityReceive
            receiveResponse) {
        this.mRequestParams = request;
        this.mIReceiveResponse = receiveResponse;

    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        UserLoginRequest userLoginRequest = new UserLoginRequest(mRequestParams.getTelephone(), AppConfig.APPLICATION_ID);

        ResponseResult userValidateResult = HttpProxy.getInstance().getWebService().userValidate(userLoginRequest, USER_LOGIN);
        if (!userValidateResult.isResult()) {
            return userValidateResult;
        }
        //TODO:userValidate 调用成功
        UserValidateResponse userValidateResponse = (UserValidateResponse) userValidateResult.getResponseData().get(UserValidateResponse.USER_VALIDATE_DATA);
        if (!"".equals(userValidateResponse.getmBcryptSalt())) {
            String sault = SAULT_PREFIX + userValidateResponse.getmBcryptSalt();
            int isSuccess = userValidateResponse.getIsSuccess();
            String bcryptPassword = BCryptUtil.hashpw(mRequestParams.getPassword(), sault);
            //TODO:新用户，有Bcrypt 密码 传bcrypt 密码，老用户传明码
            mRequestParams.setPassword(bcryptPassword);
            mRequestParams.setBCrypt(true);
        }

        ResponseResult registerResponse = HttpProxy.getInstance().getWebService()
                .userRegister(mRequestParams, mIReceiveResponse);
        return registerResponse;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
