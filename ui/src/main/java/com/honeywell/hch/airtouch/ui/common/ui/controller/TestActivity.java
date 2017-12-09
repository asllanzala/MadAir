package com.honeywell.hch.airtouch.ui.common.ui.controller;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.IReceiveResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.http.ThinkPageClient;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.model.user.request.SwapLocationRequest;
import com.honeywell.hch.airtouch.plateform.http.model.weather.xinzhi.WeatherData;
import com.honeywell.hch.airtouch.plateform.http.task.DeleteLocationTask;
import com.honeywell.hch.airtouch.plateform.http.task.SwapLocationNameTask;
import com.honeywell.hch.airtouch.plateform.share.ShareUtility;
import com.honeywell.hch.airtouch.ui.authorize.manager.AuthorizeManager;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap.ApEnrollChoiceActivity;
import com.honeywell.hch.airtouch.ui.main.manager.devices.manager.GroupManager;
import com.honeywell.hch.airtouch.ui.userinfo.ui.login.UserLoginActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jin Qian on 1/22/2015.
 */
public class TestActivity extends BaseActivity {
    private Button loginButton;
    private Button enrollButton;
    private Button addDeviceButton;
    private Button thinkpageButton;
    private Button updateButton;
    private Button shareButton;
    private Button keyButton;
    private Button prodButton;
    private Button stageButton;
    private Button devButton;
    private Button qaButton;
    private Button locationButton;
    private CheckBox indoorCheckbox;
    private TextView homeTextView;
    private GroupManager mGroupManager;
    private AuthorizeManager mAuthorizeManager;

    // emotional share
    private ShareUtility mEps;
    private LinearLayout emotionShareLayout;
    private LinearLayout emotionShareDummyLayout;
    private Animation translateInAnimation;
    private Animation translateOutAnimation;
    private ImageView weChatShareImageView;
    private ImageView weBoShareImageView;
    private ImageView captureImageView;
    private ImageView captureImageView2;
    private LinearLayout shareCancelLayout;

    private static String TAG = "AirTouchTest";
    private String city = "Shanghai";
    private String lang = "zh-chs";
    private char temperatureUnit = 'c';


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mGroupManager = new GroupManager();
        mAuthorizeManager = new AuthorizeManager();
        keyButton = (Button) findViewById(R.id.key);
        keyButton.setOnClickListener(keyOnClick);

//        homeTextView = (TextView) findViewById(R.id.home);
        enrollButton = (Button) findViewById(R.id.btnEnroll);
        enrollButton.setOnClickListener(enrollOnClick);
        loginButton = (Button) findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(loginOnClick);
        addDeviceButton = (Button) findViewById(R.id.btnDevice);
        addDeviceButton.setOnClickListener(deviceOnClick);
        thinkpageButton = (Button) findViewById(R.id.thinkPageBtn);
        thinkpageButton.setOnClickListener(thickPageOnClick);
        updateButton = (Button) findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(updateOnClick);

        indoorCheckbox = (CheckBox) findViewById(R.id.indoorCheckbox);
        prodButton = (Button) findViewById(R.id.prodEnv);
        stageButton = (Button) findViewById(R.id.stageEnv);
        devButton = (Button) findViewById(R.id.devEnv);
        qaButton = (Button) findViewById(R.id.qaEnv);
        prodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.isChangeEnv = true;
                AppConfig.urlEnv = AppConfig.PRODUCT_ENV;
                goToLoginActivity();
            }
        });
        stageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.isChangeEnv = true;
                AppConfig.urlEnv = AppConfig.STAGE_ENV;
                goToLoginActivity();
            }
        });
        devButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.isChangeEnv = true;
                AppConfig.urlEnv = AppConfig.DEV_ENV;
//                AppManager.setIsEnterPriseVersion(true);
                goToLoginActivity();
            }
        });
        qaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConfig.isChangeEnv = true;
                AppConfig.urlEnv = AppConfig.QA_ENV;
//                AppManager.setIsEnterPriseVersion(false);
                goToLoginActivity();
            }
        });
        indoorCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    HPlusConstants.MAX_PMVALUE_LOW = 5;
                } else {
                    HPlusConstants.MAX_PMVALUE_LOW = 75;
                }
                ArrayList<Integer> deviceIdList = UserDataOperator.getNeedOpenDevice(UserAllDataContainer.shareInstance().getUserLocationDataList());
                if (deviceIdList.size() > 0) {
                    Intent intent = new Intent();
                    intent.putIntegerArrayListExtra(HPlusConstants.DEVICE_ID_LIST, deviceIdList);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_zoomin, R.anim.activity_zoomout);
                }
            }
        });


        // delete device
        locationButton = (Button) findViewById(R.id.btnLocation);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> numbers = new ArrayList<>();
                numbers.add("15821716063");

                mAuthorizeManager.checkAuthUser(numbers);

                mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                    @Override
                    public void onSuccess(ResponseResult responseResult) {

                    }
                });
                mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                    @Override
                    public void onError(ResponseResult responseResult, int id) {

                    }
                });

            }
        });


        // umeng share
        shareButton = (Button) findViewById(R.id.btnShare);
        shareButton.setOnClickListener(shareOnClick);
        weChatShareImageView = (ImageView) findViewById(R.id.wechat_share_btn_dummy);
        weChatShareImageView.setOnClickListener(weChatShareOnClick);
        weBoShareImageView = (ImageView) findViewById(R.id.webo_share_btn_dummy);
        weBoShareImageView.setOnClickListener(weboShareOnClick);
        shareCancelLayout = (LinearLayout) findViewById(R.id.share_cancel_layout_dummy);
        shareCancelLayout.setOnClickListener(shareCancelOnClick);
        emotionShareLayout = (LinearLayout) findViewById(R.id.emotion_share_layout);
        emotionShareLayout.setVisibility(View.INVISIBLE);
        translateInAnimation = AnimationUtils.loadAnimation(TestActivity.this, R.anim.share_translate_in);
        translateOutAnimation = AnimationUtils.loadAnimation(TestActivity.this, R.anim.share_translate_out);
        translateInAnimation.setAnimationListener(new translateInAnimationListener());
        emotionShareDummyLayout = (LinearLayout) findViewById(R.id.emotion_share_layout_dummy);
        emotionShareDummyLayout.setVisibility(View.INVISIBLE);

        // Umeng share
        mEps = ShareUtility.getInstance(TestActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isHasNullPoint()) {
            initDevice();
        }

    }

    private void errorHandle(ResponseResult responseResult) {
        if (responseResult.getExeptionMsg() != null || !responseResult.getExeptionMsg().equals("")) {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "Exception：" + responseResult.getExeptionMsg());
        }
    }


    View.OnClickListener enrollOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("isEnrollDemo", true);
            intent.setClass(TestActivity.this, ApEnrollChoiceActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Create Group
     */
    public View.OnClickListener loginOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAuthorizeManager.getAuthUnreadMessage();

            mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                @Override
                public void onSuccess(ResponseResult responseResult) {

                }
            });

            mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                @Override
                public void onError(ResponseResult responseResult, int id) {
                    int c = 0;
                }
            });

        }
    };

    View.OnClickListener shareOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            showToast(mTelephonyMgr.getSubscriberId());

            mAuthorizeManager.getAuthDevices();
            mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                @Override
                public void onSuccess(ResponseResult responseResult) {

                }
            });
            mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                @Override
                public void onError(ResponseResult responseResult, int id) {

                }
            });

//            emotionShareLayout.startAnimation(translateInAnimation);
        }
    };

    View.OnClickListener weboShareOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            mEps.umengShare(SHARE_MEDIA.SINA);

        }
    };

    View.OnClickListener weChatShareOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            mEps.umengShare(SHARE_MEDIA.WEIXIN_CIRCLE);


        }
    };

    View.OnClickListener shareCancelOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            emotionShareDummyLayout.setVisibility(View.INVISIBLE);
            emotionShareLayout.startAnimation(translateOutAnimation);
        }
    };


    /**
     * Group Scenario
     */
    View.OnClickListener updateOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int invitationId = 108;
            int actionId = 1;
            List<String> numbers = new ArrayList<>();
            numbers.add("15821716063");
            mAuthorizeManager.handleInvitation(invitationId, actionId);
            mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                @Override
                public void onSuccess(ResponseResult responseResult) {

                }
            });
            mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                @Override
                public void onError(ResponseResult responseResult, int id) {

                }
            });
        }

    };

    View.OnClickListener deviceOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int deviceId = 28500;
            List<String> numbers = new ArrayList<>();
            numbers.add("15800349135");
            mAuthorizeManager.removeAuthDevice(deviceId);

            mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                @Override
                public void onSuccess(ResponseResult responseResult) {

                }
            });
            mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                @Override
                public void onError(ResponseResult responseResult, int id) {

                }
            });

        }
    };

    /**
     * Delete Group
     */
    View.OnClickListener keyOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int deviceId = 28500;
            int role = 100;
            List<String> numbers = new ArrayList<>();
            numbers.add("15800349135");

            mAuthorizeManager.grantAuthToDevice(deviceId, role, numbers);

            mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                @Override
                public void onSuccess(ResponseResult responseResult) {

                }
            });
            mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                @Override
                public void onError(ResponseResult responseResult, int id) {

                }
            });

        }
    };


    /**
     * Change group name
     */
    View.OnClickListener thickPageOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pageIndex = 0;
            int pageSize = 10;
            mAuthorizeManager.getInvitations(pageIndex, pageSize, 0);
            mAuthorizeManager.setSuccessCallback(new AuthorizeManager.SuccessCallback() {
                @Override
                public void onSuccess(ResponseResult responseResult) {

                }
            });
            mAuthorizeManager.setErrorCallback(new AuthorizeManager.ErrorCallback() {
                @Override
                public void onError(ResponseResult responseResult, int id) {

                }
            });

        }

    };

    final IReceiveResponse thickPageResponse = new IReceiveResponse() {

        @Override
        public void onReceive(HTTPRequestResponse httpRequestResponse) {

            if (httpRequestResponse.getStatusCode() == StatusCode.OK) {

                switch (httpRequestResponse.getRequestID()) {
                    case ALL_DATA:
                        if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                            WeatherData weatherData = new Gson().fromJson(httpRequestResponse.getData(),
                                    WeatherData.class);

                            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "all temp:"
                                    + weatherData.getWeather().get(0).getNow().getTemperature());
                            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "all pm2.5:"
                                    + weatherData.getWeather().get(0).getNow().getAirQuality().getAirQualityIndex().getPm25());

                            RequestID requestID = RequestID.NOW_DATA;
                            ThinkPageClient.sharedInstance().getWeatherData(city, lang, temperatureUnit, requestID, thickPageResponse);
                        }
                        break;

                    case NOW_DATA:
                        if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                            WeatherData weatherData = new Gson().fromJson(httpRequestResponse.getData(),
                                    WeatherData.class);

                            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "now temp:"
                                    + weatherData.getWeather().get(0).getNow().getTemperature());

                            RequestID requestID = RequestID.AIR_DATA;
                            ThinkPageClient.sharedInstance().getWeatherData(city, lang, temperatureUnit, requestID, thickPageResponse);
                        }
                        break;

                    case AIR_DATA:
                        if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                            WeatherData weatherData = new Gson().fromJson(httpRequestResponse.getData(),
                                    WeatherData.class);

                            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "air pm2.5:"
                                    + weatherData.getWeather().get(0).getNow().getAirQuality()
                                    .getAirQualityIndex().getPm25());
                        }
                        break;

                    default:
                        break;
                }

            } else {
                if (httpRequestResponse.getException() != null) {
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "Exeption："
                            + httpRequestResponse.getException().toString());
                }
            }
        }
    };

    private Boolean isHasNullPoint() {
//        if (UserAllDataContainer.getInstance().getCurrentHome() == null)
//            return true;

//        if (CurrentUserAccountInfo.getInstance().getCurrentHome().getCurrentDevice().getHomeDevicePm25() == null)
//            return true;
//
//        if (CurrentUserAccountInfo.getInstance().getCurrentHome().getCurrentDevice().getDeviceInfo() == null)
//            return true;

        return false;
    }

    private void initDevice() {
//        UserLocation currentHome = CurrentUserAccountInfo.getInstance().getCurrentHome();
//        int deviceNumber = currentHome.getDeviceInfo().size();
//        if (deviceNumber > 0) {
//            ArrayList<HomeDevice> homeDevices = new ArrayList<>();
//            ArrayList<HomeDevicePM25> homeDevicesPM25 = currentHome.getHomeDevicesPM25();
//            ArrayList<DeviceInfo> deviceInfos = currentHome.getDeviceInfo();
//            if (homeDevicesPM25.size() > 0) {
//                for (int i = 0; i < deviceNumber; i++) {
//                    HomeDevice homeDevice = new HomeDevice();
//                    homeDevice.setHomeDevicePm25(homeDevicesPM25.get(i));
//                    homeDevice.setDeviceInfo(deviceInfos.get(i));
//                    homeDevices.add(homeDevice);
//                }
//                currentHome.setHomeDevices(homeDevices);
//            }
//        }
    }

    private void showDevice() {
//        String home = CurrentUserAccountInfo.getInstance().getCurrentHome().getName();
//        ArrayList<HomeDevicePM25> homeDevicesPM25
//                = CurrentUserAccountInfo.getInstance().getCurrentHome().getHomeDevicesPM25();
//        for (int i = 0; i < homeDevicesPM25.size(); i++) {
//            String mode = homeDevicesPM25.get(i).getAirCleanerFanModeSwitch();
//            String speed = homeDevicesPM25.get(i).getFanSpeedStatus();
//            int id = homeDevicesPM25.get(i).getDeviceID();
//            int pm25 = homeDevicesPM25.get(i).getPM25Value();
//
//            LogUtil.log(LogUtil.LogLevel.INFO, TAG, "home:" + home + "  deviceId:" + id
//                    + "  pm2.5:" + pm25 + "  mode:" + mode + " speed:" + speed);
//            homeTextView.setText("home:" + home + "  deviceId:" + id
//                    + "  pm2.5:" + pm25 + "  mode:" + mode + " speed:" + speed);
//        }
    }


    /**
     * Animation helper
     */
    private class translateInAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationEnd(Animation animation) {
            emotionShareDummyLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        mEps.addSinaCallback(requestCode, resultCode, data);

    }


    private void deleteLocation(int locationId) {
        IActivityReceive swapLocationResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                if (responseResult.isResult()) {
                    switch (responseResult.getRequestId()) {
                        case DELETE_LOCATION:
                            if (responseResult.getResponseCode() == StatusCode.OK)
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location succeed!");
                            else if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST)
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location bad request!");
                            else if (responseResult.getResponseCode() == StatusCode.NOT_FOUND)
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location not found!");
                            else
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location fail!");
                            break;
                    }
                } else {
                    errorHandle(responseResult);
                }
            }
        };

        DeleteLocationTask requestTask
                = new DeleteLocationTask(locationId, swapLocationResponse);
        requestTask.execute();


    }

    private void swapLocation(int locationId) {
        IActivityReceive swapLocationResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                if (responseResult.isResult()) {
                    switch (responseResult.getRequestId()) {
                        case SWAP_LOCATION:
                            if (responseResult.getResponseCode() == StatusCode.OK)
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location succeed!");
                            else if (responseResult.getResponseCode() == StatusCode.BAD_REQUEST)
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location bad request!");
                            else if (responseResult.getResponseCode() == StatusCode.NOT_FOUND)
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location not found!");
                            else
                                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "swap location fail!");
                            break;
                    }
                } else {
                    errorHandle(responseResult);
                }
            }
        };

        SwapLocationRequest swapLocationRequest = new SwapLocationRequest();
        swapLocationRequest.setName("jin");
        SwapLocationNameTask requestTask
                = new SwapLocationNameTask(locationId, swapLocationRequest, swapLocationResponse);
        requestTask.execute();

    }

    private void goToLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(TestActivity.this, UserLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }
}
