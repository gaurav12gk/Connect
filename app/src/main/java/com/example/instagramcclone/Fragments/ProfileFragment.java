package com.example.instagramcclone.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.Adapter.PhotoAdapter;
import com.example.instagramcclone.EditProfileActivity;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;


public class ProfileFragment extends Fragment {
    private PhotoAdapter savedPostadapter;
    private List<Post> savedpostlist;

    private CircleImageView imageprofile;
    private ImageView options, myPictures, savedPictures;
    private Button editproFile;
    private TextView followers, following, posts, fullname, bio, username;
    private RecyclerView recyclerViewpostss, recyclerViewsaves;
    private PhotoAdapter photoAdapter;
    private List<Post> myPhotoList;
    private FirebaseUser fUser;
    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("PR", Context.MODE_PRIVATE).getString("profileId", "none");
        if (data.equals("none")) {
            profileId = fUser.getUid();
        }else {
            profileId = data;
            getContext().getSharedPreferences("PR",Context.MODE_PRIVATE).edit().clear().commit();

        }
        Log.d("Game", "onCreateView:"+data);

        Log.d("Game", "onCreateView:"+data);

        imageprofile = view.findViewById(R.id.image_profile_profiletab);
        options = view.findViewById(R.id.options);
        editproFile = view.findViewById(R.id.edit_profile);
        myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        followers = view.findViewById(R.id.followers_profile);
        following = view.findViewById(R.id.following_profile);
        posts = view.findViewById(R.id.posts_profile);
        fullname = view.findViewById(R.id.fullname_profile);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username_profile);

        recyclerViewpostss = view.findViewById(R.id.recucler_view_pictures_profile);
        recyclerViewpostss.setHasFixedSize(true);
        recyclerViewpostss.setLayoutManager(new GridLayoutManager(getContext(), 3));

        myPhotoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getContext(), myPhotoList);

        userInfo();
        getfollowerAndFollowingCount();
        getPostCount();
        myPhotos();
        recyclerViewpostss.setAdapter(photoAdapter);
        recyclerViewsaves = view.findViewById(R.id.recycler_view_saved);
        recyclerViewsaves.setHasFixedSize(true);
        recyclerViewpostss.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewsaves.setLayoutManager(new GridLayoutManager(getContext(), 3));
        savedpostlist = new ArrayList<>();

        savedPostadapter = new PhotoAdapter(getContext(), savedpostlist);
        recyclerViewsaves.setAdapter(savedPostadapter);
        getsavedPhotos();
        if (profileId.equals(fUser.getUid())) {
            editproFile.setText("Edit Profile");
        } else {
            checkfollowingStatus();
        }
        editproFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btntext = editproFile.getText().toString();
                if (btntext.equals("Edit Profile")) {
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                } else {
                    if (btntext.equals("Follow")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").child(profileId)
                                .setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers").child(fUser.getUid())
                                .setValue(true);

                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").child(profileId)
                                .removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers").child(fUser.getUid())
                                .removeValue();
                    }
                }

            }
        });

        recyclerViewpostss.setVisibility(View.VISIBLE);
        recyclerViewsaves.setVisibility(GONE);
        myPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewpostss.setVisibility(View.VISIBLE);
                recyclerViewsaves.setVisibility(GONE);
            }
        });
        savedPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                recyclerViewpostss.setVisibility(GONE);
                recyclerViewsaves.setVisibility(View.VISIBLE);
            }
        });

        return view;

    }

    @Override
    public void onDestroy() {
        getContext().deleteSharedPreferences("PROFILE");
        super.onDestroy();
    }

    private void getsavedPhotos() {

        List<String> savedIds = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Saves").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    savedIds.add(snapshot1.getKey());
                }
                FirebaseDatabase.getInstance().getReference().child("Post").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        savedpostlist.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Post post = snapshot1.getValue(Post.class);
                            for (String id : savedIds) {
                                if (post.getPostid().equals(id)) savedpostlist.add(post);
                            }
                        }

                        savedPostadapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void myPhotos() {
        FirebaseDatabase.getInstance().getReference().child("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myPhotoList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Post post = snapshot1.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)) {
                        myPhotoList.add(post);
                    }
                    Log.d("publisher", "onDataChange: " + post.getPostid());
                }

                Collections.reverse(myPhotoList);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkfollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(profileId).exists()) {
                    editproFile.setText("Following");
                } else {
                    editproFile.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Post").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Post post = snapshot1.getValue(Post.class);
                    if (post.getPublisher().equals(profileId)) counter++;
                }
                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getfollowerAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);
        ref.child("follower").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText("" + snapshot.getChildrenCount());
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
                if (user.getImageurl().equals("default"))
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