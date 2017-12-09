package com.honeywell.hch.airtouch.plateform.location.manager;

import android.location.Location;

import com.google.gson.Gson;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IReceiveResponse;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.http.GPSClient;
import com.honeywell.hch.airtouch.plateform.location.model.CityInfo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2014/8/28.
 */
public class GetAddressByBaidu {
    private static final String TAG = "AirTouchGetAddressByBaidu";
    private GetAddressDataListener mCallback;
    private Location mLocation;
    private CityInfo cityLocation = null;

    public GetAddressByBaidu(GetAddressDataListener callback) {
        mCallback = callback;
    }

    public GetAddressByBaidu(GetAddressDataListener callback, Location location) {
        mCallback = callback;
        mLocation = location;
        getLocationInfo();
    }

    public void getLocationInfo() {
        IReceiveResponse receiveResponse = new IReceiveResponse() {
            @Override
            public void onReceive(HTTPRequestResponse httpRequestResponse) {
                LogUtil.log(LogUtil.LogLevel.INFO, TAG,
                        "jsonString :" + httpRequestResponse.getData());
                if (StringUtil.isEmpty(httpRequestResponse.getData()))
                    return;
                try {
                    JSONObject result = new JSONObject(httpRequestResponse.getData());
                    if (result.getInt("status") == 0) {
                        JSONObject addressComponents = result.getJSONObject("result")
                                .getJSONObject("addressComponent");
                        JSONObject location = result.getJSONObject("result").getJSONObject("location");
                        addressComponents.put("lat", location.get("lat"));
                        addressComponents.put("lng", location.get("lng"));
                        cityLocation = new Gson().fromJson(addressComponents.toString(), CityInfo.class);
                    } else {
                        cityLocation = new CityInfo();
                    }

                    mCallback.onGetAddressDataComplete(cityLocation);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, httpRequestResponse.getData());
                }
            }
        };
        GPSClient.sharedInstance().getCityAddressDataByBaidu(mLocation, receiveResponse);
    }

    public interface GetAddressDataListener {
        void onGetAddressDataComplete(CityInfo cityLocation);
    }

}
