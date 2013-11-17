package com.dgsd.android.weatherdemo.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.dgsd.android.weatherdemo.WeatherApp;
import com.dgsd.android.weatherdemo.api.IApiManager;
import com.dgsd.android.weatherdemo.api.IPersistenceManager;
import com.dgsd.android.weatherdemo.data.WeatherContentProvider;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.modules.ApiModule;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;

/**
 * Dispatches API calls on a background thread
 */
public class ApiExecutorService extends BaseApiService {
    private static final String EXTRA_LOCATION = "location";
    private static final String EXTRA_NUM_DAYS = "numDays";

    @Inject
    IApiManager mApiManager;

    @Inject
    IPersistenceManager mPersistenceManager;

    @Override
    public void onCreate() {
        super.onCreate();

        WeatherApp app = (WeatherApp) getApplication();
        app.inject(this, new ApiModule());
    }

    @Override
    protected void handleApiRequest(final String token, final Bundle extras) {
        final String location = extras == null ? null : extras.getString(EXTRA_LOCATION);
        final int numDays = extras == null ? null : extras.getInt(EXTRA_NUM_DAYS, -1);

        switch (token) {
            case AsyncRequest.ACTION_GET_WEATHER:
                List<Forecast> forecasts = mApiManager.getForecasts(location, numDays);

                if (forecasts != null && !forecasts.isEmpty()) {
                    Timber.d("Got %d forecasts", forecasts.size());
                    mPersistenceManager.persistForecasts(forecasts);
                    getContentResolver().notifyChange(WeatherContentProvider.FORECAST_LOADER_URI, null, false);
                }

                break;
        }
    }

    /**
     * Fires off asynchronous requests for API calls
     */
    public static class AsyncRequest {

        public static final String ACTION_GET_WEATHER = "get_weather";

        /**
         * Asynchronously Retreive forecasts for <code>location</code> for the next <code>numDays</code>
         *
         * @param context
         * @param location The location of the forecasts to retrieve
         * @param numDays  The number of days the forecast should be retrieved for. This must be less
         *                 than {@link com.dgsd.android.weatherdemo.api.WeatherApi.MAX_NUM_DAYS}
         * @return A token which can be used with an {@link com.dgsd.android.weatherdemo.receiver.ApiBroadcastReceiver}
         * to listen for results
         */
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
