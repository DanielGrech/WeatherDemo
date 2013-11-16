package com.dgsd.android.weatherdemo.util;

import android.os.Build;

/**
 * Simple wrapper for dealing with Android OS versions
 */
public class Api {
    private static final String TAG = Api.class.getSimpleName();

	public static final int LEVEL = Build.VERSION.SDK_INT;
    public static final int FROYO = Build.VERSION_CODES.FROYO;
    public static final int GINGERBREAD = Build.VERSION_CODES.GINGERBREAD;
    public static final int GINGERBREAD_MR1 = Build.VERSION_CODES.GINGERBREAD_MR1;
	public static final int HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;
	public static final int ICS = Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public static final int JELLYBEAN = Build.VERSION_CODES.JELLY_BEAN;
    public static final int KITKAT = Build.VERSION_CODES.KITKAT;

	public static boolean isMin(int level) {
		return LEVEL >= level;
	}
}
