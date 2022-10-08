package com.shubh.eato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.exploreItemsAdapter;
import com.shubh.eato.Adapters.reviewsAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.Models.reviewsModel;
import com.shubh.eato.databinding.ActivityReviewsBinding;
import com.shubh.eato.databinding.FragmentExploreBinding;

import java.util.ArrayList;

public class reviewsActivity extends AppCompatActivity {

    ActivityReviewsBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;
    reviewsAdapter adapter;
    ArrayList<reviewsModel> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();


        //Set the Recyclerview with adapter

        adapter = new reviewsAdapter(list, getApplicationContext());
        binding.reviewsRec.setAdapter(adapter);
        binding.reviewsRec.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //Fetch the data from database to populate the recycler view via model
        mDatabase.getReference().child(FirebaseVar.ALLITEMS).child("sand1").child(FirebaseVar.COMMENTS).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    if(snapshot1.exists())
                    {
                        reviewsModel adp = snapshot1.getValue(reviewsModel.class);

                        list.add(adp);
                    }


                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.backBtnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}