package com.dgsd.android.weatherdemo.api;

import android.content.Context;
import com.dgsd.android.weatherdemo.data.WeatherContentProvider;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.util.DaoUtils;
import com.dgsd.android.weatherdemo.util.ProviderUtils;

import java.util.List;

/**
 * Default implementation of {@link com.dgsd.android.weatherdemo.api.IPersistenceManager}
 */
public class PersistenceManager implements IPersistenceManager {

    private final Context mContext;

    public PersistenceManager(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void persistForecasts(final List<Forecast> forecasts) {
        if (forecasts != null && !forecasts.isEmpty()) {
            for (Forecast f : forecasts) {
                mContext.getContentResolver()
                        .insert(WeatherContentProvider.FORECASTS_URI, DaoUtils.convert(f));
            }
        }
    }
}
