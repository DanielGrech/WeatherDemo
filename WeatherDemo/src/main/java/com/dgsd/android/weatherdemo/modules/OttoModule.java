package com.dgsd.android.weatherdemo.modules;

import com.dgsd.android.weatherdemo.activity.MainActivity;
import com.dgsd.android.weatherdemo.fragment.ForecastFragment;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Provides an otto {@link com.squareup.otto.Bus} instance
 * to the application
 */
@Module(
        injects = {
                MainActivity.class,
                ForecastFragment.class
        }
)
public class OttoModule {

    @Provides
    @Singleton
    public Bus providesBus() {
        return new Bus(ThreadEnforcer.ANY);
    }
}
