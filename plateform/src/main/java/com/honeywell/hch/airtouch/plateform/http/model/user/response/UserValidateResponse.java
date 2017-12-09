package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 1/19/2015.
 */
public class UserValidateResponse implements IRequestParams, Serializable {
    public static final String USER_VALIDATE_DATA = "user_validate_data";

    @SerializedName("success")
    private int isSuccess;

    @SerializedName("bcryptSalt")
    private String mBcryptSalt;

    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getmBcryptSalt() {
        return mBcryptSalt;
    }

    public void setmBcryptSalt(String mBcryptSalt) {
        this.mBcryptSalt = mBcryptSalt;
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
