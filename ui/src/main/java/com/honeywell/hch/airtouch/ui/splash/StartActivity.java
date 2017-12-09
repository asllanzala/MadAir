package com.honeywell.hch.airtouch.ui.splash;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.util.SharePreferenceUtil;
import com.honeywell.hch.airtouch.library.util.StatusBarUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModel;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceModelSharedPreference;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirDeviceStatus;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.model.UserLocationData;
import com.honeywell.hch.airtouch.plateform.http.model.notification.PushMessageModel;
import com.honeywell.hch.airtouch.plateform.http.task.GetConfigTask;
import com.honeywell.hch.airtouch.plateform.location.manager.GpsUtil;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.storage.TryDemoSharePreference;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.plateform.update.UpdateManager;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.CloseActivityUtil;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.main.ui.common.MainActivity;
import com.honeywell.hch.airtouch.ui.trydemo.ui.TryDemoMainActivity;
import com.honeywell.hch.airtouch.ui.tutorial.controller.TutorialActivity;
import com.honeywell.hch.airtouch.plateform.http.manager.UserCacheDataManager;
import com.honeywell.hch.airtouch.ui.userinfo.ui.login.MobileGetSmsActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.login.UserLoginActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jin Qian on 2/26/2015.
 */
public class StartActivity extends BaseActivity {

    public static final String FROM_ANOTHER_ACTIVITY = "from_another_activity";
    public static final String FROM_START_ACTIVITY = "from_start_activity";
    private ImageView welcomeBackground;

    private LinearLayout mLoginLayout;
    private TextView mLoginTv;
    private Button mRegsiterBtn;
    public static boolean first;
    private TextView mTryOutTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
         /*
         * press home button to quit, after that, show main page if return back to app.
         */
        if (!isTaskRoot() && !getIntent().getBooleanExtra(FROM_ANOTHER_ACTIVITY, false)) {
            finish();
            return;
        }
        init();
        initListener();
        restorePreference();
        getFilterConfig();
        clearGroupControlPreference();
        initDataFromCache();
        initMadAir();
    }


//    /**
//     * 初始化定位，适用时需要适用
//     */
//    public static String initGPS(Context context) {
//        CityChinaDBService mCityChinaDBService = new CityChinaDBService(context);
//        CityIndiaDBService mCityIndiaDBService = new CityIndiaDBService(context);
//        GpsUtil gpsUtil = new GpsUtil(mCityChinaDBService, mCityIndiaDBService);
//        gpsUtil.initGps();
//        String CountryID = "";
//        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//        CountryID = manager.getSimCountryIso().toUpperCase();  //CN
//        Log.d("StartActivity", "CountryID--->>>" + CountryID);
//        Log.d("StartActivity", "cityCode: " + UserInfoSharePreference.getCountryCode());
//        return CountryID;
//    }

    private void init() {
        StatusBarUtil.changeStatusBarTextColor(findViewById(R.id.root_view_id), View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //登陆前 加载
        CloseActivityUtil.beforeLoginActivityList.add(this);
        mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
        mLoginTv = (TextView) findViewById(R.id.loginin_id);
        mRegsiterBtn = (Button) findViewById(R.id.register_id);
        mTryOutTv = (TextView) findViewById(R.id.loginin_try_id);
        mTryOutTv.setText(getSpanable(R.string.start_login_try, new int[]{R.string.spannable_string_try}));
        mLoginTv.setText(getSpanable(R.string.start_sign_in, new int[]{R.string.spannable_string_sign_in}));
        welcomeBackground = (ImageView) findViewById(R.id.welcome_background);
        if (AppConfig.isLauchendFirstTime) {
            AppConfig.isLauchendFirstTime = false;
            welcomeBackground.setBackgroundResource(R.drawable.start_bg);
        } else {
            welcomeBackground.setBackgroundResource(R.drawable.start_back_bg);
        }
        mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE, this);
    }

    private void initListener() {
        mLoginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent(UserLoginActivity.class);

            }
        });
        mRegsiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGetSmsActivity();
            }
        });
        mTryOutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTryOutAllDevice();
            }
        });
    }

    private void restorePreference() {
        // Restore preferences
        first = SharePreferenceUtil.getPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG,
                HPlusConstants.FIRST_RUN, true);
        if (first) {
            SharePreferenceUtil.setPrefBoolean(HPlusConstants.PREFERENCE_USER_CONFIG, HPlusConstants.FIRST_RUN, false);
            startIntent(TutorialActivity.class);
        }
    }

    private void getFilterConfig() {
        //get filter configer
        GetConfigTask requestTask = new GetConfigTask(null);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    private void clearGroupControlPreference() {
        // delete data in GroupControl
        SharePreferenceUtil.clearPreference(mContext, SharePreferenceUtil
                .getSharedPreferenceInstanceByName(HPlusConstants.PREFERENCE_GROUP_CONTROL_FLASH));
    }

    private void initDataFromCache() {
        if (UserInfoSharePreference.isUserAccountHasData()) {
            mLoginLayout.setVisibility(View.GONE);
            final Intent intent = dealWithIntent();
             /*
         * stay welcome page for a while
         */
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                        intent.setClass(StartActivity.this, MainActivity.class);
                        intent.putExtra(FROM_START_ACTIVITY, true);
                        gotoActivityWithIntent(intent, false);
                        finish();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            mLoginLayout.setVisibility(View.VISIBLE);
            UpdateManager.getInstance().checkUpgrade();
        }
    }

    private void goToGetSmsActivity() {
        Intent i = new Intent();
        i.putExtra(HPlusConstants.NEW_USER, true);
        i.putExtra(FROM_START_ACTIVITY, true);
        i.setClass(this, MobileGetSmsActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

    private Intent dealWithIntent() {
        final boolean isNeedUpdate = getIntent().getBooleanExtra(PushMessageModel.PUSHPARAMETERUPDATE, false);
        final PushMessageModel mPushMessageModel = (PushMessageModel) getIntent().getSerializableExtra(PushMessageModel.PUSHPARAMETER);
        if (!isNeedUpdate && mPushMessageModel != null) {
            UserAllDataContainer.shareInstance().setmPushMessageModel(mPushMessageModel);
        }
        Intent intent = new Intent();
        intent.putExtra(PushMessageModel.PUSHPARAMETER, (Serializable) mPushMessageModel);
        intent.putExtra(PushMessageModel.PUSHPARAMETERUPDATE, isNeedUpdate);
        return intent;
    }

    protected void startIntent(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtra(FROM_START_ACTIVITY, true);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

    private void goToTryOutAllDevice() {
        if (TryDemoSharePreference.isShownTryDemoEntrance()) {
            mAlertDialog = MessageBox.createSimpleDialog(this, null, getString(R.string.try_demo_entrance),
                    getString(R.string.ok), rightButton);
        } else {
            goToTryDemoActivity();
        }
    }

    private void goToTryDemoActivity() {
        Intent i = new Intent();
        i.setClass(StartActivity.this, TryDemoMainActivity.class);
        i.putExtra(FROM_START_ACTIVITY, true);
        startActivity(i);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        finish();
    }

    MessageBox.MyOnClick rightButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            TryDemoSharePreference.saveTryDemoEntrance(false);
            goToTryDemoActivity();
        }
    };

    @Override
    public void onPermissionGranted(int permissionCode) {
        startGpsService(GpsUtil.FROM_START_ACTIVITY);
    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permission, permissionCode);
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE:

                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startGpsService(GpsUtil.FROM_START_ACTIVITY);
                }
                break;
        }
    }



    private void initMadAir() {
        for (MadAirDeviceModel device : MadAirDeviceModelSharedPreference.getDeviceList()) {
            if (device != null)
                MadAirDeviceModelSharedPreference.saveStatus(device.getMacAddress(), MadAirDeviceStatus.DISCONNECT);
        }
    }
}
