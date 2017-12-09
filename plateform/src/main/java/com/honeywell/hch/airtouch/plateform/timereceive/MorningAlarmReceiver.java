package com.honeywell.hch.airtouch.plateform.timereceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jin Qian on 3/12/2015.
 */
public class MorningAlarmReceiver extends BroadcastReceiver {
    public static final String TIME_CHANGE_ACTION = "TimeChange";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(TIME_CHANGE_ACTION);
        newIntent.setAction(TIME_CHANGE_ACTION);
        context.sendBroadcast(newIntent);
    }
}
