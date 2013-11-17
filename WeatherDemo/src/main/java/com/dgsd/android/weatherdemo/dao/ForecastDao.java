package com.dgsd.android.weatherdemo.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.model.WeatherDescription;
import com.dgsd.android.weatherdemo.model.WindDirection;
import com.dgsd.android.weatherdemo.util.ContentValuesBuilder;
import com.dgsd.android.weatherdemo.util.EnumUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.dgsd.android.weatherdemo.data.Db.Field.*;

/**
 * Performs java object --> database conversion for {@link com.dgsd.android.weatherdemo.model.Forecast}
 * objects (and visa-versa)
 */
public class ForecastDao implements IDao<Forecast> {

    private Map<String, Integer> mCursorCols;

    @Override
    public Forecast build(final Cursor cursor) {
        if (mCursorCols == null) {
            mCursorCols = buildCursorCols(cursor);
        }

        final Forecast f = new Forecast();
        f.setId(cursor.getLong(mCursorCols.get(ID.getName())));
        f.setDateStr(cursor.getString(mCursorCols.get(DATE_STR.getName())));

        String desc = cursor.getString(mCursorCols.get(DESCRIPTION.getName()));
        if (!TextUtils.isEmpty(desc)) {
            WeatherDescription description = new WeatherDescription();
            description.setValue(desc);
            f.addWeatherDescriptionLine(description);
        }

        f.setDate(cursor.getLong(mCursorCols.get(DATE.getName())));
        f.setMaxTemp(cursor.getFloat(mCursorCols.get(MAX_TEMP.getName())));
        f.setMinTemp(cursor.getFloat(mCursorCols.get(MIN_TEMP.getName())));
        f.setWeatherCode(cursor.getInt(mCursorCols.get(WEATHER_CODE.getName())));
        f.setWindDirectionAngle(cursor.getInt(mCursorCols.get(WIND_DIR_ANGLE.getName())));
        f.setWindSpeed(cursor.getInt(mCursorCols.get(WIND_SPEED.getName())));
        f.setWindDirectionCode(EnumUtils.from(WindDirection.class,
                cursor.getInt(mCursorCols.get(WIND_DIR_CODE.getName()))));

        return f;
    }

    @Override
    public ContentValues convert(final Forecast forecast) {
        final ContentValuesBuilder builder = new ContentValuesBuilder()
                .put(DATE_STR, forecast.getDateStr())
                .put(DATE, forecast.getDate())
                .put(MAX_TEMP, forecast.getMaxTemp())
                .put(MIN_TEMP, forecast.getMinTemp())
                .put(DESCRIPTION, forecast.buildWeatherDescription())
                .put(WEATHER_CODE, forecast.getWeatherCode())
                .put(WIND_DIR_ANGLE, forecast.getWindDirectionAngle())
                .put(WIND_SPEED, forecast.getWindSpeed());

        if (forecast.getId() > 0) {
            builder.put(ID, forecast.getId());
        }

        if (forecast.getWindDirectionCode() != null) {
            builder.put(WIND_DIR_CODE, forecast.getWindDirectionCode());
        }

        return builder.build();
    }

    private static Map<String, Integer> buildCursorCols(final Cursor cursor) {
        Map<String, Integer> retval = new ConcurrentHashMap<>();
        for (int i = 0, len = cursor.getColumnCount(); i < len; i++) {
            retval.put(cursor.getColumnName(i), i);
        }

        return retval;
    }
}
