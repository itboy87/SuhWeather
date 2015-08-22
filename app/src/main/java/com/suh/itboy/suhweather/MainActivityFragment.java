package com.suh.itboy.suhweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.suh.itboy.suhweather.Interfaces.WeatherDataSync;
import com.suh.itboy.suhweather.Network.WeatherSync;
import com.suh.itboy.suhweather.Utils.WeatherUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements WeatherDataSync {

    public static final String TAG = MainActivityFragment.class.getSimpleName();
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

        updateWeatherData();

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
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_refresh:
                updateWeatherData();
                break;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onWeatherDataSynced(String[] forecastArray) {

        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if (null == forecastArray) {
            Toast.makeText(getActivity(), WeatherUtil.getWeatherDataParseError(), Toast.LENGTH_SHORT).show();
        } else {
            List<String> weakForecast = new ArrayList<>(Arrays.asList(forecastArray));
            mForecastAdapter.clear();
            mForecastAdapter.addAll(weakForecast);
        }

    }

    public void updateWeatherData() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String cityId = preferences.getString(getString(R.string.pref_city_key), getString(R.string.pref_city_value));
        Log.d(TAG, cityId);
        WeatherSync weatherSync = new WeatherSync(this);
        weatherSync.execute(cityId);
    }
}
