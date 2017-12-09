package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;


import android.os.Bundle;

/**
 * Created by Qian Jin on 9/7/16.
 */
public class NullResponse extends ResponseCommand {

    public NullResponse(byte[] responseData) {
        super(responseData);

        mBody = new byte[]{0};
        mCrc = new byte[]{0, 0};
    }

    @Override
    public Bundle readData() {
        return new Bundle();
    }

}
