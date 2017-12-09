package com.honeywell.hch.airtouch.plateform.appmanager.protocol;

import android.content.Context;

/**
 * Created by wuyuan on 3/10/16.
 * 这里用于区分应用宝和googleplay的不同
 */
public interface UpdateProtocol {
    public void initUpdate(Context context);

    public void update(Context context);

}
