package com.shubh.eato.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.shubh.eato.Models.ItemsExploreModel;
import com.shubh.eato.Models.reviewsModel;
import com.shubh.eato.R;

import java.util.ArrayList;

public class reviewsAdapter extends RecyclerView.Adapter<reviewsAdapter.reviewsHolder>{

    ArrayList<reviewsModel> list;
    Context context;

    public reviewsAdapter(ArrayList<reviewsModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public reviewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_reviews,parent,false);
        return new reviewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull reviewsHolder holder, int position) {
        reviewsModel adp=list.get(position);

        holder.date.setText(adp.getPublishedDate());
        holder.name.setText(adp.getPublisherName());
        holder.nStar.setText(adp.getPublishedStar());

        Glide.with(context).load(adp.getPublisherImg()).into(holder.img);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class reviewsHolder extends RecyclerView.ViewHolder{

        ShapeableImageView img;
        TextView name, date, nStar;
        public reviewsHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.publisherProfileImg);
            name = itemView.findViewById(R.id.publisherNamereview);
            date = itemView.findViewById(R.id.publishedDateReview);
            nStar = itemView.findViewById(R.id.publishedStarReview);

        }
    }
}
