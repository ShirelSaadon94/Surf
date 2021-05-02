package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;

//import com.example.myapplication.Fragments.FragmentWeeklyWaveForecast;
import com.example.myapplication.R;
import com.example.myapplication.Objects.WaveForecast;
import com.example.myapplication.Fragments.WeatherAirFragment;
import com.example.myapplication.Fragments.WeatherFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements WeatherFragment.OnFragmentInteractionListener, WeatherAirFragment.OnFragmentInteractionListener {
    //for location
    private static final int PERMISSION_REGULAR_LOCATION_REQUEST_CODE = 133;
    private static final int PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 134;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    String city;
    WeatherFragment weatherFragment;
    OkHttpClient client = new OkHttpClient();
    ArrayList<WaveForecast> waveForecastArray;

    GridLayout gridLayout;


    private static final String TAG = "pttt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout=(GridLayout)findViewById(R.id.mainGrid);

        setSingleEvent(gridLayout);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");


    }

    private void setSingleEvent(GridLayout gridLayout) {


            CardView cardView=(CardView)gridLayout.getChildAt(0);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i= new Intent(MainActivity.this, ForecastActivity.class);
                    startActivity(i);
                }
            });


            CardView cardViewProduct=(CardView)gridLayout.getChildAt(1);
            cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(i);
            }
        });


        }






    @Override
    public void messageFromParentFragment(Uri uri) {
        Log.i("TAG", "received communication from parent fragment");
    }

    @Override
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }





}

