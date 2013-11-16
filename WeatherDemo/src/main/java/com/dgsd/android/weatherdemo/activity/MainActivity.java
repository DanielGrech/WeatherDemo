package com.dgsd.android.weatherdemo.activity;

import android.os.Bundle;
import android.widget.TextView;
import com.dgsd.android.weatherdemo.api.WeatherApi;
import com.dgsd.android.weatherdemo.service.ApiExecutorService;

/**
 *
 */
public class MainActivity extends BaseActivity {
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView tv = new TextView(this);
        tv.setText(mEventBus.toString());
        setContentView(tv);

        ApiExecutorService.AsyncRequest.getWeather(this, "Sydney", WeatherApi.MAX_NUM_DAYS);
    }
}
