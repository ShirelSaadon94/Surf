package com.example.myapplication.Activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyRecyclerViewAdapter;
import com.example.myapplication.Objects.Place;
import com.example.myapplication.Objects.Product;
import com.example.myapplication.R;
import com.example.myapplication.RecyclerResturantsAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DisplayRestaurants extends AppCompatActivity {
    private static final String TAG = "pttt";
    private ListView mListView;
    private RecyclerView recyclerView;
    private ArrayList<Place> resultList=new ArrayList<Place>();
    Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resturants);
        recyclerView = findViewById(R.id.RECYCLER_VIEW_RES);
        Intent intent = getIntent();
        lastLocation=intent.getParcelableExtra("EXTRA_LOCATION");
        Log.d(TAG, "onCreate: "+lastLocation.getLongitude() + " , " +lastLocation.getLatitude());
        resultList=new ArrayList<Place>();
        int radius = 1000;
        openHttpRequestForPlaces(lastLocation.getLatitude(),lastLocation.getLongitude());


    }



    private void openHttpRequestForPlaces(double lat, double lng) {

        Log.d(TAG, "openHttpRequestForPlaces: Searching for places around" +
                lat+" ," +lng);

        int myRadius = 1500;
        String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
        String tempLocation = "" +lastLocation.getLatitude()+ "," + "" + lastLocation.getLongitude();
        String tempRadius = "&radius=" + myRadius;
        String tempType = "&type=restaurant";
        String apiKey = "&key=AIzaSyAXKuBMkrUW0J99RPpuOgC02rmS1DZNcCY";

        String url = baseUrl + tempLocation + tempRadius + tempType + apiKey;
        Log.d(TAG, "openHttpRequestForPlaces: "+url);

        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(TAG, "openHttpRequestForPlaces: Requesting:\n" + url);
        Request request = new Request.Builder()
                .url(url)
                .header("Content-Type", "application/json")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "onFailure: Request failed:" + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d(TAG, "onResponse: Request successful");
                if (response == null) {
                    Log.d(TAG, "onResponse: Response is null");
                } else {
                    String responseString = response.body().string();
                    Log.d(TAG, "onResponse: success: " + responseString);
                    try {
                        JSONObject jsonObj = new JSONObject(responseString);
                        JSONArray predsJsonArray = jsonObj.getJSONArray("results");


                        // Extract the descriptions from the results
                        resultList = new ArrayList<Place>(predsJsonArray.length());
                        for (int i = 0; i < predsJsonArray.length(); i++) {
                            Place place = new Place();
                            place.name = predsJsonArray.getJSONObject(i).getString("name");
                            JSONObject location=predsJsonArray.getJSONObject(i).getJSONObject("geometry").getJSONObject("location");
                            Log.d(TAG, "onResponse: "+location.toString());
                            Double lat1 = location.getDouble("lat");
                            Double lng1 = location.getDouble("lng");
                            Log.d(TAG, "onResponse: "+lat +","+lng);
                            Double dis =CalculationByDistance(lat1,lng1);
                            Log.d(TAG, "onResponse: "+dis);
                            Double res=CalculationByDistance(lat1,lng1);
                            if(res==0.0){
                                place.distance="";
                            }
                            else
                            place.distance=""+res+"Km";

                            if(predsJsonArray.getJSONObject(i).has("opening_hours")){
                                JSONObject opening_hours=predsJsonArray.getJSONObject(i).getJSONObject("opening_hours");
                                Boolean Z=opening_hours.getBoolean("open_now");
                                place.isOpen=isOpenNow(Z);
                                Log.d(TAG, "onResponse: "+isOpenNow(Z));
                                Log.d(TAG, "onResponse: "+Z);

                            }



                            resultList.add(place);
                        }

                        Log.d(TAG, "onResponse: "+resultList.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.setVisibility(View.VISIBLE);
                                RecyclerResturantsAdapter adapter =new RecyclerResturantsAdapter(DisplayRestaurants.this,resultList);
                                recyclerView.setAdapter(adapter);









                            }
                        });




                    } catch (JSONException e) {
                        Log.d(TAG, "onResponse: Exception: " + e.getMessage());
                    }
                }
            }
        });
    }
    public double CalculationByDistance(Double lat1,Double lng1) {
        try {


            int Radius = 6371;// radius of earth in Km
            double dLat = Math.toRadians(lastLocation.getLatitude() - lat1);
            double dLon = Math.toRadians(lastLocation.getLongitude() - lng1);
            Log.d(TAG, "CalculationByDistance: " + dLat + " , " + dLon);
            double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                    + Math.cos(Math.toRadians(lat1))
                    * Math.cos(Math.toRadians(lastLocation.getLongitude())) * Math.sin(dLon / 2)
                    * Math.sin(dLon / 2);
            Log.d(TAG, "CalculationByDistance: " + a);
            double c = 2 * Math.asin(Math.sqrt(a));
            double valueResult = Radius * c;
            double km = valueResult / 1;
            DecimalFormat newFormat = new DecimalFormat("####");
            int kmInDec = Integer.valueOf(newFormat.format(km));
            double meter = valueResult % 1000;
            int meterInDec = Integer.valueOf(newFormat.format(meter));
            Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                    + " Meter   " + meterInDec);
            Double  x=Radius*c;
            x =(Double) Math.floor(x * 100) / 100;
            return x;
        }catch (NumberFormatException e){
            return 0.0;

        }

    }


    public String isOpenNow(Boolean isOpen){
        String result="";
        if(isOpen.booleanValue()==true){
            result="Open now!";
        }
        else
        {
            result="Closed!";

        }
        return result;



    }




}