package com.shubh.eato.DashBoard;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.exploreItemsAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.R;
import com.shubh.eato.databinding.FragmentAllItemsBinding;

import java.util.ArrayList;


public class AllItemsFragment extends Fragment {

    public AllItemsFragment() {
        // Required empty public constructor
    }

    FragmentAllItemsBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;
    ArrayList<ItemsExploreModel> list;
    ArrayList<ItemsExploreModel> listKey;
    exploreItemsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllItemsBinding.inflate(getLayoutInflater());

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        list = new ArrayList<>();
        listKey = new ArrayList<>();



        //Set the Recyclerview with adapter
        adapter = new exploreItemsAdapter(list, listKey, getContext());
        binding.recyclerviewAllItems.setAdapter(adapter);
        binding.recyclerviewAllItems.setLayoutManager(new GridLayoutManager(getContext(), 2));

        //Fetch the data from database to populate the recycler view via model
        mDatabase.getReference().child(FirebaseVar.ALLITEMS).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                listKey.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                        ItemsExploreModel adp = snapshot1.getValue(ItemsExploreModel.class);
                        ItemsExploreModel adp2 = new ItemsExploreModel(snapshot1.getKey());

                        list.add(adp);
                        listKey.add(adp2);
                        adapter.showShimmer = false;

                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.SearchBox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<ItemsExploreModel> list2=new ArrayList<>();
                ArrayList<ItemsExploreModel> listKey2=new ArrayList<>();

                mDatabase.getReference().child(FirebaseVar.ALLITEMS).orderByChild("Search").startAt(newText.toLowerCase()).endAt(newText.toLowerCase()+"\uf8ff").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                     list2.clear();
                     listKey2.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                                ItemsExploreModel adp = snapshot1.getValue(ItemsExploreModel.class);
                                ItemsExploreModel adp2 = new ItemsExploreModel(snapshot1.getKey());

                                adapter.showShimmer = false;
                                list2.add(adp);
                                listKey2.add(adp2);

                        }


                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapter = new exploreItemsAdapter(list2, listKey2, getContext());

                binding.recyclerviewAllItems.setAdapter(adapter);
                return false;
            }
        });




        return binding.getRoot();
    }
}