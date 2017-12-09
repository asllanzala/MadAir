package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class GrantAuthToDeviceResponse implements Serializable {
    public static final String GRANT_AUTH_DATA = "auth_user_data";

    @SerializedName("responseList")
    private List<GrantAuthDevice> mGrantAuthDevices;

    public List<GrantAuthDevice> getGrantAuthDevices() {
        return mGrantAuthDevices;
    }

    public void setmGrantAuthDevices(List<GrantAuthDevice> mGrantAuthDevices) {
        this.mGrantAuthDevices = mGrantAuthDevices;
    }
}
