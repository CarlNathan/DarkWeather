package com.example.carludren.darkweather;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.carludren.darkweather.Weather.Day;
import com.example.carludren.darkweather.adapters.DayAdapter;

import java.util.Arrays;

public class DailyForecastActivity extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);

        Intent intent = getIntent();
        Parcelable[] parcelables = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelables, parcelables.length, Day[].class);

        DayAdapter dayAdapter = new DayAdapter(this, mDays);

        setListAdapter(dayAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String dayOfWeek = mDays[position].getDayOfWeek();
        String conditions = mDays[position].getSummary();
        String hiTemp = String.valueOf(mDays[position].getTemperatureMax());
        String lowTemp = String.valueOf(mDays[position].getTemperatureMin());

        String message = String.format("%s will be %s with a high of %s and a low of %s", dayOfWeek,
                conditions, hiTemp, lowTemp);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
