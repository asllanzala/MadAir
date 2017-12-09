package com.honeywell.hch.airtouch.plateform.http;


import com.honeywell.hch.airtouch.plateform.config.AppConfig;

/**
 * Created by allanhwmac on 15/12/30.
 */
public class HttpProxy {

    private static IWebService mWebService;
    private static HttpProxy mHttpProxy;

    public static synchronized HttpProxy getInstance(){
        if (null == mHttpProxy) {
            mHttpProxy = new HttpProxy();
        }
        return mHttpProxy;
    }

    public IWebService getWebService() {
        if (null == mWebService || AppConfig.isChangeEnv) {
            mWebService = new HttpWebService();
        }
        return mWebService;
    }

    public void setWebService (IWebService webService) {
        mWebService = webService;
    }

}
