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
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class FragmentHeader extends Fragment {
    private static final String TAG ="ptt" ;
    OkHttpClient client;
    private String city;
    private String descrption;
    private String temp;
    private String iconId;

    private ImageView weatherIcon;
    View view;
    private TextView textViewTemp;
    private TextView textViewCity;
    private TextView textViewdDirection;
    private FragmentHeader.OnFragmentInteractionListener mListener;

    public FragmentHeader(String city) {
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        openWeatherForecastDialog();
    }
    private void initViews() {


            textViewTemp = view.findViewById(R.id.weatherfragment_LBL_weatherTemp);
            textViewdDirection = view.findViewById(R.id.weatherfragment_LBL_weatherDescription);
            textViewCity=view.findViewById(R.id.city_current);





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
            iconId = (String) weatherDescriptionJson.get("icon");
            Log.d(TAG, "parseJsonWeatherAir: "+iconId);
            String weatherDescription = (String) weatherDescriptionJson.get("description");

            Log.d(TAG, "initViews: Getting image resource");
            String iconUrl = "https://www.weatherbit.io/static/img/icons/" + iconId + ".png";
            Log.d(TAG, "initViews: Fetching icon: " + iconUrl);


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weatherIcon = view.findViewById(R.id.weatherfragment_IMG_weatherIcon);
                    String iconUrl = "https://www.weatherbit.io/static/img/icons/" + iconId + ".png";
                    Log.d(TAG, "initViews: Fetching icon: " + iconUrl);
                    Glide.with(weatherIcon).load(iconUrl).into(weatherIcon);
                    textViewdDirection.setText(weatherDescription);
                    textViewTemp.setText(realTemp);
                    textViewCity.setText(city);

                }
            });


                } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentHeader.OnFragmentInteractionListener) {
            mListener = (FragmentHeader.OnFragmentInteractionListener) context;
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
