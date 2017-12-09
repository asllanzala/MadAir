package com.honeywell.hch.airtouch.plateform.timereceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jin Qian on 3/12/2015.
 */
public class NightAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(MorningAlarmReceiver.TIME_CHANGE_ACTION);
        newIntent.setAction(MorningAlarmReceiver.TIME_CHANGE_ACTION);
        context.sendBroadcast(newIntent);
    }
}
