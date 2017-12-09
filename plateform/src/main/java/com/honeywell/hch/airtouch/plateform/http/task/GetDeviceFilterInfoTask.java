package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.devices.airtouch.model.AirtouchRunStatus;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by wuyuan on 9/25/15.
 */
public class GetDeviceFilterInfoTask extends BaseRequestTask {

    private IActivityReceive mIReceiveResponse;

    private int mDeviceId;
    private String mSessionId;

    private AirtouchRunStatus mRunstatus;

    public GetDeviceFilterInfoTask(IActivityReceive iReceiveResponse) {
        this.mIReceiveResponse = iReceiveResponse;
        this.mSessionId = UserInfoSharePreference.getSessionId();
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

//        ResponseResult reloginResult = reloginSuccessOrNot(RequestID.GET_DEVICE_CAPABILITY);
//        if (reloginResult.isResult()) {
//            if (mRunstatus == null) {
//                ResponseResult runstatusResponse = HttpProxy.getInstance().getWebService().getDeviceRunStatus(mDeviceId, mSessionId);
//                if (runstatusResponse != null && runstatusResponse.getResponseData() != null) {
//                    mRunstatus = (AirtouchRunStatus) runstatusResponse.getResponseData().getSerializable(HPlusConstants.DEVICE_RUNSTATUS_KEY);
//                }
//
//                // if get run status failed.
//                if (mRunstatus == null) {
//                    return new ResponseResult(false, StatusCode.RETURN_RESPONSE_NULL, "", RequestID.GET_DEVICE_CAPABILITY);
//                }
//            }
//            return HttpProxy.getInstance().getWebService().getDeviceCapability(mDeviceId, mSessionId);
//
//
//        }
//        return reloginResult;
        return null;

    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }

}
