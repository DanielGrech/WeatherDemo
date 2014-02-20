package com.dgsd.android.weatherdemo.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.ButterKnife;
import com.dgsd.android.weatherdemo.R;
import com.dgsd.android.weatherdemo.api.WeatherCodeMap;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.util.WeatherIconManager;
import com.larvalabs.svgandroid.SVGParser;

/**
 * Displays the information from a single forecast in a concise way
 */
public class ForecastSummaryView extends LinearLayout {

    @InjectView(R.id.date)
    protected TextView mDate;

    @InjectView(R.id.icon)
    protected ImageView mImage;

    @InjectView(R.id.temperature)
    protected TextView mTemperature;

    public ForecastSummaryView(final Context context) {
        super(context);
    }

    public ForecastSummaryView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ForecastSummaryView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        mImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    /**
     * Populate the view with the information found in <code>forecast</code>
     *
     * @param forecast The forecast to represent in the view
     */
    public void populate(Forecast forecast) {
        mTemperature.setText(getContext().getString(R.string.x_degrees, (int) forecast.getMaxTemp()));

        Time time = new Time();
        time.set(forecast.getDate() * 1000);
        time.normalize(true);
        mDate.setText(getDayString(time.weekDay));

        final Drawable icon = WeatherIconManager.get(getContext(), forecast.getWeatherCode());
        if (icon == null) {
            mImage.setVisibility(View.GONE);
        } else {
            mImage.setVisibility(View.VISIBLE);
            mImage.setImageDrawable(icon);
        }
    }

    /**
     * @param dayOfWeek The day of the week [0-6] of the weekday to retrieve
     * @return A string representing the given weekday
     * @see {@link android.text.format.Time}
     */
    private String getDayString(int dayOfWeek) {
        final String[] daysArr = getContext().getResources().getStringArray(R.array.days_of_week_abbrev);
        return daysArr[dayOfWeek];
    }
}
