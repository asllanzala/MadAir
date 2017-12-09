package com.honeywell.hch.airtouch.plateform.http.model.user.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Jin Qian on 1/16/2015.
 */
public class UserLoginEnterpriseRequest extends UserLoginRequest implements IRequestParams, Serializable {

    @SerializedName("UserType")
    private String mUserType;

    public UserLoginEnterpriseRequest(String username, String password, String applicationID) {
        super(username, password, applicationID);
    }

    public UserLoginEnterpriseRequest(String username, String password, String userType, String applicationID) {
        super(username, password, applicationID);

        mUserType = userType;
    }

    public String getUserType() {
        return mUserType;
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
