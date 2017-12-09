package com.honeywell.hch.airtouch.plateform.http.model.home.request;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;

/**
 * Created by Vincent on 5/8/16.
 */
public class HomeControlRequest implements IRequestParams, Serializable {
    private int mSenario;  //0--Away(off) 1--Home(Auto) 2--Sleep 3 --Awake

    public HomeControlRequest(int senario) {
        this.mSenario = senario;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(mSenario);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }
}