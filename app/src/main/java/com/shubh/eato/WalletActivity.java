package com.shubh.eato;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.shubh.eato.FirebaseVarClass.FirebaseVar;
import com.shubh.eato.Wallet.RewardFragment;
import com.shubh.eato.Wallet.TransactionFragment;
import com.shubh.eato.databinding.ActivityWalletBinding;

import java.util.ArrayList;
import java.util.List;

public class WalletActivity extends AppCompatActivity {

    ActivityWalletBinding binding;
    FirebaseDatabase mDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUSer;
    private WalletActivity activity;
    private ViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;

        //instances
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        initView();

        //start shimmer animation
        binding.currentBalanceWalletShimmer.startShimmer();
        binding.pointsAmountWalletShimmer.startShimmer();

        //refrences
        DatabaseReference userRef = mDatabase.getReference().child(FirebaseVar.USERS).child(mUSer.getUid());

        //fetch the balances
        fetchAndSetBalance(userRef);



    }

    private void fetchAndSetBalance(DatabaseReference reference) {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.pointsAmountWallet.setText(Double.toString(snapshot.child(FirebaseVar.POINTS).getValue(Double.class)));
                binding.pointsAmountWalletShimmer.stopShimmer();
                binding.pointsAmountWalletShimmer.setShimmer(null);

                binding.currentBalanceWallet.setText("₹ "+Double.toString(snapshot.child(FirebaseVar.CURRENTBALANCE).getValue(Double.class)));
                binding.currentBalanceWalletShimmer.stopShimmer();
                binding.currentBalanceWalletShimmer.setShimmer(null);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initView() {
        setupViewPager(binding.viewPagerWallet);


        new TabLayoutMediator(binding.tabLayoutWallet, binding.viewPagerWallet,
                (tab, position) -> {
                    tab.setText(adapter.mFragmentTitleList.get(position));
                }).attach();

        for (int i = 0; i < binding.tabLayoutWallet.getTabCount(); i++) {

            TextView tv = (TextView) LayoutInflater.from(WalletActivity.this)
                    .inflate(R.layout.custom_tab, null);

            binding.tabLayoutWallet.getTabAt(i).setCustomView(tv);
        }
    }

    private void setupViewPager(ViewPager2 viewPager) {
        adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(),
                activity.getLifecycle());
        adapter.addFragment(new TransactionFragment(), "Transactions");
        adapter.addFragment(new RewardFragment(), "Rewards");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

    }


    class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return mFragmentList.size();
        }
    }
}