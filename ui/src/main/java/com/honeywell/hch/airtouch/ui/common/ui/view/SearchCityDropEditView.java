package com.honeywell.hch.airtouch.ui.common.ui.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.honeywell.hch.airtouch.library.util.DensityUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.database.manager.CityChinaDBService;
import com.honeywell.hch.airtouch.plateform.database.manager.CityIndiaDBService;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.homemanage.ui.adapter.AddHomeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h127856 on 16/10/12.
 */
public class SearchCityDropEditView extends RelativeLayout {

    private Context mContext;

    private final int ITEMSIZE = 3;
    private final int ITEMHEIGHT = 150;

    private CityChinaDBService mCityChinaDBService;
    private CityIndiaDBService mCityIndiaDBService;
    protected ArrayList<City> mCitiesList = new ArrayList<>();
    private InputMethodManager mInputMethodManager;
    private ListView mAddHomeListView;
    private HPlusEditText mSearchCityEditText;
    private AddHomeAdapter<DropTextModel> homeSpinnerTypeAdapter;

    private Activity mParentActivity;
    private IAfterSearchEditTextChange mAfterAction;

    public SearchCityDropEditView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public SearchCityDropEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();

    }

    public SearchCityDropEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    /**
     * 在findbyId之后一定要先调用这个方法，进行参数初始化
     * @param parentActivity
     * @param afterSearchEditTextChange
     */
    public void initActivityParams(Activity parentActivity, IAfterSearchEditTextChange afterSearchEditTextChange){
        mParentActivity = parentActivity;
        mAfterAction = afterSearchEditTextChange;
    }

    public HPlusEditText getSearchCityEditText(){
        return mSearchCityEditText;
    }

    private void initView(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.search_city_drop_edit_text, this);
        mSearchCityEditText = (HPlusEditText)findViewById(R.id.search_place_et);
        mAddHomeListView = (ListView)findViewById(R.id.home_city_listView);
        initData();
    }

    private void initData(){
        mCityChinaDBService = new CityChinaDBService(mContext);
        mCityIndiaDBService = new CityIndiaDBService(mContext);
        mInputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        mSearchCityEditText = (HPlusEditText) findViewById(R.id.search_place_et);
        mSearchCityEditText.setEditorHint(mContext.getString(R.string.home_manager_input_city));
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
                mAfterAction.AfterSearchEditTextChange();
            }
        });

    }

    private void updateCitiesListFromSearch() {
        String searchCityName = mSearchCityEditText.getEditorText().trim();
        if (!StringUtil.isEmpty(searchCityName)) {
            mCitiesList = getCitiesByKey(searchCityName, mCityChinaDBService, mCityIndiaDBService);
            if (null == mCitiesList)
                return;
        }
        setCityListViewHeight(mCitiesList.size());
        DropTextModel[] citylist = getCityArrays(mCitiesList);
        homeSpinnerTypeAdapter = new AddHomeAdapter<DropTextModel>(mContext, citylist);
        mAddHomeListView.setAdapter(homeSpinnerTypeAdapter);
        mAddHomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // show city name in the EditText
                mInputMethodManager.hideSoftInputFromWindow(mParentActivity.getCurrentFocus().getWindowToken(), 0);
                String cityName = homeSpinnerTypeAdapter.getItemValue(position);
                mSearchCityEditText.setEditorText(cityName);
                hideCityListView();
            }
        });
    }

    private ArrayList<City> getCitiesByKey(String searchCityName, CityChinaDBService mCityChinaDBService, CityIndiaDBService mCityIndiaDBService) {
        if (AppConfig.shareInstance().isIndiaAccount()) {
            return mCityIndiaDBService.getCitiesByKey(searchCityName);
        } else {
            return mCityChinaDBService.getCitiesByKey(searchCityName);
        }
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

    //将citylist 分装成droptext
    private DropTextModel[] getCityArrays(ArrayList<City> mCitiesList) {
        List<DropTextModel> dropTextModelsList = new ArrayList<>();
        for (City city : mCitiesList) {
            DropTextModel dropTextModel = new DropTextModel(city);
            dropTextModelsList.add(dropTextModel);
        }
        DropTextModel[] stringsArray = new DropTextModel[dropTextModelsList.size()];
        return dropTextModelsList.toArray(stringsArray);
    }

    private void hideCityListView() {
        mCitiesList.clear();
        homeSpinnerTypeAdapter.clearHomeData();
        mSearchCityEditText.clearFocus();
    }


    public interface IAfterSearchEditTextChange{
        void AfterSearchEditTextChange();
    }

}
