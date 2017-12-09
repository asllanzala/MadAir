package com.honeywell.hch.airtouch.framework.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeywell.hch.airtouch.plateform.download.DownloadUtils;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;


/**
 * Created by wuyuan on 5/25/16.
 */
public class DownLoadCompleteReceiver  extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        // 安装应用
        DownloadUtils.getInstance().install(context, downId, UpdateManager.getInstance().getVersionCollector().getVersionName());
    }
}
