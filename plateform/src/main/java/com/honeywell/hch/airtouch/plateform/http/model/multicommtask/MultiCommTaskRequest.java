package com.honeywell.hch.airtouch.plateform.http.model.multicommtask;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 10/30/15.
 */
public class MultiCommTaskRequest implements IRequestParams, Serializable {
    private List<Integer> mDeviceIdList = new ArrayList<>();

    public MultiCommTaskRequest(List<Integer> deviceIds) {
        mDeviceIdList = deviceIds;
    }

    public int getDeviceListLength(){
        return mDeviceIdList.size();
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(mDeviceIdList);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
