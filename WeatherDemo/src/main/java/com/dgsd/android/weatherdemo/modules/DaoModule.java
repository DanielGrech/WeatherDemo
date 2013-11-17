package com.dgsd.android.weatherdemo.modules;

import com.dgsd.android.weatherdemo.dao.ForecastDao;
import com.dgsd.android.weatherdemo.util.DaoUtils;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Provides access to our model DAO objects
 */
@Module(
        staticInjections = {
                DaoUtils.class
        }
)
public class DaoModule {

    @Provides
    @Singleton
    public ForecastDao providesForecastDao() {
        return new ForecastDao();
    }
}
