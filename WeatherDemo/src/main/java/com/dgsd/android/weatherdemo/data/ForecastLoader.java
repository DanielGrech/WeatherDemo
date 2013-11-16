package com.dgsd.android.weatherdemo.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.util.DaoUtils;
import com.dgsd.android.weatherdemo.util.ProviderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ForecastLoader extends AsyncLoader<List<Forecast>> {

    public ForecastLoader(final Context context) {
        super(context);
    }

    @Override
    protected Uri getContentUri() {
        return WeatherContentProvider.FORECAST_LOADER_URI;
    }

    @Override
    public List<Forecast> loadInBackground() {
        Cursor cursor = null;
        try {
            cursor = ProviderUtils.query(WeatherContentProvider.FORECASTS_URI).cursor(getContext());
            if (cursor != null && cursor.moveToFirst()) {
                final List<Forecast> retval = new ArrayList<>(cursor.getCount());
                do {
                    retval.add(DaoUtils.build(Forecast.class, cursor));
                } while (cursor.moveToNext());
                return retval;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }
}
