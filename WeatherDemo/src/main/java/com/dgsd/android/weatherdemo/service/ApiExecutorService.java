package com.dgsd.android.weatherdemo.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.dgsd.android.weatherdemo.WeatherApp;
import com.dgsd.android.weatherdemo.api.IApiManager;
import com.dgsd.android.weatherdemo.api.IPersistenceManager;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.modules.ApiModule;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;
import java.util.Timer;

/**
 * Dispatches API calls on a background thread
 */
public class ApiExecutorService extends MultiThreadedService {

    public static final String ACTION_API_START = "api_start";
    public static final String ACTION_API_FINISH = "api_finish";
    public static final String ACTION_API_ERROR = "api_error";

    public static final String EXTRA_ERROR_MESSAGE = "error_message";
    private static final String EXTRA_LOCATION = "location";
    private static final String EXTRA_NUM_DAYS = "numDays";

    @Inject
    IApiManager mApiManager;

    @Inject
    IPersistenceManager mPersistenceManager;

    public ApiExecutorService() {
        super(ApiExecutorService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        WeatherApp app = (WeatherApp) getApplication();
        app.inject(this, new ApiModule());
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final String action = intent.getAction();
        if (!TextUtils.isEmpty(action)) {

            final String location = intent.getStringExtra(EXTRA_LOCATION);
            final int numDays = intent.getIntExtra(EXTRA_NUM_DAYS, -1);

            try {
                sendStartBroadcast();
                switch (action) {
                    case AsyncRequest.ACTION_GET_WEATHER:
                        List<Forecast> forecasts = mApiManager.getForecasts(location, numDays);

                        if (forecasts != null && !forecasts.isEmpty()) {
                            Timber.d("Got %d forecasts", forecasts.size());
//                            mPersistenceManager.persistForecasts(forecasts);
                        }

                        break;
                }
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


    public static class AsyncRequest {

        public static final String ACTION_GET_WEATHER = "get_weather";

        public static String getWeather(Context context, String location, int numDays) {
            final Intent intent = new Intent(context, ApiExecutorService.class);
            intent.setAction(ACTION_GET_WEATHER);

            intent.putExtra(ApiExecutorService.EXTRA_LOCATION, location);
            intent.putExtra(ApiExecutorService.EXTRA_NUM_DAYS, numDays);

            context.startService(intent);
            return intent.getAction();
        }
    }


}
