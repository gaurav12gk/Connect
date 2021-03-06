package com.example.instagramcclone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramcclone.Fragments.ProfileFragment;
import com.example.instagramcclone.MainActivity;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> mUsers;
    private boolean isfragment;
    private FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> mUsers, boolean isfragment) {
        this.context = context;
        this.mUsers = mUsers;
        this.isfragment = isfragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
       return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = mUsers.get(position);
        holder.btnfollow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getName());
        Picasso.get().load(user.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageprofile);
        isfollowed(user.getID(), holder.btnfollow);

        if (user.getID().equals(firebaseUser.getUid())) {
            holder.btnfollow.setVisibility(View.GONE);
        }

        // FOLLOW-> FOllowing and putting in firebase who is following whom and who has whose follower
        holder.btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(holder.btnfollow.getText().toString().equals("Follow")){
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(user.getID()).setValue(true);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getID()).child("follower")
                            .child(firebaseUser.getUid()).setValue(true);


                }else{
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(user.getID()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getID()).child("follower")
                            .child(firebaseUser.getUid()).removeValue();


                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isfragment){
                    context.getSharedPreferences("PR",Context.MODE_PRIVATE).edit().putString("profileId",user.getID()).apply();
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentactivity,new ProfileFragment())
                            .commit();

                }
                else{
                    Intent intent=new Intent(context, MainActivity.class);
                    intent.putExtra("publisherId",user.getID());
                    context.startActivity(intent);
                }
            }
        });

    }

    private void isfollowed(final String id, final Button btnfollow) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                .child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(id).exists()) {
                    btnfollow.setText("Following");
                } else btnfollow.setText("Follow");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageprofile;
        public TextView username, fullname;
        public Button btnfollow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageprofile = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernameitemusr);
            btnfollow = itemView.findViewById(R.id.button_follow);
            fullname = itemView.findViewById(R.id.fullname);
        }
    }
}
