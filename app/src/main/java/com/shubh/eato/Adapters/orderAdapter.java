package com.shubh.eato.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.Models.ordersModel;
import com.shubh.eato.R;

import java.util.ArrayList;

public class orderAdapter extends RecyclerView.Adapter<orderAdapter.orderHolder> {

    ArrayList<ordersModel> list;
    ArrayList<ordersModel> listKey;
    public boolean showShimmer = true;
    Context context;
    DatabaseReference succesRef;

    public orderAdapter(ArrayList<ordersModel> list, ArrayList<ordersModel> listKey, Context context, DatabaseReference reference) {
        this.list = list;
        this.listKey = listKey;
        this.context = context;
        succesRef = reference;
    }

    @NonNull
    @Override
    public orderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_order, parent, false);
        return new orderHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull orderHolder holder, int position) {

        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        }
        else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            ordersModel adp=list.get(position);
            ordersModel adp2=listKey.get(position);
            holder.date.setText(adp.getTime());
            holder.nItems.setText("2");
            holder.code.setText(adp.getVerificationCode());
            String imgStatus = adp.getStatus();

            if(imgStatus.equals("Pending"))
            {
                holder.img.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_cooking_process));
            }
            else if(imgStatus.equals("Done"))
            {
                holder.img.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_cooked));

            }
            holder.price.setText(adp.getPayableAmount());

//            holder.rateBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    holder.ratingBar.setVisibility(View.VISIBLE);
//                    holder.rateBtn.setVisibility(View.GONE);
//                    holder.ratingBar.getRating();
//                    succesRef.child(adp2.getKey());
//
//                }
//            });




        }
    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 8;
        return showShimmer ? SHIMMER_ITEM_NUMBER : list.size();
    }

    public static class orderHolder extends RecyclerView.ViewHolder {
        ShapeableImageView img;
        TextView date, nItems, code, price;
//        Button rateBtn;
        ShimmerFrameLayout shimmerFrameLayout;
//        RatingBar ratingBar;


        public orderHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.orderStatusImgDetail);
            date = itemView.findViewById(R.id.dateOrder);
            nItems = itemView.findViewById(R.id.nItemsOrder);
            code = itemView.findViewById(R.id.verificationCodeOrder);
            price = itemView.findViewById(R.id.itemPriceOrder);
//            rateBtn = itemView.findViewById(R.id.rateOrderButton);
            shimmerFrameLayout = itemView.findViewById(R.id.orderItemsShimmer);
           // ratingBar = itemView.findViewById(R.id.ratingBarOrder);

        }
    }
}
