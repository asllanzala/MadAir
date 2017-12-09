package com.honeywell.hch.airtouch.ui.main.manager.Message.manager;

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageRequest;
import com.honeywell.hch.airtouch.plateform.http.task.DeleteMessageListTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetMessageByIdTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetMessagesPerPageTask;
import com.honeywell.hch.airtouch.plateform.http.task.GetUnreadMessageTask;

/**
 * Created by Vincent on 22/9/16.
 */
public class MessageManager {

    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public MessageManager() {

    }

    public interface SuccessCallback {
        void onSuccess(ResponseResult responseResult);
    }

    public interface ErrorCallback {
        void onError(ResponseResult responseResult, int id);
    }

    public void setSuccessCallback(SuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }


    public void sendSuccessCallBack(ResponseResult responseResult) {
        if (mSuccessCallback != null) {
            mSuccessCallback.onSuccess(responseResult);
        }
    }

    public void sendErrorCallBack(ResponseResult responseResult, int strId) {
        if (mErrorCallback != null && !responseResult.isAutoRefresh()) {
            mErrorCallback.onError(responseResult, strId);
        }
    }

    IActivityReceive mResponse = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {

            if (responseResult.isResult()
                    && (responseResult.getResponseCode() == StatusCode.OK)) {
                switch (responseResult.getRequestId()) {
                    case GET_MESSAGES_PER_PAGE:
                    case DELETE_MESSAGES:
                    case GET_MESSAGE_BY_ID:
                        sendSuccessCallBack(responseResult);
                        break;
                }
            } else {
                //fail response
                switch (responseResult.getRequestId()) {
                    case GET_MESSAGES_PER_PAGE:
                    case DELETE_MESSAGES:
                    case GET_MESSAGE_BY_ID:
                        sendErrorCallBack(responseResult, R.string.authorize_response_error);
                        break;
                }
            }
        }
    };

    public void getMessagesPerPage(int fromRecordNo, int pageSize, String language, int compareMsgPoolId, int loadMode) {
        GetMessagesPerPageTask requestTask
                = new GetMessagesPerPageTask(fromRecordNo, pageSize, language, compareMsgPoolId, loadMode, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void deleteMessagesList(MessageRequest messageRequest) {
        DeleteMessageListTask requestTask
                = new DeleteMessageListTask(messageRequest, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getMessageByIdTask(int messageId, String language) {
        GetMessageByIdTask requestTask
                = new GetMessageByIdTask(messageId, language, null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void getUnreadMessages() {
        GetUnreadMessageTask requestTask
                = new GetUnreadMessageTask(null, mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

}
