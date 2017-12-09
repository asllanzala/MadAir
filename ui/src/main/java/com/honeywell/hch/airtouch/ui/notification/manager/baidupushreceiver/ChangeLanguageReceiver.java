package com.honeywell.hch.airtouch.ui.notification.manager.baidupushreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.library.util.LocalUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.http.model.notification.UserClientInfoRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.notification.manager.manager.BaiduPushManager;

/**
 * Created by Vincent on 25/3/16.
 */
public class ChangeLanguageReceiver extends BroadcastReceiver {
    public static final String ACTION_LOCALE_CHANGED = "android.intent.action.LOCALE_CHANGED";
    private final String TAG = "ChangeLanguageReceiver";
    private BaiduPushManager mBaiduPushManager = new BaiduPushManager();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_LOCALE_CHANGED.equals(intent.getAction())) {
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Change language");
            String mToken = UserInfoSharePreference.getTokenFromBaiduPush();
            String mobilePhone = UserInfoSharePreference.getMobilePhone();
            String mHplusUserId = UserInfoSharePreference.getUserId();
            if (!"".equals(mToken) && !"".equals(mobilePhone) && (!mHplusUserId.equals("") || mHplusUserId != null)) {
                UserClientInfoRequest userClientInfoRequest = new UserClientInfoRequest(mHplusUserId, mobilePhone, 1, LocalUtil.getLanguage(context), mToken);
                mBaiduPushManager.putUserClientInfo(userClientInfoRequest);
                LogUtil.log(LogUtil.LogLevel.INFO, TAG, "Change language2");
            }
        }
    }
}
