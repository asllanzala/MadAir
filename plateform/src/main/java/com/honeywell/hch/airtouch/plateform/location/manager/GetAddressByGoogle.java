package com.honeywell.hch.airtouch.plateform.location.manager;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.honeywell.hch.airtouch.library.http.model.HTTPRequestResponse;
import com.honeywell.hch.airtouch.library.http.model.IReceiveResponse;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.library.util.StringUtil;
import com.honeywell.hch.airtouch.plateform.http.GPSClient;
import com.honeywell.hch.airtouch.plateform.location.model.AddressComponents;
import com.honeywell.hch.airtouch.plateform.location.model.CityInfo;
import com.honeywell.hch.airtouch.plateform.location.model.LocationAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by admin on 2014/8/28.
 */
public class GetAddressByGoogle {
    private static final String TAG = "AirTouchGetAddressByGoogle";
    private GetAddressDataByGoogleListener mCallback;
    private Location mLocation;
    private CityInfo cityLocation = null;

    public GetAddressByGoogle(GetAddressDataByGoogleListener callback) {
        mCallback = callback;
    }

    public GetAddressByGoogle(GetAddressDataByGoogleListener callback, Location location) {
        mCallback = callback;
        mLocation = location;
        getLocationInfo();
    }

    public void getLocationInfo() {
        IReceiveResponse receiveResponse = new IReceiveResponse() {
            @Override
            public void onReceive(HTTPRequestResponse httpRequestResponse) {
                if (StringUtil.isEmpty(httpRequestResponse.getData()))
                    return;
                cityLocation = new CityInfo();
                try {
                    JSONObject result = new JSONObject(httpRequestResponse.getData());
                    if (result.has("results")) {
                        String response = result.getJSONArray("results").toString();
                        Type locationType = new TypeToken<List<LocationAddress>>() {}.getType();
                        List<LocationAddress> locationAddresses = new Gson().fromJson(response, locationType);
                        for (LocationAddress la : locationAddresses) {
                            List<AddressComponents> addressComponents = la.getAddressComponents();
                            for (AddressComponents ac :addressComponents) {
                                if (ac.getTypes() != null && ac.getTypes().get(0).equals("locality")) {
                                    cityLocation.setCity(ac.getLongName());
                                    break;
                                }
                            }
                        }
                    }

                    mCallback.onGetAddressDataComplete(cityLocation);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, httpRequestResponse.getData());
                }
            }
        };
        GPSClient.sharedInstance().getCityAddressDataByGoogle(mLocation, receiveResponse);
    }

    public interface GetAddressDataByGoogleListener {
        void onGetAddressDataComplete(CityInfo cityLocation);
    }

}
