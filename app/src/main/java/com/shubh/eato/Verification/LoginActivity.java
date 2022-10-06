package com.shubh.eato.Verification;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    String combine_phone2;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instances of database
        database = FirebaseDatabase.getInstance();

        binding.loginSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_number = binding.loginPhoneNoEdit.getText().toString();
                combine_phone2 = "+91" + phone_number;
                if (phone_number.isEmpty() || phone_number.length() != 10) {   // to make this text view visible.
                    Toast.makeText(LoginActivity.this, "enter valid mobile number", Toast.LENGTH_SHORT).show();

                } else {


                    DatabaseReference reference = database.getReference().child(FirebaseVar.USERS);
                    reference.orderByChild("PhoneNo").equalTo(combine_phone2).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.getValue() != null) {
                                Toast.makeText(LoginActivity.this, combine_phone2, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);

                                intent.putExtra("MobileNo", combine_phone2);
                                intent.putExtra("boolean", "true");
                                startActivity(intent);
                            } else {

                                Toast.makeText(LoginActivity.this, "User not found. Please Sign up", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
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