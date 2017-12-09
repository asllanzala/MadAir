package com.honeywell.hch.airtouch.plateform.smartlink.udpmode;


import com.honeywell.hch.airtouch.library.util.ByteUtil;

import java.io.Serializable;

/**
 * Created by wuyuan on 11/19/15.
 */
public class UDPCMDHeaderData implements Serializable {

    private int type;

    private String cmd = "OK";

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public byte[] getmtypeByte(){
        return ByteUtil.intToBytes(type);
    }

    public byte[] getcmdByte(){
        return cmd.getBytes();
    }
}
