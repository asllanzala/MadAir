package com.honeywell.hch.airtouch.plateform.http.model.user.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by zhujunyu on 2016/12/21.
 */

public class FeedBackRequest implements IRequestParams, Serializable {

    @SerializedName("userInfo")
    private UserInfoRequest userInfoRequest;


    @SerializedName("userInputInfo")
    private UserInputInfoRequest userInputInfoRequest;

    public FeedBackRequest(UserInfoRequest userInfoRequest, UserInputInfoRequest userInputInfoRequest) {
        this.userInfoRequest = userInfoRequest;
        this.userInputInfoRequest = userInputInfoRequest;
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
