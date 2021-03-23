package com.example.instagramcclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.instagramcclone.Adapter.UserAdapter;
import com.example.instagramcclone.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FollowerActivity extends AppCompatActivity {
    private String id;
    private String title;
    private List<String> idList;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        Toolbar toolbar = findViewById(R.id.toolbar_follower);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = findViewById(R.id.recycler_view_follower);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUser = new ArrayList<>();
        userAdapter = new UserAdapter(this, mUser, false);
        recyclerView.setAdapter(userAdapter);
        idList = new ArrayList<>();
        switch (title) {
            case "followers":
                getfollowers();
                break;
            case "following":
                getfollowing();
                break;
            case "likes":
                getlikes();
                break;

        }
    }

    private void getlikes() {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("gg", "onDataChange:"+id);

                idList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    idList.add(snapshot1.getKey());

                }
                showUsers();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getfollowing() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    idList.add(snapshot1.getKey());

                }
                showUsers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getfollowers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    idList.add(snapshot1.getKey());

                }
                showUsers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showUsers() {
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    User user=snapshot1.getValue(User.class);
                    for(String id: idList)
                    {
                        if(user.getID().equals(id))
                        {
                            mUser.add(user);
                        }
                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}