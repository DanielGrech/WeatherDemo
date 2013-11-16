package com.dgsd.android.weatherdemo.dao;

import android.content.ContentValues;
import android.text.TextUtils;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.model.WeatherDescription;
import com.dgsd.android.weatherdemo.model.WindDirection;
import com.dgsd.android.weatherdemo.util.EnumUtils;

import static com.dgsd.android.weatherdemo.data.Db.Field.*;

/**
 * Performs java object --> database conversion for {@link com.dgsd.android.weatherdemo.model.Forecast}
 * objects (and visa-versa)
 */
public class ForecastDao implements IDao<Forecast> {
    @Override
    public Forecast build(final ContentValues values) {
        final Forecast f = new Forecast();

        Long id = values.getAsLong(ID.getName());
        if (id != null) {
            f.setId(id);
        }

        values.getAsString(DATE_STR.getName());

        String desc = values.getAsString(DESCRIPTION.getName());
        if (!TextUtils.isEmpty(desc)) {
            WeatherDescription description = new WeatherDescription();
            description.setValue(desc);
            f.addWeatherDescriptionLine(description);
        }


        Long date = values.getAsLong(DATE.getName());
        if (date != null) {
            f.setDate(date);
        }

        Float max = values.getAsFloat(MAX_TEMP.getName());
        if (max != null) {
            f.setMaxTemp(max);
        }

        Float min = values.getAsFloat(MIN_TEMP.getName());
        if (min != null) {
            f.setMinTemp(min);
        }

        Integer weatherCode = values.getAsInteger(WEATHER_CODE.getName());
        if (weatherCode != null) {
            f.setWeatherCode(weatherCode);
        }

        Integer windDir = values.getAsInteger(WIND_DIR_ANGLE.getName());
        if (windDir != null) {
            f.setWindDirectionAngle(windDir);
        }

        Integer windSpeed = values.getAsInteger(WIND_SPEED.getName());
        if (windSpeed != null) {
            f.setWindSpeed(windSpeed);
        }

        Integer windDirCode = values.getAsInteger(WIND_DIR_CODE.getName());
        if (windDirCode != null) {
            f.setWindDirectionCode(EnumUtils.from(WindDirection.class, windDirCode));
        }

        return f;
    }

    @Override
    public ContentValues convert(final Forecast forecast) {
        return null;
    }
}
