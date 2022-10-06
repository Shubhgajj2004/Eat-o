package com.shubh.eato.DashBoard;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.categoryItemsAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.databinding.FragmentFavouriteBinding;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    public FavouriteFragment() {
        // Required empty public constructor
    }

    FragmentFavouriteBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    ArrayList<ItemsExploreModel> list = new ArrayList<>();
    ArrayList<ItemsExploreModel> listKey = new ArrayList<>();
    categoryItemsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFavouriteBinding.inflate(getLayoutInflater());

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //fetch the data from firebase
//        fetchData(mDatabase, mUSer, binding.favRec);




        //Set the Recyclerview with adapter
        adapter = new categoryItemsAdapter(list, listKey, getContext());
        binding.favRec.setAdapter(adapter);
        binding.favRec.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //Fetch the data from database to populate the recycler view via model
//        mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid()).child(FirebaseVar.FAVOURITE).addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list.clear();
//                listKey.clear();
//
//                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
//
////                    Toast.makeText(getContext(), snapshot1.getKey(), Toast.LENGTH_SHORT).show();
//
//                    mDatabase.getReference().child(FirebaseVar.ALLITEMS).child(snapshot1.getKey()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot2) {
//                            ItemsExploreModel adp = snapshot2.getValue(ItemsExploreModel.class);
//                            ItemsExploreModel adp2 = new ItemsExploreModel(snapshot2.getKey());
//
//
//                            list.add(adp);
//                            listKey.add(adp2);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//
//                }
//
//
//                adapter.notifyDataSetChanged();
//                binding.totalFavDishes.setText(Integer.toString(list.size()));
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        //ans


        getFavKeys(new favcallBack() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCallback(ArrayList<ItemsExploreModel> ltKey) {

                ///

                List<Task<DataSnapshot>> tasks = new ArrayList<>();
                ///

                for (ItemsExploreModel keys : ltKey) {
//                    Toast.makeText(getContext(), keys.getKey(), Toast.LENGTH_SHORT).show();


                   tasks.add( mDatabase.getReference().child(FirebaseVar.ALLITEMS).child(keys.getKey()).get());
//                    mDatabase.getReference().child(FirebaseVar.ALLITEMS).child(keys.getKey()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DataSnapshot> task) {
//                            ItemsExploreModel adp = task.getResult().getValue(ItemsExploreModel.class);
//                            ItemsExploreModel adp2 = new ItemsExploreModel(task.getResult().getKey());
//
//
//                            list.add(adp);
//                            listKey.add(adp2);
//                        }
//                    });


                }

                Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                    @Override
                    public void onSuccess(List<Object> objects) {
                        int i=0;
                        for(Object object : objects){

                            ItemsExploreModel adp = ((DataSnapshot) object).getValue(ItemsExploreModel.class);
                            ItemsExploreModel adp2 = new ItemsExploreModel(ltKey.get(i).getKey());
                            list.add(adp);
                            listKey.add(adp2);
                            adapter.showShimmer = false;
                            i++;

                        }
                        adapter.notifyDataSetChanged();

                    }
                });


                binding.totalFavDishes.setText(Integer.toString(ltKey.size()));

            }
        });


        return binding.getRoot();
    }


    //callBack to return the list of keys
    public interface favcallBack {
        void onCallback(ArrayList<ItemsExploreModel> ltKey);
    }

    //fetch the data from fav key
    public interface favData {
        void onDataBack(ItemsExploreModel ml1, ItemsExploreModel ml2);
    }

    //fetch the keys from favorite section of user
    public void getFavKeys(favcallBack favcallBack) {
        ArrayList<ItemsExploreModel> itemKeys = new ArrayList<>();

        mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid()).child(FirebaseVar.FAVOURITE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemKeys.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    ItemsExploreModel adp = new ItemsExploreModel(snapshot1.getKey());
                    itemKeys.add(adp);

                }

                favcallBack.onCallback(itemKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //get the data from allItems node
    public void getFavData(favData favData, String favKey) {

        mDatabase.getReference().child(FirebaseVar.ALLITEMS).child(favKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2) {

                if (snapshot2.exists()) {
                    ItemsExploreModel adp = snapshot2.getValue(ItemsExploreModel.class);
                    ItemsExploreModel adp2 = new ItemsExploreModel(snapshot2.getKey());

//                    Toast.makeText(getContext(), adp.getItemName(), Toast.LENGTH_SHORT).show();
                    favData.onDataBack(adp, adp2);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
