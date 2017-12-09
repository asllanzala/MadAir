package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.R;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class LatestAuthMsg implements  Serializable {

    @SerializedName("messageId")
    private int mMessageId;

    @SerializedName("senderName")
    private String mSenderName;

    @SerializedName("messageType")
    private int mMessageType;

    public String getmSenderName() {
        return mSenderName;
    }

    public void setmSenderName(String mSenderName) {
        this.mSenderName = mSenderName;
    }

    public int getmMessageType() {
        return mMessageType;
    }

    public void setmMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public void setMessageId(int messageId) {
        mMessageId = messageId;
    }

    public void setSenderName(String senderName) {
        mSenderName = senderName;
    }

    public void setMessageType(int messageType) {
        mMessageType = messageType;
    }

    public int parseMessageType() {
        int messageType = 0;
        switch (mMessageType) {
            case 1:
                messageType = R.string.authorize_message_title_accept;
                break;
            case 2:
                messageType = R.string.authorize_message_title_decline;
                break;
            case 3:
                messageType = R.string.authorize_message_remove;
                break;
            case 4:
                messageType = R.string.authorize_message_invitation;
                break;
            case 5:
                messageType = R.string.authorize_message_degrade;
                break;
            case 6:
                messageType = R.string.authorize_message_revoke;
                break;
            default:
                messageType = R.string.authorize_message_invitation;
                break;

        }
        return messageType;
    }

    public LatestAuthMsg( String mSenderName, int mMessageType) {
        this.mSenderName = mSenderName;
        this.mMessageType = mMessageType;
    }

    public LatestAuthMsg(){}
}
