package com.honeywell.hch.airtouch.ui.main.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.AsyncTaskExecutorUtil;
import com.honeywell.hch.airtouch.library.http.model.IActivityReceive;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.plateform.appmanager.AppManager;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.task.GetConfigTask;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.ui.DashBoadConstant;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.splash.StartActivity;
import com.honeywell.hch.airtouch.ui.userinfo.ui.changepassword.ChangePasswordActivity;

/**
 * Created by Vincent on 4/8/16.
 */
public class ProfileActivity extends BaseActivity implements View.OnTouchListener {

    public static final String CHANGE_PASSWORD_SUCCESS = "change_password_success";
    private TextView mUserNameTv;
    private TextView mNumberTv;
    private Button mSignOutBtn;
    private TextView mProfileTitleTv;
    private final float ALPHA_100 = 1f;
    private final float ALPHA_70 = 0.7f;
    private TextView mAccountTv;
    private LinearLayout mChangePassWordLl;
    private ImageView mMobileIv;
    private TextView mCityTv;
    private CityChinaDBService mCityChinaDBService;
    private CityIndiaDBService mCityIndiaDBService;
    private LinearLayout mSelectCityLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initStatusBar();
        init();
        initData();
    }

    private void init() {
        mProfileTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mUserNameTv = (TextView) findViewById(R.id.me_user_name_tv);
        mNumberTv = (TextView) findViewById(R.id.me_mobile_number_tv);
        mSignOutBtn = (Button) findViewById(R.id.me_sign_out_button);
        mAccountTv = (TextView) findViewById(R.id.me_account_tv);
        mChangePassWordLl = (LinearLayout) findViewById(R.id.me_change_password_ll);
        mMobileIv = (ImageView) findViewById(R.id.me_mobile_number_iv);
        mCityTv = (TextView) findViewById(R.id.profile_city_tv);
        mSelectCityLl = (LinearLayout)findViewById(R.id.profile_location_ll);
        initDragDownManager(R.id.title_id);
    }

    private void initData() {
        mCityChinaDBService = new CityChinaDBService(this);
        mCityIndiaDBService = new CityIndiaDBService(this);
        mProfileTitleTv.setText(getString(R.string.me_profile));
        mUserNameTv.setText(UserInfoSharePreference.getNickName());
        mNumberTv.setText(UserInfoSharePreference.getMobilePhone());
        mSignOutBtn.setOnTouchListener(this);
        if (AppManager.isEnterPriseVersion()) {
            mAccountTv.setText(getString(R.string.me_user_id));
            mChangePassWordLl.setVisibility(View.GONE);
            mMobileIv.setImageResource(R.drawable.business_account);
        }
        if(AppManager.getInstance().getLocalProtocol().canShowSelectCity()){
            mSelectCityLl.setVisibility(View.VISIBLE);
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.me_change_password_ll) {
            Intent intent = new Intent(mContext, ChangePasswordActivity.class);
            startActivityForResult(intent, DashBoadConstant.CHANGE_PASSWORD_CODE);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.profile_location_ll) {
            startIntent(SelectCityActivity.class);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void logout() {
        AppManager.getInstance().setIsEnterPriseVersion(false);
        Intent logOutIntent = new Intent(HPlusConstants.LOGOUT_ACTION);
        logOutIntent.putExtra(HPlusConstants.NEED_UPDATE, false);
        AppManager.getInstance().getApplication().getApplicationContext().sendOrderedBroadcast(logOutIntent, null);
        Intent intent = new Intent();
        intent.putExtra(StartActivity.FROM_ANOTHER_ACTIVITY, true);
        intent.setClass(ProfileActivity.this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void testIntentStatus() {
        IActivityReceive mResponse = new IActivityReceive() {
            @Override
            public void onReceive(ResponseResult responseResult) {
                disMissDialog();
                switch (responseResult.getRequestId()) {
                    case GET_ALL_DEVICE_TYPE_CONFIG:
                        if (responseResult.isResult()) {
                            logout();
                        } else {
                            errorHandle(responseResult, getString(R.string.me_signout_fail));
                        }
                        break;

                }
            }
        };
        mDialog = LoadingProgressDialog.show(mContext, getString(R.string.me_signout_loading));
        GetConfigTask requestTask = new GetConfigTask(mResponse);
        AsyncTaskExecutorUtil.executeAsyncTask(requestTask);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.me_sign_out_button) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mSignOutBtn.setAlpha(ALPHA_70);
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                mSignOutBtn.setAlpha(ALPHA_100);

                if (isNetworkOff()) {
                    return false;
                }
                testIntentStatus();
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode) {
            case DashBoadConstant.CHANGE_PASSWORD_CODE:
                boolean isChangePassword = intent.getBooleanExtra(CHANGE_PASSWORD_SUCCESS, false);
                if (isChangePassword) {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.change_password_success), false);
                }
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCityName();
    }

    private void setCityName(){
        String cityCode = "";
        if (UserInfoSharePreference.getIsUsingGpsCityCode()) {
            cityCode = UserInfoSharePreference.getGpsCityCode();
        } else {
            cityCode = UserInfoSharePreference.getManualCityCode();
        }
        City city = null;
        if (AppConfig.shareInstance().isIndiaAccount()) {
            city = mCityIndiaDBService.getCityByCode(cityCode);

        } else {
            city = mCityChinaDBService.getCityByCode(cityCode);
        }
        String language = AppConfig.shareInstance().getLanguage();
        if (HPlusConstants.CHINA_LANGUAGE_CODE.equals(language)) {
            mCityTv.setText(city.getNameZh());
        } else {
            mCityTv.setText(city.getNameEn());
        }
    }
}
