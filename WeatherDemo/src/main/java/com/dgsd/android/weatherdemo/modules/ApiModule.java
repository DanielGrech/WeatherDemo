package com.dgsd.android.weatherdemo.modules;

import android.content.Context;
import com.dgsd.android.weatherdemo.BuildConfig;
import com.dgsd.android.weatherdemo.WeatherApp;
import com.dgsd.android.weatherdemo.api.*;
import com.dgsd.android.weatherdemo.service.ApiExecutorService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides access to the WeatherWorld API
 */
@Module(
        injects = {
                ApiExecutorService.class
        },
        complete = false
)
public class ApiModule {

    @Provides
    @Named("server_url")
    public String providesServerUrl() {
        return BuildConfig.BASE_SERVER;
    }

    @Provides
    @Named("auth_key")
    public String providesApiAuthKey() {
        return BuildConfig.API_AUTH_KEY;
    }

    @Provides
    @Singleton
    public WeatherApi providesWeatherApi(@Named("server_url") String server) {
        return new RestAdapter.Builder()
                .setServer(server)
                .build()
                .create(WeatherApi.class);
    }

    @Provides
    @Singleton
    public IPersistenceManager providesPersistenceManager(WeatherApp app) {
        return new PersistenceManager(app);
    }

    @Provides
    @Singleton
    public IApiManager providesApiManager(@Named("auth_key") String authKey,
                                          WeatherApi api) {
        return new ApiManager(authKey, api);
    }
}
