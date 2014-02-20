package com.dgsd.android.weatherdemo.modules;

import android.content.Context;
import com.dgsd.android.weatherdemo.WeatherApp;
import dagger.Module;
import dagger.Provides;

/**
 * Provides injection of our global {@link android.app.Application} object
 */
@Module(
        library = true
)
public class AppModule {

    private WeatherApp mApp;

    public AppModule(WeatherApp app) {
        mApp = app;
    }

    @ForApplication
    @Provides
    public Context providesApp() {
        return mApp;
    }
}
