package com.example.myapplication.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MyRecyclerViewAdapter;
import com.example.myapplication.Objects.Product;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity {


    private static final String TAG = "CustomerTells";
    private RecyclerView recyclerView;
    private ArrayList<Product> reviewList=new ArrayList<Product>();
    private DatabaseReference mDatabase;
    private MaterialButton submitBtn;
    ////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        recyclerView = findViewById(R.id.RECYCLER_VIEW);
        reviewsList();
        submitBtn = findViewById(R.id.add_product);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(ProductsActivity.this, NewGiveItemActivity.class);
                startActivity(i);
            }
        });
    }
    public void reviewsList() {
        Log.d(TAG, "reviewsList: ");
        ArrayList<Product> reviewsList=new ArrayList<Product>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ENTER");
                reviewsList.removeAll(reviewsList);
                if(dataSnapshot.getChildrenCount()==0){
                    Log.d(TAG, "onDataChange: 0 child" );
                }else {
                    populatelist(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void populatelist(DataSnapshot dataSnapshot) {
        reviewList.removeAll(reviewList);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Log.d(TAG, "populatelist: "+snapshot.getClass()+"  "+snapshot.getValue());
            Product review = snapshot.getValue(Product.class);
            reviewList.add(review);
            Log.d(TAG, "populateList: " + review.toString());

            if (reviewList.size() == dataSnapshot.getChildrenCount()) {
                recyclerView.setVisibility(View.VISIBLE);
                MyRecyclerViewAdapter adapter =new MyRecyclerViewAdapter(this,reviewList);
                recyclerView.setAdapter(adapter);
                Log.d(TAG, "populatelist: DONE");
            }
        }
    }





}