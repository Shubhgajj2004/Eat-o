package com.shubh.eato.Verification;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.databinding.ActivitySignUpDetailBinding;

public class SignUpDetailActivity extends AppCompatActivity {

    private ActivitySignUpDetailBinding binding;
    private String[] Hostels = {"Bhabha Bhavan", "Gajjar Bhavan", "Swami Vivekanand Bhavan", "Tagore Bhavan", "Nehru Bhavan", "Quarters", "Localities"};
    private String[] Roles = {"Student", "Faculty", "Staff Member", "Visitor"};
    private String[] Branch = {"None", "Computer Science & Engineering", "Electronics & Communication Engineering", "Electrical Engineering", "Civil Engineering", "Chemical Engineering", "Chemistry", "Mathematics", "Physics", "PhD"};
    private String selectedHostels, selectedRoles, selectedBranch;
    private RadioButton checkedbutton;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private static Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //instaces
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        //Adapters for all spinners
        ArrayAdapter<String> adapterCity;
        ArrayAdapter<String> adapterRole;
        ArrayAdapter<String> adapterBranch;
        adapterCity = new ArrayAdapter<>(SignUpDetailActivity.this, android.R.layout.simple_spinner_item, Hostels);
        adapterRole = new ArrayAdapter<>(SignUpDetailActivity.this, android.R.layout.simple_spinner_item, Roles);
        adapterBranch = new ArrayAdapter<>(SignUpDetailActivity.this, android.R.layout.simple_spinner_item, Branch);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.hostelSignUp.setAdapter(adapterCity);
        binding.roleSignUp.setAdapter(adapterRole);
        binding.branchSignUp.setAdapter(adapterBranch);


        //Click listner for spinners
        binding.hostelSignUp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedHostels = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.roleSignUp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedRoles = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        binding.branchSignUp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                selectedBranch = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        //To Pick image with some editing features
        binding.profileSignUp.setOnClickListener(view -> ImagePicker.with(SignUpDetailActivity.this)
                .crop()                     //Crop image(Optional), Check Customization for more option
                .start());


        binding.detailSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //for radio groups (gender)
                int ButtonsID = binding.genderSignUp.getCheckedRadioButtonId();
                checkedbutton = findViewById(ButtonsID);

                //Check all inputs are empty or not
                if (!isEmpty(binding, SignUpDetailActivity.this, checkedbutton)) {
                    if (mUser != null) {
                        DatabaseReference reference = mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid());

                        reference.child(FirebaseVar.NAME).setValue(binding.nameSignUp.getText().toString()).addOnSuccessListener(unused -> reference.child(FirebaseVar.ROLE).setValue(selectedRoles).addOnSuccessListener(unused1 -> reference.child(FirebaseVar.BRANCH).setValue(selectedBranch).addOnSuccessListener(unused11 -> reference.child(FirebaseVar.HOSTEL).setValue(selectedHostels).addOnSuccessListener(unused111 -> reference.child(FirebaseVar.GENDER).setValue(checkedbutton.getText().toString()).addOnSuccessListener(unused1111 -> Toast.makeText(SignUpDetailActivity.this, "Success", Toast.LENGTH_SHORT).show())))));

                    }


                }


            }
        });


    }


    //if any Edit text is empty then return error
    public static boolean isEmpty(ActivitySignUpDetailBinding binding1, Context context, RadioButton gender) {
        if (binding1.nameSignUp.getText().toString().isEmpty()) {
            Toast.makeText(context, "Name is Empty", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (binding1.roomSignUp.getText().toString().isEmpty()) {
            Toast.makeText(context, "Room no is Empty", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (gender == null) {
            Toast.makeText(context, "Select gender", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

//    public static boolean upload(DatabaseReference reference, String path, String userUid, String value)
//    {
//
//        reference.child(path).setValue(value).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                return true;
//            }
//        })
//
//        return false;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            imgUri = data.getData();
            if (!imgUri.equals(""))
                binding.profileSignUp.setImageURI(imgUri);
            mDatabase.getReference().child(FirebaseVar.USERS).child(mUser.getUid()).child(FirebaseVar.PROFILEIMG).setValue(imgUri.toString());

        } catch (Exception ignored) {
        }
    }


}