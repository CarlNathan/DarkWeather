package com.example.carludren.darkweather.Weather;

import com.example.carludren.darkweather.R;

/**
 * Created by carludren on 3/22/17.
 */

public class Forecast {

    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDailyForecast;

    public Current getCurrent() {
        return mCurrent;
    }

    public void setCurrent(Current current) {
        mCurrent = current;
    }

    public Hour[] getHourlyForecast() {
        return mHourlyForecast;
    }

    public void setHourlyForecast(Hour[] hourlyForecast) {
        mHourlyForecast = hourlyForecast;
    }

    public Day[] getDailyForecast() {
        return mDailyForecast;
    }

    public void setDailyForecast(Day[] dailyForecast) {
        mDailyForecast = dailyForecast;
    }

    public static int getIconId(String iconString) {
        int icon;
        switch (iconString) {
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
}
