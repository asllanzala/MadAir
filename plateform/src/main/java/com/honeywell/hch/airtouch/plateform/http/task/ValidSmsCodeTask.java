package com.honeywell.hch.airtouch.plateform.http.task;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SmsValidRequest;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class ValidSmsCodeTask extends BaseRequestTask {
    private IActivityReceive mIReceiveResponse;
    private SmsValidRequest mRequestParams;
    private String mPhoneNumber;
    private String mSmsCode;

    public ValidSmsCodeTask(String phoneNumber, String smsCode,SmsValidRequest request, IActivityReceive
            receiveResponse) {
        mPhoneNumber = phoneNumber;
        mSmsCode = smsCode;
        this.mRequestParams = request;
        this.mIReceiveResponse = receiveResponse;

    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult getSmsCodeResponse = HttpProxy.getInstance().getWebService()
                .verifySmsCode(mPhoneNumber,mSmsCode,mRequestParams, mIReceiveResponse);

        return getSmsCodeResponse;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
