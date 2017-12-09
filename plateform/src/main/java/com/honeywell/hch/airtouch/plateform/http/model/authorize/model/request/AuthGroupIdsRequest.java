package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.request;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Chen on 4/12/16.
 */
public class AuthGroupIdsRequest implements IRequestParams, Serializable {
    private List<Integer> mAuthGroupIds = new ArrayList<>();

    public AuthGroupIdsRequest(List<Integer> authGroupIds) {
        mAuthGroupIds = authGroupIds;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(mAuthGroupIds);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
