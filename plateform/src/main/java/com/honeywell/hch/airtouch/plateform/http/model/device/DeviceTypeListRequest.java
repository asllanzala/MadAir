package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class DeviceTypeListRequest implements IRequestParams, Serializable {

    private List<Integer> mLocationList = DeviceType.getAllSupportDeviceType();


    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(mLocationList);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
