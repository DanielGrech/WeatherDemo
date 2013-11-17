package com.dgsd.android.weatherdemo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;
import com.dgsd.android.weatherdemo.R;
import com.dgsd.android.weatherdemo.api.WeatherCodeMap;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

/**
 * Created by daniel on 17/11/2013.
 */
public class WeatherIconManager {

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
