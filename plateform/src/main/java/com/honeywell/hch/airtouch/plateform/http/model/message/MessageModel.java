package com.honeywell.hch.airtouch.plateform.http.model.message;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 22/9/16.
 */
public class MessageModel implements Serializable {

    public final static String GET_MESSAGES_PER_PAGE_PARAMETER = "GET_MESSAGES_PER_PAGE_PARAMETER";
    @SerializedName("messageID")
    private int mMessageId;

    /*
        MessageCategory：
        Authorization = 1
        RemoteControl = 2
        WaterError = 3
     */
    @SerializedName("messageCategory")
    private int mMessageCategory;

    @SerializedName("messageTime")
    private String mMessageTime;

    @SerializedName("messageContent")
    private String mMessageContent;

    @SerializedName("isReaded")
    private boolean isReaded;

    private boolean isSelected;

    public int getmMessageId() {
        return mMessageId;
    }

    public void setmMessageId(int mMessageId) {
        this.mMessageId = mMessageId;
    }

    public int getmMessageCategory() {
        return mMessageCategory;
    }

    public void setmMessageCategory(int mMessageCategory) {
        this.mMessageCategory = mMessageCategory;
    }

    public String getmMessageTime() {
        return mMessageTime;
    }

    public void setmMessageTime(String mMessageTime) {
        this.mMessageTime = mMessageTime;
    }

    public String getmMessageContent() {
        return mMessageContent;
    }

    public void setmMessageContent(String mMessageContent) {
        this.mMessageContent = mMessageContent;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setIsReaded(boolean isReaded) {
        this.isReaded = isReaded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}
