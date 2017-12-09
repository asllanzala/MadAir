package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.BCryptUtil;
import com.honeywell.hch.airtouch.library.util.ByteUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserLoginRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLoginResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserValidateResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by wuyuan on 15/5/19.
 */
public class UserLoginTask extends BaseRequestTask {
    private IActivityReceive mIReceiveResponse;
    private IRequestParams requestParams;

    public UserLoginTask(IActivityReceive iReceiveResponse, IRequestParams requestParams) {
        this.mIReceiveResponse = iReceiveResponse;
        this.requestParams = requestParams;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {


        return getLoginResult(((UserLoginRequest) requestParams).getUsername(),
                ((UserLoginRequest) requestParams).getPassword(), AppConfig.APPLICATION_ID);
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
