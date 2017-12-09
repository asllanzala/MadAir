package com.honeywell.hch.airtouch.plateform.update;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.download.DownloadUtils;
import com.honeywell.hch.airtouch.plateform.http.model.update.VersionCollector;

/**
 * Created by wuyuan on 5/24/16.
 */
public class UpdateManager {

    public static final String VERSION_DATA = "version_data";
    public static final String UPDATE_TYPE = "update_type";
    public static final String PERMESSION_TYPE = "permission_type";

    public static final String UPDATE_SHAREPREFERENCE_FILE_NAME = "update_info_sp";
    public static final String VERSION_NAME_KEY = "version_name";
    public static final String VERSION_CODE_KEY = "version_code";
    public static final String IS_FORCE_UPDATE_KEY = "is_force_update";
    public static final String MIN_VERSION_CODE_KEY = "min_version_code";
    public static final String NEED_TO_UPDATE_LIST_KEY = "need_to_update_list";
    public static final String DESCRIPTION_EN_KEY = "description_en";
    public static final String DESCRIPTION_CH_KEY = "description_ch";
    public static final String DOWNLOAD_ID_KEY = "downloadId";

    private static final String TAG = "UpdateManager";

    private static UpdateManager mUpdateManager;

    private boolean isNeedCheck = true;

    private VersionCollector mVersionCollector = null;

    public static UpdateManager getInstance() {
        if (mUpdateManager == null) {
            mUpdateManager = new UpdateManager();
        }
        return mUpdateManager;
    }

    public VersionCollector getVersionCollector() {
        if (mVersionCollector == null) {
            mVersionCollector = new VersionCollector();
            restoreVersionCollector();
        }
        return mVersionCollector;
    }

    public void setVersionCollector(VersionCollector versionCollector) {
        mVersionCollector = versionCollector;
        persistVersionCollector();
    }


    @SuppressLint("NewApi")
    public void checkUpgrade() {

        if (isNeedCheck && !isDownLoading() && !UpgradeCheckThread.isRunning()) {
            // 没有正在下载，或者下载失败的情况下载再次检测更新,这个逻辑是为了保证正在下载的时候，不要重复提醒
            UpgradeCheckThread upgradeCheckThread = new UpgradeCheckThread();
            AsyncTaskExecutorUtil.executeAsyncTask(upgradeCheckThread);
        }


    }

    public void openUrl(Context context, String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    public boolean isDownLoading() {
        VersionCollector preVersionCollector = getVersionCollector();
        long preDownloadId = preVersionCollector.getDownloadId();
        int downLoadStatus = DownloadUtils.getInstance().getDownLoadStatus(preDownloadId);
        return downLoadStatus == DownloadManager.STATUS_RUNNING || downLoadStatus == DownloadManager.STATUS_PENDING;
    }

    public void starDownLoadAPK(Context context, boolean isPermission, boolean isNeedStorageData) {
        isNeedCheck = false;
        if (isNeedStorageData) {
            AppConfig.shareInstance().setUpdateVersionTime(0);
        }
        if (AppManager.getInstance().getmIsYingYongBao()) {
            if (isPermission && UpdateManager.getInstance().isDownManagerEnable(context)) {
                if (!isDownLoading()) {
                    UpdateManager.getInstance().startDownLoadOrInStallApk(UpdateManager.getInstance().getVersionCollector());
                }

            } else {
                UpdateManager.getInstance().openUrl(context, HPlusConstants.YINGYONGBAO_URL);
            }
        } else {
            UpdateManager.getInstance().openUrl(context, HPlusConstants.GOOGLE_PLAY_URL);
        }
    }

    public boolean startDownLoadOrInStallApk(VersionCollector versionData) {

        boolean installSuccess = false;
        VersionCollector preVersionCollector = getVersionCollector();
        long preDownloadId = preVersionCollector.getDownloadId();
        int preDownloadStatus = DownloadUtils.getInstance().getDownloadStatusByDownloadId(preDownloadId);
        int preVersCode = getVersionCollector().getVersionCode();
        int curVersionCode = versionData.getVersionCode();

        if (preDownloadStatus == DownloadManager.STATUS_SUCCESSFUL && preVersCode == curVersionCode) {
            Log.v(TAG, "上次下载成功过，尝试直接安装");
            // 下载成功，但是上次没有安装
            installSuccess = DownloadUtils.getInstance().install(AppManager.getInstance().getApplication(), preDownloadId, UpdateManager.getInstance()
                    .getVersionCollector().getVersionName());
        }

        // 不能成功安装，2种情况：没下载过，或者下载的包被删除了
        if (!installSuccess) {
            Log.v(TAG, "不能直接安装，从新去下载");
            UpdateManager.getInstance().setVersionCollector(versionData);
            // 还没下载，开始去下载PK
            long downloadId = DownloadUtils.getInstance().startDownload(versionData);

            // 把新的downloadId存储起来
            getVersionCollector().setDownloadId(downloadId);
            persistVersionCollector();
        }
        return true;
    }


    public void persistVersionCollector() {
        SharePreferenceUtil.clearPreference(AppManager.getInstance().getApplication(), SharePreferenceUtil.getSharedPreferenceInstanceByName(UPDATE_SHAREPREFERENCE_FILE_NAME));

        SharePreferenceUtil.setPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, VERSION_NAME_KEY, mVersionCollector.getVersionName());
        SharePreferenceUtil.setPrefInt(UPDATE_SHAREPREFERENCE_FILE_NAME, VERSION_CODE_KEY, mVersionCollector.getVersionCode());
        SharePreferenceUtil.setPrefInt(UPDATE_SHAREPREFERENCE_FILE_NAME, IS_FORCE_UPDATE_KEY, mVersionCollector.getIsForceUpdate());
        SharePreferenceUtil.setPrefInt(UPDATE_SHAREPREFERENCE_FILE_NAME, MIN_VERSION_CODE_KEY, mVersionCollector.getMinVersionCode());

        SharePreferenceUtil.setPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, NEED_TO_UPDATE_LIST_KEY, mVersionCollector.getNeedUpdateListToString());
        SharePreferenceUtil.setPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, DESCRIPTION_EN_KEY, mVersionCollector.getDescriptionEn());
        SharePreferenceUtil.setPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, DESCRIPTION_CH_KEY, mVersionCollector.getDescriptionCh());
        SharePreferenceUtil.setPrefLong(UPDATE_SHAREPREFERENCE_FILE_NAME, DOWNLOAD_ID_KEY, mVersionCollector.getDownloadId());

    }

    public void restoreVersionCollector() {
        mVersionCollector.setVersionName(SharePreferenceUtil.getPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, VERSION_NAME_KEY, ""));
        mVersionCollector.setVersionCode(SharePreferenceUtil.getPrefInt(UPDATE_SHAREPREFERENCE_FILE_NAME, VERSION_CODE_KEY, 0));
        mVersionCollector.setMinVersionCode(SharePreferenceUtil.getPrefInt(UPDATE_SHAREPREFERENCE_FILE_NAME, MIN_VERSION_CODE_KEY, 0));
        mVersionCollector.setDescriptionCh(SharePreferenceUtil.getPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, DESCRIPTION_CH_KEY, ""));

        mVersionCollector.setDescriptionEn(SharePreferenceUtil.getPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, DESCRIPTION_EN_KEY, ""));
        mVersionCollector.setIsForceUpdate(SharePreferenceUtil.getPrefInt(UPDATE_SHAREPREFERENCE_FILE_NAME, IS_FORCE_UPDATE_KEY, 0));

        mVersionCollector.setDownloadId(SharePreferenceUtil.getPrefLong(UPDATE_SHAREPREFERENCE_FILE_NAME, DOWNLOAD_ID_KEY, 0));

        mVersionCollector.setNeedUpdateList(StringUtil.StringtoInt(SharePreferenceUtil.getPrefString(UPDATE_SHAREPREFERENCE_FILE_NAME, NEED_TO_UPDATE_LIST_KEY, "")));

    }

    public boolean isDownManagerEnable(Context context) {
        int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
            return false;
        }
        return true;
    }

    public void setNeedCheck(boolean needCheck) {
        isNeedCheck = needCheck;
    }

}
