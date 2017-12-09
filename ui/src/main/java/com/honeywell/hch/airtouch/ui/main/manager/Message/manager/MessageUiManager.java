package com.honeywell.hch.airtouch.ui.main.manager.Message.manager;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageDetailResponse;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.message.MessageRequest;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.authorize.ui.view.AuthorizeMessageCardView;
import com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessageHandleActivity;
import com.honeywell.hch.airtouch.ui.notification.manager.config.BaiduPushConfig;

import java.util.ArrayList;
import java.util.List;

import static com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessageHandleActivity.AUTHORIZE_TYPE;
import static com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessageHandleActivity.REGULAR_NOTICE_TYPE;
import static com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessageHandleActivity.REMOTE_CONTROL_TYPE;
import static com.honeywell.hch.airtouch.ui.main.ui.messagecenter.MessageHandleActivity.WATER_ERROR_TYPE;

/**
 * Created by Vincent on 22/9/16.
 */
public class MessageUiManager {

    private final String TAG = "HomeManagerUiManager";
    private final int EMPTY = 0;
    private MessageManager mMessageManager;

    public MessageUiManager() {
        mMessageManager = new MessageManager();
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(MessageManager.SuccessCallback successCallback) {
        mMessageManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(MessageManager.ErrorCallback errorCallback) {
        mMessageManager.setErrorCallback(errorCallback);
    }

    public void getMessagesPerPage(int fromRecordNo, int pageSize, String language, int compareMsgPoolId, int loadMode) {
        mMessageManager.getMessagesPerPage(fromRecordNo, pageSize, language, compareMsgPoolId, loadMode);
    }

    public void getMessageByIdTask(int messageId, String language) {
        mMessageManager.getMessageByIdTask(messageId, language);
    }

    public void deleteMessagesList(ArrayList<MessageModel> messageModelArrayList, boolean isClearAll) {
        MessageRequest messageListRequest = new MessageRequest(getMessagesSelectedIds(messageModelArrayList, isClearAll));
        mMessageManager.deleteMessagesList(messageListRequest);
    }

    public String remindNotificationAction(int action) {
        switch (action) {
            case AuthorizeMessageCardView.ACCEPTACTION:
                return AppManager.getInstance().getApplication().getString(R.string.authorize_accept_success);
            case AuthorizeMessageCardView.DECLINEACTON:
                return AppManager.getInstance().getApplication().getString(R.string.authorize_decline_success);
            default:
                return null;
        }
    }

    public ArrayList<MessageModel> getMessagesResponse(ResponseResult responseResult) {
        return (ArrayList<MessageModel>) responseResult.getResponseData()
                .getSerializable(MessageModel.GET_MESSAGES_PER_PAGE_PARAMETER);
    }

    public void reflashGetAuthMessagesResponse(ArrayList<MessageModel> adapterMessageList, ArrayList<MessageModel> loadMessageListResponse) {
        List<MessageModel> messageModeListTemple = new ArrayList<>();
        if (loadMessageListResponse != null && adapterMessageList != null) {
            for (MessageModel messageModel : loadMessageListResponse) {
                for (MessageModel messageModelFromAdapter : adapterMessageList) {
                    if (messageModel.getmMessageId() == messageModelFromAdapter.getmMessageId()) {
                        messageModeListTemple.add(messageModel);

                    }
                }
            }
            loadMessageListResponse.removeAll(messageModeListTemple);
        }
    }

    /**
     * 获取allDevice界面中所有选中的设备列表
     *
     * @return
     */
    public Integer[] getMessagesSelectedIds(ArrayList<MessageModel> messageModelArrayList, boolean isClearAll) {
        List<Integer> idList = new ArrayList<>();
        if (isClearAll) {
            for (MessageModel messageModel : messageModelArrayList) {
                idList.add(messageModel.getmMessageId());
            }
        } else {
            for (MessageModel messageModel : messageModelArrayList) {
                if (messageModel.isSelected()) {
                    idList.add(messageModel.getmMessageId());
                }
            }

        }

        Integer[] integerArray = new Integer[idList.size()];
        return idList.toArray(integerArray);
    }

    public MessageDetailResponse getMessageDetailResponse(ResponseResult responseResult) {
        return (MessageDetailResponse) responseResult.getResponseData().getSerializable(MessageDetailResponse.GET_MESSAGES_BY_ID_PARAMETER);
    }

    public boolean isSupportMessageType(int messageCategory) {
        switch (messageCategory) {
            case AUTHORIZE_TYPE:
            case REMOTE_CONTROL_TYPE:
            case WATER_ERROR_TYPE:
            case REGULAR_NOTICE_TYPE:
                return true;
            default:
                return false;
        }
    }

    public int parseMessageType(int messageType) {
        switch (messageType) {
            case BaiduPushConfig.GRANTAUTHDEVICE:
            case BaiduPushConfig.GRANTAUTHGROUP:
            case BaiduPushConfig.REMOVEAUTHDEVICE:
            case BaiduPushConfig.REMOVEAUTHGROUP:
                return AUTHORIZE_TYPE;
            case BaiduPushConfig.PUMPOVERTIMEERR:
            case BaiduPushConfig.PUMPFREQBOOTUPERR:
            case BaiduPushConfig.PUMPFAULT:
            case BaiduPushConfig.PIPEBLOCKERR:
            case BaiduPushConfig.INFTDSFAULT:
            case BaiduPushConfig.OUTFTDSFAULT:
            case BaiduPushConfig.WATERLEAKAGEERR:
            case BaiduPushConfig.EEPROMERR:
            case BaiduPushConfig.NO_WATER:
                return WATER_ERROR_TYPE;
            case BaiduPushConfig.REMOTEENABLE:
            case BaiduPushConfig.REMOTEDISABLE:
                return REMOTE_CONTROL_TYPE;
            case BaiduPushConfig.REGULAR_NOTICE:
                return REGULAR_NOTICE_TYPE;
            default:
                return AUTHORIZE_TYPE;
        }
    }
}
