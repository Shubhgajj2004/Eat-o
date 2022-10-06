package com.shubh.eato.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.FoodItemDetailActivity;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.R;

import java.util.ArrayList;

public class categoryItemsAdapter extends RecyclerView.Adapter<categoryItemsAdapter.catHolder> {

    ArrayList<ItemsExploreModel> list;
    ArrayList<ItemsExploreModel> listKey;
    public  boolean showShimmer = true;
    Context context;


    public categoryItemsAdapter(ArrayList<ItemsExploreModel> list, ArrayList<ItemsExploreModel> listKey, Context context) {
        this.list = list;
        this.listKey = listKey;
        this.context = context;
    }

    @NonNull
    @Override
    public catHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_category_food_item, parent, false);
        return new catHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull catHolder holder, int position) {

        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            ItemsExploreModel adp = list.get(position);
            ItemsExploreModel adp2 = listKey.get(position);

            holder.nStar.setText(Integer.toString(adp.getItemRating()));
            holder.nPeople.setText(Integer.toString(adp.getMonthlyBuy()));
            holder.itemName.setText(adp.getItemName());
            holder.itemPrice.setText(Integer.toString(adp.getItemPrice()));
            holder.itemName.setText(adp.getItemName());

            Glide.with(context).load(adp.getImg()).into(holder.itemImg);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, FoodItemDetailActivity.class);

                    intent.putExtra(FirebaseVar.IMG, adp.getImg());
                    intent.putExtra(FirebaseVar.NAME, adp.getItemName());
                    intent.putExtra(FirebaseVar.ITEMPRICE, adp.getItemPrice());
                    intent.putExtra(FirebaseVar.ITEMRATING, adp.getItemRating());
                    intent.putExtra(FirebaseVar.MONTHLYBUY, adp.getMonthlyBuy());
                    intent.putExtra(FirebaseVar.KEY, adp2.getKey());

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 5;
        return showShimmer ? SHIMMER_ITEM_NUMBER : list.size();
    }

    public class catHolder extends RecyclerView.ViewHolder {

        ShimmerFrameLayout shimmerFrameLayout;
        TextView nStar, nPeople, itemName, itemPrice;
        ShapeableImageView itemImg;

        public catHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.catShimmer);
            nStar = itemView.findViewById(R.id.nStarCat);
            nPeople = itemView.findViewById(R.id.nPeopleCat);
            itemName = itemView.findViewById(R.id.itemNameCat);
            itemPrice = itemView.findViewById(R.id.itemCatPrice);
            itemImg = itemView.findViewById(R.id.itemCatImg);


        }
    }
}
