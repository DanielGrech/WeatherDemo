package com.dgsd.android.weatherdemo.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * Forecast description
 */
public class WeatherDescription implements Parcelable {

    @SerializedName("value")
    private String mValue;

    public String getValue() {
        return mValue;
    }

    public void setValue(final String value) {
        mValue = value;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mValue);
    }

    public static final Creator<WeatherDescription> CREATOR = new Creator<WeatherDescription>() {
        public WeatherDescription createFromParcel(Parcel in) {
            final WeatherDescription d = new WeatherDescription();
            d.mValue = in.readString();
            return d;
        }

        public WeatherDescription[] newArray(int size) {
            return new WeatherDescription[size];
        }
    };

}
