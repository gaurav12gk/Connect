package com.example.instagramcclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.instagramcclone.Adapter.TagAdapter;
import com.example.instagramcclone.Model.User;
import com.example.instagramcclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.instagramcclone.Adapter.UserAdapter;


public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<User> mUsers;
    private UserAdapter userAdapter;
    private RecyclerView recyclerviewtag;
    private List<String> mHashTags, mHashcount;
    private TagAdapter tagAdapter;


    private SocialAutoCompleteTextView searchBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_user);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerviewtag = view.findViewById(R.id.recyclerview_tags);
        recyclerviewtag.setHasFixedSize(true);
        recyclerviewtag.setLayoutManager(new LinearLayoutManager(getContext()));
        mHashTags = new ArrayList<>();
        mHashcount = new ArrayList<>();

        tagAdapter = new TagAdapter(getContext(), mHashTags, mHashcount);
        recyclerviewtag.setAdapter(tagAdapter);
        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(), mUsers, true);
        recyclerView.setAdapter(userAdapter);
        searchBar = view.findViewById(R.id.searching_bar);
        readUsers();
        readtags();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchuser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
filter(s.toString());
            }
        });
        return view;
    }

    private void readtags() {
        FirebaseDatabase.getInstance().getReference().child("HashTags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mHashTags.clear();
                mHashcount.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    mHashTags.add(snapshot1.getKey());
                    mHashcount.add(snapshot1.getChildrenCount() + "");
                }
                tagAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (TextUtils.isEmpty(searchBar.getText().toString())) {
                    mUsers.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        User user = snapshot1.getValue(User.class);
                        mUsers.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void searchuser(String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("Username").startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot2 : snapshot.getChildren()) {
                    User user = snapshot2.getValue(User.class);
                    mUsers.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filter(String s)
    {
        List<String> mSearchTags=new ArrayList<>();
        List<String> mSearchTagsCount = new ArrayList<>();
         for(String s1: mHashTags)
         {
             if(s1.toLowerCase().contains(s.toLowerCase())){
                 mSearchTags.add(s1);
                 mSearchTagsCount.add(mHashcount.get(mHashTags.indexOf(s1)));
         }
         }
         tagAdapter.filter(mSearchTags,mSearchTagsCount);


    }
}