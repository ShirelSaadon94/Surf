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

import com.example.myapplication.R;

public class FragmentSwell extends Fragment {
    private static final String TAG ="pttt" ;
    private int swell;
    private int directionSwell;
    TextView textViewSwell;
    TextView textViewDirectionSwell;
    View view;
    ImageView imageViewSwellDirection;
    private FragmentSwell.OnFragmentInteractionListener mListener;

    public FragmentSwell(int swell, int directionSwell) {
        this.swell = swell;
        this.directionSwell = directionSwell;
    }

    public FragmentSwell() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView"+swell +directionSwell);


        if (view == null) {
            view = inflater.inflate(R.layout.fragment_swell, container, false);
        }

        initViews();

        return view;


    }

    private void initViews() {
        imageViewSwellDirection=view.findViewById(R.id.fragment_image_swelDirection);
        textViewSwell=view.findViewById(R.id.fragment_swell);
        String s = convertDegreeToCardinalDirection(directionSwell, imageViewSwellDirection);
        textViewDirectionSwell=view.findViewById(R.id.fragment_Diretion_swell);
        textViewSwell.setText(""+swell);
        textViewDirectionSwell.setText(s);


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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSwell.OnFragmentInteractionListener) {
            mListener = (FragmentSwell.OnFragmentInteractionListener) context;
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
