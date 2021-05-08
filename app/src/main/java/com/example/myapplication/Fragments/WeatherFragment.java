package com.example.myapplication.Fragments;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Fragments.WeatherAirFragment;
import com.example.myapplication.Objects.WaveForecast;
import com.example.myapplication.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class WeatherFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public static final String TAG = "pttt";
    private String city = "";
    private Location location;
    private ArrayList<WaveForecast> waveForecastArray;
    private TextView textViewWave;
    private TextView textViewWater;
    private TextView textViewSwel;
    private TextView textViewSwelDirection;
    private ImageView imageSwelDirection;
    private TextView textViewWind;
    private TextView textViewWindDirection;
    private ImageView imageWindDirection;
    private ImageView weatherIcon;
    private int water;
    private int wind;
    private int swel;
    private int swelD;
    private int windD;
    private int minWave;
    private int maxWave;
    WeatherAirFragment weatherAirFragment;
    GridLayout gridLayout;

    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_weather, container, false);
        }

        initViews();

        return view;


    }

    public WeatherFragment(int water, int wind, int swel, int swelD, int windD, int maxWave, int minWave, String city) {

        this.water = water;
        this.wind = wind;
        this.swel = swel;
        this.swelD = swelD;
        this.windD = windD;
        this.maxWave = maxWave;
        this.minWave = minWave;
        this.city = city;
    }

    @Override
    public String toString() {
        return "WeatherFragment{" +
                "water=" + water +
                ", wind=" + wind +
                ", swel=" + swel +
                ", swelD=" + swelD +
                ", windD=" + windD +
                ", minWave=" + minWave +
                ", maxWave=" + maxWave +
                '}';
    }

    public WeatherFragment() {

    }

    public Location getLocation() {
        return location;
    }

    OkHttpClient client = new OkHttpClient();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        openWeatherForecastDialog();
    }
    private void initViews() {

        Log.d(TAG, "initViews: initing weatherCard");
        Log.d(TAG, "initViews: " + this.toString());

        // Weather fragment

        textViewWave = view.findViewById(R.id.fragment_wave_cm);
        textViewWater = view.findViewById(R.id.fragment_water_temp);
        textViewWindDirection = view.findViewById(R.id.fragment_Diretion_wind);
        textViewSwel = view.findViewById(R.id.fragment_swell);
        textViewSwelDirection = view.findViewById(R.id.fragment_Diretion_swell);
        textViewWind = view.findViewById(R.id.fragment_wind);
        textViewWindDirection = view.findViewById(R.id.fragment_Diretion_wind);
        imageSwelDirection = view.findViewById(R.id.fragment_image_swelDirection);
        imageWindDirection = view.findViewById(R.id.fragment_image_windDirection);




        textViewWave.setText(""+maxWave);
        String s = convertDegreeToCardinalDirection(swelD, imageSwelDirection);
        textViewSwelDirection.setText(s);
        String w = convertDegreeToCardinalDirection(windD, imageWindDirection);
        textViewWindDirection.setText(w);
        textViewWater.setText(water + "°");
        textViewWind.setText(""+wind);
        textViewSwel.setText(""+swel);

    }

    public String convertDegreeToCardinalDirection(int directionInDegrees, ImageView d) {
        String cardinalDirection = "";
        if ((directionInDegrees >= 326.25) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 33.75)) {
            cardinalDirection = "North";
            d.setImageResource(R.drawable.d_north);
        } else if ((directionInDegrees >= 33.75) && (directionInDegrees <= 78.75)) {
            cardinalDirection = "North East";
            d.setImageResource(R.drawable.d_north_east);

        } else if ((directionInDegrees >= 78.75) && (directionInDegrees <= 101.25)) {
            cardinalDirection = "East";
            d.setImageResource(R.drawable.d_east);

        } else if ((directionInDegrees >= 101.25) && (directionInDegrees <= 168.75)) {
            cardinalDirection = "South East";
            d.setImageResource(R.drawable.d_east_south);
        } else if ((directionInDegrees >= 168.75) && (directionInDegrees <= 213.75)) {
            cardinalDirection = "South";
            d.setImageResource(R.drawable.d_south);

        } else if ((directionInDegrees >= 213.75) && (directionInDegrees <= 258.75)) {
            cardinalDirection = "South West";
            d.setImageResource(R.drawable.d_west_south);
        } else if ((directionInDegrees >= 258.75) && (directionInDegrees <= 303.75)) {
            cardinalDirection = "West";
            d.setImageResource(R.drawable.d_west);

        } else if ((directionInDegrees >= 303.75) && (directionInDegrees <= 326.25)) {
            cardinalDirection = "North West";
            d.setImageResource(R.drawable.d_north_west);

        } else {
            cardinalDirection = "?";
        }
        return cardinalDirection;
    }



    private void openWeatherForecastDialog() {
        Log.d(TAG, "openWeatherDetailsDialog: Opening forecast dialog");

        String url = "https://api.weatherbit.io/v2.0/current?city=" + city
                + "&key=" + getString(R.string.weather_icons_api_key);

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "onFailure: Exception: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                try {
                    parseJsonWeatherAir(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + responseString.toString());
            }
        });


    }

    private void parseJsonWeatherAir(String responseString) throws JSONException {
        try {
            JSONObject obj = new JSONObject(responseString);
            JSONArray containerArray = (JSONArray) obj.get("data");
            JSONObject container = (JSONObject) containerArray.get(0);
            JSONObject weatherDescriptionJson = (JSONObject) container.get("weather");

            Object realTempObject = container.get("temp");
            String myRealTemp = "";
            if (realTempObject instanceof Integer) {
                myRealTemp = "" + Math.round((int) container.get("temp"));
            } else {
                myRealTemp = "" + Math.round((double) container.get("temp"));
            }
            final String realTemp = myRealTemp;
            String iconID = (String) weatherDescriptionJson.get("icon");
            String weatherDescription = (String) weatherDescriptionJson.get("description");

            Log.d(TAG, "initViews: Getting image resource");
            String iconUrl = "https://www.weatherbit.io/static/img/icons/" + iconID + ".png";
            Log.d(TAG, "initViews: Fetching icon: " + iconUrl);

            String finalMyRealTemp = myRealTemp;
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    weatherAirFragment = new WeatherAirFragment(city, weatherDescription, iconID, finalMyRealTemp);
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.twoFragment, weatherAirFragment).commit();

                }
            });
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
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
        void messageFromParentFragment(Uri uri);
    }





}
    











