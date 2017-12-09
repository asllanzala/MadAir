package com.honeywell.hch.airtouch.plateform.http.model.message;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Vincent on 23/9/16.
 */
public class MessageRequest implements IRequestParams, Serializable {
    @SerializedName("msgIdList")

    private Integer[] msgIdList;


    public MessageRequest(Integer[] ids) {
        msgIdList = ids.clone();
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(msgIdList);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }
}
