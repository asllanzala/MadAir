package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.devices.common.DeviceType;
import com.honeywell.hch.airtouch.plateform.http.model.enroll.EnrollDeviceTypeModel;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.models.EnrollScanEntity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.beforeplay.EnrollDeviceInfoActivity;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 7/3/17.
 */

public class ApEnrollModelChoiceActivity extends EnrollBaseActivity {

    private TextView mTitleTextView;

    private ListView mModelLv;
    private ModelListAdapter mModelListAdapter;
    private String mSelectedModel;
    private List<EnrollDeviceTypeModel> mEnrollDeviceTypeModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollmodelchoice);
        initStatusBar();
        initView();
        initData();
        initDragDownManager(R.id.root_view_id);
        initItemClickListener();
    }

    private void initView() {
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mModelLv = (ListView) findViewById(R.id.enroll_model_list);
    }

    private void initData() {
        List<String> modelList = parseModel();
        mTitleTextView.setText(R.string.enroll_choice_model_title);
        mModelListAdapter = new ModelListAdapter(this, modelList);
        mModelLv.setAdapter(mModelListAdapter);
    }

    private void initItemClickListener() {
        mModelLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mSelectedModel = mModelListAdapter.getItem(position);
                setDeviceType();
                startIntent(EnrollDeviceInfoActivity.class);
            }
        });
    }

    private List<String> parseModel() {
        mEnrollDeviceTypeModelList = EnrollScanEntity.getEntityInstance().getmEnrollDeviceTypeModelList();
        List<String> modelList = new ArrayList<>();
        for (EnrollDeviceTypeModel enrollDeviceTypeModel : mEnrollDeviceTypeModelList) {
            if (DeviceType.isAirTouchS(enrollDeviceTypeModel.getmType()) || DeviceType.isAirTouchSUpdate(enrollDeviceTypeModel.getmType())) {
                for (String model : enrollDeviceTypeModel.getmModels()) {

                    modelList.add(model);
                }
            }
        }
        return modelList;
    }

    private void setDeviceType() {
        for (EnrollDeviceTypeModel enrollDeviceTypeModel : mEnrollDeviceTypeModelList) {
            for (String model : enrollDeviceTypeModel.getmModels()) {
                if (model.equals(mSelectedModel)) {
                    EnrollScanEntity.getEntityInstance().setmDeviceType(enrollDeviceTypeModel.getmType());
                }

            }
        }
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            backIntent();
        }
    }

    class ModelListAdapter extends BaseAdapter {

        private Context mContext = null;
        private List<String> mModelNameList;

        public ModelListAdapter(Context ctx, List<String> modelNames) {
            mContext = ctx;
            mModelNameList = modelNames;
        }

        @Override
        public int getCount() {
            return mModelNameList == null ? 0 : mModelNameList.size();
        }

        @Override
        public String getItem(int position) {
            return mModelNameList == null ? null : mModelNameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.list_item_model_select, null);
            }
            TextView mModelNameTextView = ViewHolderUtil.get(convertView, R.id.list_item_model_name_tv);
            mModelNameTextView.setText(mModelNameList.get(position));

            return convertView;
        }
    }

}
