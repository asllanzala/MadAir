package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SmsValidRequest;

/**
 * Created by Jin Qian on 15/8/31.
 */
public class GetSmsCodeTask extends BaseRequestTask {
    private IActivityReceive mIReceiveResponse;
    private SmsValidRequest mRequestParams;

    public GetSmsCodeTask(SmsValidRequest request, IActivityReceive
            receiveResponse) {
        this.mRequestParams = request;
        this.mIReceiveResponse = receiveResponse;

    }


    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult getSmsCodeResponse = HttpProxy.getInstance().getWebService()
                .getSmsCode(mRequestParams, mIReceiveResponse);

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
