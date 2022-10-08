package com.shubh.eato.DashBoard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.orderAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.ordersModel;
import com.shubh.eato.databinding.FragmentOrdersBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class OrdersFragment extends Fragment {

    public OrdersFragment(){}

    FragmentOrdersBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;
    ArrayList<ordersModel> list;
    ArrayList<ordersModel> listKey;
    orderAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(getLayoutInflater());

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        list = new ArrayList<>();
        listKey = new ArrayList<>();

        //refrences
       DatabaseReference succesfullRef =  mDatabase.getReference().child(FirebaseVar.USERS).child(mUSer.getUid()).child(FirebaseVar.ORDERS).child(FirebaseVar.SUCCEDSFULORDERS);

        //Set the Recyclerview with adapter
        adapter = new orderAdapter(list, listKey, getContext(), succesfullRef);
        binding.ordersRec.setAdapter(adapter);
        binding.ordersRec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mDatabase.getReference().child(FirebaseVar.USERS).child(mUSer.getUid())
                .child(FirebaseVar.ORDERS).child(FirebaseVar.SUCCEDSFULORDERS)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            ordersModel adp = snapshot1.child("Details").getValue(ordersModel.class);
                            ordersModel adp2 = new ordersModel(snapshot1.getKey());

                            adapter.showShimmer = false;
                            list.add(adp);
                            listKey.add(adp2);


                        }

//                        Collections.sort(list, new Comparator<ordersModel>() {
//                            @Override
//                            public int compare(ordersModel t1, ordersModel t2) {
//                                if (!t1.getTime().compareTo(t2.getTime())) {
//                                    return 1;
//                                } else {
//                                    return  -1;
//                                }
//                            }
//                        });

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






        return binding.getRoot();
    }
}