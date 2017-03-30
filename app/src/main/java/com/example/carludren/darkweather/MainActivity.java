package com.example.carludren.darkweather;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carludren.darkweather.UI.AlertDialogFragment;
import com.example.carludren.darkweather.Weather.Current;
import com.example.carludren.darkweather.Weather.Day;
import com.example.carludren.darkweather.Weather.Forecast;
import com.example.carludren.darkweather.Weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";

    private Forecast mForecast;
    @BindView(R.id.tempView) TextView mTemperatureLabel;
    @BindView(R.id.dateView) TextView mDateLabel;
    @BindView(R.id.humidityValueLabel) TextView mHumidityLabel;
    @BindView(R.id.rainValueLabel) TextView mRainLabel;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImageVIew) ImageView mImageView;
    @BindView(R.id.refreshButton) ImageView mRefreshButton;
    @BindView(R.id.refreshSpinner) ProgressBar mRefreshSpinner;
    @BindView(R.id.hourlyButton) Button mHourlyButton;
    @BindView(R.id.dailyButton) Button mDailyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRefreshSpinner.setVisibility(View.INVISIBLE);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCurrentWeather(40.0493, -75.2771);
            }
        });


        fetchCurrentWeather(40.0493, -75.2771);
    }

    private void fetchCurrentWeather(double latitude, double longitude) {
        String apiKey = "57be4ac5c8a4b592d4744d2b4d0f8f9b";
        String forecastUrl = "https://api.darksky.net/forecast/"+ apiKey + "/"
                + latitude + "," + longitude;


        if (isNetworkAvailable()) {
            showProgressBar();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String  jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mForecast = parseForecastDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hidProgressBar();
                                    updateDisplay();
                                }
                            });
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hidProgressBar();
                                }
                            });
                            alertUser(getResources().getString(R.string.error_title),
                                    getResources().getString(R.string.error_message),
                                    getResources().getString(R.string.error_ok_button_text));
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception Caught", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hidProgressBar();
                }
            });
            alertUser(getResources().getString(R.string.network_unavailable_title),
                    getResources().getString(R.string.network_unavailable_message),
                    getResources().getString(R.string.network_unavailable_ok_button_text));
        }
    }

    private void showProgressBar() {
        mRefreshSpinner.animate();
        mRefreshSpinner.setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.INVISIBLE);
    }

    private void hidProgressBar() {
        mRefreshSpinner.setVisibility(View.INVISIBLE);
        mRefreshButton.setVisibility(View.VISIBLE);
    }

    private void updateDisplay() {
        mTemperatureLabel.setText(String.valueOf(mForecast.getCurrent().getTemperature()));
        mDateLabel.setText(String.valueOf(mForecast.getCurrent().getFormattedTime()));
        mHumidityLabel.setText(mForecast.getCurrent().getHumidity() + "%");
        mRainLabel.setText(mForecast.getCurrent().getPrecipChance() + "%");
        mSummaryLabel.setText(String.valueOf(mForecast.getCurrent().getSummary()));
        Drawable drawable = ContextCompat.getDrawable(this, mForecast.getCurrent().getIconId());
        mImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyDetails(jsonData));
        forecast.setDailyForecast(getDailyDetails(jsonData));


        return forecast;
    }

    private Day[] getDailyDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray dailyData = daily.getJSONArray("data");

        Day[] days = new Day[dailyData.length()];

        for (int index = 0; index < dailyData.length(); index++) {
            JSONObject jsonDay = dailyData.getJSONObject(index);
            Day day = new Day();
            day.setTimeZone(timezone);
            day.setTime(jsonDay.getLong("time"));
            day.setSummary(jsonDay.getString("summary"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"));
            day.setTemperatureMin(jsonDay.getDouble("temperatureMin"));

            days[index] = day;
        }

        return days;
    }

    private Hour[] getHourlyDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray hourlyData = hourly.getJSONArray("data");

        Hour[] hours = new Hour[hourlyData.length()];

        for (int index = 0; index < hourlyData.length(); index++) {
            JSONObject jsonHour = hourlyData.getJSONObject(index);
            Hour hour = new Hour();
            hour.setIcon(jsonHour.getString("icon"));
            hour.setSummary(jsonHour.getString("summary"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTimeZone(timezone);
            hour.setTemperature(jsonHour.getDouble("temperature"));

            hours[index] = hour;
        }
        return hours;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");
        String timezone = forecast.getString("timezone");

        Current current = new Current();

        current.setTime(currently.getLong("time"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setHumidity(currently.getDouble("humidity"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setIcon(currently.getString("icon"));
        current.setTimeZone(timezone);

        return current;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void alertUser(String title, String message, String affirmative) {
        AlertDialogFragment dialog = new AlertDialogFragment();
        Bundle args =  new Bundle();
        args.putString(getResources().getString(R.string.alert_title_key), title);
        args.putString(getResources().getString(R.string.alert_message_key), message);
        args.putString(getResources().getString(R.string.alert_affirmative_key), affirmative);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDilayActivity(View view) {
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra(DAILY_FORECAST, mForecast.getDailyForecast());

        startActivity(intent);
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        Intent intent = new Intent(this, HourlyActivity.class);
        intent.putExtra(HOURLY_FORECAST, mForecast.getHourlyForecast());
        startActivity(intent);
    }
}
