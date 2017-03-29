package com.example.carludren.darkweather.Weather;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by carludren on 3/22/17.
 */

public class Day implements Parcelable {
    private long mTime;
    private String mSummary;
    private double mTemperatureMax;
    private double mTemperatureMin;
    private String mIcon;
    private String mTimeZone;

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public int getTemperatureMax() {
        return (int) Math.round(mTemperatureMax);
    }

    public void setTemperatureMax(double temperatureMax) {
        mTemperatureMax = temperatureMax;
    }

    public double getTemperatureMin() {
        return (int) Math.round(mTemperatureMin);
    }

    public void setTemperatureMin(double temperatureMin) {
        mTemperatureMin = temperatureMin;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public int getIconId() {
        return Forecast.getIconId(mIcon);
    }

    public String getDayOfWeek() {
        SimpleDateFormat format = new SimpleDateFormat("EEEE");
        format.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date date = new Date(mTime * 1000);
        return format.format(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mTime);
        dest.writeString(mIcon);
        dest.writeString(mSummary);
        dest.writeString(mTimeZone);
        dest.writeDouble(mTemperatureMax);
        dest.writeDouble(mTemperatureMin);
    }

    public Day() {}

    private Day(Parcel in) {
        mTime = in.readLong();
        mIcon = in.readString();
        mSummary = in.readString();
        mTimeZone = in.readString();
        mTemperatureMax = in.readDouble();
        mTemperatureMin = in.readDouble();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
