package com.dgsd.android.weatherdemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import timber.log.Timber;

import java.lang.reflect.Modifier;

public class Db extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DB_NAME = "weather.db";

    private static Db sInstance;

    public static final Object[] LOCK = new Object[0];

    public static Db get(Context c) {
        if (sInstance == null)
            sInstance = new Db(c);

        return sInstance;
    }

    protected Db(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        runForEachTable(new TableRunnable() {
            @Override
            public void run(final DbTable table) {
                db.execSQL(table.getCreateSql());
            }
        });
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

    private void runForEachTable(TableRunnable runnable) {
        java.lang.reflect.Field[] declaredFields = Db.Table.class.getDeclaredFields();
        for (java.lang.reflect.Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(DbTable.class)) {
                try {
                    runnable.run((DbTable) field.get(null));
                } catch (IllegalAccessException e) {
                    Timber.e(e, "Error executing table runnable: " + field.getName());
                }
            }
        }
    }

    private interface TableRunnable {
        public void run(DbTable table);
    }

    public static class Field {
        private Field() {
        }

        public static final DbField ID = new DbField("_id", "integer", "primary key");
        public static final DbField DATE_STR = new DbField("date_str", "text");
        public static final DbField DATE = new DbField("date", "integer");
        public static final DbField MAX_TEMP = new DbField("max", "real");
        public static final DbField MIN_TEMP = new DbField("min", "real");
        public static final DbField DESCRIPTION = new DbField("desc", "text");
        public static final DbField WEATHER_CODE = new DbField("weather_code", "integer");
        public static final DbField WIND_DIR_CODE = new DbField("wind_dir_code", "integer");
        public static final DbField WIND_DIR_ANGLE = new DbField("wind_dir_angle", "integer");
        public static final DbField WIND_SPEED = new DbField("wind_speed", "integer");
    }

    public static class Table {
        private Table() {
        }

        public static final DbTable FORECASTS = DbTable.with("forecasts")
                .columns(Db.Field.ID,
                        Db.Field.DATE_STR,
                        Db.Field.DATE,
                        Db.Field.MAX_TEMP,
                        Db.Field.MIN_TEMP,
                        Db.Field.DESCRIPTION,
                        Db.Field.WEATHER_CODE,
                        Db.Field.WIND_DIR_CODE,
                        Db.Field.WIND_DIR_ANGLE,
                        Db.Field.WIND_SPEED)
                .scripts("CREATE UNIQUE INDEX unique_forecast_by_date ON forecasts(" + Db.Field.DATE_STR + ")")
                .create();
    }
}