package com.dgsd.android.weatherdemo;

import android.content.Context;
import com.dgsd.android.weatherdemo.modules.AppModule;
import com.dgsd.android.weatherdemo.modules.OttoModule;

/**
 *
 */
public class DiModules {

    public static Object[] asList(Context context) {
        return new Object[]{
                new AppModule((WeatherApp) context.getApplicationContext()),
                new OttoModule()
        };
    }

}
