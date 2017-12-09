package com.honeywell.hch.airtouch.plateform.http.model.authorize.model;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.R;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/1/16.
 * Data model of authorization message received from server
 */
public class AuthMessageModel implements  Serializable {

    public static final String AUTH_MESSAGE_DATA_BY_ID = "auth_message_data_by_id";

    public static final int USER = 200;

    public static final int GUEST = 100;

    public static final int MASTER= 300;

    public static final int DEVICE = 0;

    public static final int GROUP = 1;

    private final int HUNDRED = 100; //取最高位

    private final int EMPTY = 0;

    @SerializedName("messageID")
    private int mMessageId;

    @SerializedName("messageType")
    private int mMessageType;

    @SerializedName("senderID")
    private int mSenderID;

    @SerializedName("senderName")
    private String mSenderName;

    @SerializedName("locationId")
    private int mLocationId;

    @SerializedName("locationName")
    private String mLocationName;

    /*
        1 对方接受消息 （设备所有者）
        2 对方拒绝消息  （设备所有者）
        3 对方解除消息  （设备所有者）
        4 设备授权消息  （设备接受者）
        5 设备所有者降低授权消息 （设备接受者）
        6 设备所有者解除授权消息  （设备接受者）
     */
    @SerializedName("targetType")
    private int mTargetType;

    @SerializedName("targetID")
    private int mTargetID;

    @SerializedName("targetName")
    private String mTargetName;

    @SerializedName("authorityRole")
    private int mAuthorityRole;

    @SerializedName("invitationTime")
    private String mInvitationTime;

    @SerializedName("expirationTime")
    private String mExpirationTime;

    @SerializedName("isExpired")
    private boolean mIsExpired;

    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public int getMessageId() {
        return mMessageId;
    }

    public int getMessageType() {
        return mMessageType;
    }

    public int getSenderID() {
        return mSenderID;
    }

    public String getSenderName() {
        return mSenderName;
    }

    public int getLocationId() {
        return mLocationId;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public int getTargetType() {
        return mTargetType;
    }

    public int getTargetID() {
        return mTargetID;
    }

    public String getTargetName() {
        return mTargetName;
    }

    public int getAuthorityRole() {
        return mAuthorityRole;
    }

    public String getInvitationTime() {
        return mInvitationTime;
    }

    public String getExpirationTime() {
        return mExpirationTime;
    }

    public boolean isExpired() {
        return mIsExpired;
    }

    public int parseRole() {
        int uiRole = 0;
        switch (mAuthorityRole / HUNDRED) {
            case USER / HUNDRED:
                uiRole = R.string.authorize_control_role;
                break;
            case GUEST / HUNDRED:
                uiRole = R.string.authorize_viewer_role;
                break;
            case MASTER / HUNDRED:
                uiRole = R.string.authorize_master_role;
                break;
            default:
                uiRole = R.string.authorize_viewer_role;
                break;
        }
        return uiRole;
    }

    public int parseTargetType() {
        int targetType = EMPTY;
        switch (mTargetType) {
            case DEVICE:
                targetType = R.string.authorize_message_device;
                break;
            case GROUP:
                targetType = R.string.authorize_message_group;
                break;
        }
        return targetType;
    }

    public int parseMessageType() {
        int messageType = EMPTY;
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

    public void setmLocationName(String mLocationName) {
        this.mLocationName = mLocationName;
    }

    public void setmMessageId(int mMessageId) {
        this.mMessageId = mMessageId;
    }

    public void setmMessageType(int mMessageType) {
        this.mMessageType = mMessageType;
    }

    public void setmSenderID(int mSenderID) {
        this.mSenderID = mSenderID;
    }

    public void setmSenderName(String mSenderName) {
        this.mSenderName = mSenderName;
    }

    public void setmLocationId(int mLocationId) {
        this.mLocationId = mLocationId;
    }

    public void setmTargetType(int mTargetType) {
        this.mTargetType = mTargetType;
    }

    public void setmTargetID(int mTargetID) {
        this.mTargetID = mTargetID;
    }

    public void setmTargetName(String mTargetName) {
        this.mTargetName = mTargetName;
    }

    public void setmAuthorityRole(int mAuthorityRole) {
        this.mAuthorityRole = mAuthorityRole;
    }

    public void setmInvitationTime(String mInvitationTime) {
        this.mInvitationTime = mInvitationTime;
    }

    public void setmExpirationTime(String mExpirationTime) {
        this.mExpirationTime = mExpirationTime;
    }

    public void setmIsExpired(boolean mIsExpired) {
        this.mIsExpired = mIsExpired;
    }
}
