package com.tuna.petstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.tuna.petstore.R;
import com.tuna.petstore.fragment.PetFragment;
import com.tuna.petstore.fragment.PlanFragment;
import com.tuna.petstore.fragment.StatisticsFragment;
import com.tuna.petstore.helper.BottomNavigationBehavior;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private PlanFragment planFragment;
    private PetFragment petFragment;
    private StatisticsFragment statisticsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navi);
        floatingActionButton = findViewById(R.id.fabpet);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(MainActivity.this, AddPetsActivity.class));
            }
        });
        statisticsFragment = new StatisticsFragment();
        planFragment = new PlanFragment();
        petFragment = new PetFragment();
        loadFragment(petFragment);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        floatingActionButton.setVisibility(View.VISIBLE);
                        loadFragment(petFragment);
                        return true;
                    case R.id.setting:
                        loadFragment(statisticsFragment);
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        return true;
                    case R.id.plan:
                        loadFragment(planFragment);
                        floatingActionButton.setVisibility(View.INVISIBLE);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
