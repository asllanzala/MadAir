package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.ui.adapter.CitiesAdapter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.SideBar;

import java.util.ArrayList;


public class EditGPSActivity extends EnrollBaseActivity implements View.OnClickListener {

    public static final String TAG = "AirTouchEditGPS";

    private Button confirmButton = null;

    private ListView cityListView = null;
    private EditText searchEditText;
    private TextView gpsAddressTextView = null;
    private SideBar citiesSidebar = null;

    private InputMethodManager imm = null;

    protected ArrayList<City> mCitiesList = new ArrayList<>();

    private CitiesAdapter citiesAdapter = null;
    private CityChinaDBService mCityChinaDBService = null;
    private CityIndiaDBService mCityIndiaDBService = null;
    private City currentGPSCity = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gps);
        initStatusBar();
        dealWithIntent();
        initView();
        initListener();
        initAdapter();
        initItemCallBack();
        initDragDownManager(R.id.root_view_id);
    }

    private void dealWithIntent() {
        Intent intent = getIntent();
        currentGPSCity = (City) intent.getSerializableExtra("currentGPS");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initListener() {
        View.OnTouchListener touchListener = new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        };
        cityListView.setOnTouchListener(touchListener);
        OnScrollListener scrollListener = new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        };
        cityListView.setOnScrollListener(scrollListener);
        confirmButton.setOnClickListener(this);
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        };
        searchEditText.setOnEditorActionListener(editorActionListener);
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateCitiesListFromSearch(true);
            }
        });
    }

    private void initAdapter(){
        mCityChinaDBService = new CityChinaDBService(this);
        mCityIndiaDBService = new CityIndiaDBService(this);
        mCitiesList = getAllCities();
        citiesAdapter = new CitiesAdapter(this, false);
        if (currentGPSCity != null) {
            citiesAdapter.setCurrentCityKey(currentGPSCity.getNameEn() + currentGPSCity.getCode());
        }
        refreshList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    private void updateCitiesListFromSearch(boolean hideSoftInput) {
        String searchCityName = searchEditText.getText().toString().trim();
        if (!StringUtil.isEmpty(searchCityName)) {
            mCitiesList = getCitiesByKey(searchCityName);
            citiesSidebar.setVisibility(View.GONE);
            if (null == mCitiesList)
                return;
        } else {
            mCitiesList = getAllCities();
            citiesSidebar.setVisibility(View.VISIBLE);
        }
        citiesAdapter = new CitiesAdapter(this, !StringUtil.isEmpty(searchCityName));
        refreshList();
        initItemCallBack();
    }

    private void refreshList() {
        citiesAdapter.setData(mCitiesList);
        citiesAdapter.setPreferenceView(confirmButton);
        cityListView.setAdapter(citiesAdapter);
    }

    public void initView() {
        gpsAddressTextView = (TextView) findViewById(R.id.gps_address);
        if (currentGPSCity != null) {
            gpsAddressTextView.setText(AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH) ?
                    currentGPSCity.getNameZh() : currentGPSCity.getNameEn());
        }
        citiesSidebar = (SideBar) findViewById(R.id.sidebar);
        confirmButton = (Button) findViewById(R.id.ok_btn);
        confirmButton.setOnClickListener(this);
        cityListView = (ListView) findViewById(R.id.city_listView);
        citiesSidebar.setListView(cityListView);
        searchEditText = (EditText) findViewById(R.id.search_editText);
        ((TextView) findViewById(R.id.title_textview_id)).setText(getString(R.string.select_location));
    }

    public void doClick(View v) {
        backIntent();
    }

    public ArrayList<City> getAllCities() {
        if (AppConfig.shareInstance().isIndiaAccount()) {
            return mCityIndiaDBService.findAllCities();
        } else {
            return mCityChinaDBService.findAllCities();
        }
    }

    public ArrayList<City> getCitiesByKey(String searchCityName) {
        if (AppConfig.shareInstance().isIndiaAccount()) {
            return mCityIndiaDBService.getCitiesByKey(searchCityName);
        } else {
            return mCityChinaDBService.getCitiesByKey(searchCityName);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ok_btn) {
            City city = citiesAdapter.getSelectedCity();
            Intent intent = new Intent();
            intent.putExtra("city", city);
            setResult(RESULT_OK, intent);
            backIntent();
        }
    }

    private void initItemCallBack() {
        // click image icon to remove home
        citiesAdapter.setSelectedCityCallBack(new CitiesAdapter.SelectedCityCallBack() {
            @Override
            public void callback(City city) {
                gpsAddressTextView.setText(city.getNameEn());
                gpsAddressTextView.setText(AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH) ?
                        city.getNameZh() : city.getNameEn());
            }
        });
    }

}

