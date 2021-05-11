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

public class FragmentHeightWave extends Fragment {

    private static final String TAG ="pttt" ;
    private int heightWave;
    private TextView textViewHeight;
    View view;
    private FragmentHeightWave.OnFragmentInteractionListener mListener;

    public FragmentHeightWave(int heightWave) {
        this.heightWave = heightWave;
    }

    public FragmentHeightWave() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_height_wave, container, false);
        }

        initViews();

        return view;


    }

    private void initViews() {
        textViewHeight = view.findViewById(R.id.fragment_wave);
        textViewHeight.setText(""+heightWave);




    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentHeightWave.OnFragmentInteractionListener) {
            mListener = (FragmentHeightWave.OnFragmentInteractionListener) context;
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
