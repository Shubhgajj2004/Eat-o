package com.shubh.eato.DashBoard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.exploreItemsAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.ProfileActivity;
import com.shubh.eato.WalletActivity;
import com.shubh.eato.categoryItemActivity;
import com.shubh.eato.databinding.FragmentExploreBinding;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {


    public ExploreFragment() {
        // Required empty public constructor
    }

    FragmentExploreBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentExploreBinding.inflate(getLayoutInflater());


        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        //references
        DatabaseReference sliderImages = mDatabase.getReference().child(FirebaseVar.ADMIN).child(FirebaseVar.SLIDERIMAGES);

        //start shimmer
        binding.southIndianIconShimmer.startShimmer();
        binding.northIndianIconShimmer.startShimmer();
        binding.chieneseIconShimmer.startShimmer();
        binding.shimmerAd1.startShimmer();
        binding.shimmermemeImgSlider.startShimmer();

        //fetch and set the slider images
        setSliderImage(sliderImages, FirebaseVar.SHOW1, binding.adImgSlider, binding.shimmerAd1);
        setSliderImage(sliderImages, FirebaseVar.SHOW2, binding.show2, binding.shimmerShow2);
        setSliderImage(sliderImages, FirebaseVar.OFFER, binding.memeImgSlider, binding.shimmermemeImgSlider);

        //set the icon
        setIcon(mDatabase, FirebaseVar.SOUTHINDIAN, binding.southIndianIcon, binding.southIndianIconShimmer, getContext());
        setIcon(mDatabase, FirebaseVar.CHIENESE, binding.chieneseIcon, binding.chieneseIconShimmer, getContext());
        setIcon(mDatabase, FirebaseVar.NORTHINDIAN, binding.northIndianIcon, binding.northIndianIconShimmer, getContext());


        //fetch the data from firebase
        fetchData(mDatabase, FirebaseVar.ISPOPULAR, binding.popularRec);
        fetchData(mDatabase, FirebaseVar.ISCHATITEMS, binding.chatRec);
        fetchData(mDatabase, FirebaseVar.ISDRINKS, binding.drinksRec);
        fetchData(mDatabase, FirebaseVar.ISSANDWICHES, binding.sandwichRec);

        binding.walletIconExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), WalletActivity.class));
            }
        });

        binding.profileExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ProfileActivity.class));
            }
        });


        return binding.getRoot();
    }


    public void fetchData(FirebaseDatabase mDatabase, String id, RecyclerView recyclerView) {
        ArrayList<ItemsExploreModel> list = new ArrayList<>();
        ArrayList<ItemsExploreModel> listKey = new ArrayList<>();
        exploreItemsAdapter adapter;


        //Set the Recyclerview with adapter
        adapter = new exploreItemsAdapter(list, listKey, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Fetch the data from database to populate the recycler view via model
        mDatabase.getReference().child(FirebaseVar.ALLITEMS).orderByChild(id).equalTo(true).addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                listKey.clear();

                if (getActivity() == null) {
                    return;
                } else {


                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        ItemsExploreModel adp = snapshot1.getValue(ItemsExploreModel.class);
                        ItemsExploreModel adp2 = new ItemsExploreModel(snapshot1.getKey());

                        list.add(adp);
                        listKey.add(adp2);
                        adapter.showShimmer = false;

                    }
                }


                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setIcon(FirebaseDatabase database, String id, ShapeableImageView img, ShimmerFrameLayout fm, Context context) {
        mDatabase.getReference().child(FirebaseVar.ICONS).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(context).load(snapshot.getValue(String.class)).into(img);
                fm.stopShimmer();
                fm.setShimmer(null);

                //goto all items of category on clicking category button
                if (id == FirebaseVar.SOUTHINDIAN) {
                    btnClick(binding.southIndianIcon, FirebaseVar.ISSOUTHINDIAN, "South", snapshot.getValue(String.class));
                } else if (id == FirebaseVar.NORTHINDIAN) {
                    btnClick(binding.northIndianIcon, FirebaseVar.ISNORTHINDIAN, "North", snapshot.getValue(String.class));
                } else if (id == FirebaseVar.CHIENESE) {
                    btnClick(binding.chieneseIcon, FirebaseVar.ISCHINESE, "Chienese", snapshot.getValue(String.class));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sendData(String data, String name, String uri) {
        Intent intent = new Intent(getContext(), categoryItemActivity.class);
        intent.putExtra(FirebaseVar.TYPE, data);
        intent.putExtra(FirebaseVar.NAME, name);
        intent.putExtra(FirebaseVar.IMG, uri);
        startActivity(intent);

    }


    public void btnClick(ShapeableImageView img, String path, String name, String uri) {


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendData(path, name, uri);

            }
        });


    }

    public void setSliderImage(DatabaseReference reference, String id, ShapeableImageView img, ShimmerFrameLayout shimmer) {
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (getActivity() == null) {
                    return;
                } else {


                    Glide.with(getContext()).load(snapshot.getValue(String.class)).into(img);
                    shimmer.stopShimmer();
                    shimmer.setShimmer(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}