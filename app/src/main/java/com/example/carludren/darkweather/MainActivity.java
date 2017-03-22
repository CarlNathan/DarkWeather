package com.example.carludren.darkweather;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.carludren.darkweather.UI.AlertDialogFragment;
import com.example.carludren.darkweather.Weather.Current;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private Current mCurrent;
    @BindView(R.id.tempView) TextView mTemperatureLabel;
    @BindView(R.id.dateView) TextView mDateLabel;
    @BindView(R.id.humidityValueLabel) TextView mHumidityLabel;
    @BindView(R.id.rainValueLabel) TextView mRainLabel;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.iconImageVIew) ImageView mImageView;
    @BindView(R.id.refreshButton) ImageView mRefreshButton;
    @BindView(R.id.refreshSpinner) ProgressBar mRefreshSpinner;

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
                            mCurrent = getCurrentDetails(jsonData);
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
        mTemperatureLabel.setText(String.valueOf(mCurrent.getTemperature()));
        mDateLabel.setText(String.valueOf(mCurrent.getFormattedTime()));
        mHumidityLabel.setText(mCurrent.getHumidity() + "%");
        mRainLabel.setText(mCurrent.getPrecipChance() + "%");
        mSummaryLabel.setText(String.valueOf(mCurrent.getSummary()));
        Drawable drawable = ContextCompat.getDrawable(this, mCurrent.getIconId());
        mImageView.setImageDrawable(drawable);
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();

        current.setTime(currently.getLong("time"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setHumidity(currently.getDouble("humidity"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setIcon(currently.getString("icon"));
        current.setTimeZone(forecast.getString("timezone"));

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
}
