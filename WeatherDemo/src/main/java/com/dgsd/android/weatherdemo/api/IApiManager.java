package com.dgsd.android.weatherdemo.api;

import com.dgsd.android.weatherdemo.model.Forecast;

import java.util.List;

/**
 * Responsible for making raw Api calls and persisting any results
 */
public interface IApiManager {

    /**
     * Get the weather for the upcoming <code>numDays</code> days in <code>location</code>
     *
     * @param location The name of the location to retrieve forecasts for
     * @param numDays  The number of days to retrieve forecasts
     * @return A list of forecasts retreived from the API
     */
    public List<Forecast> getForecasts(String location, int numDays);
}
