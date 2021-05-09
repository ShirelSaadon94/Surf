package com.example.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class WeatherAirFragment extends Fragment {
    private String city;
    private String descrption;
    private String temp;
    private String iconId;
    private ImageView weatherIcon;
    View view;
    private TextView textViewTemp;
    private TextView textViewdDirection;
    public static final String TAG = "pttt";
    private OnFragmentInteractionListener mListener;

    public WeatherAirFragment(String city) {
        this.city = city;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_air_weather, container, false);
        }

        initViews();

        return view;


    }
    private void initViews() {
        textViewTemp = view.findViewById(R.id.weatherfragment_LBL_weatherTemp);
        textViewdDirection = view.findViewById(R.id.weatherfragment_LBL_weatherDescription);
        weatherIcon = view.findViewById(R.id.weatherfragment_IMG_weatherIcon);


        textViewdDirection.setText(descrption);
        textViewTemp.setText(temp);
        if (iconId != null) {
            Log.d(TAG, "initViews: Getting image resource");
            String iconUrl = "https://www.weatherbit.io/static/img/icons/" + iconId + ".png";
            Log.d(TAG, "initViews: Fetching icon: " + iconUrl);
            Glide.with(weatherIcon).load(iconUrl).into(weatherIcon);
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }






}
