package com.example.carludren.darkweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private CurrentWeather mCurrentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "57be4ac5c8a4b592d4744d2b4d0f8f9b";
        double latitude = 37.8267;
        double longitude = -122.4233;
        String forecastUrl = "https://api.darksky.net/forecast/"+ apiKey + "/"
                + latitude + "," + longitude;


        if (isNetworkAvailable()) {
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
                            mCurrentWeather = getCurrentDetails(jsonData);
                        } else {
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
            Toast.makeText(this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
            alertUser(getResources().getString(R.string.network_unavailable_title),
                    getResources().getString(R.string.network_unavailable_message),
                    getResources().getString(R.string.network_unavailable_ok_button_text));
        }
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        JSONObject currently = forecast.getJSONObject("currently");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setIcon(currently.getString("icon"));

        return currentWeather;
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
