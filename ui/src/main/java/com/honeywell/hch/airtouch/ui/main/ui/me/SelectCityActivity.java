package com.honeywell.hch.airtouch.ui.main.ui.me;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.plateform.location.manager.GpsUtil;
import com.honeywell.hch.airtouch.plateform.permission.Permission;
import com.honeywell.hch.airtouch.plateform.storage.UserInfoSharePreference;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.controller.BaseActivity;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.MessageBox;
import com.honeywell.hch.airtouch.ui.common.ui.view.TypeTextView;
import com.honeywell.hch.airtouch.ui.homemanage.manager.HomeManagerUiManager;
import com.honeywell.hch.airtouch.ui.homemanage.ui.adapter.AddHomeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vincent on 4/8/16.
 */
public class SelectCityActivity extends BaseActivity {

    protected String TAG = "SelectCityActivity";
    private CityChinaDBService mCityChinaDBService;
    private CityIndiaDBService mCityIndiaDBService;
    protected ArrayList<City> mCitiesList = new ArrayList<>();
    private InputMethodManager mInputMethodManager;
    private ListView mCityListView;
    private HPlusEditText mSearchCityEditText;
    private Button mButton;
    private AddHomeAdapter<DropTextModel> homeSpinnerTypeAdapter;
    private final int ITEMSIZE = 3;
    private final int ITEMHEIGHT = 150;
    private TextView mTitleTv;
    private HomeManagerUiManager mHomeManagerUiManager;
    private BroadcastReceiver mGPSResultReceiver;
    private TypeTextView mLocatingRightTv;
    private TextView mLocatingLeftTv;
    private boolean isNeedShowCityList = true; // 自动定位这次不需要下拉列表
    private City mManualCity;
    private boolean isUsingGps = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initStatusBar();
        setupUI(findViewById(R.id.root_view_id));
        initView();
        initEditText();
        initData();
        registerGPSResultReceiver();
        disableConnectButton();
    }

    private void initView() {
        initDragDownManager(R.id.root_view_id);
        mCityChinaDBService = new CityChinaDBService(this);
        mCityIndiaDBService = new CityIndiaDBService(this);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mTitleTv = (TextView) findViewById(R.id.title_textview_id);
        mSearchCityEditText = (HPlusEditText) findViewById(R.id.search_place_et);
        mCityListView = (ListView) findViewById(R.id.home_city_listView);
        mButton = (Button) findViewById(R.id.select_city_btn);
        mLocatingRightTv = (TypeTextView) findViewById(R.id.loading_right_tv);
        mLocatingLeftTv = (TextView) findViewById(R.id.location_auto_locating_tv);
        mHomeManagerUiManager = new HomeManagerUiManager();
    }

    private void initData() {
        mTitleTv.setText(getString(R.string.location_city));
        disableConnectButton();
    }

    private void registerGPSResultReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HPlusConstants.GPS_RESULT);
        mGPSResultReceiver = new GPSResultReceiver();
        registerReceiver(mGPSResultReceiver, intentFilter);
    }

    private class GPSResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String gpsCity = UserInfoSharePreference.getGpsCityCode();
            int fromWhere = intent.getIntExtra(GpsUtil.GPS_FROME_WHERE,GpsUtil.FROM_ENROLL_PROCESS);
            LogUtil.log(LogUtil.LogLevel.DEBUG,TAG,"fromWhere: "+fromWhere);
            if(fromWhere == GpsUtil.FROM_SELECTED_CITY){
                if (!StringUtil.isEmpty(gpsCity)
                        && !AppConfig.LOCATION_FAIL.equals(gpsCity)) {
                    autoLocatingViewStatus(false, true);
                    enableConnectButton();
                    isNeedShowCityList = false;
                    mSearchCityEditText.getEditText().setText(UserDataOperator.getCityName(gpsCity));
                    isUsingGps = true;
                    if(UserInfoSharePreference.getIsUsingGpsCityCode()){
                        saveLocationData();
                    }
                } else {
                    mDropDownAnimationManager.showDragDownLayout(getString(R.string.location_locating_fail), true);
                    autoLocatingViewStatus(false, false);
                }
            }

        }
    }

    private void initEditText() {
        mSearchCityEditText.setEditorHint(getString(R.string.location_selected_city_hint));
        mSearchCityEditText.showImageButton(false);
        String currentName = mHomeManagerUiManager.getCurrentCity(mCityChinaDBService, mCityIndiaDBService);
        if (!StringUtil.isEmpty(currentName)) {
            mSearchCityEditText.setEditorText(currentName);
        }
        mSearchCityEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtil.isEmpty(mSearchCityEditText.getEditorText())) {
                    mCitiesList.clear();
                    if (homeSpinnerTypeAdapter != null) {
                        homeSpinnerTypeAdapter.notifyDataSetChanged();
                    }
                }
                if (isNeedShowCityList) {
                    updateCitiesListFromSearch();
                    decideButtonShowOrNot();
                } else {
                    isNeedShowCityList = true;
                }
            }
        });

    }

    private void decideButtonShowOrNot() {
        mManualCity = mHomeManagerUiManager.isCityCanFoundFromLocal(mSearchCityEditText.getEditorText(),
                mCityChinaDBService, mCityIndiaDBService);
        if (!StringUtil.isEmpty(mSearchCityEditText.getEditorText()) &&
                (mManualCity.getNameZh() != null || mManualCity.getNameEn() != null)) {
            enableConnectButton();
        } else {
            disableConnectButton();
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.select_city_btn) {
            if (!isUsingGps) {
                MessageBox.createTwoButtonDialog(this, null,
                        getString(R.string.location_locating_manual_remind), getString(R.string.cancel),
                        null, getString(R.string.ok), saveCityButton);
            } else {
                saveLocationData();
                backIntent();
            }
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        } else if (v.getId() == R.id.location_auto_locating_ll) {
            mHPlusPermission.checkAndRequestPermission(Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE, this);
        }
    }

    private void saveLocationData() {
        UserInfoSharePreference.saveIsUsingGpsCityCode(isUsingGps);
        UserAllDataContainer.shareInstance().getWeatherRefreshManager().addCityListRefresh(mHomeManagerUiManager.getCurrentHomeCityList(), true);
    }

    private MessageBox.MyOnClick saveCityButton = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            if (!isNetworkOff()) {
                UserInfoSharePreference.saveManualCityCode(mManualCity.getCode());
                saveLocationData();
                backIntent();
            }

        }
    };

    private void autoLocatingViewStatus(boolean isLoating, boolean isSuccess) {
        if (isLoating) {
            mLocatingRightTv.setVisibility(View.VISIBLE);
            mLocatingRightTv.startLoop();
            mLocatingLeftTv.setText(getText(R.string.location_locating));
            disableConnectButton();
            mSearchCityEditText.setClickable(false);
            disableComponent();
        } else {
            mLocatingRightTv.setVisibility(View.GONE);
            mLocatingRightTv.stop();
            mLocatingLeftTv.setText(getText(R.string.location_auto_locating));
            if (isSuccess) {
                enableConnectButton();
                enableComponent();
            }
        }
    }

    private void disableComponent() {
        mSearchCityEditText.getEditText().setEnabled(false);
        mSearchCityEditText.getImageView().setEnabled(false);
    }

    private void enableComponent() {
        mSearchCityEditText.getEditText().setEnabled(true);
        mSearchCityEditText.getImageView().setEnabled(true);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (homeSpinnerTypeAdapter != null) {
                hideCityListView();
                setCityListViewHeight(mCitiesList.size());
            }
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
        return super.onTouchEvent(event);
    }

    private void disableConnectButton() {
        mButton.setClickable(false);
        mButton.setEnabled(false);
    }

    private void enableConnectButton() {
        mButton.setClickable(true);
        mButton.setEnabled(true);
    }

    private void hideCityListView() {
        mCitiesList.clear();
        homeSpinnerTypeAdapter.clearHomeData();
        mSearchCityEditText.clearFocus();
    }

    private void updateCitiesListFromSearch() {
        isUsingGps = false;
        String searchCityName = mSearchCityEditText.getEditorText().trim();
        if (!StringUtil.isEmpty(searchCityName)) {
            mCitiesList = mHomeManagerUiManager.getCitiesByKey(searchCityName, mCityChinaDBService, mCityIndiaDBService);
            if (null == mCitiesList)
                return;
        }
        setCityListViewHeight(mCitiesList.size());
        DropTextModel[] citylist = mHomeManagerUiManager.getCityArrays(mCitiesList);
        homeSpinnerTypeAdapter = new AddHomeAdapter<DropTextModel>(this, citylist);
        mCityListView.setAdapter(homeSpinnerTypeAdapter);
        mCityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show city name in the EditText
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                String cityName = homeSpinnerTypeAdapter.getItemValue(position);
                mSearchCityEditText.getEditText().setText(cityName);
                hideCityListView();
            }
        });
    }

    private void setCityListViewHeight(int size) {
        ViewGroup.LayoutParams params = mCityListView.getLayoutParams();
        if (size > ITEMSIZE) {
            params.height = DensityUtil.dip2px(ITEMHEIGHT); // 3.5 height of listView
        } else {
            params.height = ListView.LayoutParams.WRAP_CONTENT;
        }
        mCityListView.setLayoutParams(params);
    }

    @Override
    public void onPermissionGranted(int permissionCode) {
        startGpsService(GpsUtil.FROM_SELECTED_CITY);
        autoLocatingViewStatus(true, true);
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "onPermissionGranted");
    }

    @Override
    public void onPermissionNotGranted(String[] permission, int permissionCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(permission, permissionCode);
            LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "onPermissionNotGranted");
        }
    }

    @Override
    public void onPermissionDenied(int permissionCode) {
        LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "onPermissionDenied");
        noLocatedPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case Permission.PermissionCodes.LOCATION_SERVICE_REQUEST_CODE:
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "onRequestPermissionsResult");
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startGpsService(GpsUtil.FROM_SELECTED_CITY);
                }
                break;
        }
    }

    private void noLocatedPermission() {
        MessageBox.createTwoButtonDialog((Activity) mContext, null, getString(R.string.no_located_permission_scantxt),
                getString(R.string.cancel), null, getString(R.string.go_to_setting), goToSetting);

    }

    protected MessageBox.MyOnClick goToSetting = new MessageBox.MyOnClick() {
        @Override
        public void onClick(View v) {
            goToPermissionSetting();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mGPSResultReceiver);
    }
}
