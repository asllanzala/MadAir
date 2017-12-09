package com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.request;

import com.honeywell.hch.airtouch.ui.main.ui.dashboard.madair.model.command.Command;


/**
 * Created by Qian Jin on 9/7/16.
 */
public class RequestCommand extends Command {

    public RequestCommand(RequestLength length, RequestType type, byte[] requestBody) {
        super(length, type, requestBody);
    }

}
