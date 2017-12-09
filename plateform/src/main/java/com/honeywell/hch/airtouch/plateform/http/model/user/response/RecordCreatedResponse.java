package com.honeywell.hch.airtouch.plateform.http.model.user.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jin Qian on 1/16/2015.
 */
public class RecordCreatedResponse {
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
