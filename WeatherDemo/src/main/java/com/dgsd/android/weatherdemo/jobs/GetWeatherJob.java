package com.dgsd.android.weatherdemo.jobs;

import android.content.ContentResolver;
import com.dgsd.android.weatherdemo.api.IApiManager;
import com.dgsd.android.weatherdemo.api.IPersistenceManager;
import com.dgsd.android.weatherdemo.data.WeatherContentProvider;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.path.android.jobqueue.Params;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.List;

/**
 * Fetch results from the WeatherWorld API and
 * persist them
 */
public class GetWeatherJob extends BaseJob {

    private final String mLocation;
    private final int mNumberOfDays;

    @Inject
    transient IApiManager mApiManager;

    @Inject
    transient IPersistenceManager mPersistenceManager;

    @Inject
    transient ContentResolver mContentResolver;

    public GetWeatherJob(String location, int numDays) {
        // Group by location to ensure we only have 1
        // concurrently executing request per location
        super(new Params(PRIORITY_DEFAULT).groupBy(location));
        mLocation = location;
        mNumberOfDays = numDays;
    }

    @Override
    protected void runJob() {
        List<Forecast> forecasts =
                mApiManager.getForecasts(mLocation, mNumberOfDays);

        if (forecasts != null && !forecasts.isEmpty()) {
            Timber.d("Got %d forecasts", forecasts.size());
            mPersistenceManager.persistForecasts(forecasts);
            mContentResolver.notifyChange(WeatherContentProvider.FORECAST_LOADER_URI,                    null, false);
        }
    }
}
