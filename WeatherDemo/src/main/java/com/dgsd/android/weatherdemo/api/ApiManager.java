package com.dgsd.android.weatherdemo.api;

import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.model.WeatherResponse;

import java.util.List;

/**
 * Default implementation of {@link com.dgsd.android.weatherdemo.api.IApiManager}
 */
public class ApiManager implements IApiManager {

    private final WeatherApi mApi;
    private final String mAuthKey;

    public ApiManager(String authKey, WeatherApi weatherApi) {
        mAuthKey = authKey;
        mApi = weatherApi;
    }

    @Override
    public List<Forecast> getForecasts(final String location, final int numDays) {
        WeatherResponse response = mApi.getWeather(mAuthKey, location, numDays);
        return response == null ?
                null : response.getForecasts();
    }
}