package com.honeywell.hch.airtouch.plateform.http.task;

import android.util.Log;

import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.HttpProxy;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

/**
 * Created by Jin Qian on 15/8/24.
 */
public class CheckDeviceIsOnlineTask extends BaseRequestTask {
    private String mMacId;
    private String mSessionId;
    private IActivityReceive mIReceiveResponse;
    private IRequestParams mRequestParams;

    private int mCheckPollCount = 0;
    private final int CHECK_ALIVE_MAX_TIME = 5;

    public CheckDeviceIsOnlineTask(String macId, IRequestParams requestParams, IActivityReceive iReceiveResponse) {
        this.mMacId = macId;
        this.mSessionId = UserInfoSharePreference.getSessionId();
        this.mRequestParams = requestParams;
        this.mIReceiveResponse = iReceiveResponse;
    }

    @Override
    protected ResponseResult doInBackground(Object... params) {

        ResponseResult checkResult = new ResponseResult();
        while (mCheckPollCount < CHECK_ALIVE_MAX_TIME){
            checkResult = HttpProxy.getInstance().getWebService()
                    .checkMac(mMacId, mSessionId, mRequestParams, mIReceiveResponse);

            if (checkResult.getFlag() == HPlusConstants.CHECK_MAC_ALIVE) {
                Log.e("Manager", "checkMacPolling  addHomeAndDevice");
                mCheckPollCount = 0;
                break;
            } else if (checkResult.getFlag() == HPlusConstants.CHECK_MAC_AGAIN) {
                if (mCheckPollCount <= CHECK_ALIVE_MAX_TIME) {
                    mCheckPollCount++;

                    try{
                        Thread.sleep(6000);
                    }catch (Exception e){

                    }
                    continue;
                } else {
                    mCheckPollCount = 0;
                    break;
                }
            }else{
                break;
            }

        }


        return checkResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {

        if (mIReceiveResponse != null) {
            mIReceiveResponse.onReceive(responseResult);
        }
        super.onPostExecute(responseResult);
    }
}
