package com.shubh.eato.DashBoard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.Adapters.cartAdapter;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Models.itemCartModel;
import com.shubh.eato.SuccesfullyOrderedActivity;
import com.shubh.eato.databinding.FragmentCheckoutBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


public class CheckoutFragment extends Fragment {


    public CheckoutFragment() {
        // Required empty public constructor
    }

    FragmentCheckoutBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    Boolean isRoomDeliver = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(getLayoutInflater());

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        //reference
        DatabaseReference tempOrder = mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                .child(FirebaseVar.ORDERS).child(FirebaseVar.TEMPORDER);
        DatabaseReference adminRef = mDatabase.getReference().child(FirebaseVar.ADMIN);


        binding.billShimmer.startShimmer();

        //fetch the data from firebase
        fetchData(tempOrder, binding.cartRec, new sizeCallBack() {
            @Override
            public void onListSize(int listSize) {
                if (listSize == 0) {
                    binding.populatedCartL.setVisibility(View.GONE);
                    binding.emptyCartL.setVisibility(View.VISIBLE);
                } else {
                    binding.populatedCartL.setVisibility(View.VISIBLE);
                    binding.emptyCartL.setVisibility(View.GONE);

                }
            }
        });


        //setting up the prices
        fetchPrices(tempOrder, adminRef, new callBack() {
            @Override
            public void onPriceBack(double subtotal, double taxAndFees, double delivery, double total, boolean addressExist, String Address) {
                binding.subtotalPrice.setText("₹ " + subtotal);
                binding.taxAndFees.setText("₹ " + taxAndFees);
                binding.deliveryFees.setText("₹ " + delivery);
                binding.totalPay.setText("₹ " + total);

                if (addressExist) {
                    binding.deliverySwitch.setChecked(true);
                    binding.addressDelivery.setText(Address);
                }
            }
        });

        //check whether room delivery or not
        binding.deliverySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isRoomDeliver = b;
                    binding.addressDelivery.setVisibility(View.VISIBLE);
                    binding.addressDeliveryBtn.setVisibility(View.VISIBLE);
                } else {
                    binding.addressDelivery.setVisibility(View.GONE);
                    binding.addressDeliveryBtn.setVisibility(View.GONE);
                    tempOrder.child(FirebaseVar.ADDRESS).removeValue();

                }
            }
        });


        binding.addressDeliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempOrder.child(FirebaseVar.ADDRESS).setValue(binding.addressDelivery.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), "Selected ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date currentime=Calendar.getInstance().getTime();
                String pattern = "dd MMM, HH:MM";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(currentime);
                startActivity(new Intent(getContext(), SuccesfullyOrderedActivity.class));


                Random rnd = new Random();
                int number = rnd.nextInt(999999);

                tempOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        DatabaseReference orderRef =  mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                                .child(FirebaseVar.ORDERS).child(FirebaseVar.SUCCEDSFULORDERS).push();
                        orderRef.child("OrderDetails").setValue(snapshot.getValue());
                        orderRef.child("Details").child(FirebaseVar.PAYABLEAMOUNT).setValue(binding.totalPay.getText().toString());
                        orderRef.child("Details").child(FirebaseVar.STATUS).setValue("Pending");
                        orderRef.child("Details").child(FirebaseVar.VERIFICATIONCODE).setValue(String.format("%06d", number));
                        orderRef.child("Details").child(FirebaseVar.TIME).setValue(date).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference temp =  mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid())
                                        .child(FirebaseVar.ORDERS).child(FirebaseVar.TEMPORDER);
                                temp.removeValue();
                            }
                        });
                        if(isRoomDeliver)
                        {
                            orderRef.child("Details").child(FirebaseVar.ADDRESS).setValue(binding.addressDelivery.getText().toString());
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

//        //fragment time
//        getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
//            @Override
//            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
//                // We use a String here, but any type that can be put in a Bundle is supported
//                String result = bundle.getString("bundleKey");
//                Toast.makeText(getContext(), "result", Toast.LENGTH_SHORT).show();
//                // Do something with the result
//            }
//        });

        binding.timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                int hours = calendar.get(Calendar.HOUR_OF_DAY);
//                int minutes = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity() , new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                        Toast.makeText(getContext(), "sdfsdf", Toast.LENGTH_SHORT).show();
//                        Calendar c = Calendar.getInstance();
//                        c.set(Calendar.HOUR_OF_DAY, i);
//                        c.set(Calendar.MINUTE, i1);
//                        c.setTimeZone(TimeZone.getDefault());
//                        SimpleDateFormat format = new SimpleDateFormat("k:mm a");
//                        String time = format.format(c.getTime());
//
//                        binding.pickedTimeCheckout.setText(time);
//                    }
//                }, hours, minutes, false);

                DialogFragment timePicker = new TimePickerFragment2();
                timePicker.show(getChildFragmentManager(), "time picker");



//                DialogFragment newFragment  = new TimePickerFragment();
//                newFragment.show(getChildFragmentManager(), "7:30");
            }
        });


        return binding.getRoot();
    }


    public static class TimePickerFragment2 extends DialogFragment {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                    Bundle result = new Bundle();
//                    result.putString("bundleKey", Integer.toString(i));
//                    getParentFragmentManager().setFragmentResult("requestKey", result);
//                    requireActivity().getSupportFragmentManager().setFragmentResult("TimePickerRequestKey", new Bundle());
                   Toast.makeText(getContext(), Integer.toString(i) + " : " + Integer.toString(i1), Toast.LENGTH_SHORT).show();
                }

            }, hour, minute, false);
        }
    }



    public interface callBack {
        void onPriceBack(double subtotal, double taxAndFees, double delivery, double total, boolean addressExist, String Address);
    }


    public void fetchPrices(DatabaseReference reference, DatabaseReference adminRef, callBack callback) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double sum = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.child(FirebaseVar.ITEMPRICE).exists() && snapshot1.child(FirebaseVar.AMOUNT).exists()) {
                        int price = snapshot1.child(FirebaseVar.ITEMPRICE).getValue(Integer.class);
                        int amount = snapshot1.child(FirebaseVar.AMOUNT).getValue(Integer.class);

                        sum = sum + (price * amount);
                    }

                }

                double finalSum = sum;
                double finalSum1 = sum;
                adminRef.child(FirebaseVar.TAXANDFEESPERCENT).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot3) {
                        double tax = snapshot3.getValue(Double.class) * finalSum / 100;
                        double totalWithTaxCharge = finalSum + tax;

                        adminRef.child(FirebaseVar.DELIVERYCHARGE).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot4) {
                                double deliveryCharge = snapshot4.getValue(Double.class);

                                reference.child(FirebaseVar.ADDRESS).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot5) {
                                        if (snapshot5.exists()) {

                                            double totalPay = totalWithTaxCharge + deliveryCharge;
                                            binding.billShimmer.stopShimmer();
                                            binding.billShimmer.setShimmer(null);
                                            callback.onPriceBack(finalSum1, tax, deliveryCharge, totalPay, true, snapshot5.getValue(String.class));

                                        } else {
                                            binding.billShimmer.stopShimmer();
                                            binding.billShimmer.setShimmer(null);
                                            callback.onPriceBack(finalSum1, tax, 0, totalWithTaxCharge, false, "");

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public interface sizeCallBack {
        void onListSize(int listSize);
    }


    public void fetchData(DatabaseReference reference, RecyclerView recyclerView, sizeCallBack callBack) {
        ArrayList<itemCartModel> list = new ArrayList<>();
        ArrayList<itemCartModel> listKey = new ArrayList<>();
        cartAdapter adapter;


        //Set the Recyclerview with adapter
        adapter = new cartAdapter(list, listKey, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        //Fetch the data from database to populate the recycler view via model
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                listKey.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    if (snapshot1.child(FirebaseVar.ITEMPRICE).exists()) {
                        itemCartModel adp = snapshot1.getValue(itemCartModel.class);
                        itemCartModel adp2 = new itemCartModel(snapshot1.getKey());

                        adapter.showShimmer2 = false;
                        list.add(adp);
                        listKey.add(adp2);
                    }


                }

                callBack.onListSize(list.size());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
