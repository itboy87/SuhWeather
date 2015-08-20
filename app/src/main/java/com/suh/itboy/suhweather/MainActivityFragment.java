package com.suh.itboy.suhweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.suh.itboy.suhweather.Interfaces.WeatherDataSync;
import com.suh.itboy.suhweather.Network.WeatherSync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements WeatherDataSync {
    public static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?id=1164909&units=matric&mode=json&cnt=7";
    ArrayAdapter<String> mForecastAdapter;
    ProgressDialog progressDialog;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("Loading Weather Data.");
        progressDialog.show();

        WeatherSync weatherSync = new WeatherSync(this);
        weatherSync.execute(WEATHER_URL);

        /*String forecastArray[] = {
          "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHER STATION - 60/51",
                "Sun - Sunnt - 88/68"
        };

        List<String> weakForecast = new ArrayList<>(Arrays.asList(forecastArray));*/

        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.textView_forecast,
                new ArrayList<String>()
        );

        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    @Override
    public void onWeatherDataSynced(String forecastString) {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        Toast.makeText(getActivity(), forecastString, Toast.LENGTH_SHORT).show();
        String daysArray[] = {
                "Today",
                "Tomorrow",
                "Weds",
                "Thurs",
                "Fri",
                "Sat",
                "Sun"
        };

        if (forecastString != null){
            try {
                JSONObject weatherJson = new JSONObject(forecastString);
                JSONArray weatherDays = weatherJson.getJSONArray("list");
                for (int i = 0; i < weatherDays.length(); i++) {
                    JSONArray weather = weatherDays.getJSONObject(i).getJSONArray("weather");
                    String deg = weatherDays.getJSONObject(i).getString("deg");
                    String description = weather.getJSONObject(0).getString("description");
                    daysArray[i] = daysArray[i] + " - " + description + " - " + deg;
                }

                List<String> weakForecast = new ArrayList<>(Arrays.asList(daysArray));
                mForecastAdapter.clear();
                mForecastAdapter.addAll(weakForecast);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
