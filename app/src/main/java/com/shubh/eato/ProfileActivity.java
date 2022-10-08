package com.shubh.eato;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.shubh.eato.Profile.AttributionActivity;
import com.shubh.eato.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.feedbackButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, feedbackActivity.class));
            }
        });




    }

    public void backBtn(View view) {
        finish();
    }

    public void attributionOnClick(View view) {
        startActivity(new Intent(ProfileActivity.this, AttributionActivity.class));

    }
}