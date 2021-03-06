package com.dgsd.android.weatherdemo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import com.google.gson.annotations.SerializedName;
import timber.log.Timber;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Forecast for a single day
 */
public class Forecast implements Parcelable {

    private transient long mId = -1;

    @SerializedName("date")
    private String mDateStr;

    @SerializedName("tempMaxC")
    private float mMaxTemp;

    @SerializedName("tempMinC")
    private float mMinTemp;

    @SerializedName("weatherDesc")
    private List<WeatherDescription> mWeatherDescription;

    @SerializedName("weatherCode")
    private int mWeatherCode;

    @SerializedName("winddirection")
    private WindDirection mWindDirectionCode;

    @SerializedName("winddirDegree")
    private int mWindDirectionAngle;

    @SerializedName("windspeedKmph")
    private int mWindSpeed;

    private transient long mDate = -1;

    public long getId() {
        return mId;
    }

    public void setId(final long id) {
        mId = id;
    }

    public float getMaxTemp() {
        return mMaxTemp;
    }

    public void setMaxTemp(final float maxTemp) {
        mMaxTemp = maxTemp;
    }

    public float getMinTemp() {
        return mMinTemp;
    }

    public void setMinTemp(final float minTemp) {
        mMinTemp = minTemp;
    }

    public List<WeatherDescription> getWeatherDescription() {
        return mWeatherDescription;
    }

    public void setWeatherDescription(final List<WeatherDescription> weatherDescription) {
        mWeatherDescription = weatherDescription;
    }

    public void addWeatherDescriptionLine(WeatherDescription desc) {
        if (mWeatherDescription == null) {
            mWeatherDescription = new ArrayList<>(1);
        }

        mWeatherDescription.add(desc);
    }

    public int getWeatherCode() {
        return mWeatherCode;
    }

    public void setWeatherCode(final int weatherCode) {
        mWeatherCode = weatherCode;
    }

    public WindDirection getWindDirectionCode() {
        return mWindDirectionCode;
    }

    public void setWindDirectionCode(final WindDirection windDirectionCode) {
        mWindDirectionCode = windDirectionCode;
    }

    public int getWindDirectionAngle() {
        return mWindDirectionAngle;
    }

    public void setWindDirectionAngle(final int windDirectionAngle) {
        mWindDirectionAngle = windDirectionAngle;
    }

    public long getDate() {
        if (mDate < 0 && !TextUtils.isEmpty(mDateStr)) {
            DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d = dfm.parse(mDateStr);
                mDate = d.getTime() / 1000;
            } catch (ParseException e) {
                Timber.e("Error parsing datestring: " + mDateStr, e);
            }
        }
        return mDate;
    }

    public void setDate(final long date) {
        mDate = date;
    }

    public String getDateStr() {
        return mDateStr;
    }

    public void setDateStr(String dateStr) {
        mDateStr = dateStr;
    }

    public int getWindSpeed() {
        return mWindSpeed;
    }

    public void setWindSpeed(final int windSpeed) {
        mWindSpeed = windSpeed;
    }

    public String buildWeatherDescription() {
        final StringBuilder desc = new StringBuilder();
        if (mWeatherDescription != null) {
            for (int i = 0, size = mWeatherDescription.size(); i < size; i++) {
                if (i != 0) {
                    desc.append('\n');
                }
                desc.append(mWeatherDescription.get(i).getValue());
            }
        }

        return desc.toString();
    }

    public boolean isToday() {
        return DateUtils.isToday(getDate() * 1000);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mDateStr);
        dest.writeFloat(mMaxTemp);
        dest.writeFloat(mMinTemp);
        dest.writeList(mWeatherDescription);
        dest.writeInt(mWeatherCode);
        dest.writeSerializable(mWindDirectionCode);
        dest.writeInt(mWindSpeed);
        dest.writeLong(mDate);
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {
        public Forecast createFromParcel(Parcel in) {
            final Forecast f = new Forecast();

            f.mId = in.readLong();
            f.mDateStr = in.readString();
            f.mMaxTemp = in.readFloat();
            f.mMinTemp = in.readFloat();

            f.mWeatherDescription = new ArrayList<>();
            in.readList(f.mWeatherDescription, WeatherDescription.class.getClassLoader());

            f.mWeatherCode = in.readInt();
            f.mWindDirectionCode = (WindDirection) in.readSerializable();
            f.mWindSpeed = in.readInt();
            f.mDate = in.readLong();


            return f;
        }

        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };
}
