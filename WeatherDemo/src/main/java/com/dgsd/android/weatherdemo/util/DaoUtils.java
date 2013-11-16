package com.dgsd.android.weatherdemo.util;

import android.content.ContentValues;
import android.database.Cursor;
import com.dgsd.android.weatherdemo.dao.ForecastDao;
import com.dgsd.android.weatherdemo.dao.IDao;
import com.dgsd.android.weatherdemo.model.Forecast;

import javax.inject.Inject;

/**
 * Created by daniel on 17/11/2013.
 */
public class DaoUtils {

    @Inject
    protected static ForecastDao sForecastDao;

    public static ContentValues convert(Object obj) {
        IDao dao = getDao(obj.getClass());
        return dao.convert(obj);
    }

    public static <T> T build(Class<T> cls, Cursor cursor) {
        IDao<T> dao = getDao(cls);
        return dao.build(cursor);
    }

    public static <T> IDao<T> getDao(Class<T> cls) {
        if (cls.equals(Forecast.class)) {
            return (IDao<T>) sForecastDao;
        } else {
            throw new IllegalArgumentException("No Dao for class: " + cls.getSimpleName());
        }
    }

}
