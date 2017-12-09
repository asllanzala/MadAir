package com.honeywell.hch.airtouch.plateform.http.model.authorize.model.request;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qian Jin on 2/14/16.
 */
public class AuthUserRequest implements IRequestParams, Serializable {
    private List<String> mAuthPhoneNumbers = new ArrayList<>();

    public AuthUserRequest(List<String> authPhoneNumbers) {
        mAuthPhoneNumbers = authPhoneNumbers;
    }

    @Override
    public String getRequest(Gson gson) {
        return gson.toJson(mAuthPhoneNumbers);
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }

}
