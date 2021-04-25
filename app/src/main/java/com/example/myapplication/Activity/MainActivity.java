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
    WeatherAirFragment weatherAirFragment;
    WeatherFragment weatherFragment;
    OkHttpClient client = new OkHttpClient();
    ArrayList<WaveForecast> waveForecastArray;
    WaveForecast waveForecast;

    GridLayout gridLayout;


    private static final String TAG = "pttt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
//        request();
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
                    Intent i= new Intent(MainActivity.this, ProductsActivity.class);
                    startActivity(i);
                }
            });
            CardView cardViewProduct=(CardView)gridLayout.getChildAt(1);
            cardViewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, ForecastActivity.class);
                startActivity(i);
            }
        });

        }




    public void getCity() throws IOException {
        Log.d(TAG, "getCity: ");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        Log.d(TAG, "getCity: " + address);
        city = addresses.get(0).getLocality();
        Log.d("pttt", "getCity: " + city);

    }



    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLocation = location;
                            try {
                                getCity();
                                Log.d(TAG, "onSuccess: " + city);
                                getWaveForecast();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // Logic to handle location object
                        } else {
                            return;
                        }
                    }
                });


        Log.d("pttt", "Location Success !!!");
    }





    public String getCurrentHour() {
        Date myDate1 = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTime(myDate1);
        Date time = calendar.getTime();
        Log.d(TAG, "parseJson: " + time.toString());
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00");
        String dateAsString = outputFmt.format(time);
        Log.d(TAG, "parseJson: " + dateAsString);
        return dateAsString;
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
    public void getWaveForecast() {
        Log.d(TAG, "getWaveForecast: ");
        String currentHour = getCurrentHour();

        String url = "http://api.stormglass.io/v2/weather/point?lat="
                +currentLocation.getLatitude()+"&lng="+currentLocation.getLongitude()+"&params=swellDirection,swellPeriod,waterTemperature,waveHeight,windDirection,windSpeed100m&start="+currentHour+"&key="
                +getString(R.string.wave);

        Log.d(TAG, "x: "+url);
        //OkHttpClient okHttpClient1 = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "onFailure: Exception: " + e.getMessage());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String responseString = response.body().string();
                parseJson(responseString);


                Log.d(TAG, "onResponse: " + responseString.toString());
            }
        });


    }



    private void parseJson(String responseString) {





        try {
            waveForecastArray = new ArrayList<>();

            JSONObject all = new JSONObject(responseString);
            JSONArray byHour = (JSONArray) all.get("hours");
            JSONObject urlContainer = (JSONObject) byHour.get(0);
            JSONObject swellDirection = (JSONObject) urlContainer.get("swellDirection");
            JSONObject swellPeriod = (JSONObject) urlContainer.get("swellPeriod");
            JSONObject windDirection = (JSONObject) urlContainer.get("windDirection");
            JSONObject windSpeed100m = (JSONObject) urlContainer.get("windSpeed100m");
            JSONObject waterTemperature = (JSONObject) urlContainer.get("waterTemperature");
            JSONObject waveHeight = (JSONObject) urlContainer.get("waveHeight");
            String ackwardDate = (String)urlContainer.get("time");
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+mm:mm");
            Date myDate = myFormat.parse(ackwardDate);
            int realswellDirection= (int)Math.round ((double)  swellDirection.get("icon"));
            int realswellPeriod= (int)Math.round ((double) swellPeriod.get("icon"));
            int realwindDirection= (int)Math.round ((double) windDirection.get("icon"));
            int realwindSpeed100m= (int)Math.round ((double) windSpeed100m.get("noaa")*3.6);
            int realwaterTemperature= (int)Math.round ((double) waterTemperature.get("meto"));
            int maxwaveHeight= (int)Math.round (((double)waveHeight.get("noaa"))*10);
            int minwaveHeight= (int)Math.round ((double)waveHeight.get("dwd")*10);



            ArrayList<JSONObject> dailyArray = new ArrayList<>();
//            for (int i = 1; i < 24; i++) {
//                dailyArray.add((JSONObject) byHour.get(i));
//                Log.d(TAG, "parseJson: "+(JSONObject) byHour.get(i));
//            }
//
//            for (JSONObject object : dailyArray) {
//                JSONObject swellDirection = (JSONObject) object.get("swellDirection");
//                JSONObject swellPeriod = (JSONObject) object.get("swellPeriod");
//                JSONObject windDirection = (JSONObject) object.get("windDirection");
//                JSONObject windSpeed100m = (JSONObject) object.get("windSpeed100m");
//                JSONObject waterTemperature = (JSONObject) object.get("waterTemperature");
//                JSONObject waveHeight = (JSONObject) object.get("waveHeight");
//                String ackwardDate = (String)object.get("time");
//                SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+mm:mm");
//                Date myDate = myFormat.parse(ackwardDate);
//
//                int realswellDirection= (int)Math.round ((double)  swellDirection.get("noaa"));
//                int realswellPeriod= (int)Math.round ((double) swellPeriod.get("noaa"));
//                int realwindDirection= (int)Math.round ((double) windDirection.get("icon"));
//                int realwindSpeed100m= (int)Math.round ((double) windSpeed100m.get("noaa"));
//                int realwaterTemperature= (int)Math.round ((double) waterTemperature.get("noaa"));
//                int maxwaveHeight= (int)Math.round (((double)waveHeight.get("noaa"))*10);
//                int minwaveHeight= (int)Math.round ((double)waveHeight.get("dwd")*10);
//                waveForecastArray.add(new WaveForecast(myDate,realwindSpeed100m,realswellPeriod,realwaterTemperature,realswellDirection,realwindDirection,maxwaveHeight*10,minwaveHeight*10));
//
//            }
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    weatherFragment = new WeatherFragment(realwaterTemperature,realwindSpeed100m,realswellPeriod,realswellDirection,realwindDirection,maxwaveHeight,minwaveHeight,city);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.firstFragment, weatherFragment);
                    transaction.commit();
                }
            });
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

    }








    public String convertDegreeToCardinalDirection(int directionInDegrees){
        String cardinalDirection="";
        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 33.75)    ){
            cardinalDirection ="north";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 78.75)){
            cardinalDirection ="ne";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "east";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "es";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "south";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "sw";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "west";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "nw";
        } else {
            cardinalDirection = "?";
        }
        return cardinalDirection;
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

