package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request;


import android.util.Log;

import com.honeywell.hch.airtouch.library.util.DateTimeUtil;
import com.honeywell.hch.airtouch.plateform.devices.madair.model.MadAirMotorSpeed;
import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.Command;


/**
 * Created by Qian Jin on 9/9/16.
 */
public class SimpleRequestFactory {

    public static final long ONE_DAY = 24 * 60 * 60 * 1000;

    public static RequestCommand createRequestCommand(RequestType requestType) {

        switch (requestType) {

            case SYNC:
                return new RequestCommand(RequestLength.SYNC, RequestType.SYNC, getCommonBody());

            case FLASH_DATA:
                return new RequestCommand(RequestLength.FLASH_UPDATE,
                        RequestType.FLASH_DATA, getFlashUpdateBody());

            case GET_DATA_REPORT:
                return new RequestCommand(RequestLength.GET_DATA_REPORT,
                        RequestType.GET_DATA_REPORT, getCommonBody());

            default:
                return new RequestCommand(RequestLength.NULL_LENGTH, RequestType.NULL_TYPE, new byte[]{0});
        }

    }

    public static RequestCommand createMotorRequestCommand(MadAirMotorSpeed madAirMotorSpeed) {
        return new RequestCommand(RequestLength.MOTOR_SPEED,
                RequestType.MOTOR_SPEED, new byte[]{0x01, madAirMotorSpeed.getSpeed()});
    }

    private static byte[] getCommonBody() {
        byte[] result = new byte[6];
        byte[] timeStamps = get4BytesFromLong(getNowStampDeviation());
        byte[] reserved = new byte[] {0x00, 0x00};
        System.arraycopy(timeStamps, 0, result, 0, 4);
        System.arraycopy(reserved, 0, result, 4, 2);

        return result;
    }

    private static byte[] getFlashUpdateBody() {
        byte[] result = new byte[10];
        byte[] timeStamps = get4BytesFromLong(get30dayAgoStampDeviation());
        byte[] reserved = new byte[] {0x00, 0x00};
        System.arraycopy(timeStamps, 0, result, 0, 4);
        System.arraycopy(timeStamps, 0, result, 4, 4);
        System.arraycopy(reserved, 0, result, 8, 2);

        return result;
    }

    private static long getNowStampDeviation() {
        return DateTimeUtil.compareNowTime(DateTimeUtil.getDateTimeFromString
                (Command.DATE_FORMAT, Command.DATE_2015).getTime()) / 1000;
    }

    private static long get30dayAgoStampDeviation() {
        long time2015 = DateTimeUtil.getDateTimeFromString(Command.DATE_FORMAT, Command.DATE_2015).getTime();
        long time30dayAgo = System.currentTimeMillis() - 31 * ONE_DAY;

        return (time30dayAgo - time2015) / 1000;
    }

    private static byte[] get4BytesFromLong(long data) {
        return new byte[] {(byte) (0x00FF & data >> 24), (byte) (0x00FF & data >> 16),
                (byte) (0x00FF & data >> 8), (byte) (0x00FF & data)};
    }

}
