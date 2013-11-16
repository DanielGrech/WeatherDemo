package com.dgsd.android.weatherdemo.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dgsd.android.weatherdemo.model.Forecast;
import timber.log.Timber;

import java.util.List;

public abstract class BaseApiService extends MultiThreadedService {

    public static final String ACTION_API_START = "api_start";
    public static final String ACTION_API_FINISH = "api_finish";
    public static final String ACTION_API_ERROR = "api_error";

    public static final String EXTRA_ERROR_MESSAGE = "error_message";

    protected abstract void handleApiRequest(String token, Bundle extras);

    public BaseApiService() {
        super(BaseApiService.class.getSimpleName());
    }

    @Override
    protected final void onHandleIntent(final Intent intent) {
        final String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {

            try {
                sendStartBroadcast();
                handleApiRequest(action, intent.getExtras());
            } catch (Throwable t) {
                //TODO: Do a better job handling errors (Catch specific exceptions , User-friendly error messages)
                Timber.e(t, "Error executing API request");
                sendErrorBroadcast(t.getMessage());
            } finally {
                sendFinishBroadcast();
            }
        }
    }

    private void sendStartBroadcast() {
        broadcast(new Intent(ACTION_API_START));
    }

    private void sendFinishBroadcast() {
        broadcast(new Intent(ACTION_API_FINISH));
    }

    private void sendErrorBroadcast(String errorMessage) {
        final Intent intent = new Intent(ACTION_API_ERROR);
        intent.putExtra(EXTRA_ERROR_MESSAGE, errorMessage);
        broadcast(intent);
    }

    private void broadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
