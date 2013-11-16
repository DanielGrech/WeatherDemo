package com.dgsd.android.weatherdemo.model;

import com.dgsd.android.weatherdemo.R;
import com.google.gson.Gson;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 *
 */
public class WeatherResponseTests extends BaseModelTests {

    public void testBasicWeatherResponseParse() {
        final String json = getJsonFromResource(R.raw.get_weather);
        assertThat(json).isNotNull();

        WeatherResponse response = new Gson().fromJson(json, WeatherResponse.class);
        assertThat(response).isNotNull();

        WeatherResponse.ResponseData data = response.getData();
        assertThat(data).isNotNull();
        assertThat(data.forecasts).isEqualTo(response.getForecasts());

        List<Forecast> forecasts = response.getForecasts();
        assertThat(forecasts).isNotNull().hasSize(5);
    }

}
