package com.honeywell.hch.airtouch.plateform.http.model.user;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.IRequestParams;

/**
 * Created by zhujunyu on 2016/12/21.
 */

public class FeedBackDeleteImgRequest implements IRequestParams {

    private String imageUrl;
    public FeedBackDeleteImgRequest(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String getRequest(Gson gson) {
        return imageUrl;
    }

    @Override
    public String getPrintableRequest(Gson gson) {
        return getRequest(gson);
    }
}
