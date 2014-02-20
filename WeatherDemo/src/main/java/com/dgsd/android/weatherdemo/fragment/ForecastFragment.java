package com.dgsd.android.weatherdemo.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.ButterKnife;
import com.dgsd.android.weatherdemo.R;
import com.dgsd.android.weatherdemo.data.ForecastLoader;
import com.dgsd.android.weatherdemo.model.Forecast;
import com.dgsd.android.weatherdemo.util.WeatherIconManager;
import com.dgsd.android.weatherdemo.view.ForecastSummaryView;

import java.util.List;

/**
 *
 */
public class ForecastFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Forecast>> {

    private static final DecelerateInterpolator ANIM_DECELERATE_INTERPOLATOR = new DecelerateInterpolator(1.5f);
    private static final int ANIM_TRANSLATION_AMOUNT = 100;
    private static final float ANIM_TODAY_TEMP_SCALE = 1.3f;

    private static final int LOADER_ID_FORECASTS = 0x01;

    @InjectView(R.id.todays_weather)
    protected TextView mTodaysMax;

    @InjectView(R.id.todays_weather_subtext)
    protected TextView mTodaysSubtext;

    @InjectView(R.id.todays_date)
    protected TextView mTodaysDate;

    @InjectView(R.id.todays_weather_description)
    protected TextView mTodaysDescription;

    @InjectView(R.id.wind_dir_wrapper)
    protected ViewGroup mWindDirWrapper;

    @InjectView(R.id.wind_icon)
    protected ImageView mWindIcon;

    @InjectView(R.id.today_wind_speed)
    protected TextView mTodayWindSpeed;

    @InjectView(R.id.today_wind_direction)
    protected ImageView mTodayWindDirection;

    @InjectView(R.id.forecast_summary_group)
    protected ViewGroup mForecastSummaryGroup;

    @InjectView(R.id.forecast1)
    protected ForecastSummaryView mForecast1;

    @InjectView(R.id.forecast2)
    protected ForecastSummaryView mForecast2;

    @InjectView(R.id.forecast3)
    protected ForecastSummaryView mForecast3;

    @InjectView(R.id.forecast4)
    protected ForecastSummaryView mForecast4;

    private boolean mIsFullView;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.frag_forecast, container, false);
        ButterKnife.inject(this, v);

        //Setup the initial animation state of our views
        mTodaysDate.setAlpha(0);
        mTodaysDate.setTranslationX(ANIM_TRANSLATION_AMOUNT);

        mTodaysSubtext.setAlpha(0);
        mTodaysSubtext.setTranslationX(-ANIM_TRANSLATION_AMOUNT);

        mForecastSummaryGroup.setAlpha(0f);
        mForecastSummaryGroup.setTranslationY(ANIM_TRANSLATION_AMOUNT);

        mWindDirWrapper.setAlpha(0f);
        mWindIcon.setImageDrawable(WeatherIconManager.getSvgDrawable(getActivity(), R.raw.wind));

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        reload(LOADER_ID_FORECASTS, this);
    }

    @Override
    public Loader<List<Forecast>> onCreateLoader(final int id, final Bundle args) {
        return new ForecastLoader(getActivity());
    }

    @Override
    public void onLoadFinished(final Loader<List<Forecast>> loader, final List<Forecast> data) {
        if (data == null || data.isEmpty()) {
            //TODO: Hide all and show empty view
        } else {
            populate(data);
        }
    }

    @Override
    public void onLoaderReset(final Loader<List<Forecast>> loader) {

    }

    private void populate(List<Forecast> forecasts) {
        final ForecastSummaryView[] views = {
                mForecast1, mForecast2, mForecast3, mForecast4
        };

        int currentView = 0;
        for (Forecast f : forecasts) {
            if (f.isToday()) {
                populateToday(f);
            } else if (currentView < views.length) {
                final ForecastSummaryView view = views[currentView++];
                view.populate(f);
            }
        }


        //Hide any extra views we dont have data for
        if (currentView < views.length) {
            for (int i = currentView, len = views.length; i < len; i++) {
                views[i].setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.container)
    public void onScreenClicked() {
        if (mIsFullView) {
            fadeOut();
        } else {
            fadeIn();
        }

        mIsFullView = !mIsFullView;
        mEventBus.post(new OnFullViewToggleEvent(mIsFullView));
    }

    private void fadeOut() {
        mTodaysMax.animate()
                .scaleX(ANIM_TODAY_TEMP_SCALE).scaleY(ANIM_TODAY_TEMP_SCALE)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mWindDirWrapper.animate()
                .alpha(0f)
                .setStartDelay(100)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mTodaysDate.animate()
                .alpha(0f)
                .setStartDelay(100)
                .translationX(ANIM_TRANSLATION_AMOUNT)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mTodaysSubtext.animate()
                .alpha(0f)
                .setStartDelay(100)
                .translationX(-ANIM_TRANSLATION_AMOUNT)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mForecastSummaryGroup.animate()
                .alpha(0)
                .setStartDelay(100)
                .translationY(ANIM_TRANSLATION_AMOUNT)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);
    }

    private void fadeIn() {
        mTodaysMax.animate()
                .scaleX(1f).scaleY(1f)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mWindDirWrapper.animate()
                .alpha(1f)
                .setStartDelay(100)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mTodaysDate.animate()
                .alpha(1f)
                .translationX(0)
                .setStartDelay(100)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mTodaysSubtext.animate()
                .alpha(1f)
                .translationX(0)
                .setStartDelay(100)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);

        mForecastSummaryGroup.animate()
                .alpha(1f)
                .translationY(0)
                .setStartDelay(100)
                .setInterpolator(ANIM_DECELERATE_INTERPOLATOR);
    }


    private void populateToday(Forecast today) {
        mTodaysSubtext.setText(getString(R.string.max_x_degrees_min, (int) today.getMinTemp()));
        mTodayWindSpeed.setText(getString(R.string.x_kmph, today.getWindSpeed()));

        if (today.getWindDirectionAngle() == 0) {
            mTodayWindDirection.setVisibility(View.GONE);
        } else {
            mTodayWindDirection.setVisibility(View.VISIBLE);
            mTodayWindDirection.setRotation(today.getWindDirectionAngle());
        }

        //On our first load, let's do a nice animation!
        final String desc = today.buildWeatherDescription();
        final String todaysMaxStr = getString(R.string.x_degrees, (int) today.getMaxTemp());
        if (TextUtils.isEmpty(mTodaysMax.getText())) {
            mTodaysMax.setAlpha(0f);
            mTodaysMax.setScaleX(0.75f);
            mTodaysMax.setScaleY(0.75f);

            mTodaysMax.animate().alpha(1f)
                    .scaleX(ANIM_TODAY_TEMP_SCALE)
                    .scaleY(ANIM_TODAY_TEMP_SCALE)
                    .setStartDelay(200)
                    .setDuration(800)
                    .setInterpolator(ANIM_DECELERATE_INTERPOLATOR)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(final Animator animation) {
                            mTodaysMax.setText(todaysMaxStr);

                            if (TextUtils.isEmpty(desc)) {
                                mTodaysDescription.setVisibility(View.INVISIBLE);
                            } else {
                                mTodaysDescription.setText(desc);
                                mTodaysDescription.setVisibility(View.VISIBLE);
                            }
                        }
                    }).start();
        } else {
            mTodaysMax.setText(todaysMaxStr);

            if (TextUtils.isEmpty(desc)) {
                mTodaysDescription.setVisibility(View.INVISIBLE);
            } else {
                mTodaysDescription.setText(desc);
                mTodaysDescription.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class OnFullViewToggleEvent {
        public final boolean fullViewVisible;

        public OnFullViewToggleEvent(boolean fullViewVisible) {
            this.fullViewVisible = fullViewVisible;
        }
    }
}
