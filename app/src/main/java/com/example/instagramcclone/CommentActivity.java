package com.example.instagramcclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramcclone.Adapter.CommentAdapter;
import com.example.instagramcclone.Model.Comment;
import com.example.instagramcclone.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.interfaces.DSAKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView recyclerViewcomment;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
 private EditText addComment;
 private CircleImageView imageprofile;
 private TextView postComment;

 private String postId;
 private String authorId;
 FirebaseUser fUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Toolbar toolbar=findViewById(R.id.toolbar_comment);
        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Comments");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
        addComment=findViewById(R.id.addacomment);
        imageprofile=findViewById(R.id.image_profile_comment);
        postComment=findViewById(R.id.posting_comement);
        recyclerViewcomment =findViewById(R.id.comments_recycleview);
        recyclerViewcomment.setHasFixedSize(true);
        recyclerViewcomment.setLayoutManager(new LinearLayoutManager(this));
        commentList=new ArrayList<>();
        commentAdapter=new CommentAdapter(this,commentList);
        recyclerViewcomment.setAdapter(commentAdapter);
        Intent intent=  getIntent();
        postId=intent.getStringExtra("postid");
        authorId=intent.getStringExtra("authorid");
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        getuserimage();
        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(addComment.getText().toString()))
                    Toast.makeText(CommentActivity.this, "Comment is Empty!", Toast.LENGTH_SHORT).show();
           else{
               putcomment();
                    addComment.setText(null);
                }
            }
        });
//Creating method for getting the comment from the database so that the user can see the comment in tht recycler view
        getcomment();

    }

    private void getcomment() {
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Comment comment=snapshot1.getValue(Comment.class);
                    commentList.add(comment);
                }commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void putcomment() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("comments",addComment.getText().toString());
        map.put("publisher",fUser.getUid());
        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CommentActivity.this, "Comment Added!", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(CommentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getuserimage() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user.getImageurl().equals("default"))imageprofile.setImageResource(R.mipmap.ic_launcher);
               else Picasso.get().load(user.getImageurl()).into(imageprofile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}