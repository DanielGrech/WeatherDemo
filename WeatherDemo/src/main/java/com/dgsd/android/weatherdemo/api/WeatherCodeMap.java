package com.dgsd.android.weatherdemo.api;

import com.dgsd.android.weatherdemo.R;

import java.util.HashMap;

/**
 * Mapping of weather codes as outlined by
 * <a href="http://www.worldweatheronline.com/feed/wwoConditionCodes.txt">WeatherWorldOnline</a>
 */
public class WeatherCodeMap {

    private static final int NO_DESCRIPTION = -1;

    private static final HashMap<Integer, WeatherCodeInfo> MAP = new HashMap<>();

    static {
        MAP.put(395, new WeatherCodeInfo(R.string.heavy_snow_with_thunder, WeatherCodeGroup.HEAVY_SNOW));
        MAP.put(392, new WeatherCodeInfo(R.string.light_snow_with_thunder, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(389, new WeatherCodeInfo(R.string.heavy_rain_with_thunder, WeatherCodeGroup.THUNDER));
        MAP.put(386, new WeatherCodeInfo(R.string.light_rain_with_thunder, WeatherCodeGroup.THUNDER));
        MAP.put(377, new WeatherCodeInfo(R.string.hail, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(374, new WeatherCodeInfo(R.string.hail, WeatherCodeGroup.HAIL));
        MAP.put(371, new WeatherCodeInfo(R.string.moderate_snow, WeatherCodeGroup.HEAVY_SNOW));
        MAP.put(368, new WeatherCodeInfo(R.string.light_snow_fall, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(365, new WeatherCodeInfo(R.string.heavy_sleet_showers, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(362, new WeatherCodeInfo(R.string.light_sleet_showers, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(359, new WeatherCodeInfo(R.string.torrential_rain, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(356, new WeatherCodeInfo(R.string.moderate_to_heavy_rain, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(353, new WeatherCodeInfo(R.string.light_shower, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(350, new WeatherCodeInfo(R.string.hail, WeatherCodeGroup.HAIL));
        MAP.put(338, new WeatherCodeInfo(R.string.heavy_snow, WeatherCodeGroup.HEAVY_SNOW));
        MAP.put(335, new WeatherCodeInfo(R.string.patchy_heavy_snow, WeatherCodeGroup.HEAVY_SNOW));
        MAP.put(332, new WeatherCodeInfo(R.string.moderate_snow, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(329, new WeatherCodeInfo(R.string.moderate_snow, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(326, new WeatherCodeInfo(R.string.light_snow, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(323, new WeatherCodeInfo(R.string.patchy_light_snow, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(320, new WeatherCodeInfo(R.string.moderate_sleet, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(317, new WeatherCodeInfo(R.string.light_sleet, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(314, new WeatherCodeInfo(R.string.moderate_freezing_rain, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(311, new WeatherCodeInfo(R.string.light_freezing_rain, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(308, new WeatherCodeInfo(R.string.heavy_rain, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(305, new WeatherCodeInfo(R.string.heavy_rain_at_times, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(302, new WeatherCodeInfo(R.string.moderate_rain, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(299, new WeatherCodeInfo(R.string.moderate_rain_at_times, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(296, new WeatherCodeInfo(R.string.light_rain, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(293, new WeatherCodeInfo(R.string.patchy_light_rain, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(284, new WeatherCodeInfo(R.string.heavy_freezing_drizzle, WeatherCodeGroup.HEAVY_RAIN));
        MAP.put(281, new WeatherCodeInfo(R.string.freezing_drizzle, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(266, new WeatherCodeInfo(R.string.light_drizzle, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(263, new WeatherCodeInfo(R.string.patchy_light_drizzle, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(260, new WeatherCodeInfo(R.string.freezing_fog, WeatherCodeGroup.FOG));
        MAP.put(248, new WeatherCodeInfo(R.string.fog, WeatherCodeGroup.FOG));
        MAP.put(230, new WeatherCodeInfo(R.string.blizzard, WeatherCodeGroup.HEAVY_SNOW));
        MAP.put(227, new WeatherCodeInfo(R.string.blowing_snow, WeatherCodeGroup.HEAVY_SNOW));
        MAP.put(200, new WeatherCodeInfo(R.string.thundery_outbreaks, WeatherCodeGroup.THUNDER));
        MAP.put(185, new WeatherCodeInfo(R.string.patch_freezing_drizzle_nearby, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(182, new WeatherCodeInfo(R.string.patchy_sleet_nearby, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(179, new WeatherCodeInfo(R.string.patch_snow_nearby, WeatherCodeGroup.LIGHT_SNOW));
        MAP.put(176, new WeatherCodeInfo(R.string.patchy_rain_nearby, WeatherCodeGroup.LIGHT_RAIN));
        MAP.put(143, new WeatherCodeInfo(R.string.mist, WeatherCodeGroup.OVERCAST));
        MAP.put(122, new WeatherCodeInfo(R.string.overcast, WeatherCodeGroup.OVERCAST));
        MAP.put(119, new WeatherCodeInfo(R.string.cloudy, WeatherCodeGroup.OVERCAST));
        MAP.put(116, new WeatherCodeInfo(R.string.partly_cloudy, WeatherCodeGroup.OVERCAST));
        MAP.put(113, new WeatherCodeInfo(R.string.sunny, WeatherCodeGroup.SUNNY));
    }

    private WeatherCodeMap() {
    }

    public static WeatherCodeGroup getGroup(int code) {
        WeatherCodeInfo info = MAP.get(code);
        return info == null ?
                WeatherCodeGroup.UNKNOWN : info.group;
    }

    public static int getDescriptionResource(int code) {
        WeatherCodeInfo info = MAP.get(code);
        return info == null ?
                NO_DESCRIPTION : info.descriptionResCode;
    }

    private static class WeatherCodeInfo {
        private final int descriptionResCode;
        private final WeatherCodeGroup group;

        public WeatherCodeInfo(int descriptionResCode, WeatherCodeGroup group) {
            this.descriptionResCode = descriptionResCode;
            this.group = group;
        }
    }
}
