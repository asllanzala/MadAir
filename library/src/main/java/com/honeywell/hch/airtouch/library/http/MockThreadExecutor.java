package com.honeywell.hch.airtouch.library.http;

import java.util.concurrent.Executor;

/**
 * Created by h127856 on 16/9/30.
 */
class MockThreadExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    }
}