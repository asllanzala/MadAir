package com.honeywell.hch.airtouch.ui.enroll.ui.controller.afterplay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.http.model.ResponseResult;
import com.honeywell.hch.airtouch.library.util.NetWorkUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.config.AppConfig;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.plateform.database.model.City;
import com.honeywell.hch.airtouch.plateform.http.manager.UserAllDataContainer;
import com.honeywell.hch.airtouch.plateform.http.manager.UserDataOperator;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.common.manager.model.DropTextModel;
import com.honeywell.hch.airtouch.ui.common.ui.view.DropEditText;
import com.honeywell.hch.airtouch.ui.common.ui.view.HPlusEditText;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.interfacefile.ISelectedLocationView;
import com.honeywell.hch.airtouch.ui.enroll.manager.presenter.EnrollSelectedLocationPresenter;
import com.honeywell.hch.airtouch.ui.enroll.ui.adapter.HomeSpinnerAdapter;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap.EditGPSActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.view.EnrollLoadingButton;

/**
 * Created by h127856 on 16/10/12.
 */
public class EnrollSelectedLocationActivity extends EnrollBaseActivity implements ISelectedLocationView {

    public static final String FROM_BACK_ACTION = "from_back_action";

    private final int SELECT_LOCATION_REQUEST = 10;


    private TextView mNewHomeTextBtn;
    private TextView mExistHomeTextBtn;
    private RelativeLayout mAddHomeWaysLayout;

    private LinearLayout mNewHomeLayout;
    private TextView mSelectCityNameTextView;
    private HPlusEditText mHomeName;


    private EnrollLoadingButton mEnrollLoadingBtn;

    private LinearLayout mExistHomeLayout;
    private DropEditText mDropEditText;
    private HomeSpinnerAdapter<DropTextModel> homeSpinnerTypeAdapter;

    private EnrollSelectedLocationPresenter mEnrollSelectedLocationPresenter;

    private boolean isOnResume = false;

    //    //新建家的时候，区分新建家和已有家的选择情况。否则在出现两则都存在的情况，会使用了错误的值
    private City mSelectedGPSCity;
    //
//    //已经有家的时候，从下拉中选择的家
    private City mDropSelectCity;

    private boolean isNewHome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_selected_location);
        initStatusBar();
        setupUI(findViewById(R.id.root_view_id));
        initDragDownManager(R.id.root_view_id);
        initView();

        mEnrollSelectedLocationPresenter = new EnrollSelectedLocationPresenter(this);
        mEnrollSelectedLocationPresenter.initActivity(null);

        mEnrollLoadingBtn.setButtonStatus(!isNewHome, false);
    }


    @Override
    public void showSelectedHomeWayLayout() {
        isNewHome = false;
        mAddHomeWaysLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showOnlyNewHomeLayout() {
        isNewHome = true;
        mAddHomeWaysLayout.setVisibility(View.GONE);
        mNewHomeLayout.setVisibility(View.VISIBLE);
        mExistHomeLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isOnResume) {
            mDropEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEnrollSelectedLocationPresenter.initExistHomeDropContent();
                }
            }, 300);
            isOnResume = true;
        }
    }


    @Override
    public void initDropEditText(DropTextModel[] dropTextModels) {
        if (dropTextModels != null && dropTextModels.length > 0) {
            mDropEditText.getmEditText().setFocusable(false);
            mDropEditText.getmEditText().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mDropEditText.dealWithClickEvent();
                    return false;
                }
            });

            homeSpinnerTypeAdapter = new HomeSpinnerAdapter<>(this, dropTextModels);
            mDropEditText.setAdapter(homeSpinnerTypeAdapter, mDropEditText.getWidth());
            mDropEditText.getmDropImage().setVisibility(View.VISIBLE);

            mDropEditText.getmEditText().setText(dropTextModels[0].getTextViewString());
            mDropSelectCity = dropTextModels[0].getmCity();
            DIYInstallationState.setLocationId(dropTextModels[0].getLocationId());
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (mDropEditText != null && !mDropEditText.isTouchInThisDropEditText(event.getX(), event.getY())) {
            mDropEditText.closeDropPopWindow();
        }
        return super.dispatchTouchEvent(event);

    }

    @Override
    public void initSelctedCityText( City city) {
        mSelectedGPSCity = city;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getSerializableExtra("city") == null)
            return;

        City city = (City) data.getSerializableExtra("city");
        switch (requestCode) {
            case SELECT_LOCATION_REQUEST:
                mSelectedGPSCity = city;
                mSelectCityNameTextView.setText(AppConfig.shareInstance().getLanguage().equals(AppConfig.LANGUAGE_ZH) ?
                        city.getNameZh() : city.getNameEn());
                setBtnClickable();
                setViewGetFocus(mEnrollLoadingBtn.getmRootRl());
                break;
            default:
                break;
        }
    }

    @Override
    public void setAddHomeErrorView(ResponseResult responseResult, int errorMsgStrId) {
        setViewItemEnable(true);
        mEnrollLoadingBtn.setButtonStatus(true, false);
        mDropDownAnimationManager.showDragDownLayout(getString(errorMsgStrId), true);

    }

    @Override
    public void setAddHomeSuccessView() {
        String cityCode = isNewHome ? mSelectedGPSCity.getCode() : mDropSelectCity.getCode();
        DIYInstallationState.setCityCode(cityCode);

        //如果新增一个家，返回后默认选中这个家
        mEnrollSelectedLocationPresenter.initExistHomeDropContent();
        mDropEditText.getmEditText().setText(homeSpinnerTypeAdapter.getItemValue(homeSpinnerTypeAdapter.getCount() - 1).getTextViewString());
        mDropSelectCity = homeSpinnerTypeAdapter.getItemValue(homeSpinnerTypeAdapter.getCount() - 1).getmCity();
        DIYInstallationState.setLocationId(homeSpinnerTypeAdapter.getItemValue(homeSpinnerTypeAdapter.getCount() - 1).getLocationId());


        mEnrollLoadingBtn.setButtonStatus(true, false);
        setViewItemEnable(true);
        startToAddDeviceActivity();
    }

    /**
     * 需求：添加了一个家成功后，从下一个界面返回回来的时候，需要默认显示的是已存在的那个界面，并且新建家布局里的homeName要清空，但是家的地址不清空
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String cityName = mSelectCityNameTextView.getText().toString();
        boolean isFromBack = intent.getBooleanExtra(FROM_BACK_ACTION, false);
        if (isFromBack) {
            showExistHome();
            mDropSelectCity = homeSpinnerTypeAdapter.getItemValue(homeSpinnerTypeAdapter.getCount() - 1).getmCity();
            mHomeName.setEditorText("");
            if (!StringUtil.isEmpty(cityName)) {
                mSelectCityNameTextView.setText(cityName);
            }

            mEnrollSelectedLocationPresenter.initActivity(mSelectedGPSCity);
        }
    }

    private void initView() {

        super.initTitleView(false, getString(R.string.where_to_putdevice), EnrollConstants.TOTAL_FOUR_STEP, EnrollConstants.STEP_THREE,
                getString(R.string.home_selected_ways_id), false);

        initSelectWayLayout();

        initExistHomeLayout();

        initNewHomeLayout();

        initBottomBtn();

    }

    private void startToAddDeviceActivity() {
        startIntent(EnrollNameYourDeviceActivity.class);
    }

    private void setViewItemEnable(boolean isClickable) {
        mNewHomeTextBtn.setEnabled(isClickable);
        mNewHomeTextBtn.setClickable(isClickable);
        mExistHomeTextBtn.setEnabled(isClickable);
        mExistHomeTextBtn.setClickable(isClickable);

        mHomeName.getEditText().setEnabled(isClickable);
        mSelectCityNameTextView.setEnabled(isClickable);
        mSelectCityNameTextView.setClickable(isClickable);
    }


    private void setBtnClickable() {
        if (isNewHome) {
            if (!StringUtil.isEmpty(mHomeName.getEditorText()) && !StringUtil.isEmpty(mSelectCityNameTextView.getText().toString())) {
                mEnrollLoadingBtn.setButtonStatus(true, false);
            } else {
                mEnrollLoadingBtn.setButtonStatus(false, false);
            }
        }

    }


    private void initSelectWayLayout() {
        mNewHomeTextBtn = (TextView) findViewById(R.id.new_place_id);
        mNewHomeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewHome = true;
                setBtnClickable();
                mNewHomeTextBtn.setBackgroundResource(R.drawable.tab_blue);
                mExistHomeTextBtn.setBackgroundResource(R.drawable.tab_grey);
                mExistHomeLayout.setVisibility(View.GONE);
                mNewHomeLayout.setVisibility(View.VISIBLE);
            }
        });
        mExistHomeTextBtn = (TextView) findViewById(R.id.exist_place_id);
        mExistHomeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExistHome();
            }
        });
        mAddHomeWaysLayout = (RelativeLayout) findViewById(R.id.add_home_ways_layout);
        mNewHomeLayout = (LinearLayout) findViewById(R.id.new_home_layout);
    }

    private void initExistHomeLayout() {
        mExistHomeLayout = (LinearLayout) findViewById(R.id.exist_home_layout);
        mDropEditText = (DropEditText) findViewById(R.id.enroll_home_et);
        mDropEditText.setfterSelectResultInterface(new DropEditText.IAfterSelectResult() {
            @Override
            public void setSelectResult(String cityCode) {
            }

            @Override
            public void setSelectResult(DropTextModel dropTextModel) {
                //选择了一个已有的家时候，需要记录当前选择的locationId
                mDropSelectCity = dropTextModel.getmCity();
                DIYInstallationState.setLocationId(dropTextModel.getLocationId());

            }
        });
    }

    private void showExistHome() {
        isNewHome = false;
        mEnrollLoadingBtn.setButtonStatus(true, false);
        mExistHomeTextBtn.setBackgroundResource(R.drawable.tab_blue);
        mNewHomeTextBtn.setBackgroundResource(R.drawable.tab_grey);
        mExistHomeLayout.setVisibility(View.VISIBLE);
        mNewHomeLayout.setVisibility(View.GONE);
    }

    private void initNewHomeLayout() {
        mSelectCityNameTextView = (TextView) findViewById(R.id.city_name_tv);
        mSelectCityNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrollSelectedLocationActivity.this, EditGPSActivity.class);
                intent.putExtra("currentGPS", mSelectedGPSCity);
                startActivityForResult(intent, SELECT_LOCATION_REQUEST);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });

        mHomeName = (HPlusEditText) findViewById(R.id.home_name_tv);
        mHomeName.setEditorHint(mContext.getString(R.string.my_home));
        mHomeName.showImageButton(false);
        mHomeName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StringUtil.maxCharacterFilter(mHomeName.getEditText());
                StringUtil.addOrEditHomeFilter(mHomeName.getEditText());
            }

            @Override
            public void afterTextChanged(Editable s) {
                setBtnClickable();
            }
        });
        mHomeName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setBtnClickable();
            }
        });


    }

    private void initBottomBtn() {
        mEnrollLoadingBtn = (EnrollLoadingButton) findViewById(R.id.nextBtn_id);
        mEnrollLoadingBtn.initLoadingText(getResources().getString(R.string.samart_next_btn), getResources().getString(R.string.create_home));

        mEnrollLoadingBtn.getmRootRl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewHome) {
                    if (!NetWorkUtil.isNetworkAvailable(EnrollSelectedLocationActivity.this)) {
                        mDropDownAnimationManager.showDragDownLayout(getString(R.string.new_place_failed), true);
                        return;
                    }
                    if (UserDataOperator.isHaveSameHomeName(mHomeName.getEditorText(), mSelectedGPSCity.getCode())) {
                        mDropDownAnimationManager.showDragDownLayout(getString(R.string.same_home), true);
                        return;
                    }
                    setViewItemEnable(false);
                    mEnrollLoadingBtn.setButtonStatus(true, true);
                    mEnrollSelectedLocationPresenter.addHome(mHomeName.getEditorText(), mSelectedGPSCity.getCode());

                } else {
                    startToAddDeviceActivity();
                }
            }
        });

    }

    @Override
    public void addTheSameHome() {
        mDropDownAnimationManager.showDragDownLayout(getString(R.string.same_home), true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
