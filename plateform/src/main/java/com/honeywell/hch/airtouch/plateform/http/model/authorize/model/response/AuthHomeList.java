package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vincent on 24/2/16.
 */
public class AuthHomeList implements Serializable {
    public static final String AUTH_HOME_LIST_DATA = "auth_home_list_data";

     public static enum Type{
         MYHOME,
         TOMEHOME
    }

    private String mOwnerName;

    protected int mOwnerId;

    @SerializedName("type")
    private Type type;  //0 my home ,1 authorize to me home

    @SerializedName("authHomeList")
    private List<AuthHomeModel> mAuthHome;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<AuthHomeModel> getmAuthHome() {
        return mAuthHome;
    }

    public void setmAuthHome(List<AuthHomeModel> mAuthHome) {
        this.mAuthHome = mAuthHome;
    }

    public AuthHomeList(Type type, List<AuthHomeModel> mAuthHome) {
        this.type = type;
        this.mAuthHome = mAuthHome;
    }

    public AuthHomeList(){}

    public String getmOwnerName() {
        return mOwnerName;
    }

    public void setmOwnerName(String mOwnerName) {
        this.mOwnerName = mOwnerName;
    }

    public int getmOwnerId() {
        return mOwnerId;
    }

    public void setmOwnerId(int mOwnerId) {
        this.mOwnerId = mOwnerId;
    }
}
