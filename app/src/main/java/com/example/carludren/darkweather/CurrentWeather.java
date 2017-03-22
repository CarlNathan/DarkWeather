package com.example.carludren.darkweather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by carludren on 3/21/17.
 */

public class CurrentWeather {
    private String mIcon;
    private long mTime;
    private double mTemperature;
    private double mHumidity;
    private double mPrecipChance;
    private String mSummary;
    private String mTimeZone;

    public String getTimeZone() {
        return mTimeZone;
    }

    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
    }

    public String getFormattedTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(mTimeZone));
        Date dateTime = new Date(mTime * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getIconId() {
        int icon;
        switch (mIcon) {
            case "clear-day": icon = R.drawable.clear_day;
                break;
            case "clear-night": icon = R.drawable.clear_night;
                break;
            case "rain": icon = R.drawable.rain;
                break;
            case "snow": icon = R.drawable.snow;
                break;
            case "sleet": icon = R.drawable.sleet;
                break;
            case "wind": icon = R.drawable.wind;
                break;
            case "fog": icon = R.drawable.fog;
                break;
            case "cloudy": icon = R.drawable.cloudy;
                break;
            case "partly-cloudy-day": icon = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night": icon = R.drawable.cloudy_night;
                break;
            default: icon = R.drawable.clear_day;
                break;

        }
        return icon;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public int getTemperature() {
        return (int) Math.round(mTemperature);
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public int getHumidity() {
        int percentage = (int) Math.round(mHumidity * 100);
        return percentage;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public int getPrecipChance() {
        int percentage = (int) Math.round(mPrecipChance * 100);
        return percentage;
    }

    public void setPrecipChance(double precipChance) {
        mPrecipChance = precipChance;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }
}
