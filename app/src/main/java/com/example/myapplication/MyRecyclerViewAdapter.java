package com.example.myapplication;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.example.myapplication.Interface.ItemDetailsListener;
import com.example.myapplication.Objects.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "Dogood";
    private static final String GIVE_ITEM = "giveItem";
    private static final String OWNER_USER = "ownerUser";

    private Context context;
    private ArrayList<Product> items;


    public MyRecyclerViewAdapter(Context context, ArrayList<Product> items) {
        Log.d(TAG, "RecyclerViewGiveAdapter: Im in adapter with: " + items.toString());
        this.context = context;
        this.items = items;

    }

    @NonNull
    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new MyRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: Got item: " + items.get(position).toString());
        Product temp = items.get(position);
        holder.itemPrice.setText(temp.getPrice());
        holder.itemName.setText(temp.getName());
        holder.itemState.setText(temp.getState());
        holder.postDate.setText(temp.getDate().toString());
        ShapeableImageView itemImage = holder.itemPhoto;
        getPhotoFromStorage(itemImage, position);
        holder.rowCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openItemDetails(position);
            }
        });
    }

    private void getPhotoFromStorage(ShapeableImageView itemImage, int position) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Log.d(TAG, "getPhotoFromStorage: Fetching photo from storage");
        String itemID = items.get(position).getKey();

        String path = "gs://" + context.getString(R.string.google_storage_bucket) + "/" + itemID + ".jpg";
        Log.d(TAG, "getPhotoFromStorage: Fetching: " + path);
        StorageReference gsReference = storage.getReferenceFromUrl(path);

        gsReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "onSuccess: " + uri);
                // create a ProgressDrawable object which we will show as placeholder
                CircularProgressDrawable drawable = new CircularProgressDrawable(context);
                drawable.setColorSchemeColors(R.color.design_default_color_surface, R.color.design_default_color_error, R.color.black);
                drawable.setCenterRadius(30f);
                drawable.setStrokeWidth(5f);
                // set all other properties as you would see fit and start it
                drawable.start();
                Glide.with(context).load(uri).placeholder(drawable).into(itemImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: Exception: " + exception.getMessage());
            }
        });
    }
    private void openItemDetails(int position) {
        Log.d(TAG, "openItemDetails: ");
        ((ItemDetailsListener) context).getSelectedItem(items.get(position), true);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * An inner class to specify each row contents
     */
    public class ViewHolder extends RecyclerView.ViewHolder { // To hold each row

        MaterialTextView itemName, itemState, itemPrice, itemDescription, postDate;
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
            rowCard = itemView.findViewById(R.id.giveRow_giveitem_row);
            itemName = itemView.findViewById(R.id.giveRow_LBL_itemName);
            itemState = itemView.findViewById(R.id.giveRow_LBL_itemState);
            itemPrice = itemView.findViewById(R.id.giveRow_LBL_itemPrice);
            postDate = itemView.findViewById(R.id.giveRow_LBL_postDate);
            itemPhoto = itemView.findViewById(R.id.giveRow_IMG_itemPicture);
        }


    }
}