package com.dgsd.android.weatherdemo.model;

import com.dgsd.android.weatherdemo.R;
import com.google.gson.Gson;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;


public class ForecastTests extends BaseModelTests {

    public void testBasicForecastParse() {
        String json = getJsonFromResource(R.raw.single_forecast);
        assertThat(json).isNotNull();

        Forecast f = new Gson().fromJson(json, Forecast.class);

        assertThat(f.getMaxTemp()).isEqualTo(27);
        assertThat(f.getMinTemp()).isEqualTo(16);
        assertThat(f.getWeatherCode()).isEqualTo(116);
        assertThat(f.getDateStr()).isEqualTo("2013-11-19");
        assertThat(f.getWindDirectionAngle()).isEqualTo(126);
        assertThat(f.getWindSpeed()).isEqualTo(17);
        assertThat(f.getWindDirectionCode()).isEqualTo(WindDirection.SE);

        final List<WeatherDescription> description = f.getWeatherDescription();
        assertThat(description).isNotNull().hasSize(1);

        final WeatherDescription desc = description.get(0);
        assertThat(desc).isNotNull();
        assertThat(desc.getValue()).isEqualTo("Partly Cloudy");
    }

}
