package com.suh.itboy.suhweather.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.suh.itboy.suhweather.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by itboy on 8/20/2015.
 */
public class WeatherUtil {
    public static Context context;
    private static String error_message = "";
    public static String getCurrentReadableDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd");
        return dateFormat.format(c.getTime());
    }

    public static String formatLowHighs(double low, double high) {
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        preference.getString(context.getString(R.string.pref_unit_key), context.getString(R.string.pref_unit_metric));

        return String.valueOf(Math.round(low)) + "/" + String.valueOf(Math.round(high));
    }

    public static String[] getWeatherDataFromJson(String forecastString, int numDays) {
        error_message = "";

        final String W_LIST = "list";
        final String W_WEATHER = "weather";
        final String W_TEMP = "temp";
        final String W_MAX = "max";
        final String W_MIN = "min";
        final String W_MAIN = "main";
//        final String W_DESCRIPTION = "description";

        if (forecastString != null) {
            String daysArray[] = new String[numDays];
            try {
                JSONObject weatherJson = new JSONObject(forecastString);
                JSONArray weatherDays = weatherJson.getJSONArray(W_LIST);

                for (int i = 0; i < weatherDays.length(); i++) {
                    JSONObject forecastDay = weatherDays.getJSONObject(i);
                    JSONArray weather = forecastDay.getJSONArray(W_WEATHER);
                    JSONObject temp = forecastDay.getJSONObject(W_TEMP);

                    double low = temp.getDouble(W_MIN);
                    double high = temp.getDouble(W_MAX);
                    String main = weather.getJSONObject(0).getString(W_MAIN);

                    daysArray[i] =
                            WeatherUtil.getCurrentReadableDate()
                                    + " - " + main + " - " +
                                    WeatherUtil.formatLowHighs(low, high);
                }

                return daysArray;

            } catch (JSONException e) {
                try {
                    error_message = new JSONObject(forecastString).getString("message");
//                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                } catch (JSONException e1) {
//                    Toast.makeText(getActivity(), "Unknown Error Parsing JSON", Toast.LENGTH_SHORT).show();
                    error_message = "Unknown Error Parsing JSON";
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getWeatherDataParseError() {
        return error_message;
    }
}
