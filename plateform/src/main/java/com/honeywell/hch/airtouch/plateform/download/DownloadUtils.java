package com.honeywell.hch.airtouch.plateform.download;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.honeywell.hch.airtouch.library.util.FileUtils;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.R;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.http.model.update.VersionCollector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadUtils {
	

	public static final String LOG_TAG = DownloadUtils.class.getSimpleName();
	public static final String APP_NAME_AIR_TOUCH_FILE_NAME = "AirTouch";
	public static final long FILE_DOWNLOAD_ID_INVALID = -1;
	
	public static final String DIR_BASE = Environment.getExternalStorageDirectory().toString() + "/AirTouch/";
	public static final String DIR_APK_NAME_FULL = DIR_BASE + "apk";
	public static final String DIR_APK_NAME_AIRTOUCH_APK = "AirTouch/apk";

	public static final String DOWN_LOAD_APK_URL = "https://acscloud.honeywell.com.cn/hplus/apk/yingyongbao-release.apk";

	public DownloadManager mDownloadManager;
	public static DownloadUtils mDownloadUtils = null;
	
	public static synchronized DownloadUtils getInstance()
	{
		if(mDownloadUtils == null)
		{
			mDownloadUtils = new DownloadUtils();
		}
		return mDownloadUtils;
	}
	
	private DownloadUtils()
	{
		mDownloadManager = (DownloadManager) AppManager.getInstance().getApplication().getSystemService(Activity.DOWNLOAD_SERVICE);
	}
	
	/**
	 * Enqueue a new download. The download will start automatically once the
	 * download manager is ready to execute it and connectivity is available.
	 * 
	 * @param versionCollector
	 * @return an ID for the download, unique across the system. This ID is used
	 *         to make future calls related to this download.
	 */
	public Long startDownload(VersionCollector versionCollector) {
//		if (StringUtil.isEmpty(versionCollector.download)) {
//			return FILE_DOWNLOAD_ID_INVALID;
//		}

//		Log.v(LOG_TAG, "Dowanload APK URL: " + versionCollector.download);
		File folder = new File(DIR_APK_NAME_FULL);
		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdirs();
		}

		String fileName = getFileNameApk(versionCollector.getVersionName());
		// 当文件已经存在，先删除文件
		FileUtils.deleteFile(DIR_APK_NAME_FULL + "/" + fileName);
		Uri uri = Uri.parse(DOWN_LOAD_APK_URL);
		
		DownloadManager.Request request = new DownloadManager.Request(uri);
		request.setDestinationInExternalPublicDir(DIR_APK_NAME_AIRTOUCH_APK, fileName);
		request.setTitle(AppManager.getInstance().getApplication().getApplicationContext().getString(R.string.app_name) + versionCollector.getVersionName());
//		request.setDescription(versionCollector.description);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		//发出通知，既后台下载  
		request.setShowRunningNotification(true);  
        //显示下载界面  
		request.setVisibleInDownloadsUi(true);
		
		request.setMimeType("application/cn.trinea.download.file");
		Long lastDownload = mDownloadManager.enqueue(request);

		return lastDownload;
	}


	public int getDownLoadStatus(long downloadId){
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		Cursor c = mDownloadManager.query(query);
		int status = DownloadManager.STATUS_SUCCESSFUL;
		if (c != null && c.moveToFirst()) {
			status = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
		}
		if (c != null){
			c.close();
		}
        return status;
	}

	/**
	 * Download Json data from website.
	 * 
	 * @return json data string.
	 */
	public String downloadJsonData(String urlPath) {
		String jsonResult = "";
		URL url;
		try {
			url = new URL(urlPath);

			Log.v(LOG_TAG, "Begin to read Android Version Config file...");

			// 打开连接
			URLConnection connection;
			connection = url.openConnection();

//			InputStream input;
//			input = connection.getInputStream();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							connection.getInputStream()));
			String decodedString;
			while ((decodedString = in.readLine()) != null) {
				jsonResult += decodedString;
			}
			in.close();

//			int buffersize = 512; // Get the length of
//			byte buffer[] = new byte[buffersize];
//			Log.v(LOG_TAG, "buffersize=" + buffersize);
//
//			// input stream
//			input.read(buffer);
//			// 完毕，关闭所有链接
//			input.close();

			// 读取结果：{"version_name":“1.0.1”,"version_code":"110","updated":"2014-11-13","size":"11.3 MB","description":"This is a init version","download":"http://eccap.honeywell.cn/App%20Software/AirCleaner.apk"}
//			jsonResult = new String(buffer,0,buffer.length);
			Log.v(LOG_TAG, "JSON data：" + jsonResult);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.v(LOG_TAG, "Stop to read Android Version Config file...");
		return jsonResult;
	}
	
	public String getFileNameApk(String appVersionName) {
		StringBuffer str = new StringBuffer();
		str.append(APP_NAME_AIR_TOUCH_FILE_NAME);
		str.append("_");
		str.append(appVersionName);
		str.append(".apk");
		return str.toString();
	}
	
	/**
	 * get download status, DownloadManager.STATUS_FAILED,
	 * DownloadManager.STATUS_SUCCESS
	 * 
	 * @param downloadId
	 * @return
	 */
	public int getDownloadStatusByDownloadId(long downloadId) {
		return getInt(downloadId, DownloadManager.COLUMN_STATUS);
	}
	
	/**
	 * get download status, DownloadManager.STATUS_FAILED,
	 * DownloadManager.STATUS_SUCCESS
	 * 
	 * @param downloadId
	 * @return
	 */
	public String getDownloadFileNameByDownloadId(long downloadId) {
		String fileNameWithPath = getString(downloadId, DownloadManager.COLUMN_LOCAL_FILENAME);
		String[] namePathArray = StringUtil.split(fileNameWithPath, '/');
		String name = namePathArray[namePathArray.length - 1];
		return name;
	}
	
	/**
	 * get String column
	 * 
	 * @param downloadId
	 * @param columnName
	 * @return
	 */
	private String getString(long downloadId, String columnName) {
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		String result = "";
		Cursor c = null;
		try {
			c = mDownloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getString(c.getColumnIndex(columnName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}
	
	/**
	 * get int column
	 * 
	 * @param downloadId
	 * @param columnName
	 * @return
	 */
	private int getInt(long downloadId, String columnName) {
		DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
		int result = -1;
		Cursor c = null;
		try {
			c = mDownloadManager.query(query);
			if (c != null && c.moveToFirst()) {
				result = c.getInt(c.getColumnIndex(columnName));
			}
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}
	
	public boolean install(Context context, long downloadId, String appVersionName) {
		// if download successful, install apk
		if (getDownloadStatusByDownloadId(downloadId) == DownloadManager.STATUS_SUCCESSFUL) {
			String apkFilePath = new StringBuilder(DIR_APK_NAME_FULL).append(File.separator)
					.append(getFileNameApk(appVersionName)).toString();
			return install(context, apkFilePath);
		}
		return false;
	}
	
	/**
	 * install app
	 * 
	 * @param context
	 * @param filePath
	 * @return whether apk exist
	 */
	private boolean install(Context context, String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		File file = new File(filePath);
		if (file != null && file.length() > 0 && file.exists() && file.isFile()) {
			i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			return true;
		}
		return false;
	}
	
}
