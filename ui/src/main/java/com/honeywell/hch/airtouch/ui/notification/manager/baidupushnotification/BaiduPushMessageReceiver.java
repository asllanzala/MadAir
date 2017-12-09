package com.honeywell.hch.airtouch.ui.notification.manager.baidupushnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.android.pushservice.PushMessageReceiver;
import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.util.LocalUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.library.util.SystemUtils;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.plateform.http.model.notification.UserClientInfoRequest;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.control.ui.device.controller.DeviceControlActivity;
import com.honeywell.hch.airtouch.ui.notification.manager.baidupushreceiver.NotificationReceiver;
import com.honeywell.hch.airtouch.ui.notification.manager.config.BaiduPushConfig;
import com.honeywell.hch.airtouch.ui.notification.manager.manager.BaiduPushManager;
import com.honeywell.hch.airtouch.ui.notification.ui.LeakDialogActivity;
import com.microsoft.windowsazure.messaging.NotificationHub;

import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


/**
 * Created by Vincent on 28/10/15.
 */

public class BaiduPushMessageReceiver extends PushMessageReceiver {

    public static final String TAG = BaiduPushMessageReceiver.class
            .getSimpleName();
    public static NotificationHub hub = null;
    public static String mChannelId, mUserId;
    public static Notification notification = new Notification();
    public static NotificationManager manager;
    public static final int NOTIFICATIONFLAG = 0;
    private static Context mContext = null;
    private static String mTagUserId;
    private String mToken;
    private String mobilePhone;
    private String mHplusUserId;
    private BaiduPushManager mBaiduPushManager = new BaiduPushManager();
    private int index = 0;
    private static String mLanguage;
    private static final String ANDROID = "Android";

    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        String responseString = "onBind errorCode=" + errorCode + " appid="
                + appid + " userId=" + userId + " channelId=" + channelId
                + " requestId=" + requestId;
        Log.d(TAG, responseString);
        initData(channelId, userId, context);
        if (errorCode == 0) {
            index = 0;
            submitUserClientInfo();
            try {
                if (hub == null) {
                    hub = new NotificationHub(
                            BaiduPushConfig.getNotificationHubName(),
                            BaiduPushConfig.getNotificationHubConnectionString(),
                            mContext);
                    Log.i(TAG, "Notification hub initialized");
                    Log.i(TAG, "language: " + LocalUtil.getLanguage(mContext));
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            registerWithNotificationHubs(mTagUserId);
        } else {
            if (index <= 5) {
                PushManager.startWork(mContext,
                        PushConstants.LOGIN_TYPE_API_KEY,
                        BaiduPushConfig.getMetaValue(mContext, BaiduPushConfig.BAIDUPUSHAPIKEY));
                index++;
            }

        }

    }

    private void initData(String channelId, String userId, Context context) {
        if (mContext == null) {
            mContext = context;
        }
        mChannelId = channelId;
        mUserId = userId;
        manager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        mobilePhone = UserInfoSharePreference.getMobilePhone();
        mHplusUserId = UserInfoSharePreference.getUserId();
        mToken = mUserId + "-" + mChannelId;
        mTagUserId = mobilePhone + "_" + mToken;
        mLanguage = LocalUtil.getLanguage(mContext);
    }

    private void submitUserClientInfo() {
        UserInfoSharePreference.saveUserTokenFromBaiduPush(mToken);
        UserClientInfoRequest userClientInfoRequest = new UserClientInfoRequest(mHplusUserId, mobilePhone, 1, mLanguage, mToken);
        mBaiduPushManager.putUserClientInfo(userClientInfoRequest);
    }

    public static void registerWithNotificationHubs(final String tagUserId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (hub != null) {
                        hub.registerBaidu(mUserId, mChannelId, tagUserId, mLanguage, ANDROID);
                    }
                    LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "Registered with Notification Hub - '"
                            + BaiduPushConfig.getNotificationHubName() + "'"
                            + " with UserId - '"
                            + mUserId + "' and Channel Id - '"
                            + mChannelId + "userName: " + tagUserId);
                } catch (Exception e) {
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "Hub exception: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * get message method
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 message=\"" + message;
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, messageString);
        if (!StringUtil.isEmpty(message)) try {
            JSONObject responseObject = new JSONObject(message);
            PushMessageModel pushMessageModel
                    = new Gson().fromJson(responseObject.toString(), PushMessageModel.class);
            boolean isFront = SystemUtils.isApplicationBroughtToBackground(context);
            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "是否在前台： " + isFront);
            if (BaiduPushConfig.startEntrance(pushMessageModel) == DeviceControlActivity.class
                    && isFront) {
                initWaterReceiver(context, pushMessageModel);
            } else {
                initNotification(context, pushMessageModel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initWaterReceiver(Context mContext, PushMessageModel pushMessageModel) {
        Intent intent = new Intent(mContext, LeakDialogActivity.class);
        intent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) pushMessageModel);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void initNotification(Context mContext, PushMessageModel pushMessageModel) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int nowTime = (int) now.getTime();
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "nowTime: " + nowTime);
        Intent broadcastIntent = new Intent(mContext, NotificationReceiver.class);
        broadcastIntent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) pushMessageModel);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(mContext, nowTime, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentText(pushMessageModel.getmBaiduPushAlert().getmAlert())
                .setContentTitle(mContext.getString(R.string.app_name))
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher);

        builder.setAutoCancel(true);
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "showNotification ");
        NotificationManager manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(nowTime, builder.build());
    }

    /**
     * get notifiation method
     */
    @Override
    public void onNotificationClicked(Context context, String title,
                                      String description, String customContentString) {
    }

    /**
     * notification arrived
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {

        String notifyString = "onNotificationArrived  title=\"" + title
                + "\" description=\"" + description + "\" customContent="
                + customContentString;
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, notifyString);

    }

    /**
     * setTags() 。
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onSetTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, responseString);
    }

    /**
     * delTags() 。
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        String responseString = "onDelTags errorCode=" + errorCode
                + " sucessTags=" + sucessTags + " failTags=" + failTags
                + " requestId=" + requestId;
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, responseString);
    }

    /**
     * listTags() 。
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        String responseString = "onListTags errorCode=" + errorCode + " tags="
                + tags;
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, responseString);

    }

    /**
     * PushManager.stopWork() 。
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        String responseString = "onUnbind errorCode=" + errorCode
                + " requestId = " + requestId;
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, responseString);
//        registerWithNotificationHubs("");

    }

}
