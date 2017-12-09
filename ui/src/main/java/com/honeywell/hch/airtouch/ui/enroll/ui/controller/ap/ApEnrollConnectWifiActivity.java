package com.honeywell.hch.airtouch.ui.enroll.ui.controller.ap;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IReceiveResponse;
import com.honeywell.hch.airtouch.library.http.model.RequestID;
import com.honeywell.hch.airtouch.library.http.model.StatusCode;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.library.util.ViewHolderUtil;
import com.honeywell.hch.airtouch.plateform.ap.EnrollmentClient;
import com.honeywell.hch.airtouch.plateform.ap.WAPIRouter;
import com.honeywell.hch.airtouch.plateform.config.DIYInstallationState;
import com.honeywell.hch.airtouch.ui.R;
import com.honeywell.hch.airtouch.ui.enroll.constant.EnrollConstants;
import com.honeywell.hch.airtouch.ui.enroll.ui.controller.common.EnrollBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Enrollment Step 3 - SmartPhone ask Air Touch to scan surrounding's SSID.
 * If there are too many SSID so that user can not find what he want,
 * press rescan to get SSID list again.
 */
public class ApEnrollConnectWifiActivity extends EnrollBaseActivity {

    private static final String TAG = "AirTouchEnrollConnectWifi";

    private Button rescanButton;

    private ArrayList<WAPIRouter> mWAPIRouters;
    private ListView mNetworkList;
    private NetworkListAdapter networkListAdapter;
    private TextView mContentTv;

    private RelativeLayout mScaningLayout;
    private ImageView mLoadingImageview;
    private AnimationDrawable mLoadingAnim;

    private TextView mTitleTextView;
    private TextView mTitleContentTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollconnectwifi);

        super.TAG = TAG;
        initStatusBar();
        init();
        initTitle();
        initDragDownManager(R.id.root_view_id);
    }

    private void init(){
        rescanButton = (Button) findViewById(R.id.rescan);
        rescanButton.setOnClickListener(rescanOnClick);
        mScaningLayout = (RelativeLayout) findViewById(R.id.rescan_prompt_view);
        mLoadingImageview = (ImageView) findViewById(R.id.loading_image);
        mNetworkList = (ListView) findViewById(R.id.wifi_list);
        mNetworkList.setOnItemClickListener(mScanResultClickListener);
        networkListAdapter = new NetworkListAdapter(ApEnrollConnectWifiActivity.this);
        mNetworkList.setAdapter(networkListAdapter);
        mContentTv = (TextView) findViewById(R.id.input_tip_id);
        mContentTv.setText(getString(R.string.enroll_page3_title_line1));
        mContentTv.setVisibility(View.GONE);
    }

    private void initTitle() {
        mTitleTextView = (TextView) findViewById(R.id.title_textview_id);
        mTitleTextView.setText(R.string.enroll_title_network_found);
        mTitleContentTv = (TextView) findViewById(R.id.input_tip_id);
        mTitleContentTv.setText(R.string.network_found_by_device_title_content);
        mTitleContentTv.setVisibility(View.VISIBLE);

        int totalStep = DIYInstallationState.getIsDeviceAlreadyEnrolled() ? EnrollConstants.TOTAL_TWO_STEP : EnrollConstants.TOTAL_FOUR_STEP;

        initEnrollStepView(true, true, totalStep, EnrollConstants.STEP_TWO);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadWAPIRouters();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }

    private class NetworkListAdapter extends ArrayAdapter<WAPIRouter> {

        public NetworkListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public WAPIRouter getItem(int position) {
            return mWAPIRouters.get(position);
        }

        @Override
        public int getCount() {
            if (mWAPIRouters == null) {
                return 0;
            }
            return mWAPIRouters.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            WAPIRouter wapiRouter = getItem(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_network,
                        parent, false);
            }
            TextView ssidTextView = ViewHolderUtil.get(convertView, R.id.list_item_network_text);
            ImageView lockImage = ViewHolderUtil.get(convertView, R.id.list_item_network_lock_image);
            ImageView wifiImage = ViewHolderUtil.get(convertView, R.id.list_item_network_wifi_image);

            lockImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.wifi_lock));
            wifiImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.wifi_signal3));
            if (wapiRouter.getSSID() != null) {
                ssidTextView.setText(wapiRouter.getSSID());
                if (getString(R.string.enroll_other).equals(wapiRouter.getSSID())) {
                    lockImage.setImageDrawable(null);
                    wifiImage.setImageDrawable(null);
                }
            }
            if (!wapiRouter.isLocked()) {
                lockImage.setImageDrawable(null);
            }

            return convertView;
        }

    }

    OnClickListener rescanOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mWAPIRouters != null) {
                mWAPIRouters.removeAll(mWAPIRouters);
                networkListAdapter.notifyDataSetChanged();
            }
            loadWAPIRouters();

        }

    };

    private void startRescanBtnAnimation(){
        Animation operatingAnim = AnimationUtils.loadAnimation(ApEnrollConnectWifiActivity.this, R.anim.enroll_rescan_rotate);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            rescanButton.startAnimation(operatingAnim);
        }
    }

    private OnItemClickListener mScanResultClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            WAPIRouter wapiRouter = (WAPIRouter) parent.getItemAtPosition(position);
            DIYInstallationState.setWAPIRouter(wapiRouter);
            startIntent(ApConnectDeviceToInternetActivity.class);
        }
    };

    private void loadWAPIRouters() {
        IReceiveResponse getWAPIRoutersResponse = new IReceiveResponse() {
            @Override
            public void onReceive(HTTPRequestResponse httpRequestResponse) {
                if (mWAPIRouters != null) {
                    mWAPIRouters.clear();
                } else {
                    mWAPIRouters = new ArrayList<>();
                }

                if (httpRequestResponse.getStatusCode() != StatusCode.OK) {

                    showRetryActionWithReconnectWifi(getResources().getString(R.string.not_connect_device_network));

                    WAPIRouter otherRouter = new WAPIRouter();
                    otherRouter.setSSID(getString(R.string.enroll_other));
                    mWAPIRouters.add(otherRouter);

                    onFinish();
                    return;
                }
                if (!StringUtil.isEmpty(httpRequestResponse.getData())) {
                    List<WAPIRouter> wapiRouters = null;
                    Type wapiRouterType = new TypeToken<List<WAPIRouter>>() {
                    }.getType();
                    try {
                        JSONObject responseJSON = new JSONObject(httpRequestResponse.getData());
                        if (responseJSON.has("Routers")) {
                            String response = responseJSON.getJSONArray("Routers").toString();
                            wapiRouters = new Gson().fromJson(response,
                                    wapiRouterType);
                        }
                        if (wapiRouters != null) {
                            HashMap<String, WAPIRouter> routerHashMap = new HashMap<>();
                            for (Iterator iterator = wapiRouters.iterator(); iterator.hasNext(); ) {
                                WAPIRouter element = (WAPIRouter) iterator.next();
                                routerHashMap.put(element.getSSID(), element);
                            }
                            Iterator it = routerHashMap.keySet().iterator();
                            while (it.hasNext()) {
                                String key = it.next().toString();
                                mWAPIRouters.add(routerHashMap.get(key));
                            }
                        }
                        Collections.sort(mWAPIRouters);

                        WAPIRouter otherRouter = new WAPIRouter();
                        otherRouter.setSSID(getString(R.string.enroll_other));
                        mWAPIRouters.add(otherRouter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                onFinish();
            }
        };

        //正在扫描的时候，不需要再次扫描，否则会出现多次弹框的情况
        if (mScaningLayout.getVisibility() != View.VISIBLE){
            EnrollmentClient.sharedInstance()
                    .getWAPIRouters(RequestID.GET_ROUTER, getWAPIRoutersResponse);
            disableRescanButton();

            mScaningLayout.setVisibility(View.VISIBLE);
            mLoadingAnim = (AnimationDrawable) mLoadingImageview.getBackground();
            mLoadingAnim.start();

            //start rotation
            startRescanBtnAnimation();
        }

    }

    protected void onFinish() {
        networkListAdapter.notifyDataSetChanged();
        dismissDialog();
        enableRescanButton();
    }

    private void dismissDialog() {
        if (mScaningLayout != null) {
            mScaningLayout.setVisibility(View.GONE);
            if (mLoadingAnim != null) {
                mLoadingAnim.stop();
            }
            rescanButton.clearAnimation();
        }
    }

    private void disableRescanButton() {
        rescanButton.setClickable(false);
        rescanButton.setTextColor(getResources().getColor(R.color.enroll_light_grey));
    }

    private void enableRescanButton() {
//        rescanPromptView.setVisibility(View.INVISIBLE);
        rescanButton.setClickable(true);
        rescanButton.setTextColor(getResources().getColor(R.color.enroll_text_grey));
    }

    public void doClick(View v) {
        if (v.getId() == R.id.enroll_back_layout) {
            returnBack();
        }
    }

    private void returnBack() {
        backIntent();
    }
}