package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.response;


import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.Command;


/**
 * Created by Qian Jin on 9/9/16.
 */
public class SimpleResponseFactory {

    public static ResponseCommand createConcreteResponseCommand(byte[] data) {
        if (data == null || data.length < Command.MIN_TOTAL_LENGTH)
            return new NullResponse(data);

        switch (data[1]) {

            case ResponseType.SYNC_ACK:
                return new SyncResponse(data);

            case ResponseType.FLASH_DATA_ACK:
                return new FlashDataResponse(data);

            case ResponseType.DATA_REPORT:
                return new GetReportResponse(data);

            case ResponseType.MOTOR_ACK:
                return new MotorSpeedResponse(data);

            default:
                return new NullResponse(data);
        }

    }

    public static ResponseCommand createConcreteAuthResponseCommand(byte[] data) {
        if (data == null || data.length < Command.MIN_TOTAL_LENGTH)
            return new NullResponse(data);

        switch (data[1]) {

            case ResponseType.SYNC_ACK:
                return new SyncResponse(data);

            case ResponseType.FLASH_DATA_ACK:
                byte[] trim = new byte[12];
                System.arraycopy(data, 0, trim, 0, 12);
                return new AuthFlashDataResponse(trim);

            case ResponseType.DATA_REPORT:
                return new AuthGetReportResponse(data);

            case ResponseType.MOTOR_ACK:
                return new MotorSpeedResponse(data);

            default:
                return new NullResponse(data);
        }

    }

}
