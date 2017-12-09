package com.honeywell.hch.airtouch.ui.userinfo.manager;

import android.content.Context;
import android.util.Patterns;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.model.authorize.model.response.CheckAuthUserResponse;
import com.honeywell.hch.airtouch.plateform.http.model.user.response.UserLoginResponse;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.notification.manager.config.BaiduPushConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by h127856 on 6/22/16.
 * 登录，注册，
 */
public class UserAccountRelatedUIManager extends UserAccountBaseUIManager {

    public final static int INDIA_PHONE_LENGTH = 10;
    public final static int CHINA_PHONE_LENGTH = 11;

    public final static int USER_NOT_EXIST = 1;
    public final static int USER_PASSWORD_ERROR = 2;
    public final static int OTHERS_ERROR = 0;
    public final static int USER_ALLREADY_EXIST = 3;
    public final static int USER_REGISTER_ERROR = 4;

    private final static String USER_NOT_FOUND = "UserNotFound";
    private final static String USER_ALREADY_EXISTS = "UserAlreadyExists";

    private Context mContext;
    private String mMobilePhone;
    private String mUserPassword;
    private UserAccountManager mUserAccountManager;


    public UserAccountRelatedUIManager(Context context) {
        mUserAccountManager = new UserAccountManager();
        mContext = context;
    }


    /**
     * 设置success callBack
     *
     * @param successCallback
     */
    public void setSuccessCallback(UserAccountManager.SuccessCallback successCallback) {
        mUserAccountManager.setSuccessCallback(successCallback);
    }

    /**
     * 设置error callBack
     *
     * @param errorCallback
     */
    public void setErrorCallback(UserAccountManager.ErrorCallback errorCallback) {
        mUserAccountManager.setErrorCallback(errorCallback);
    }


    /**
     * 登录成功后的处理
     *
     * @param responseResult
     */
    public void dealWithLoginSuccess(ResponseResult responseResult) {

        UserLoginResponse userLoginResponse = (UserLoginResponse) responseResult.getResponseData().get(HPlusConstants.USER_LOGIN_INFO);
        String nickname = userLoginResponse.getUserInfo().getFirstName();
        String countryCode = userLoginResponse.getUserInfo().getCountryPhoneNum();
        String userId = userLoginResponse.getUserInfo().getUserID();
        String sessionId = userLoginResponse.getSessionId();
        int userType = userLoginResponse.getUserInfo().getUserType();

        UserInfoSharePreference.saveUserInfoInSp(nickname, mMobilePhone, mUserPassword
                , userId, countryCode);
        UserInfoSharePreference.saveLoginSession(sessionId);
        UserInfoSharePreference.saveUserType(userType);

        // delete data in GroupControl
//        SharePreferenceUtil.clearPreference(mContext, SharePreferenceUtil
//                .getSharedPreferenceInstanceByName(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH));
        if (!AppConfig.shareInstance().isIndiaAccount()) {
            // register Baidu push service
            PushManager.startWork(mContext, PushConstants.LOGIN_TYPE_API_KEY,
                    BaiduPushConfig.getMetaValue(mContext, BaiduPushConfig.BAIDUPUSHAPIKEY));
        }

    }

    /**
     * 更新密码成功或是忘记密码修改成功
     *
     * @param newPassword
     */
    public void dealWithUpatePasswordSuccess(String newPassword) {
        UserInfoSharePreference.savePassword(newPassword);
    }


    public boolean validPassword(String password) {
        if (StringUtil.isEmpty(password)) {
            return false;
        }
        Matcher passwordMatcher = Pattern.compile("^[A-z0-9]{6,30}+$").matcher(password);
        // Password is at least 6 characters
        if (password.length() < HPlusConstants.MIN_USER_PASSWORD) {
            return false;
        }
        // Password does not match rules
        if (passwordMatcher.matches() == false) {
            return false;
        }
        return true;
    }


    public boolean validPhoneNumber(String phoneNumber, String countryCode) {
        if (StringUtil.isEmpty(phoneNumber) || StringUtil.isEmpty(countryCode)) {
            return false;
        }
        Matcher smsMatcher = Patterns.PHONE.matcher(phoneNumber);
        if (smsMatcher.matches() == false) {
            return false;
        }
        // India version
        if (countryCode.equals(HPlusConstants.CHINA_CODE) && phoneNumber.length() != CHINA_PHONE_LENGTH) {
            return false;
        }
        if (countryCode.equals(HPlusConstants.INDIA_CODE) && phoneNumber.length() != INDIA_PHONE_LENGTH) {
            return false;
        }
        return true;
    }


    /**
     * 登录
     *
     * @param phoneNumber
     * @param password
     */
    public void loginEvent(String phoneNumber, String password) {
        mMobilePhone = phoneNumber;
        mUserPassword = password;
        mUserAccountManager.startLogin(phoneNumber, password);
    }

    /**
     * 获取验证码
     */
    public void getSmsVerifyCode(String phoneNumber, String contryCode) {
        mUserAccountManager.getSmsCode(phoneNumber, contryCode);
    }

    /**
     * 注册新用户
     */
    public void registerNewUser(String nickname, String mobilePhone, String password, String countryCode) {
        mUserAccountManager.registerNewUser(nickname, mobilePhone, password, countryCode);
    }

    /**
     * 验证短信
     *
     * @param phoneNumber
     * @param smsCode
     */
    public void verifySmsCode(String phoneNumber, String smsCode) {
        mUserAccountManager.validSmsCode(phoneNumber, smsCode);
    }


    /**
     * 忘记密码
     *
     * @param phoneNumber
     * @param newPassword
     */
    public void forgetPassword(String phoneNumber, String newPassword) {
        mUserAccountManager.updatePassword(phoneNumber, newPassword);
    }

    /**
     * 验证用户是否是HPlus用户
     *
     * @param phoneNumber
     */
    public void checkAuthUser(String phoneNumber) {
        List<String> phoneList = new ArrayList<>();
        phoneList.add(phoneNumber);
        mUserAccountManager.checkAuthUser(phoneList);
    }

    /**
     * 更改密码
     *
     * @param oldPassword
     * @param newPassword
     */
    public void changePassword(String oldPassword, String newPassword) {
        mUserAccountManager.changePassword(oldPassword, newPassword);
    }

    /**
     * 获取phoneNumber的限制长度
     *
     * @param cityCode
     * @return
     */
    public int getPhoneNumberLength(String cityCode) {
        if (StringUtil.isEmpty(cityCode) || HPlusConstants.CHINA_CODE.equals(cityCode)) {
            return UserAccountRelatedUIManager.CHINA_PHONE_LENGTH;
        } else {
            return UserAccountRelatedUIManager.INDIA_PHONE_LENGTH;
        }
    }

    /**
     * 忘记密码失败后的解析，针对400错误
     *
     * @param responseResult
     * @return
     */
    public int getRestPasswordError(ResponseResult responseResult) {

        if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
            if (!StringUtil.isEmpty(responseResult.getExeptionMsg())) {
                if (responseResult.getExeptionMsg().contains(USER_NOT_FOUND)) {
                    return USER_NOT_EXIST;
                }
            }
            return USER_PASSWORD_ERROR;
        }
        return OTHERS_ERROR;
    }


    /**
     * 注册接口的400错误
     * @param responseResult
     * @return
     */
    public int getRegisterApiError(ResponseResult responseResult) {

        if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST) {
            if (!StringUtil.isEmpty(responseResult.getExeptionMsg())) {
                if (responseResult.getExeptionMsg().contains(USER_ALREADY_EXISTS)) {
                    return USER_ALLREADY_EXIST;
                }
            }
            return USER_REGISTER_ERROR;
        }
        return OTHERS_ERROR;
    }


    /**
     * 是否是hplus账号
     *
     * @param responseResult
     * @return
     */
    public boolean isHplusUser(ResponseResult responseResult) {
        List<CheckAuthUserResponse> checkAuthUserResponseList = (List<CheckAuthUserResponse>) responseResult.getResponseData()
                .getSerializable(CheckAuthUserResponse.AUTH_USER_DATA);
        if (checkAuthUserResponseList != null && checkAuthUserResponseList.size() > 0) {
            for (CheckAuthUserResponse checkAuthUserResponse : checkAuthUserResponseList) {
                if (checkAuthUserResponse.isHPlusUser() == true) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 是否是企业账号，企业账号下是不支持忘记密码功能的
     *
     * @param responseResult
     * @return
     */
    public boolean isEnterpriseAccount(ResponseResult responseResult) {
        List<CheckAuthUserResponse> checkAuthUserResponseList = (List<CheckAuthUserResponse>) responseResult.getResponseData()
                .getSerializable(CheckAuthUserResponse.AUTH_USER_DATA);
        if (checkAuthUserResponseList != null && checkAuthUserResponseList.size() > 0) {
            for (CheckAuthUserResponse checkAuthUserResponse : checkAuthUserResponseList) {
                if (checkAuthUserResponse.getmUserType() == HPlusConstants.ENTERPRISE_ACCOUNT) {
                    return true;
                }
            }
        }
        return false;
    }

}
