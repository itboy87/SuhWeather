package com.suh.itboy.suhweather.Network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by itboy on 8/20/2015.
 */
public class WeatherSync extends AsyncTask<String, Integer, String> {
    public static final String TAG = WeatherSync.class.getSimpleName();
    HttpURLConnection urlConnection;
    BufferedReader bufferedReader;

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            if (inputStream == null)
                return null;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuffer.append(line);
            }

            if (stringBuffer.length() == 0)
                return null;

            Log.d(TAG, stringBuffer.toString());
            return stringBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
