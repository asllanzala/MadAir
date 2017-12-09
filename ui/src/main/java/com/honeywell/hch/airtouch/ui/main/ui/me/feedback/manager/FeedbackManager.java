package com.honeywell.hch.airtouch.ui.main.ui.me.feedback.manager;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.http.manager.model.HomeDevice;
import com.honeywell.hch.airtouch.plateform.http.model.user.FeedBackDeleteImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackImgRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.FeedBackRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserInfoRequest;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.UserInputInfoRequest;
import com.honeywell.hch.airtouch.plateform.http.task.FeedbackDeleteImgTask;
import com.honeywell.hch.airtouch.plateform.http.task.FeedbackImgTask;
import com.honeywell.hch.airtouch.plateform.http.task.FeedbackTextTask;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


/**
 * Created by zhujunyu on 2016/12/22.
 */

public class FeedbackManager {
    private SuccessCallback mSuccessCallback;
    private ErrorCallback mErrorCallback;

    public void feedback(Context context, String feedbackText, String cityName, ArrayList<String> imageUrls, ArrayList<String> imageTypes, HomeDevice homeDevice) {
        String userName = UserInfoSharePreference.getNickName();
        String userAccount = UserInfoSharePreference.getMobilePhone();
        String pVersion = Build.VERSION.RELEASE;
        String phoneBrand = android.os.Build.MODEL;
        String phoneModel = phoneBrand;
        String location;
        if (cityName != null) {
            location = cityName;
        } else {
            location = "";
        }

        String appVersion = String.valueOf(getVersionName());
        String language = AppConfig.shareInstance().getLanguage();
        String netModel = (NetWorkUtil.isConnectMobileNetwork(context) ? "MOBILE" : "WIFI");

        UserInfoRequest userInfoRequest = new UserInfoRequest(userAccount, userName, "true", "Android", pVersion, phoneBrand, phoneModel, appVersion, netModel, location, language);

        String[] imageUrl = imageUrls.toArray(new String[imageUrls.size()]);
        String[] imageType = imageTypes.toArray(new String[imageTypes.size()]);

        String title = "";
        if (length(feedbackText) >= 20) {
            try {
                title = subFeedBackstring(feedbackText, 20);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            title = feedbackText;
        }
        UserInputInfoRequest userInputInfoRequest;
        if (homeDevice == null) {
            userInputInfoRequest = new UserInputInfoRequest(title, feedbackText, "33", imageUrl, imageType);
        } else {
            userInputInfoRequest = new UserInputInfoRequest(title, feedbackText, "33", imageUrl, imageType, homeDevice);
        }


        FeedBackRequest feedBackRequest = new FeedBackRequest(userInfoRequest, userInputInfoRequest);
        FeedbackTextTask requestTask
                = new FeedbackTextTask(feedBackRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void feedbackImg(String feedbackStream) {
        FeedBackImgRequest feedBackRequest = new FeedBackImgRequest(feedbackStream);
        FeedbackImgTask requestTask
                = new FeedbackImgTask(feedBackRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void deleteFeedbackImg(String imageUrl) {

        FeedBackDeleteImgRequest feedBackDeleteImgRequest = new FeedBackDeleteImgRequest(imageUrl);
        FeedbackDeleteImgTask requestTask = new FeedbackDeleteImgTask(feedBackDeleteImgRequest, mReceiver);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    public void setSuccessCallback(SuccessCallback successCallback) {
        mSuccessCallback = successCallback;
    }


    public void setErrorCallback(ErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }

    public void sendSuccessCallBack(ResponseResult responseResult) {
        if (mSuccessCallback != null) {
            mSuccessCallback.onSuccess(responseResult);
        }
    }


    public void sendErrorCallBack(ResponseResult responseResult) {
        if (mErrorCallback != null && !responseResult.isAutoRefresh()) {
            mErrorCallback.onError(responseResult);
        }
    }

    IActivityReceive mReceiver = new IActivityReceive() {
        @Override
        public void onReceive(ResponseResult responseResult) {
            switch (responseResult.getRequestId()) {
                case FEED_BACK_IMG:
                    if (responseResult.isResult()) {
                        sendSuccessCallBack(responseResult);

                    } else {
                        sendErrorCallBack(responseResult);
                    }
                    break;
                case FEED_BACK_TEXT:
                    if (responseResult.isResult()) {
                        sendSuccessCallBack(responseResult);

                    } else {
                        sendErrorCallBack(responseResult);
                    }
                    break;
            }

        }
    };

    public interface SuccessCallback {
        void onSuccess(ResponseResult responseResult);
    }

    public interface ErrorCallback {
        void onError(ResponseResult responseResult);
    }

    private String getVersionName() {
        String versionName = "0";
        try {
            //获取当前的version code
            PackageManager packageManager = AppManager.getInstance().getApplication().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(AppManager.getInstance().getApplication().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (Exception e) {

        }
        int left = versionName.indexOf("(");
        if (left > 0) {
            versionName = versionName.substring(0, left);
        }

        return versionName;

    }

    public static int length(String s) {
        if (s == null)
            return 0;
        char[] c = s.toCharArray();
        int len = 0;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (!isLetter(c[i])) {
                len++;
            }
        }
        return len;
    }

    public static boolean isLetter(char c) {
        int k = 0x80;
        return c / k == 0 ? true : false;
    }


    public static boolean isChineseChar(char c)
            throws UnsupportedEncodingException {
        return String.valueOf(c).getBytes("UTF-8").length > 1;
    }


    public static String subFeedBackstring(String orignal, int count)
            throws UnsupportedEncodingException {
        // 原始字符不为null，也不是空字符串
        if (orignal != null && !"".equals(orignal)) {
            // 将原始字符串转换为GBK编码格式
            orignal = new String(orignal.getBytes(), "UTF-8");

            // 要截取的字节数大于0，且小于原始字符串的字节数
            if (count > 0 && count < orignal.getBytes("UTF-8").length) {
                StringBuffer buff = new StringBuffer();
                char c;
                for (int i = 0; i < count; i++) {
                    System.out.println(count);
                    c = orignal.charAt(i);
                    buff.append(c);
                    if (isChineseChar(c)) {
                        // 遇到中文汉字，截取字节总数减1
                        --count;
                    }
                }
                return new String(buff.toString().getBytes(), "UTF-8");
            }
        }
        return orignal;
    }

}
