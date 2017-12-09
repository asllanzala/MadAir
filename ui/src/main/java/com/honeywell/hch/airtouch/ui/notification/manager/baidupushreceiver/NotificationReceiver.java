package com.honeywell.hch.airtouch.ui.notification.manager.baidupushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.SystemUtils;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.notification.manager.config.BaiduPushConfig;

import java.io.Serializable;


/**
 * Created by Vincent on 29/3/16.
 */
public class NotificationReceiver extends BroadcastReceiver {
    private final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        PushMessageModel pushMessageModel = (PushMessageModel) intent.getSerializableExtra(PushMessageModel.PUSHPARAMETER);
        if (pushMessageModel != null) {
            boolean isNeedUpdate = BaiduPushConfig.startEntrance(pushMessageModel) == null;
            if (SystemUtils.isAppAlive(context)) {
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "isAppLive ");
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                CurrentUserAccountInfo accountApp = UserAllDataContainer.getInstance().getCurrentUserAccount();
                boolean isLoginSuccess = UserInfoSharePreference.isUserAccountHasData();
                if (!isNeedUpdate && isLoginSuccess) {
                    LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "pushMessageModel) != null ");
                    Intent entranceIntent = new Intent(context, BaiduPushConfig.startEntrance(pushMessageModel));
                    entranceIntent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) pushMessageModel);
                    entranceIntent.putExtra(PushMessageModel.PUSHPARAMETERUPDATE, isNeedUpdate);
                    Intent[] intents = {mainIntent, entranceIntent};
                    context.startActivities(intents);
                } else {
                    LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "pushMessageModel) == null ");
                    mainIntent.putExtra(PushMessageModel.PUSHPARAMETERUPDATE, isNeedUpdate);
                    mainIntent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) pushMessageModel);
                    context.startActivity(mainIntent);
                }

            } else {
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "isAppLive not ");
                Intent launchIntent = context.getPackageManager().
                        getLaunchIntentForPackage(context.getPackageName());
                launchIntent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

                launchIntent.putExtra(PushMessageModel.PUSHPARAMETERUPDATE, isNeedUpdate);
                launchIntent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) pushMessageModel);
                context.startActivity(launchIntent);
            }
        }

    }
}
