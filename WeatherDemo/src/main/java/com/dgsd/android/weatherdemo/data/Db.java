package com.dgsd.android.weatherdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DB_NAME ="weather.db";

    private static Db sInstance;

    public static final Object[] LOCK = new Object[0];

    public static Db get(Context c) {
        if(sInstance == null)
            sInstance = new Db(c);

        return sInstance;
    }

    protected Db(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Support db upgrade...
        throw new IllegalStateException("We havent implemented db upgrades yet!");
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        synchronized (LOCK) {
            return super.getReadableDatabase();
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        synchronized (LOCK) {
            return super.getWritableDatabase();
        }
    }
}