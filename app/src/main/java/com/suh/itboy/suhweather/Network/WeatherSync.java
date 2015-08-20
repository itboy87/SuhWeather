package com.suh.itboy.suhweather.Network;

import android.os.AsyncTask;
import android.util.Log;

import com.suh.itboy.suhweather.Interfaces.WeatherDataSync;

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
public class WeatherSync extends AsyncTask<String, Integer, String> {
    public static final String TAG = WeatherSync.class.getSimpleName();
    WeatherDataSync dataSyncListener;

    public WeatherSync(WeatherDataSync dataSyncListener) {
        this.dataSyncListener = dataSyncListener;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Thread.sleep(2000);
            URL url = new URL(params[0]);
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
            return stringBuffer.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        dataSyncListener.onWeatherDataSynced(s);
    }
}
