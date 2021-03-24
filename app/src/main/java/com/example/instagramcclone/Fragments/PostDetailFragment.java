package com.example.instagramcclone.Fragments;

import android.content.Context;
import android.content.pm.FeatureGroupInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramcclone.Adapter.PostAdapter;
import com.example.instagramcclone.MainActivity;
import com.example.instagramcclone.Model.Post;
import com.example.instagramcclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {

private String postId;
private RecyclerView recyclerView;
private PostAdapter postAdapter;
private List<Post>  postlist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_post_detail, container, false);

        //getting the postid throught the activity using the sharedprefences
        postId=getContext().getSharedPreferences("P", Context.MODE_PRIVATE).getString("postId","none");
        //Defining the recycler view for the post when we click the post int the mypictures tab in the profile fragment
        recyclerView=view.findViewById(R.id.recycler_view_postimage);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //defining the post list
        postlist=new ArrayList<>();
        postAdapter=new PostAdapter(getContext(), postlist, ((MainActivity) getActivity()).deviceWidth);
        recyclerView.setAdapter(postAdapter);    //settting the adapter to the recycler view


        //Quering throught the database to get the post with the defined postid using firebase database
       FirebaseDatabase.getInstance().getReference().child("Post").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postlist.clear();
                postlist.add(snapshot.getValue(Post.class));
                postAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }
}