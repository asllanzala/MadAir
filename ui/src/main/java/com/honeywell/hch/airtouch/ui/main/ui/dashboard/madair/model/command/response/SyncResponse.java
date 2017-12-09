package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.util.LogUtil;

/**
 * Created by Qian Jin on 9/7/16.
 */
public class SyncResponse extends ResponseCommand {

    public SyncResponse(byte[] data) {
        super(data);

        if (data == null || data.length < MIN_TOTAL_LENGTH) {
            mBody = new byte[]{0};
            mCrc = new byte[]{0, 0};
            return;
        }

        // init fields
        mLength = data[0];
        mType = data[1];
        mBody = new byte[6];
        System.arraycopy(data, 2, mBody, 0, 6);
        mCrc = new byte[] {data[8], data[9]};

    }

    @Override
    public Bundle readData() {

        Bundle bundle = new Bundle();

        if (IsDataValidate()) {
            bundle.putInt(BUNDLE_RESPONSE_TYPE, mType);
        } else {
            LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "SyncResponse crc is wrong.");
        }

        return bundle;
    }

}
