package com.dgsd.android.weatherdemo;

import android.app.Application;
import com.dgsd.android.weatherdemo.util.ReleaseLogger;
import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by daniel on 16/11/2013.
 */
public class WeatherApp extends Application {

    private ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            //Default logger
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ReleaseLogger(getClass().getSimpleName()));
        }

        mObjectGraph = ObjectGraph.create(DiModules.asList(this));
        mObjectGraph.injectStatics();
    }

    public void inject(Object obj, Object... extraModules) {
        ObjectGraph og = mObjectGraph;
        if(extraModules != null && extraModules.length > 0) {
            og = mObjectGraph.plus(extraModules);
        }
        og.inject(obj);
    }
}
