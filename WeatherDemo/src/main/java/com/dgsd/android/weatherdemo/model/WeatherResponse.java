package com.dgsd.android.weatherdemo.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Return value of the WorldWeatherOnline Api
 */
public class WeatherResponse implements Parcelable {

    @SerializedName("data")
    private ResponseData mData;

    public List<Forecast> getForecasts() {
        return mData == null ? null : mData.forecasts;
    }

    public void setForecasts(List<Forecast> forecasts) {
        if (mData == null) {
            mData = new ResponseData();
        }

        mData.forecasts = forecasts;
    }

    ResponseData getData() {
        return mData;
    }

    static class ResponseData implements Parcelable {
        @SerializedName("weather")
        List<Forecast> forecasts;

        @Override
        public int describeContents() {
            return hashCode();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(forecasts);
        }

        public static final Parcelable.Creator<ResponseData> CREATOR = new Parcelable.Creator<ResponseData>() {
            public ResponseData createFromParcel(Parcel in) {
                final ResponseData d = new ResponseData();
                d.forecasts = new ArrayList<>();
                in.readList(d.forecasts, ResponseData.class.getClassLoader());
                return d;
            }

            public ResponseData[] newArray(int size) {
                return new ResponseData[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mData, 0);
    }

    public static final Parcelable.Creator<WeatherResponse> CREATOR = new Parcelable.Creator<WeatherResponse>() {
        public WeatherResponse createFromParcel(Parcel in) {
            final WeatherResponse r = new WeatherResponse();
            r.mData = in.readParcelable(ResponseData.class.getClassLoader());
            return r;
        }

        public WeatherResponse[] newArray(int size) {
            return new WeatherResponse[size];
        }
    };
}
