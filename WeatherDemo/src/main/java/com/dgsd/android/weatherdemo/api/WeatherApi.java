package com.dgsd.android.weatherdemo.api;

import com.dgsd.android.weatherdemo.model.WeatherResponse;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by daniel on 16/11/2013.
 */
public interface WeatherApi {

    public static final int MAX_NUM_DAYS = 5;

    /**
     * Get the weather for the upcoming <code>numDays</code> days in location <code>query</code>
     *
     * @param apiKey
     * @param query   The query to send to the Api. Eg 'Sydney', '-33.0, 151.0'
     * @param numDays The number of days to request the forecast for. This must be less than {@link #MAX_NUM_DAYS}
     * @return The response from the API
     */
    @GET("/weather.ashx?format=json&cc=no&extra=utcDateTime%2C+localObsTime%2C+isDayTime")
    public WeatherResponse getWeather(@Query("key") String apiKey,
                                      @Query("q") String query,
                                      @Query("num_of_days") int numDays);
}
