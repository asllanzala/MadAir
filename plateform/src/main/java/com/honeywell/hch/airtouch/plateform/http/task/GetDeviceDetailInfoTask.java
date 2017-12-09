package com.honeywell.hch.airtouch.plateform.http.task;


import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;

/**
 * Created by Vincent on 10/3/16.
 */
public class GetDeviceDetailInfoTask extends BaseRequestTask {

    private static boolean isTaskRunning = false;

    @Override
    protected ResponseResult doInBackground(Object... params) {
        isTaskRunning = true;
        ResponseResult reLoginResult = reloginSuccessOrNot(RequestID.GET_DEVICE_DETAIL_INFO);
        if (reLoginResult.isResult()) {
            reloadDeviceInfo();
        }

        return reLoginResult;
    }

    @Override
    protected void onPostExecute(ResponseResult responseResult) {
        isTaskRunning = false;
        super.onPostExecute(responseResult);
    }


    public static boolean isTaskRunning() {
        return isTaskRunning;
    }

}
