package com.example.instagramcclone;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.instagramcclone.Fragments.HomeFragment;
import com.example.instagramcclone.Fragments.NotificationFragment;
import com.example.instagramcclone.Fragments.ProfileFragment;
import com.example.instagramcclone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class MainActivity extends AppCompatActivity {
    private Fragment selectorfragment;
    ChipNavigationBar chipNavigationBar;
    public int deviceWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceWidth = displayMetrics.widthPixels;

        chipNavigationBar = findViewById(R.id.chipnaviagation);
        chipNavigationBar.setItemSelected(R.id.navhome, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, new HomeFragment()).commit();
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.navhome:
                        selectorfragment = new HomeFragment();
                        break;
                    case R.id.navsearch:
                        selectorfragment = new SearchFragment();
                        break;
                    case R.id.navpersons:
                        selectorfragment = new ProfileFragment();
                        break;
                    case R.id.navfavourite:
                        selectorfragment = new NotificationFragment();
                        break;
                    case R.id.navadd:
                        selectorfragment = null;
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                        break;
                }
                if (selectorfragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, selectorfragment).commit();
            }
        });

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId", null);
            if (profileId != null) {
                getSharedPreferences("PR", MODE_PRIVATE).edit().putString("profileId", profileId).apply();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, new ProfileFragment()).commit();
                chipNavigationBar.setItemSelected(R.id.navpersons,true);
            }
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, new HomeFragment()).commit();
        }
    }
}