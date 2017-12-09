package com.honeywell.hch.airtouch.plateform.timereceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.plateform.update.UpdateManager;


/**
 * Created by wuyuan on 5/26/16.
 */
public class CheckUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdateManager.getInstance().setNeedCheck(true);
        UpdateManager.getInstance().checkUpgrade();
    }
}
