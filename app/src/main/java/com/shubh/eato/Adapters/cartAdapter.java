package com.shubh.eato.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.itemCartModel;
import com.shubh.eato.R;

import java.util.ArrayList;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.cartHolder>{

    ArrayList<itemCartModel> list;
    ArrayList<itemCartModel> listKey;
    public  boolean showShimmer2 = true;
    Context context;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    public cartAdapter(ArrayList<itemCartModel> list, ArrayList<itemCartModel> listKey, Context context) {
        this.list = list;
        this.listKey = listKey;
        this.context = context;
    }

    @NonNull
    @Override
    public cartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sample_cart_items,parent,false);
        return new cartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull cartHolder holder, int position) {

        //Make instances of firebase
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        if (showShimmer2) {
            holder.shimmerFrameLayout.startShimmer();
        }
        else {


            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            itemCartModel adp = list.get(position);
            itemCartModel adp2 = listKey.get(position);

            holder.name.setText(adp.getItemName());
            holder.price.setText(Integer.toString(adp.getItemPrice()*adp.getAmount()));
            holder.amount.setText(Integer.toString(adp.getAmount()));

            Glide.with(context).load(adp.getImg()).into(holder.img);

            //refrences
            DatabaseReference amountRefrence = mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                    .child(FirebaseVar.ORDERS).child(FirebaseVar.TEMPORDER)
                    .child(adp2.getKey());

            //remove the item if amount is zero
            if(adp.getAmount() <= 0)
            {
                amountRefrence.removeValue();

            }

            holder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    amountRefrence.removeValue();
                }
            });


            holder.incBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    increDecreAmount(amountRefrence, 0, adp.getAmount());

                }
            });
            holder.decBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    increDecreAmount(amountRefrence, 1, adp.getAmount());

                }
            });

        }

        }



    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 3;
        return showShimmer2 ? SHIMMER_ITEM_NUMBER : list.size();
    }

    public static class cartHolder extends RecyclerView.ViewHolder{

        ShimmerFrameLayout shimmerFrameLayout;
        ShapeableImageView img;
        TextView name, price, amount;
        View cancel, incBtn, decBtn;

        public cartHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.cartItemsShimmer);
            img = itemView.findViewById(R.id.itemCartImg);
            name = itemView.findViewById(R.id.itemNameCart);
            price = itemView.findViewById(R.id.itemPriceCart);
            amount= itemView.findViewById(R.id.itemAmountCart);
            cancel = itemView.findViewById(R.id.cancelCart);
            incBtn = itemView.findViewById(R.id.cartIncBtn);
            decBtn = itemView.findViewById(R.id.cartDecBtn);
        }
    }



    //flag = 0 for increment and 1 for decrement
    public void increDecreAmount(DatabaseReference reference, int flag, int value)
    {
        if(flag == 0)
        {
            reference.child(FirebaseVar.AMOUNT).setValue(++value);
        }
        else
        {
            reference.child(FirebaseVar.AMOUNT).setValue(--value);
        }
    }


}
