package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.Model.Comment;
import com.example.instagramcclone.Model.User;
import com.example.instagramcclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context mCOntext;
    List<Comment> mCommmnts;
    FirebaseUser fUser;

    public CommentAdapter(Context mCOntext, List<Comment> mCommmnts) {
        this.mCOntext = mCOntext;
        this.mCommmnts = mCommmnts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCOntext).inflate(R.layout.comment_item,parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Comment comment=mCommmnts.get(position);
        holder.comment.setText(comment.getComment());
       //Now we need the user for that we have to extract the user from the User branch in the
        //          firebase database
        FirebaseDatabase.getInstance().getReference().child("User").child(comment.getPublisher()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);

                holder.username.setText(user.getUsername());
                if(user.getImageurl().equals("default"))
                    holder.imageProfile.setImageResource(R.mipmap.ic_launcher);      //IF url of the image is empty then we set the user image as the ic_launcher
                else Picasso.get().load(user.getImageurl()).into(holder.imageProfile);  //Getting user image from the imgage url
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mCommmnts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageProfile;
        public TextView username;
        public TextView comment;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            imageProfile=itemView.findViewById(R.id.image_porfile);
            username=itemView.findViewById(R.id.username_incomment);
            comment=itemView.findViewById(R.id.commenting_text);
        }
    }
}
