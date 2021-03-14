package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.Fragments.PostDetailFragment;
import com.example.instagramcclone.Fragments.ProfileFragment;
import com.example.instagramcclone.Model.Notification;
import com.example.instagramcclone.Model.Post;
import com.example.instagramcclone.Model.User;
import com.example.instagramcclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context mcontext;
    private List<Notification> mNotification;

    public NotificationAdapter(Context mcontext, List<Notification> mNotification) {
        this.mcontext = mcontext;
        this.mNotification = mNotification;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = mNotification.get(position);
        getUser(holder.imageProfile, holder.username, notification.getUserid());
        holder.comment.setText(notification.getText());
        if (notification.isPost()) {
            holder.postImage.setVisibility(View.VISIBLE);
            getpostIMage(holder.postImage, notification.getPostid());
        } else {
            holder.postImage.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notification.isPost()) {
                    mcontext.getSharedPreferences("PR", Context.MODE_PRIVATE).edit()
                            .putString("postid", notification.getPostid()).apply();
                    ((FragmentActivity) mcontext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentactivity, new PostDetailFragment()).commit();
                } else {
                    mcontext.getSharedPreferences("PR", Context.MODE_PRIVATE).edit().putString("profileId", notification.getUserid())
                            .apply();
                    ((FragmentActivity) mcontext).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentactivity, new ProfileFragment()).commit();

                }
            }
        });
    }

    private void getpostIMage(final ImageView postImage, String postid) {
        FirebaseDatabase.getInstance().getReference().child("Post").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser(ImageView imageProfile, TextView username, String userid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImageurl().equals("default")) {
                    imageProfile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(user.getImageurl()).into(imageProfile);
                }
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return mNotification.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageProfile, postImage;
        public TextView username, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProfile = itemView.findViewById(R.id.image_profile_notification);
            postImage = itemView.findViewById(R.id.post_image_notification);
            username = itemView.findViewById(R.id.username_notification);
            comment = itemView.findViewById(R.id.comment_notification);

        }
    }
}
