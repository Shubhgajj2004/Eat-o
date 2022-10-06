package com.shubh.eato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.categoryItemsAdapter;
import com.shubh.eato.Adapters.exploreItemsAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.databinding.ActivityCategoryItemBinding;
import com.shubh.eato.databinding.FragmentExploreBinding;

import java.util.ArrayList;

public class categoryItemActivity extends AppCompatActivity {


    ActivityCategoryItemBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;
//    exploreItemsAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        //fetch the data from firebase
        fetchData(mDatabase, getIntent().getStringExtra(FirebaseVar.TYPE), binding.categoryRec);

        //set the data fetched from intent
        Glide.with(categoryItemActivity.this).load(getIntent().getStringExtra(FirebaseVar.IMG)).into(binding.showCatIcon);
        binding.placeCatName.setText(getIntent().getStringExtra(FirebaseVar.NAME));



    }

    public void fetchData(FirebaseDatabase mDatabase, String id, RecyclerView recyclerView) {
        ArrayList<ItemsExploreModel> list = new ArrayList<>();
        ArrayList<ItemsExploreModel> listKey = new ArrayList<>();
        categoryItemsAdapter adapter;


        //Set the Recyclerview with adapter
        adapter = new categoryItemsAdapter(list, listKey, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        //Fetch the data from database to populate the recycler view via model
        mDatabase.getReference().child(FirebaseVar.ALLITEMS).orderByChild(id).equalTo(true).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                listKey.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ItemsExploreModel adp = snapshot1.getValue(ItemsExploreModel.class);

                    ItemsExploreModel adp2 = new ItemsExploreModel(snapshot1.getKey());

                    adapter.showShimmer = false;
                    list.add(adp);
                    listKey.add(adp2);

                }

                adapter.notifyDataSetChanged();
                binding.totalCatDishes.setText(Integer.toString(list.size()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}