package com.example.myapplication.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;


import com.example.myapplication.Fragments.FragmentHeader;
import com.example.myapplication.Fragments.FragmentHeightWave;
import com.example.myapplication.Fragments.FragmentSwell;
import com.example.myapplication.Fragments.FragmentWater;
import com.example.myapplication.Fragments.FragmentWind;
import com.example.myapplication.Objects.DayForecast;
import com.example.myapplication.R;
import com.example.myapplication.Objects.WaveForecast;
import com.example.myapplication.Fragments.WeatherAirFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
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

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ForecastActivity extends AppCompatActivity implements FragmentHeader.OnFragmentInteractionListener,FragmentWater.OnFragmentInteractionListener, FragmentWind.OnFragmentInteractionListener ,FragmentHeightWave.OnFragmentInteractionListener,FragmentSwell.OnFragmentInteractionListener, WeatherAirFragment.OnFragmentInteractionListener {
    private static final int PERMISSION_REGULAR_LOCATION_REQUEST_CODE = 133;
    private static final int PERMISSION_BACKGROUND_LOCATION_REQUEST_CODE = 134;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location lastLocation;
    String city;
    FragmentHeader weatherFragment;
    FragmentHeightWave fragmentHeightWave;
    FragmentSwell fragmentSwell;
    FragmentWind fragmentWind;
    FragmentWater fragmentWater;
    OkHttpClient client = new OkHttpClient();
    ArrayList<DayForecast> waveForecastArray;
    WaveForecast waveForecast;
    String[] axisData;
    int[] yAxisData;



    private static final String TAG = "pttt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Intent intent = getIntent();
        lastLocation=intent.getParcelableExtra("EXTRA_LOCATION");
        getWaveForecast();
        axisData=new String[4];
        yAxisData=new int[4];
    }



    public void x(){
        LineChartView lineChartView = findViewById(R.id.chart);


        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();
        Line line = new Line(yAxisValues);
        for(int i = 0; i < axisData.length; i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++){
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        Axis axis = new Axis();
        axis.setValues(axisValues);
        data.setAxisXBottom(axis);
        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);
        line = new Line(yAxisValues).setColor(Color.parseColor("#03A9F4"));
        axis.setTextSize(4);
        axis.setTextColor(Color.parseColor("#FF9800"));
        data.setAxisXBottom(axis);
        yAxis.setName("Height WAVE");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(4);
        data.setAxisYLeft(yAxis);
        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 100;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);


    }

    public void getCity() throws IOException {
        Log.d(TAG, "getCity: ");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        Log.d(TAG, "getCity: " + address);
        city = addresses.get(0).getLocality();
        Log.d("pttt", "getCity: " + city);

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






    public void getWaveForecast() {
        Log.d(TAG, "getWaveForecast: ");
        String currentHour = getCurrentHour();

        String url = "http://api.stormglass.io/v2/weather/point?lat="
                +lastLocation.getLatitude()+"&lng="+lastLocation.getLongitude()+"&params=swellDirection,swellPeriod,waterTemperature,waveHeight,windDirection,windSpeed100m&start="+currentHour+"&key="
                +getString(R.string.wave);


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
        Log.d(TAG, "parseJson: ");

        try {
            getCity();
            Log.d(TAG, "parseJson: ");
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
            Log.d(TAG, "parseJson222222222: "+ackwardDate);
            SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+mm:mm");
            Date myDate = myFormat.parse(ackwardDate);
            int realswellDirection= (int)Math.round ((double)  swellDirection.get("icon"));
            int realswellPeriod= (int)Math.round ((double) swellPeriod.get("icon"));
            int realwindDirection= (int)Math.round ((double) windDirection.get("icon"));
            int realwindSpeed100m= (int)Math.round ((double) windSpeed100m.get("noaa")*3.6);
            int realwaterTemperature= (int)Math.round ((double) waterTemperature.get("noaa"));
            int maxwaveHeight= (int)Math.round (((double)waveHeight.get("noaa"))*100);
            Log.d(TAG, "parseJson: "+"Ddd");
            int minwaveHeight= (int)Math.round ((double)waveHeight.get("sg")*100);




            int k=0;
            for(int z=0;z<96;z+=24){
                urlContainer = (JSONObject) byHour.get(z);
                Log.d(TAG, "parseJson: "+urlContainer.toString());
                JSONObject waveHeight1 = (JSONObject) ((JSONObject) urlContainer.get("waveHeight"));
                int maxwaveHeight1= (int)Math.round (((double)waveHeight1.get("noaa"))*100);
                String ackwardDate1 = (String)urlContainer.get("time");
                SimpleDateFormat myFormat1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+mm:mm");
                Date myDate1 = myFormat.parse(ackwardDate1);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                String dayOfTheWeek = sdf.format(myDate1);
                DayForecast dayForecast=new DayForecast(ackwardDate1,maxwaveHeight1,myDate1);
                axisData[k]=dayOfTheWeek;
                yAxisData[k]=maxwaveHeight1;
                waveForecastArray.add(dayForecast);
                k+=1;

            }
            x();

            for(int i=0;i<4;i++){
                Log.d(TAG, "parseJson: "+axisData[i]+yAxisData[i]);
            }




            Log.d(TAG, "parseJson: "+waveForecastArray.toString());

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    weatherFragment = new FragmentHeader(city);
                    Log.d(TAG, "run: ");
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.twoFragment, weatherFragment);
                    transaction.commit();

                    fragmentHeightWave= new FragmentHeightWave(maxwaveHeight);
                    Log.d(TAG, "run: ");
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.fragment_wave, fragmentHeightWave);
                    transaction1.commit();

                    fragmentSwell=new FragmentSwell(realswellPeriod,realswellDirection);
                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    transaction2.replace(R.id.fragment_swell, fragmentSwell);
                    transaction2.commit();

                    fragmentWind=new FragmentWind(realwindSpeed100m,realwindDirection);
                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    transaction3.replace(R.id.fragment_wind, fragmentWind);
                    transaction3.commit();

                    fragmentWater=new FragmentWater(realwaterTemperature);
                    FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                    transaction4.replace(R.id.fragment_water, fragmentWater);
                    transaction4.commit();




                }
            });
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }

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
