package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class CheckAuthUserResponse implements  Serializable {
    public static final String AUTH_USER_DATA = "auth_user_data";

    @SerializedName("phoneNumber")
    private String mPhoneNumber;

    @SerializedName("isHplusUser")
    private boolean mIsHPlusUser;

    @SerializedName("userName")
    private String mUserName;

    @SerializedName("userType")
    private int mUserType;

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public boolean isHPlusUser() {
        return mIsHPlusUser;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public void setIsHPlusUser(boolean isHPlusUser) {
        mIsHPlusUser = isHPlusUser;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public int getmUserType() {
        return mUserType;
    }

    public void setmUserType(int mUserType) {
        this.mUserType = mUserType;
    }
}
