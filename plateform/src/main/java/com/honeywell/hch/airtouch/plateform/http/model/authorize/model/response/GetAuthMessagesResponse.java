package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.AuthMessageModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class GetAuthMessagesResponse implements Serializable {
    public static final String AUTH_MESSAGE_DATA = "auth_message_data";

    @SerializedName("invitationList")
    private List<AuthMessageModel> mAuthMessages;

    public List<AuthMessageModel> getAuthMessages() {
        return mAuthMessages;
    }

    public void setmAuthMessages(List<AuthMessageModel> mAuthMessages) {
        this.mAuthMessages = mAuthMessages;
    }
}
