package com.honeywell.hch.airtouch.plateform.http.model.group;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Qian Jin on 10/13/15.
 */
public class ScenarioGroupRequest implements IRequestParams, Serializable {

    /**
     * AWAY == OFF
     */
    public static final int SCENARIO_AWAY_OFF = 0;

    /**
     * HOME == AUTO == ON
     */
    public static final int SCENARIO_HOME_AUTO = 1;
    public static final int SCENARIO_SLEEP = 2;

    @SerializedName("groupScenario")
    private int mGroupScenario;

    public ScenarioGroupRequest(int groupScenario) {
        mGroupScenario = groupScenario;
    }

    public int getmGroupScenario() {
        return mGroupScenario;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(this);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
