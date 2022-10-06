package com.shubh.eato.Greeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shubh.eato.DashboardActivity;
import com.shubh.eato.Verification.SignUpActivity;
import com.shubh.eato.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity {

    ActivityWelcomeBinding binding;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instances
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();



    }

    public void gotoLogin(View view) {

        Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
        startActivity(intent);


    }
    //every time open this app if user object is not null means student have logged in then will automatically let in DashBoard Activity
    @Override
    protected void onStart() {
        super.onStart();

        if (mUSer != null) {
            Intent intent = new Intent(WelcomeActivity.this, DashboardActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }
    }
}