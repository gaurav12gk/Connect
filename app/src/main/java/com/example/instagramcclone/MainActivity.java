package com.example.instagramcclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.instagramcclone.Fragments.HomeFragment;
import com.example.instagramcclone.Fragments.NotificationFragment;
import com.example.instagramcclone.Fragments.ProfileFragment;
import com.example.instagramcclone.Fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomnav);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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
                if (selectorfragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, selectorfragment).commit();
                }
                return true;
            }
        });
        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String profileId = intent.getString("publisherId");
            getSharedPreferences("PROFILE", MODE_PRIVATE).edit().putString("profileId", profileId).apply();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, new ProfileFragment()).commit();
            bottomNavigationView.setSelectedItemId(R.id.navpersons);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity, new HomeFragment()).commit();
        }
    }
}