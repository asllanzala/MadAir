package com.honeywell.hch.airtouch.library;

import android.app.Application;
import android.content.Context;

/**
 * Created by h127856 on 16/10/8.
 */
public class LibApplication extends Application {

    /** Instance of the current application. */
    private static Application mInstance;

    /**
     * Constructor.
     */
    public LibApplication() {
        mInstance = this;
    }

    /**
     * Gets the application context.
     *
     * @return the application context
     */
    public static Context getContext() {
        return mInstance;
    }


    public static void setApplication(Application application){
        mInstance = application;
    }
}
