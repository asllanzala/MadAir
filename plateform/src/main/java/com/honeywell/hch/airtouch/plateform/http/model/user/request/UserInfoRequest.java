package com.honeywell.hch.airtouch.plateform.http.model.user.request;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by zhujunyu on 2016/12/25.
 */

public class UserInfoRequest implements  Serializable {

    @SerializedName("userAccount")
    private String userAccount;
    @SerializedName("nickName")
    private String nickName;

    @SerializedName("active")
    private String active;

    @SerializedName("platForm")
    private String platForm;

    @SerializedName("pVersion")
    private String pVersion;

    @SerializedName("phoneBrand")
    private String phoneBrand;

    @SerializedName("phoneModel")
    private String phoneModel;

    @SerializedName("appVersion")
    private String appVersion;

    @SerializedName("network")
    private String network;

    @SerializedName("location")
    private String location;

    @SerializedName("language")
    private String language;

    public UserInfoRequest(String userAccount, String nickName, String active, String platForm, String pVersion, String phoneBrand, String phoneModel, String appVersion, String network, String location, String language) {
        this.userAccount = userAccount;
        this.nickName = nickName;
        this.active = active;
        this.platForm = platForm;
        this.pVersion = pVersion;
        this.phoneBrand = phoneBrand;
        this.phoneModel = phoneModel;
        this.appVersion = appVersion;
        this.network = network;
        this.location = location;
        this.language = language;
    }

}

