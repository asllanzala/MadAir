package com.honeywell.hch.airtouch.plateform.http.model.user.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 1/16/2015.
 */
public class UserLoginRequest implements IRequestParams, Serializable {
    @SerializedName("username")
    protected String mUsername;

    @SerializedName("password")
    protected String mPassword;

    @SerializedName("applicationID")
    protected String mApplicationID;

    @SerializedName("isBCrypt")
    protected boolean isBCrypt = false;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public boolean isBCrypt() {
        return isBCrypt;
    }

    public void setBCrypt(boolean BCrypt) {
        isBCrypt = BCrypt;
    }

    public UserLoginRequest(String username, String password, String applicationID) {
        mUsername = username;
        mPassword = password;
        mApplicationID = applicationID;
    }

    public UserLoginRequest(String username,String applicationID) {
        mUsername = username;
        mApplicationID = applicationID;
    }


    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return "{\"applicationID\":\"" + mApplicationID + "\"," +
                "\"password\":\"******\",\"username\":\"" + mUsername + "\"}";
    }

}
