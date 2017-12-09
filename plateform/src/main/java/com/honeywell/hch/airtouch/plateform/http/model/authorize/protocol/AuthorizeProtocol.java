package com.honeywell.hch.airtouch.plateform.http.model.authorize.protocol;

/**
 * Created by Vincent on 5/4/16.
 */
public interface AuthorizeProtocol {

    int getAuthSendAction();

    int getAuthRevokeAction();

    int getAuthRemoveAction();

    boolean revmoveClickAble();

    boolean revokeClickAble();

    boolean authorizeClickAble();

    boolean canShowDeviceStatus();
}
