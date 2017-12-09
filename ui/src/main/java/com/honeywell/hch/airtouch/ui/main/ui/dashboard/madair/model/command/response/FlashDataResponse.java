package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;

import android.os.Bundle;

import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.library.util.LogUtil;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirHistoryRecord;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.Command;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Qian Jin on 9/7/16.
 */
public class FlashDataResponse extends ResponseCommand {

    private static final int MAX_PACKAGE = 30;
    private static final int RESULT_FLASH_DATA_ERROR = 0;
    private static final int RESULT_FLASH_DATA_IN_PARSING = 1;
    private static final int RESULT_FLASH_DATA_END = 2;
    private static final int RESULT_FLASH_DATA_STOP = 3;
    private static final byte[] END_FLAG_ARRAY = new byte[]{(byte) 0xA5, 0x5A};

    private static HashMap<String, byte[]> mFlashDataMap = new HashMap();
    private static int mFlashDataResult;
    private static int[] mCrcPackResult = new int[MAX_PACKAGE];
    private byte mPackSequence;

    public FlashDataResponse(byte[] data) {
        super(data);

        if (data == null || data.length < MIN_TOTAL_LENGTH) {
            mBody = new byte[]{0};
            mCrc = new byte[]{0, 0};
            return;
        }

        mLength = data[0];
        mType = data[1];
        mPackSequence = data[2];

        if (mPackSequence == 1) {
            for (int i = 0; i < MAX_PACKAGE; i++)
                mCrcPackResult[i] = 0;

            mFlashDataMap.clear();
            mFlashDataResult = RESULT_FLASH_DATA_IN_PARSING;
        }

        // calculate crc
        for (int i = 0; i < 4; i++) {
            if (mPackSequence > 0 && mPackSequence <= MAX_PACKAGE)
                mCrcPackResult[mPackSequence] += convertNegative(data[i]);
        }

        if (mFlashDataResult == RESULT_FLASH_DATA_IN_PARSING)
            parseFlashData(data);

    }

    @Override
    public Bundle readData() {

        Bundle bundle = new Bundle();

        switch (mFlashDataResult) {

            case RESULT_FLASH_DATA_END:
                mFlashDataResult = RESULT_FLASH_DATA_STOP;

                if (IsDataValidate()) {
                    bundle.putInt(BUNDLE_RESPONSE_TYPE, mType);
                    bundle.putSerializable(BUNDLE_RESPONSE_FLASH_DATA, new MadAirHistoryRecord(mFlashDataMap));
                } else {
                    bundle.putInt(BUNDLE_RESPONSE_TYPE, mType);
                    LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "FlashDataResponse crc is wrong.");
                }
                break;

            case RESULT_FLASH_DATA_IN_PARSING:
                LogUtil.log(LogUtil.LogLevel.DEBUG, TAG, "FlashDataResponse parsing.");
                break;

            case RESULT_FLASH_DATA_STOP:
                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "FlashDataResponse parse end.");
                break;

            case RESULT_FLASH_DATA_ERROR:
                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "FlashDataResponse parse wrong.");
                break;

            default:
                LogUtil.log(LogUtil.LogLevel.ERROR, TAG, "FlashDataResponse unknown error.");
                break;
        }

        return bundle;
    }

    @Override
    protected boolean IsDataValidate() {

        int sum = 0;
        for (int i = 0; i < MAX_PACKAGE; i++)
            sum += mCrcPackResult[i];

        byte[] crc = get2BytesFromInt(sum);

        return (mCrc[0] == crc[0]) && (mCrc[1] == crc[1]);
    }

    private long calculateTimeStamp(byte[] timeStamp) {
        long date2015 = DateTimeUtil.getDateTimeFromString(DATE_FORMAT, DATE_2015).getTime();
        long dateDeviation = getLongFrom4bytes(timeStamp);

        return date2015 + dateDeviation * 1000;
    }

    // one package contains 2 set of data, each set contains 8 bytes
    private void parseFlashData(byte[] data) {
        byte[] flash[] = new byte[8][2];

        for (int i = 0; i < 2; i++) {
            flash[i] = new byte[8];
            System.arraycopy(data, 8 * i + 4, flash[i], 0, 8);

            parseEachFlashData(flash[i]);

            if (mFlashDataResult != RESULT_FLASH_DATA_IN_PARSING)
                return;
        }

    }

    private void parseEachFlashData(byte[] flashData) {
        if (flashData == null || flashData.length != 8) {
            mFlashDataResult = RESULT_FLASH_DATA_ERROR;

        } else if ((flashData[0] == END_FLAG_ARRAY[0]) && (flashData[1] == END_FLAG_ARRAY[1])) {
            mFlashDataResult = RESULT_FLASH_DATA_END;

            mCrc = new byte[]{flashData[2], flashData[3]};

            // calculate crc
            if (mPackSequence > 0 && mPackSequence <= MAX_PACKAGE) {
                mCrcPackResult[mPackSequence] += convertNegative(flashData[0]);
                mCrcPackResult[mPackSequence] += convertNegative(flashData[1]);
            }
        } else {
            // Notice: after end flag, do not get flash data.
            if (mFlashDataResult != RESULT_FLASH_DATA_IN_PARSING)
                return;

            mFlashDataResult = RESULT_FLASH_DATA_IN_PARSING;

            byte[] timeStamp = new byte[]{flashData[0], flashData[1], flashData[2], flashData[3]};
            byte[] storage = new byte[]{flashData[4], flashData[5], flashData[6], flashData[7]};

            Date date = DateTimeUtil.getDateFromLong(MadAirHistoryRecord.DATE_FORMAT2, calculateTimeStamp(timeStamp));
            mFlashDataMap.put(DateTimeUtil.getDateTimeString(date, MadAirHistoryRecord.DATE_FORMAT2), storage);

            // calculate crc
            for (byte b : flashData) {
                if (mPackSequence > 0 && mPackSequence <= MAX_PACKAGE)
                    mCrcPackResult[mPackSequence] += convertNegative(b);
            }
        }
    }

}

