package com.example.instagramcclone;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.instagramcclone.Fragments.ProfileFragment;
import com.example.instagramcclone.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private Uri mImageUri;
    private StorageTask uploadtask;
    private StorageReference storageRef;
    private ImageView closeicon;
    private CircleImageView imageprofile;
    private TextView save;
    private TextView changePhoto;
    private TextInputEditText fullname, username, bio;

    private FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        fullname = findViewById(R.id.fullname_editprofile);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        username = findViewById(R.id.username_editprofile);
        bio = findViewById(R.id.bio_editprofile);
        changePhoto = findViewById(R.id.change_photo);
        save = findViewById(R.id.save_Editprofile);
        imageprofile = findViewById(R.id.imageprofile_editprofile);
        closeicon = findViewById(R.id.close_editprofile);
        storageRef = FirebaseStorage.getInstance().getReference().child("Uploads");
        FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                fullname.setText(user.getName());
                username.setText(user.getUsername());
                bio.setText(user.getBio());
                Picasso.get().load(user.getImageurl()).into(imageprofile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        closeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });

        imageprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                UploadProfile();
           finish();
             }
        });
    }

    private void UploadProfile() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("Name",fullname.getText().toString());
        map.put("Username",username.getText().toString());
        map.put("bio",bio.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).updateChildren(map);

    }

    private void uploadIMage() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading Image");
        pd.show();

        if (mImageUri != null) {
            StorageReference fileref = storageRef.child(System.currentTimeMillis() + ".jpeg");
            uploadtask = fileref.putFile(mImageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileref.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloaduri=task.getResult();
                    String url=downloaduri.toString();

                    FirebaseDatabase.getInstance().getReference().child("Users").child(fuser.getUid()).child("imageurl").setValue(url);
                     pd.dismiss();
                }
                else{
                    Toast.makeText(EditProfileActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            uploadIMage();
        } else {
            Toast.makeText(this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
        }
    }


}