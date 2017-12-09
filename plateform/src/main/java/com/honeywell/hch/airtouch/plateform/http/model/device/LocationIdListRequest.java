package com.honeywell.hch.airtouch.plateform.http.model.device;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 10/13/15.
 */
public class LocationIdListRequest implements IRequestParams, Serializable {

    private List<Integer> mLocationList = new ArrayList<>();

    public LocationIdListRequest(int[] ids) {
        for (int id : ids) {
            mLocationList.add(id);
        }
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(mLocationList);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
