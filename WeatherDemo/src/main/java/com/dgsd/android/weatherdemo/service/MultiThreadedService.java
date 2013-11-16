package com.dgsd.android.weatherdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Like an Android IntentService, but avoids the pitfalls of using a queue.
 * <p/>
 * Namely, it can process multiple intents in parallel
 */
public abstract class MultiThreadedService extends Service {

    private volatile List<Looper> mServiceLoopers;
    private volatile List<ServiceHandler> mServiceHandlers;
    private String mName;
    private boolean mRedelivery;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public MultiThreadedService(String name) {
        super();
        mName = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mServiceLoopers = new ArrayList<>();
        mServiceHandlers = new ArrayList<>();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        HandlerThread thread = new HandlerThread("IntentService[" + mName + " - " + startId + "]");
        thread.start();

        final Looper looper = thread.getLooper();
        final ServiceHandler handler = new ServiceHandler(looper);
        mServiceLoopers.add(looper);
        mServiceHandlers.add(handler);

        Message msg = handler.obtainMessage();
        msg.arg1 = startId;
        msg.obj = intent;
        handler.sendMessage(msg);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onStart(intent, startId);
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        for (Looper l : mServiceLoopers)
            l.quit();
    }

    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            onHandleIntent((Intent) msg.obj);
            mServiceHandlers.remove(this);
            if (mServiceHandlers.isEmpty()) {
                stopSelf(msg.arg1);
            }
        }
    }

    protected abstract void onHandleIntent(Intent intent);
}