package com.shubh.eato;

import static com.shubh.eato.R.drawable.ic_favourite_btn;
import static com.shubh.eato.R.drawable.ic_favourite_checked_btn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.databinding.ActivityFoodItemDetailBinding;

import java.util.Objects;

public class FoodItemDetailActivity extends AppCompatActivity {

    ActivityFoodItemDetailBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    int currentValue ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate the layout
        binding = ActivityFoodItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Make instances of firebase
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        //refrences
        DatabaseReference amountRefrence = mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                .child(FirebaseVar.ORDERS).child(FirebaseVar.TEMPORDER)
                .child(getIntent().getStringExtra(FirebaseVar.KEY));


        //fetch the data and set
        //1 for int and 0 for string
        fetchAndSet(FirebaseVar.ITEMPRICE, binding.itemPriceDetail, 1);
        fetchAndSet(FirebaseVar.ITEMRATING, binding.starItemDetail, 1);
        fetchAndSet(FirebaseVar.MONTHLYBUY, binding.nPersonDetail, 1);
        fetchAndSet(FirebaseVar.NAME, binding.itemNameDetail, 0);

        Glide.with(FoodItemDetailActivity.this).load(getIntent().getStringExtra(FirebaseVar.IMG)).into(binding.itemImgDetail);


        //to check whether the item is favorite or not
        checkIsFav(new MycallBack() {
            @Override
            public void onCallback(Boolean isFav) {
                if (isFav) {
                    binding.favDetailBtn.setBackground(ContextCompat.getDrawable(FoodItemDetailActivity.this, ic_favourite_checked_btn));
                } else {
                    binding.favDetailBtn.setBackground(ContextCompat.getDrawable(FoodItemDetailActivity.this, ic_favourite_btn));

                }
            }
        });

        //set the amount
        checkAmount(new amountCallBack() {
            @Override
            public void onAmountBack(Boolean isAdded, int amount) {
                if (isAdded) {
                    currentValue = amount;
                    binding.addToCartItemDetail.setVisibility(View.GONE);
                    binding.afterClickL.setVisibility(View.VISIBLE);
                    binding.itemAmount.setText(Integer.toString(amount));
                    binding.itemPriceDetail.setText(Integer.toString(amount * getIntent().getIntExtra(FirebaseVar.ITEMPRICE, 0)));

                    if(amount == 0)
                    {
                        amountRefrence.removeValue();
                        binding.addToCartItemDetail.setVisibility(View.VISIBLE);
                        binding.afterClickL.setVisibility(View.GONE);
                        fetchAndSet(FirebaseVar.ITEMPRICE, binding.itemPriceDetail, 1);

                    }
                }


            }
        });

        //On clicking backButton goto the last activity
        binding.backBtnItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodItemDetailActivity.this, DashboardActivity.class));

            }
        });

        binding.reviewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodItemDetailActivity.this, reviewsActivity.class));
            }
        });

        //if item is already in favorite list then remove and if not then add in the list
        binding.favDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                        .child(FirebaseVar.FAVOURITE).child(getIntent().getStringExtra(FirebaseVar.KEY)).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                DatabaseReference reference = mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                                        .child(FirebaseVar.FAVOURITE).child(getIntent().getStringExtra(FirebaseVar.KEY));
                                if (Objects.equals(snapshot.getValue(String.class), "True")) {
                                    reference.removeValue();

                                } else {
                                    reference.setValue("True");

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            }
        });


        //clicking on add to cart button store the item in temporder and make invisible
        binding.addToCartItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUser != null) {

                    mDatabase.getReference().child(FirebaseVar.ALLITEMS)
                            .child(getIntent().getStringExtra(FirebaseVar.KEY)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    DatabaseReference reference = mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                                            .child(FirebaseVar.ORDERS).child(FirebaseVar.TEMPORDER)
                                            .child(getIntent().getStringExtra(FirebaseVar.KEY));

                                    reference.setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            reference.child(FirebaseVar.AMOUNT).setValue(1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    binding.addToCartItemDetail.setVisibility(View.GONE);
                                                    binding.afterClickL.setVisibility(View.VISIBLE);

                                                }
                                            });
                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(FoodItemDetailActivity.this, "Failed in adding into cart", Toast.LENGTH_SHORT).show();

                                }
                            });
                }

            }
        });

        //Incrementing the amount
        binding.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                increDecreAmount(amountRefrence, 0, currentValue);
            }
        });

        //Decrementing the amount
        binding.decBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                increDecreAmount(amountRefrence, 1, currentValue);
            }
        });


    }

    public void fetchAndSet(String id, TextView textView, int flag) {
        if (flag == 1) {
            String txt = Integer.toString(getIntent().getIntExtra(id, 0));
            textView.setText(txt);
        } else if (flag == 0) {
            textView.setText(getIntent().getStringExtra(id));
        }

    }


    public interface MycallBack {
        void onCallback(Boolean isFav);
    }

    public interface amountCallBack {
        void onAmountBack(Boolean isAdded, int amount);
    }


    // check whether item is in favorite list or not
    public void checkIsFav(MycallBack mycallBack) {
        mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid()).child(FirebaseVar.FAVOURITE).child(getIntent().getStringExtra(FirebaseVar.KEY)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (Objects.equals(snapshot.getValue(String.class), "True")) {

                    mycallBack.onCallback(true);

                } else {
                    mycallBack.onCallback(false);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void checkAmount(amountCallBack callBack) {
        mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                .child(FirebaseVar.ORDERS).child(FirebaseVar.TEMPORDER)
                .child(getIntent().getStringExtra(FirebaseVar.KEY))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.child(FirebaseVar.AMOUNT).exists()) {
                            callBack.onAmountBack(true, snapshot.child(FirebaseVar.AMOUNT).getValue(Integer.class));
                        } else {
                            callBack.onAmountBack(false, 0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

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