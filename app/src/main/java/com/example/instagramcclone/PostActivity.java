package com.example.instagramcclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

public class PostActivity extends AppCompatActivity {
private ImageView close,imageadded;
private Uri imageuri;
private SocialAutoCompleteTextView description;
private TextView post;
private  String imageurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        close=findViewById(R.id.close);
        imageadded=findViewById(R.id.imageadded);
        post=findViewById(R.id.post);
        description=findViewById(R.id.description);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this,MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
        CropImage.activity().start(PostActivity.this);

    }

    private void upload() {
        ProgressDialog pd= new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if(imageuri!=null)
        {
            StorageReference filepth= FirebaseStorage.getInstance().getReference("Posts").child( System.currentTimeMillis()+'.'+getfileExtension(imageuri)) ;
            StorageTask uploadtask=filepth.putFile(imageuri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }return filepth.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override                                                                                                    
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloaduri=task.getResult();
                    imageurl=downloaduri.toString();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Post");
                    String postid=ref.push().getKey();
                    HashMap<String,Object> map= new HashMap<>();
                                map.put("postid",postid);
                                map.put("imageurl",imageurl);map.put("description",description.getText().toString());
                                map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                ref.child(postid).setValue(map);
                                DatabaseReference mhashTagref=FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags=description.getHashtags();
                    if(!hashTags.isEmpty())
                    {
                        for (String tag:hashTags)
                        {
                            map.clear();
                            map.put("tag",tag.toLowerCase());
                            map.put("postid",postid);
                            mhashTagref.child(tag.toLowerCase()).child(postid).setValue(map);
                        }
                    }
                    pd.dismiss();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "No image is selected ", Toast.LENGTH_SHORT).show();
        }
    }

    private String getfileExtension(Uri uri) {

return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&resultCode==RESULT_OK) {
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageuri=result.getUri();
            imageadded.setImageURI(imageuri);

        }
        else{
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}