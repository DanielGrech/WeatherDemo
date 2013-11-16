package com.dgsd.android.weatherdemo.util;

import android.text.TextUtils;

/**
 * Created by daniel on 16/11/2013.
 */
public class EnumUtils {

    public static <T extends Enum> T from(Class<T> cls, String value) {
        if (cls.isEnum()) {
            T[] values = cls.getEnumConstants();
            for (T val : values) {
                if (TextUtils.equals(val.toString(), value)) {
                    return val;
                }
            }
        }

        return null;
    }

    public static <T extends Enum> T from(Class<T> cls, int ordinal) {
        if (cls.isEnum()) {
            T[] values = cls.getEnumConstants();
            for (T val : values) {
                if (val.ordinal() == ordinal) {
                    return val;
                }
            }
        }

        return null;
    }

}
