package com.example.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class FragmentWater extends Fragment {
    private static final String TAG ="pttt" ;
    private int waterTemperature;
    private TextView textViewWaterTemp;
    View view;
    private FragmentWater.OnFragmentInteractionListener mListener;

    public FragmentWater(int waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public FragmentWater() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_water, container, false);
        }

        initViews();

        return view;


    }

    private void initViews() {
        Log.d(TAG, "initViews: "+waterTemperature);
        textViewWaterTemp = view.findViewById(R.id.fragment_water_temp);
        textViewWaterTemp.setText(""+waterTemperature);




    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentWater.OnFragmentInteractionListener) {
            mListener = (FragmentWater.OnFragmentInteractionListener) context;
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
