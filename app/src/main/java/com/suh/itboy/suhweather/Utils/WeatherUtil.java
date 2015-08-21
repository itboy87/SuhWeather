package com.suh.itboy.suhweather.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by itboy on 8/20/2015.
 */
public class WeatherUtil {
    public static String getCurrentReadableDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd");
        return dateFormat.format(c.getTime());
    }

    public static String formatLowHighs(double low, double high) {
        return String.valueOf(Math.round(low)) + "/" + String.valueOf(Math.round(high));
    }

    public static String[] getWeatherDataFromJson(String forecastString, int numDays) {
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
                /*try {
                    String errorMessage = new JSONObject(forecastString).getString("message");
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                } catch (JSONException e1) {
                    Toast.makeText(getActivity(), "Unknown Error Parsing JSON", Toast.LENGTH_SHORT).show();
                    e1.printStackTrace();
                }*/
                e.printStackTrace();
            }
        }

        return null;
    }
}
