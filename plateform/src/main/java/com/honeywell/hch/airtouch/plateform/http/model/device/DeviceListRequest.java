package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 10/13/15.
 */
public class DeviceListRequest implements IRequestParams, Serializable {
    public class DeviceId {
        @SerializedName("deviceId")
        private int mDeviceId;

        public DeviceId(int deviceId) {
            mDeviceId = deviceId;
        }
    }

    private List<DeviceId> mDeviceIdList = new ArrayList<>();

    public DeviceListRequest(int[] ids) {
        for (int id : ids) {
            DeviceId deviceIdClass = new DeviceId(id);
            mDeviceIdList.add(deviceIdClass);
        }
    }

    public DeviceListRequest(Integer[] ids) {
        for (Integer id : ids) {
            DeviceId deviceIdClass = new DeviceId(id.intValue());
            mDeviceIdList.add(deviceIdClass);
        }
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
