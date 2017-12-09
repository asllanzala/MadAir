package com.honeywell.hch.airtouch.plateform.update;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.download.DownloadUtils;
import com.honeywell.hch.airtouch.plateform.http.model.update.VersionCollector;
import com.honeywell.hch.airtouch.plateform.storage.UpdateVersionPreference;

public class UpgradeCheckThread extends AsyncTask<Object, Integer, VersionCollector> {


    public static final String LOG_TAG = UpgradeCheckThread.class.getSimpleName();
    private boolean isHasPermission = true;

    private static boolean isRunning = false;

    public UpgradeCheckThread() {
    }

    @Override
    protected VersionCollector doInBackground(Object... params) {
        try {
            isRunning = true;
            // Check upgrade
            String jsonData = DownloadUtils.getInstance().downloadJsonData(AppManager.getAndroidVersionUrlPath());
//             String jsonData = FileUtils.readAssesFile();
            VersionCollector versionData = new Gson().fromJson(jsonData, VersionCollector.class);
            UpdateManager.getInstance().setVersionCollector(versionData);
            return versionData;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Download and parse error! " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(VersionCollector versionData) {
        isRunning = false;
        if (versionData != null) {
            goToUpdateVersionMinderActivity(versionData);
        }
    }

    @Override
    protected void onPreExecute() {
        // 任务启动，可以在这里显示一个对话框，这里简单处理
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // 更新进度
    }

    public static boolean isRunning() {
        return isRunning;
    }

    private void goToUpdateVersionMinderActivity(VersionCollector versionData) {

        setVersionUpdateData(versionData);
        int updateType = HPlusConstants.NO_UPDATE;
        if (isForceUpgrade(versionData)) {
            updateType = HPlusConstants.FORCE_UPDATE;
        } else if (isNormalUpgrade(versionData)) {
            updateType = HPlusConstants.NORMAL_UPDATE;
        }
        if (updateType != HPlusConstants.NO_UPDATE) {
            try {
                Class<?> cl = Class.forName("com.honeywell.hch.airtouch.ui.update.ui.UpdateVersionMinderActivity");
                Intent intent = new Intent(AppManager.getInstance().getApplication(), cl);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UpdateManager.VERSION_DATA, versionData);
                bundle.putInt(UpdateManager.UPDATE_TYPE, updateType);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                AppManager.getInstance().getApplication().startActivity(intent);
            } catch (Exception e) {

            }

        }
    }

    private boolean isForceUpgrade(VersionCollector versionData) {
        int thisVersionCode = getVersionCode();
        boolean isNeedUpdate = thisVersionCode < versionData.getVersionCode();
        if ((versionData.getIsForceUpdate() == VersionCollector.FORCE_UPDATE && isNeedUpdate) ||
                (thisVersionCode != 0 && thisVersionCode < versionData.getMinVersionCode()) ||
                isInUpdateList(versionData.getNeedUpdateList(), thisVersionCode)) {
            return true;
        }
        return false;
    }

    //初始化me 上面更新版本的红点，缓存保存服务器上的version code，保存的比服务器小就再保存
    private void setVersionUpdateData(VersionCollector versionData) {
        int thisVersionCode = getVersionCode();
        int versionCodeFromServer = versionData.getVersionCode();
        int lastVersionCodePreference = UpdateVersionPreference.getLastVersionCode();
        boolean isNeedUpdate = thisVersionCode < versionCodeFromServer;
        UpdateVersionPreference.saveOwnUpdate(isNeedUpdate);
        if (UpdateVersionPreference.LASTVERSIONDEFAULT == lastVersionCodePreference) {
            UpdateVersionPreference.saveVersionCode(versionCodeFromServer);
            if (thisVersionCode < versionCodeFromServer) {
                UpdateVersionPreference.saveMeRedDot(true);
            }
        } else if (lastVersionCodePreference < versionCodeFromServer) {
            UpdateVersionPreference.saveVersionCode(versionCodeFromServer);
            UpdateVersionPreference.saveMeRedDot(true);
        }
        Intent boradIntent = new Intent(HPlusConstants.UPDATE_ME_RED_DOT_ACTION);
        AppManager.getInstance().getApplication().getApplicationContext().sendBroadcast(boradIntent);
    }

    private boolean isNormalUpgrade(VersionCollector versionData) {

        int currentTimeHour = (int) ((System.currentTimeMillis()) / (3600 * 1000 * 24));

        AppConfig.mInitTime = SharePreferenceUtil.getPrefInt(HPlusConstants.PREFERENCE_USER_CONFIG,
                AppConfig.UPDATE_VERSION_TIME, 0);
        int thisVersionCode = getVersionCode();
        if (thisVersionCode < versionData.getVersionCode() && currentTimeHour - AppConfig.mInitTime > 6) {
            return true;
        }
        return false;
    }

    private boolean isInUpdateList(int[] updateVersionList, int thisVersonCode) {
        if (updateVersionList != null && updateVersionList.length > 0) {
            for (int versionItem : updateVersionList) {
                if (versionItem == thisVersonCode) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getVersionCode() {
        int versionCode = 0;
        try {
            //获取当前的version code
            PackageManager packageManager = AppManager.getInstance().getApplication().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(AppManager.getInstance().getApplication().getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (Exception e) {

        }
        return versionCode;

    }

}
