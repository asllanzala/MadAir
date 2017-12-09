package com.honeywell.hch.airtouch.plateform.http.model.multi;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 10/17/15.
 */
public class ScenarioMultiResponse implements  Serializable {
    public static final String SCENARIO_DATA = "scenario";

    @SerializedName("commonTaskResultList")
    private List<ScenarioMultiCommTaskResponse> mGroupCommTaskResponse;

    public List<ScenarioMultiCommTaskResponse> getGroupCommTaskResponse() {
        return mGroupCommTaskResponse;
    }

}
