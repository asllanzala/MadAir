package com.honeywell.hch.airtouch.plateform.http.model.user.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 2/26/2015.
 */
public class ChangePasswordRequest implements IRequestParams, Serializable {
    @SerializedName("oldPassword")
    private String mOldPassword;

    @SerializedName("newPassword")
    private String mNewPassword;

    @SerializedName("isBCrypt")
    protected boolean isBCrypt = false;


    public ChangePasswordRequest(String oldPassword, String newPassword) {
        mOldPassword = oldPassword;
        mNewPassword = newPassword;
    }

    public String getOldPassword() {

        return mOldPassword;
    }

    public void setOldPassword(String oldPassword) {
        mOldPassword = oldPassword;
    }

    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String newPassword) {
        mNewPassword = newPassword;
    }

    public void setBCrypt(boolean BCrypt) {
        isBCrypt = BCrypt;
    }

    public boolean isBCrypt() {
        return isBCrypt;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
