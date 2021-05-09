package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;

//import com.example.myapplication.Fragments.FragmentWeeklyWaveForecast;
import com.example.myapplication.R;
import com.example.myapplication.Objects.WaveForecast;
import com.example.myapplication.Fragments.WeatherAirFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements WeatherAirFragment.OnFragmentInteractionListener {

    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;
    String city;
    private static final int PERMISSION_REGULAR_LOCATION_REQUEST_CODE = 133;
    private static final int PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 134;

    OkHttpClient client = new OkHttpClient();
    ArrayList<WaveForecast> waveForecastArray;

    GridLayout gridLayout;


    private static final String TAG = "pttt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request();
        gridLayout=(GridLayout)findViewById(R.id.mainGrid);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return true;
    }

    public void onGroupItemClick(MenuItem item) {
        // One of the group items (using the onClick attribute) was clicked
        // The item parameter passed here indicates which item it is
        // All other menu item clicks are handled by <code><a href="/reference/android/app/Activity.html#onOptionsItemSelected(android.view.MenuItem)">onOptionsItemSelected()</a></code>
    }
    private void setSingleEvent(GridLayout gridLayout) {
            CardView cardView=(CardView)gridLayout.getChildAt(2);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: Resturant");
                    Intent intent = new Intent(MainActivity.this, DisplayRestaurants.class);
                    Double longitude = currentLocation.getLongitude();
                    Double latitude = currentLocation.getLatitude();
                    String longit = Double.toString(longitude);
                    String lat = Double.toString(latitude);
                    Log.d(TAG, "startRestaurants: "+longit +lat);
                    intent.putExtra("long", longit);
                    intent.putExtra("lat", lat);
                    startActivity(intent);
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
            CardView cardViewWeather=(CardView)gridLayout.getChildAt(0);
            cardViewWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, ForecastActivity.class);
                startActivity(i);
            }
        });





        }


    @Override
    public void messageFromChildFragment(Uri uri) {
        Log.i("TAG", "received communication from child fragment");
    }

    private void requestFirstLocationPermission() {
        // Regular location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REGULAR_LOCATION_REQUEST_CODE);
    }

    private void requestSecondLocationPermission() {
        // Background location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE);
    }

    private void request() {
        boolean per1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean per2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean per3 = android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED;

        if (!per1 || !per2) {
            // if i can ask for permission
            requestFirstLocationPermission();
        } else if (!per3) {
            // if i can ask for permission
            requestSecondLocationPermission();
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REGULAR_LOCATION_REQUEST_CODE: {
                request();
                return;
            }
            case PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE: {
                request();
                return;
            }
        }
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = location;
                            setSingleEvent(gridLayout);

                        } else {
                            return;
                        }
                    }
                });


        Log.d("pttt", "Location Success !!!");
    }


}

