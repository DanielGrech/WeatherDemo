package com.dgsd.android.weatherdemo.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import butterknife.ButterKnife;
import butterknife.ButterKnife;
import com.dgsd.android.weatherdemo.R;
import com.dgsd.android.weatherdemo.api.WeatherApi;
import com.dgsd.android.weatherdemo.fragment.ForecastFragment;
import com.dgsd.android.weatherdemo.service.ApiExecutorService;
import com.squareup.otto.Subscribe;

/**
 *
 */
public class MainActivity extends BaseActivity {

    public static final String KEY_HAS_MADE_FORECAST_REQUEST = "has_made_request";

    private ForecastFragment mForecastFragment;

    /**
     * Tracks weather or not we have made our network request for new forecasts
     */
    private boolean mHasMadeForecastRequest;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        getActionBar().hide();

        ButterKnife.inject(this);

        mForecastFragment = findFragment(R.id.forecast_frag);

        mHasMadeForecastRequest = savedInstanceState == null ?
                false : savedInstanceState.getBoolean(KEY_HAS_MADE_FORECAST_REQUEST, false);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_HAS_MADE_FORECAST_REQUEST, mHasMadeForecastRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEventBus.register(this);

        if (!mHasMadeForecastRequest) {
            //TODO: Sydney is hardcoded at the moment .. need to support picking multiple cities
            registerForApi(ApiExecutorService.AsyncRequest.getWeather(this,
                    "Sydney",
                    WeatherApi.MAX_NUM_DAYS));
            mHasMadeForecastRequest = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEventBus.unregister(this);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onShowFullViewToggled(ForecastFragment.OnFullViewToggleEvent event) {
        if (event.fullViewVisible) {
            getActionBar().show();
        } else {
            getActionBar().hide();
        }
    }
}
