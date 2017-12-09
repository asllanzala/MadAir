package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class GetAuthUnreadMsgResponse implements  Serializable {
    public static final String AUTH_UNREAD_MSG_DATA = "auth_message_data";

    @SerializedName("unreadMsgCount")
    private int mUnreadMsgCount;

    @SerializedName("latestMsg")
    private LatestAuthMsg mLatestAuthMsgs;

    public int getUnreadMsgCount() {
        return mUnreadMsgCount;
    }

    private boolean isNeedRefresh;

    public LatestAuthMsg getLatestAuthMsgs() {
        return mLatestAuthMsgs;
    }

    public void setmUnreadMsgCount(int mUnreadMsgCount) {
        this.mUnreadMsgCount = mUnreadMsgCount;
    }

    public void setmLatestAuthMsgs(LatestAuthMsg mLatestAuthMsgs) {
        this.mLatestAuthMsgs = mLatestAuthMsgs;
    }

    public GetAuthUnreadMsgResponse(int mUnreadMsgCount, LatestAuthMsg mLatestAuthMsgs, boolean isNeedRefresh) {
        this.mUnreadMsgCount = mUnreadMsgCount;
        this.mLatestAuthMsgs = mLatestAuthMsgs;
        this.isNeedRefresh = isNeedRefresh;
    }

    public GetAuthUnreadMsgResponse() {
    }

    public boolean isNeedRefresh() {
        return isNeedRefresh;
    }

    public void setIsNeedRefresh(boolean isNeedRefresh) {
        this.isNeedRefresh = isNeedRefresh;
    }
}
