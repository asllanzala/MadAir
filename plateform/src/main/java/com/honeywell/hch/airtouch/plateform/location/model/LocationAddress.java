package com.honeywell.hch.airtouch.plateform.location.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Qian Jin on 12/3/15.
 */
public class LocationAddress implements  Serializable {

    @SerializedName("address_components")
    private List<AddressComponents> mAddressComponents;

    public List<AddressComponents> getAddressComponents() {
        return mAddressComponents;
    }
}
