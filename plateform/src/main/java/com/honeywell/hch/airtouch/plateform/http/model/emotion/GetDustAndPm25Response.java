package com.honeywell.hch.airtouch.plateform.http.model.emotion;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 9/2/17.
 */

public class GetDustAndPm25Response implements Serializable {

    public static final String GET_DUST_AND_PM25_PARAMETER = "get_dust_and_pm25_patameter";

    @SerializedName("dustAndInPm25Response")
    private List<IndoorPm25Model> mInDoorPm25ModelList = new ArrayList<>();

    @SerializedName("outPm25Response")
    private List<OutDoorPm25Model> mOutDoorPm25ModelList = new ArrayList<>();

    public List<IndoorPm25Model> getmInDoorPm25ModelList() {
        return mInDoorPm25ModelList;
    }

    public List<OutDoorPm25Model> getmOutDoorPm25ModelList() {
        return mOutDoorPm25ModelList;
    }
}
