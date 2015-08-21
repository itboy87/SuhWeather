package com.suh.itboy.suhweather.Network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.suh.itboy.suhweather.Interfaces.WeatherDataSync;
import com.suh.itboy.suhweather.Utils.WeatherUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by itboy on 8/20/2015.
 *
 */
public class WeatherSync extends AsyncTask<String, Void, String[]> {
    public static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";
    public static final String TAG = WeatherSync.class.getSimpleName();
    WeatherDataSync dataSyncListener;

    public WeatherSync(WeatherDataSync dataSyncListener) {
        this.dataSyncListener = dataSyncListener;
    }

    @Override
    protected String[] doInBackground(String... params) {
        String units = "matric";
        String mode = "json";
        int days = 7;
        try {
            Thread.sleep(2000);
            Uri uri = Uri.parse(WEATHER_BASE_URL).buildUpon()
                    .appendQueryParameter("id", params[0])
                    .appendQueryParameter("units", units)
                    .appendQueryParameter("mode", mode)
                    .appendQueryParameter("cnt", String.valueOf(days)).build();

            URL url = new URL(uri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null)
                return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            if (stringBuffer.length() == 0)
                return null;

            urlConnection.disconnect();
            bufferedReader.close();

            Log.d(TAG, stringBuffer.toString());

            return WeatherUtil
                    .getWeatherDataFromJson(stringBuffer.toString(), days);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String[] forecastArray) {
        dataSyncListener.onWeatherDataSynced(forecastArray);
    }
}
