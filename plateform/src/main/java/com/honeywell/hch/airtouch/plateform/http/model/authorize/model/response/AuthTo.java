package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.plateform.R;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Qian Jin on 2/17/16.
 */
public class AuthTo implements Serializable {

    public static final int OBSERVER = 100;  //Read-only  diable to control device

    public static final int CONTROLLER = 200;   //Controller enable to control device,disable to modify scheduler

    public static final int MASTER = 300;  //Master  enable to modify scheduler

    public static final int OWNER = 400;   //Owner  can do anything

    public static final int WAIT = 0;

    public static final int ACCEPT = 1;

    private final int EMPTY = 0;

    private final int HUNDRED = 100; //取最高位

    @SerializedName("grantToUserName")
    private String mGrantToUserName;

    @SerializedName("grantToUserId")
    private int mGrantToUserId;

    @SerializedName("role")
    private int mRole;  //100 user ,200 guest

    @SerializedName("status")
    private int mStatus;

    @SerializedName("phoneNumber")
    private String mPhoneNumber;

    private List<String> mGrantToUserNameList;

    public String getGrantToUserName() {
        return mGrantToUserName;
    }

    public int getGrantToUserId() {
        return mGrantToUserId;
    }

    public int getRole() {
        return mRole;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setmGrantToUserId(int mGrantToUserId) {
        this.mGrantToUserId = mGrantToUserId;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public void setGrantToUserName(String grantToUserName) {
        mGrantToUserName = grantToUserName;
    }

    public void setGrantToUserId(int grantToUserId) {
        mGrantToUserId = grantToUserId;
    }

    public void setRole(int role) {
        mRole = role;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public void setmGrantToUserName(String mGrantToUserName) {
        this.mGrantToUserName = mGrantToUserName;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public void setmRole(int mRole) {
        this.mRole = mRole;
    }

    public int parseRole() {
        int uiRole = EMPTY;
        switch (mRole / HUNDRED) {
            case CONTROLLER / HUNDRED:
                uiRole = R.string.authorize_control_role;
                break;
            case OBSERVER / HUNDRED:
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

    public int parseStatus() {
        int uiStatus = EMPTY;
        switch (mStatus) {
            case WAIT:
                uiStatus = R.string.authorize_wait;
                break;
            case ACCEPT:
                uiStatus = R.string.authorize_accept;
                break;
        }
        return uiStatus;
    }

    public int parseStatusImage() {
        int uiStatusIm = EMPTY;
        switch (mStatus) {
            case WAIT:
                uiStatusIm = R.drawable.waiting;;
                break;
            case ACCEPT:
                uiStatusIm = R.drawable.authorized;
                break;
        }
        return uiStatusIm;
    }

    public List<String> getmGrantToUserNameList() {
        return mGrantToUserNameList;
    }

    public void setmGrantToUserNameList(List<String> mGrantToUserNameList) {
        this.mGrantToUserNameList = mGrantToUserNameList;
    }


    public String getGrantToUserNameListToString() {
        return mGrantToUserNameList.toString().replace("[", "").replace("]", "");
    }
}
