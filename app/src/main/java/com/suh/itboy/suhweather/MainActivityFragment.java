package com.suh.itboy.suhweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.suh.itboy.suhweather.Interfaces.WeatherDataSync;
import com.suh.itboy.suhweather.Network.WeatherSync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements WeatherDataSync {

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
        weatherSync.execute("1164909");

        /*
        String forecastArray[] = {
          "Today - Sunny - 88/63",
                "Tomorrow - Foggy - 70/40",
                "Weds - Cloudy - 72/63",
                "Thurs - Asteroids - 75/65",
                "Fri - Heavy Rain - 65/56",
                "Sat - HELP TRAPPED IN WEATHER STATION - 60/51",
                "Sun - Sunnt - 88/68"
        };

        List<String> weakForecast = new ArrayList<>(Arrays.asList(forecastArray));
        */

        mForecastAdapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.textView_forecast,
                new ArrayList<String>()
        );

        ListView listView = (ListView) rootView.findViewById(R.id.listView_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = mForecastAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);

            }
        });
        return rootView;
    }

    @Override
    public void onWeatherDataSynced(String[] forecastArray) {
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if (null == forecastArray) {
            Toast.makeText(getActivity(), "Error Fetching Weather Data!", Toast.LENGTH_SHORT).show();
        } else {
            List<String> weakForecast = new ArrayList<>(Arrays.asList(forecastArray));
            mForecastAdapter.clear();
            mForecastAdapter.addAll(weakForecast);
        }

    }
}
