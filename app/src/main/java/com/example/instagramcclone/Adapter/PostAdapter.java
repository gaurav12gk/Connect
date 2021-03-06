package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.CommentActivity;
import com.example.instagramcclone.FollowerActivity;
import com.example.instagramcclone.Fragments.PostDetailFragment;
import com.example.instagramcclone.Fragments.ProfileFragment;
import com.example.instagramcclone.Model.Post;
import com.example.instagramcclone.Model.User;
import com.example.instagramcclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialTextView;
import com.like.LikeButton;
import com.squareup.picasso.Picasso;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context mcontext;
    private List<Post> mPosts;
    private FirebaseUser firebaseUser;
    int deviceWidth;

    public PostAdapter(Context mcontext, List<Post> mPosts, int deviceWidth) {
        this.mcontext = mcontext;
        this.mPosts = mPosts;
        this.deviceWidth = deviceWidth;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.post_item, parent, false);
        return new PostAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postimage);
        holder.description.setText(post.getDescription());
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImageurl().equals("default")) {
                    holder.profileimage.setImageResource(R.mipmap.ic_launcher);
                } else {

                    Picasso.get().load(user.getImageurl()).into(holder.profileimage);
                }
                holder.username.setText(user.getUsername());
                holder.author.setText(user.getName());
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        isLiked(post.getPostid(), holder.like);       //calling isliked method to check where the post is liked or not
        noOFLIKes(post.getPostid(), holder.noOfLikes);           //calling nooflikes method to check how many likes are there in tthe post
        getcommentst(post.getPostid(), holder.noofcomments);
        isSaved(post.getPostid(), holder.save);
        //FOr the like button which change on clikc and turns out red.
        holder.like.setOnClickListener(v -> {

            if (holder.like.getTag().equals("like")) {       //if button is not red or you didn't like the image then on database a new branch inlikes is created where we get the datat
                FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid())
                        .child(firebaseUser.getUid()).setValue(true);

                addNotification(post.getPostid(),post.getPublisher());

            } else {
                FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();
            }

        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, CommentActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("authorid", post.getPublisher());
                mcontext.startActivity(intent);


            }
        });
        holder.noofcomments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, CommentActivity.class);
                intent.putExtra("postid", post.getPostid());
                intent.putExtra("authorid", post.getPublisher());
                mcontext.startActivity(intent);

            }
        });
        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.save.getTag().equals("save")) {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(post.getPostid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).child(post.getPostid()).removeValue();


                }
            }
        });
        holder.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.getSharedPreferences("PR", Context.MODE_PRIVATE).edit()
                        .putString("profileId",post.getPublisher()).apply();
                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity,new ProfileFragment())
                        .commit();
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.getSharedPreferences("PR", Context.MODE_PRIVATE).edit()
                        .putString("profileId",post.getPublisher()).apply();
                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity,new ProfileFragment())
                        .commit();
            }
        });
        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcontext.getSharedPreferences("PR", Context.MODE_PRIVATE).edit()
                        .putString("profileId",post.getPublisher()).apply();
                ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity,new ProfileFragment())
                        .commit();
            }
        });
        holder.postimage.setOnClickListener(v -> {
            mcontext.getSharedPreferences("P",Context.MODE_PRIVATE).edit().putString("postId",post.getPostid()).apply();
            ((FragmentActivity)mcontext).getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragmentactivity,new PostDetailFragment()).commit();

        });
        holder.noOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(mcontext, FollowerActivity.class);
                intent.putExtra("id",post.getPostid());
                intent.putExtra("title","likes");
                mcontext.startActivity(intent);

            }
        });

    }

    private void addNotification(String postid, String publisher) {

        HashMap<String,Object> map =new HashMap<>();
        map.put("userid",publisher);
        map.put("text","liked your post.");
        map.put("postid",postid);
        map.put("isPost",true);
        FirebaseDatabase.getInstance().getReference().child("Notification").child(firebaseUser.getUid()).push()
                .setValue(map);
    }

    private void isSaved(String postid, ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.child(postid).exists())
        {
            image.setImageResource(R.drawable.bookmark_dark);
            image.setTag("saved");

                } else {
                    image.setImageResource(R.drawable.bookmark);
                    image.setTag("save");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView postimage, profileimage, like, comment, save, more;
        public TextView username, noOfLikes, author, noofcomments;
        SocialTextView description;
        LikeButton likeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage = itemView.findViewById(R.id.profile_image_home_fragment_postiem);
            postimage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like_post);
            likeButton=itemView.findViewById(R.id.star_button);
            comment = itemView.findViewById(R.id.commen_post);
            save = itemView.findViewById(R.id.save_post);
            more = itemView.findViewById(R.id.more);
            username = itemView.findViewById(R.id.username_post_item_home_fragment);
            noOfLikes = itemView.findViewById(R.id.nooflikes);
            author = itemView.findViewById(R.id.author);
            noofcomments = itemView.findViewById(R.id.noofcomments);
            description = itemView.findViewById(R.id.postdescription);
            postimage.getLayoutParams().height = deviceWidth;
        }
    }

    public void isLiked(String postid, ImageView imageView) {
        //  Toast.makeText(mcontext, "You cliked", Toast.LENGTH_SHORT).show();
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(firebaseUser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.like_red);
                    imageView.setTag("liked");

                } else {
                    imageView.setImageResource(R.drawable.like);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //Isliked method finished

    //method for the no of likes counting
    private void noOFLIKes(String postid, TextView textView) {
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText(snapshot.getChildrenCount() + " likes");     //snapshot.getchildrencount() get the number of users who likes the post

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //NOfolikes counting method completed/////////************************?

    private void getcommentst(String postId, TextView textView) {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView.setText("View All " + snapshot.getChildrenCount() + " Comments");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
