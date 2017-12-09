package com.honeywell.hch.airtouch.ui.notification.manager.manager;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.http.model.notification.UserClientInfoRequest;
import com.honeywell.hch.airtouch.plateform.http.task.PutUserClientInfoTask;

/**
 * Created by Vincent on 25/3/16.
 */
public class BaiduPushManager {
    private final String TAG = "BaiduPushManager";
    //    private UserClientInfoRequest mUserClientInfoRequest;
    IActivityReceive mResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case PUSH_INFO:
                    if (responseResult.isResult()) {
                        LogUtil.log(LogUtil.LogLevel.INFO, TAG, "success--");
                    }
                    else{
                        LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "PutUserClientInfoTask error--");
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void putUserClientInfo(UserClientInfoRequest userClientInfoRequest) {
//        mUserClientInfoRequest = userClientInfoRequest;
        PutUserClientInfoTask requestTask
                = new PutUserClientInfoTask(userClientInfoRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

}
