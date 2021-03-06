package com.example.instagramcclone.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.instagramcclone.Model.Post;
import com.example.instagramcclone.Model.User;
import com.example.instagramcclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private CircleImageView imageprofile;
    private ImageView options, myPictures, savedPictures;
    private TextView followers, following, posts, fullname, bio, username;

    private FirebaseUser fUser;
    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        profileId = fUser.getUid();
        imageprofile = view.findViewById(R.id.image_profile_profiletab);
        options = view.findViewById(R.id.options);
        myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        followers = view.findViewById(R.id.followers_profile);
        following = view.findViewById(R.id.following_profile);
        posts = view.findViewById(R.id.posts_profile);
        fullname = view.findViewById(R.id.fullname_profile);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username_profile);
        userInfo();
        getfollowerAndFollowingCount();
        getPostCount();
        return view;
    }

    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter=0;
                for (DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Post post =snapshot1.getValue(Post.class);
                if(post.getPublisher().equals(profileId))counter++;
                }
                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getfollowerAndFollowingCount() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);
        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user.getImageurl().equals("default"))
                    imageprofile.setImageResource(R.mipmap.ic_launcher);      //IF url of the image is empty then we set the user image as the ic_launcher
                else Picasso.get().load(user.getImageurl()).into(imageprofile);

                username.setText(user.getUsername());
                fullname.setText(user.getName());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}