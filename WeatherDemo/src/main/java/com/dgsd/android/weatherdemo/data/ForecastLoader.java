package com.dgsd.android.weatherdemo.data;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.dgsd.android.weatherdemo.api.WeatherApi;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.util.DaoUtils;
import com.dgsd.android.weatherdemo.util.ProviderUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Loader which retrieves results from the local database and transforms
 * them into a format useable by the app.
 */
public class ForecastLoader extends AsyncLoader<List<Forecast>> {

    /**
     * Comparator which sorts by forecast date
     */
    private static Comparator<Forecast> DATE_COMPARATOR = new Comparator<Forecast>() {
        @Override
        public int compare(final Forecast lhs, final Forecast rhs) {
            return Long.compare(lhs.getDate(), rhs.getDate());
        }
    };

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
            cursor = ProviderUtils.query(WeatherContentProvider.FORECASTS_URI)
                    .sort(Db.Field.DATE + " DESC")
                    .limit(WeatherApi.MAX_NUM_DAYS)
                    .cursor(getContext());
            if (cursor != null && cursor.moveToFirst()) {
                final List<Forecast> retval = new ArrayList<>(cursor.getCount());
                do {
                    retval.add(DaoUtils.build(Forecast.class, cursor));
                } while (cursor.moveToNext());

                Collections.sort(retval, DATE_COMPARATOR);

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
