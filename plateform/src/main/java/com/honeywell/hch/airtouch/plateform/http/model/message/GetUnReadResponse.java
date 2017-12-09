package com.honeywell.hch.airtouch.plateform.http.model.message;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Vincent on 27/9/16.
 */
public class GetUnReadResponse implements Serializable {
    public static final String UNREAD_MSG_DATA = "auth_message_data";

    @SerializedName("unreadMessages")
    private int mUnreadMessages;

    public int getmUnreadMessages() {
        return mUnreadMessages;
    }

    public void setmUnreadMessages(int mUnreadMessages) {
        this.mUnreadMessages = mUnreadMessages;
    }
}
