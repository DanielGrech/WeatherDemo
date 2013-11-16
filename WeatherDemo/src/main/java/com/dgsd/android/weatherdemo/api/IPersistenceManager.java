package com.dgsd.android.weatherdemo.api;

import com.dgsd.android.weatherdemo.model.Forecast;

import java.util.List;

/**
 * Responsible for persisting the results of any API calls
 */
public interface IPersistenceManager {

    /**
     * @param forecasts List of forecasts to persist
     */
    public void persistForecasts(List<Forecast> forecasts);
}
