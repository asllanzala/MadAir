package com.honeywell.hch.airtouch.ui.homemanage.ui.controller;

import android.content.Context;
import android.content.Intent;
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

import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.HPlusConstants;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.LoadingProgressDialog;
import com.honeywell.hch.airtouch.ui.homemanage.ui.adapter.AddHomeAdapter;

import java.util.ArrayList;

/**
 * Created by Vincent on 13/7/16.
 */
public class UserAddHomeActivity extends HomeManagementBaseActivity {

    protected String TAG = "UserAddHomeActivity";
    private CityChinaDBService mCityChinaDBService;
    private CityIndiaDBService mCityIndiaDBService;
    protected ArrayList<City> mCitiesList = new ArrayList<>();
    private InputMethodManager mInputMethodManager;
    private ListView mAddHomeListView;
    private HPlusEditText mSearchCityEditText;
    private HPlusEditText mNameHomeEditText;
    private Button addHomeButton;
    private int mNewLocationId;
    private AddHomeAdapter<DropTextModel> homeSpinnerTypeAdapter;
    private final int ITEMSIZE = 3;
    private final int ITEMHEIGHT = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location_new);
        initStatusBar();
        setupUI(findViewById(R.id.root_view_id));
        initView();
        initEditText();
        initTitleView();
        initHomeManagerUiManager();
        initData();
        decideButtonShowOrNot();
    }

    private void initView() {
        initDragDownManager(R.id.root_view_id);
        mCityChinaDBService = new CityChinaDBService(this);
        mCityIndiaDBService = new CityIndiaDBService(this);
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddHomeListView = (ListView) findViewById(R.id.home_city_listView);
        addHomeButton = (Button) findViewById(R.id.add_home_button_confirm);
    }

    private void initData() {
        mTitleTextview.setText(getString(R.string.home_manager_add_home));
        mEndTextTip.setVisibility(View.GONE);
    }

    private void initEditText() {
        mSearchCityEditText = (HPlusEditText) findViewById(R.id.search_place_et);
        mSearchCityEditText.setEditorHint(getString(R.string.home_manager_input_city));
        mSearchCityEditText.setAddHomeSearchImage();
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
                updateCitiesListFromSearch();
                decideButtonShowOrNot();
            }
        });

        mNameHomeEditText = (HPlusEditText) findViewById(R.id.name_place_et);
        mNameHomeEditText.showImageButton(false);
        mNameHomeEditText.setEditorHint(getString(R.string.my_home));
        mNameHomeEditText.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StringUtil.maxCharacterFilter(mNameHomeEditText.getEditText());
                StringUtil.addOrEditHomeFilter(mNameHomeEditText.getEditText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                decideButtonShowOrNot();
            }
        });
    }

    private void decideButtonShowOrNot() {
        if (!StringUtil.isEmpty(mNameHomeEditText.getEditorText()) &&
                (!StringUtil.isEmpty(mSearchCityEditText.getEditorText()))) {
            enableConnectButton();
        } else {
            disableConnectButton();
        }
    }

    protected void dealSuccessCallBack(ResponseResult responseResult) {
        super.dealSuccessCallBack(responseResult);
        switch (responseResult.getRequestId()) {
            case ADD_LOCATION:
                mNewLocationId = responseResult.getResponseData()
                        .getInt(HPlusConstants.LOCATION_ID_BUNDLE_KEY);

                disMissDialog();
                setResult(ADD_HOME_RESULT);
                backIntent();
                break;
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.add_home_button_confirm) {
            City mSelectedCity = null;
            if (AppConfig.shareInstance().isIndiaAccount()) {
                mSelectedCity = mCityIndiaDBService.getCityByName(mSearchCityEditText.getEditorText());
            } else {
                mSelectedCity = mCityChinaDBService.getCityByName(mSearchCityEditText.getEditorText());
            }
            if (!mHomeManagerUiManager.isCityCanFound(mSearchCityEditText.getEditorText(), mCityChinaDBService, mCityIndiaDBService)) {
                mDropDownAnimationManager.showDragDownLayout(getString(R.string.city_not_found), true);
                return;
            }
            if (mHomeManagerUiManager.isSameName(mSelectedCity.getCode(), mNameHomeEditText.getEditorText())) {
                mDropDownAnimationManager.showDragDownLayout(getString(R.string.same_home), true);
            } else {
                mDialog = LoadingProgressDialog.show(mContext, getString(R.string.adding_home));
                mHomeManagerUiManager.processAddHome(mSelectedCity.getCode(), mNameHomeEditText.getEditorText());
            }
        } else if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
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
        addHomeButton.setClickable(false);
        addHomeButton.setEnabled(false);
    }

    private void enableConnectButton() {
        addHomeButton.setClickable(true);
        addHomeButton.setEnabled(true);
    }

    private void hideCityListView() {
        mCitiesList.clear();
        homeSpinnerTypeAdapter.clearHomeData();
        mSearchCityEditText.clearFocus();
    }

    private void updateCitiesListFromSearch() {
        String searchCityName = mSearchCityEditText.getEditorText().trim();
        if (!StringUtil.isEmpty(searchCityName)) {
            mCitiesList = mHomeManagerUiManager.getCitiesByKey(searchCityName, mCityChinaDBService, mCityIndiaDBService);
            if (null == mCitiesList)
                return;
        }
        setCityListViewHeight(mCitiesList.size());
        DropTextModel[] citylist = mHomeManagerUiManager.getCityArrays(mCitiesList);
        homeSpinnerTypeAdapter = new AddHomeAdapter<DropTextModel>(this, citylist);
        mAddHomeListView.setAdapter(homeSpinnerTypeAdapter);
        mAddHomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show city name in the EditText
                mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                String cityName = homeSpinnerTypeAdapter.getItemValue(position);
                mSearchCityEditText.setEditorText(cityName);
                hideCityListView();
            }
        });
    }

    private void setCityListViewHeight(int size) {
        ViewGroup.LayoutParams params = mAddHomeListView.getLayoutParams();
        if (size > ITEMSIZE) {
            params.height = DensityUtil.dip2px(ITEMHEIGHT); // 3.5 height of listView
        } else {
            params.height = ListView.LayoutParams.WRAP_CONTENT;
        }
        mAddHomeListView.setLayoutParams(params);
    }
}
