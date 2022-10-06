package com.shubh.eato.Verification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.R;
import com.shubh.eato.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    String combine_phone;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();


        binding.submitSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = binding.signUpPhoneNumber.getText().toString();
                combine_phone = "+91" + phone_number;
                if (phone_number.isEmpty() || phone_number.length() != 10) {   // to make this text view visible.
                    Toast.makeText(SignUpActivity.this, "enter valid mobile number", Toast.LENGTH_SHORT).show();

                } else {
                    DatabaseReference reference = database.getReference().child("Users");
                    reference.orderByChild("PhoneNo").equalTo(combine_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {
                                Toast.makeText(SignUpActivity.this, "You are already registered , Please login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {

                                Toast.makeText(SignUpActivity.this, combine_phone, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);

                                intent.putExtra("MobileNo", combine_phone);
                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }


            }
        });


    }
}