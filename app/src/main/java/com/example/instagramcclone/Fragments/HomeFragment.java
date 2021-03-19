package com.example.instagramcclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramcclone.Adapter.PostAdapter;
import com.example.instagramcclone.Model.Post;
import com.example.instagramcclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private RecyclerView recyclerViewpost;
    private PostAdapter postAdapter;
    private List<Post> postlist;
    private List<String> followinglist;
  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      View view=inflater.inflate(R.layout.fragment_home, container, false);
            recyclerViewpost=view.findViewById(R.id.recyclerview_post);
            recyclerViewpost.setHasFixedSize(true);
      LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
      linearLayoutManager.setStackFromEnd(true);        //So that latest post available on the top
      linearLayoutManager.setReverseLayout(true);
      recyclerViewpost.setLayoutManager(linearLayoutManager);
      postlist=new ArrayList<>();
      postAdapter=new PostAdapter(getContext(),postlist);
      recyclerViewpost.setAdapter(postAdapter);
      followinglist=new ArrayList<>();

      //creating a method to check for the user whom i following to get the latest post of that users
      checkFollowinUsers();

      return view; }


    private void checkFollowinUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followinglist.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    followinglist.add(snapshot1.getKey());

                }
                followinglist.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void readPost() {
  FirebaseDatabase.getInstance().getReference().child("Post").addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
          postlist.clear();
          for(DataSnapshot snapshot1: snapshot.getChildren())
          {
              Post post=snapshot1.getValue(Post.class);
              for(String id:followinglist)
              {
                  if (post.getPublisher().equals(id))
                      postlist.add(post);
              }
          }
          postAdapter.notifyDataSetChanged();
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
  });
  }
}