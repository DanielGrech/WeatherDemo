package com.dgsd.android.weatherdemo.modules;

import com.dgsd.android.weatherdemo.WeatherApp;
import dagger.Module;
import dagger.Provides;

/**
 *
 */
@Module(
        library = true
)
public class AppModule {

    private WeatherApp mApp;

    public AppModule(WeatherApp app) {
        mApp = app;
    }

    @Provides
    public WeatherApp providesApp() {
        return mApp;
    }
}
