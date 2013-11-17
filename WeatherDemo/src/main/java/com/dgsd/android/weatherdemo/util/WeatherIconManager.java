package com.dgsd.android.weatherdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import com.dgsd.android.weatherdemo.R;
import com.dgsd.android.weatherdemo.api.WeatherCodeMap;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * Helper class to make displaying the correct icon for a given weather code easier
 */
public class WeatherIconManager {

    /**
     * @param context
     * @param weatherCode The weather code to retrieve an icon for
     * @return A drawable for the given weather code, or null if there is none
     */
    public static Drawable get(Context context, int weatherCode) {
        int imageRes = -1;
        switch (WeatherCodeMap.getGroup(weatherCode)) {
            case SUNNY:
                imageRes = R.raw.sun;
                break;
            case HEAVY_RAIN:
                imageRes = R.raw.cloud_rain;
                break;
            case LIGHT_RAIN:
                imageRes = R.raw.cloud_drizzle;
                break;
            case HEAVY_SNOW:
                imageRes = R.raw.cloud_snow;
                break;
            case LIGHT_SNOW:
                imageRes = R.raw.cloud_snow_heavy;
                break;
            case OVERCAST:
                imageRes = R.raw.cloud;
                break;
            case THUNDER:
                imageRes = R.raw.cloud_thunder;
                break;
            case HAIL:
                imageRes = R.raw.cloud_hail;
                break;
            case FOG:
                imageRes = R.raw.cloud_fog;
                break;
        }

        if (imageRes > 0) {
            return getSvgDrawable(context, imageRes);
        } else {
            return null;
        }
    }

    /**
     * Retrieve a drawable from a raw (<code>R.raw.*</code>) resource pointing to an SVG
     *
     * @param context
     * @param imageRes The raw resource id to retrieve
     * @return A drawable representing the SVG
     */
    public static Drawable getSvgDrawable(final Context context, final int imageRes) {
        final SVG svg = SVGParser.getSVGFromResource(context.getResources(), imageRes);
        Picture picture = svg.getPicture();
        Bitmap bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        PictureDrawable drawable = new PictureDrawable(picture);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
