//package com.example.myapplication.Fragments;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.location.Location;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.format.DateFormat;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import com.bumptech.glide.Glide;
//import com.example.myapplication.Objects.DayForecast;
//import com.example.myapplication.R;
//import com.example.myapplication.R;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.Description;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//import com.jjoe64.graphview.GraphView;
//import com.jjoe64.graphview.series.DataPoint;
//import com.jjoe64.graphview.series.LineGraphSeries;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//
//public class FragmentWeeklyWaveForecast extends Fragment {
//
//    private FragmentWeeklyWaveForecast.OnFragmentInteractionListener mListener;
//
//    private static final String TAG = "pttt";
//    OkHttpClient client = new OkHttpClient();
//    View view;
//    ArrayList<DayForecast> dayForecasts;
//    LineChart lineChartDownFill;
//
//
//    public FragmentWeeklyWaveForecast(ArrayList<DayForecast> dayForecasts) {
//        this.dayForecasts = dayForecasts;
//
//
//    }
//
//    public FragmentWeeklyWaveForecast() {
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView");
//
//
//
//        view = inflater.inflate(R.layout., container, false);
//        initLineChartDownFill(view);
////       // graph = view.findViewById(R.id.graph);
////        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
////                new DataPoint(1, dayForecasts.get(0).getWave()),
////                new DataPoint(2, dayForecasts.get(1).getWave()),
////                new DataPoint(3,  dayForecasts.get(2).getWave()),
////                new DataPoint(4,  dayForecasts.get(3).getWave()),
////                new DataPoint(5,  dayForecasts.get(4).getWave())
////        });
////
////        graph.addSeries(series);
////        graph.setTitle("My Graph View");
////
////
////        // on below line we are setting
////        // text color to our graph view.
////        graph.setTitleColor(R.color.purple_200);
////
////        // on below line we are setting
////        // our title text size.
////        graph.setTitleTextSize(18);
////
////        // on below line we are adding
////        // data series to our graph view.
//        return view;
//    }
//
//    private void initLineChartDownFill(View view) {
//
//        lineChartDownFill = view.findViewById(R.id.lineChartDownFill);
//        lineChartDownFill.setTouchEnabled(false);
//        lineChartDownFill.setDragEnabled(true);
//        lineChartDownFill.setScaleEnabled(true);
//        lineChartDownFill.setPinchZoom(false);
//        lineChartDownFill.setDrawGridBackground(false);
//        lineChartDownFill.setMaxHighlightDistance(200);
//        lineChartDownFill.setViewPortOffsets(0, 0, 0, 0);
//        lineChartDownFillWithData();
//
//    }
//    private void lineChartDownFillWithData() {
//
//
//        Description description = new Description();
//        description.setText("Days Data");
//
//        lineChartDownFill.setDescription(description);
//
//
//        ArrayList<Entry> entryArrayList = new ArrayList<>();
//        entryArrayList.add(new Entry(0, 60f, "1"));
//        entryArrayList.add(new Entry(1, 55f, "2"));
//        entryArrayList.add(new Entry(2, 60f, "3"));
//        entryArrayList.add(new Entry(3, 40f, "4"));
//        entryArrayList.add(new Entry(4, 45f, "5"));
//        entryArrayList.add(new Entry(5, 36f, "6"));
//        entryArrayList.add(new Entry(6, 30f, "7"));
//        entryArrayList.add(new Entry(7, 40f, "8"));
//        entryArrayList.add(new Entry(8, 45f, "9"));
//        entryArrayList.add(new Entry(9, 60f, "10"));
//        entryArrayList.add(new Entry(10, 45f, "10"));
//        entryArrayList.add(new Entry(11, 20f, "10"));
//
//
//        //LineDataSet is the line on the graph
//        LineDataSet lineDataSet = new LineDataSet(entryArrayList, "This is y bill");
//
//        lineDataSet.setLineWidth(5f);
//        lineDataSet.setColor(Color.GRAY);
//        lineDataSet.setCircleColor(Color.GREEN);
//        lineDataSet.setCircleColor(R.color.white);
//        lineDataSet.setHighLightColor(Color.RED);
//        lineDataSet.setDrawValues(false);
//        lineDataSet.setCircleRadius(10f);
//        lineDataSet.setCircleColor(Color.YELLOW);
//
//        //to make the smooth line as the graph is adrapt change so smooth curve
//        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//        //to enable the cubic density : if 1 then it will be sharp curve
//        lineDataSet.setCubicIntensity(0.2f);
//
//        //to fill the below of smooth line in graph
//        lineDataSet.setDrawFilled(true);
//        lineDataSet.setFillColor(Color.BLACK);
//        //set the transparency
//        lineDataSet.setFillAlpha(80);
//
//        //set the gradiant then the above draw fill color will be replace
//        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.border);
//        lineDataSet.setFillDrawable(drawable);
//
//        //set legend disable or enable to hide {the left down corner name of graph}
//        Legend legend = lineChartDownFill.getLegend();
//        legend.setEnabled(false);
//
//        //to remove the cricle from the graph
//        lineDataSet.setDrawCircles(false);
//
//        //lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS);
//
//
//        ArrayList<ILineDataSet> iLineDataSetArrayList = new ArrayList<>();
//        iLineDataSetArrayList.add(lineDataSet);
//
//        //LineData is the data accord
//        LineData lineData = new LineData(iLineDataSetArrayList);
//        lineData.setValueTextSize(13f);
//        lineData.setValueTextColor(Color.BLACK);
//
//
//        lineChartDownFill.setData(lineData);
//        lineChartDownFill.invalidate();
//
//
//    }
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof FragmentWeeklyWaveForecast.OnFragmentInteractionListener) {
//            mListener = (FragmentWeeklyWaveForecast.OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void messageFromChildFragment(Uri uri);
//    }
//
//}
//
//
//
//
//
