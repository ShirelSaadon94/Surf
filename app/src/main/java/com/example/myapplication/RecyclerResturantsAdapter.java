package com.example.myapplication;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.widget.RelativeLayout;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.example.myapplication.Interface.ItemDetailsListener;
import com.example.myapplication.Objects.Place;
import com.example.myapplication.Objects.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;


public class RecyclerResturantsAdapter extends RecyclerView.Adapter<RecyclerResturantsAdapter.ViewHolder> {

    private static final String TAG = "pttt";


    private Context context;
    private ArrayList<Place> resturants;


    public RecyclerResturantsAdapter(Context context, ArrayList<Place> restaurants) {
        Log.d(TAG, "RecyclerViewGiveAdapter: Im in adapter with: " + restaurants.toString());
        this.context = context;
        this.resturants = restaurants;

    }

    @NonNull
    @Override
    public RecyclerResturantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reyclerview_resturant, parent, false);
        return new RecyclerResturantsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerResturantsAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Got item: " + resturants.get(position).toString());
        Place temp = resturants.get(position);
        holder.resName.setText(temp.getName());
        holder.resIsOpen.setText(temp.getIsOpen());
        holder.resDis.setText(""+temp.getDistance());
        if(temp.getIsOpen()=="Open now!"){
            holder.resIsOpen.setTextColor(GREEN);
        }
        else if(temp.getIsOpen()=="Closed!"){
            holder.resIsOpen.setTextColor(RED);
        }
        holder.resIsOpen.setText(temp.getIsOpen());
        holder.resDis.setText(""+temp.getDistance());



        ShapeableImageView itemImage = holder.itemPhoto;
        holder.rowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemDetails(position);
            }
        });
    }


    private void openItemDetails(int position) {




    }


    @Override
    public int getItemCount() {
        return resturants.size();
    }


    /**
     * An inner class to specify each row contents
     */
    public class ViewHolder extends RecyclerView.ViewHolder { // To hold each row

        MaterialTextView resName,resIsOpen,resDis;
        ShapeableImageView itemPhoto;
        MaterialCardView rowCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews();
        }

        /**
         * A method to initialize the views
         */
        private void initViews() {
            rowCard = itemView.findViewById(R.id.giveRow_giveRes_row);
            resName = itemView.findViewById(R.id.giveRow_LBL_resName);
            resIsOpen = itemView.findViewById(R.id.giveRow_LBL_resIsOpen);
            itemPhoto = itemView.findViewById(R.id.giveRow_IMG_resPicture);
            resDis=itemView.findViewById(R.id.giveRow_LBL_resDis);
        }


    }
}