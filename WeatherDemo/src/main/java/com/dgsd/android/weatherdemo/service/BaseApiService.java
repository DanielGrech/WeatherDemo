package com.dgsd.android.weatherdemo.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import timber.log.Timber;

/**
 * Encapsulates the logic to broadcast API results to other parts of the application
 */
public abstract class BaseApiService extends MultiThreadedService {

    public static final String ACTION_API_START = "api_start";
    public static final String ACTION_API_FINISH = "api_finish";
    public static final String ACTION_API_ERROR = "api_error";

    public static final String EXTRA_ERROR_MESSAGE = "error_message";
    public static final String EXTRA_TOKEN = "token";

    protected abstract void handleApiRequest(String token, Bundle extras);

    public BaseApiService() {
        super(BaseApiService.class.getSimpleName());
    }

    @Override
    protected final void onHandleIntent(final Intent intent) {
        final String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {

            try {
                sendStartBroadcast(action);
                handleApiRequest(action, intent.getExtras());
            } catch (Throwable t) {
                //TODO: Do a better job handling errors (Catch specific exceptions , User-friendly error messages)
                Timber.e(t, "Error executing API request");
                sendErrorBroadcast(action, t.getMessage());
            } finally {
                sendFinishBroadcast(action);
            }
        }
    }

    private void sendStartBroadcast(String token) {
        Intent intent = new Intent(ACTION_API_START);
        intent.putExtra(EXTRA_TOKEN, token);
        broadcast(intent);
    }

    private void sendFinishBroadcast(String token) {
        Intent intent = new Intent(ACTION_API_FINISH);
        intent.putExtra(EXTRA_TOKEN, token);
        broadcast(intent);
    }

    private void sendErrorBroadcast(String token, String errorMessage) {
        final Intent intent = new Intent(ACTION_API_ERROR);
        intent.putExtra(EXTRA_ERROR_MESSAGE, errorMessage);
        intent.putExtra(EXTRA_TOKEN, token);
        broadcast(intent);
    }

    private void broadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
