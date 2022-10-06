package com.shubh.eato;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.shubh.eato.DashBoard.AllItemsFragment;
import com.shubh.eato.DashBoard.CheckoutFragment;
import com.shubh.eato.DashBoard.ExploreFragment;
import com.shubh.eato.DashBoard.FavouriteFragment;
import com.shubh.eato.DashBoard.OrdersFragment;
import com.shubh.eato.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.navView.setItemIconTintList(null);
        binding.navView.setOnItemSelectedListener(this::onNavigationItemSelected);

        Fragment frag=new ExploreFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.Dashboard_Frame,frag).commit();




    }


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId())
        {
            case R.id.Explore:
                fragment=new ExploreFragment();
                break;

            case R.id.Favourite:
                fragment=new FavouriteFragment();
                break;

            case R.id.AllItems:
                fragment=new AllItemsFragment();
                break;

            case R.id.Orders:
                fragment=new OrdersFragment();
                break;

            case R.id.Checkout:
                fragment=new CheckoutFragment();
                break;

        }

        getSupportFragmentManager().beginTransaction().replace(R.id.Dashboard_Frame,fragment).commit();
        return true;
    }
}