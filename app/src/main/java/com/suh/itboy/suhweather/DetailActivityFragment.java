package com.suh.itboy.suhweather;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String forecast = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView detail_text = (TextView) rootView.findViewById(R.id.detail_text);
        detail_text.setText(forecast);
        return rootView;
    }
}