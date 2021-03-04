package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
    private Context mcontext;
    private List<Post> mPosts;
    private FirebaseUser firebaseUser;

    public PostAdapter(Context mcontext, List<Post> mPosts) {
        this.mcontext = mcontext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(mcontext).inflate(R.layout.post_item,parent,false);
                return new PostAdapter.ViewHolder(view);

             }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Post post=mPosts.get(position);
        Picasso.get().load(post.getImageurl()).into(holder.postimage);
        holder.description.setText(post.getDescription());
        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user.getImageurl().equals("default"))
                {
                  holder.profileimage.setImageResource(R.mipmap.ic_launcher);
                }else{

                    Picasso.get().load(user.getImageurl()).into(holder.profileimage);
                }
                holder.username.setText(user.getUsername());
                holder.author.setText(user.getName());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //FOr the like button which change on clikc and turns out red.
holder.like.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if()
        {       //if button is not red or you didn't like the image then on database a new branch inlikes is created where we get the datat
            FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).setValue(true);

        }else
        {
            FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostid()).child(firebaseUser.getUid()).removeValue();


        }
    }
});
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            public ImageView postimage,profileimage,like,comment,save,more;
            public TextView username,noOfLikes,author,noofcomments;
            SocialTextView description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.profile_image_home_fragment_postiem);
            postimage=itemView.findViewById(R.id.post_image);
            like=itemView.findViewById(R.id.like_post);
            comment=itemView.findViewById(R.id.commen_post);
            save =itemView.findViewById(R.id.save_post);
            more=itemView.findViewById(R.id.more);
            username=itemView.findViewById(R.id.username_post_item_home_fragment);
            noOfLikes=itemView.findViewById(R.id.nooflikes);
            author=itemView.findViewById(R.id.author);
            noofcomments=itemView.findViewById(R.id.noofcomments);
            description=itemView.findViewById(R.id.postdescription);
      }
    }
}
