package com.example.instagramcclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText username,name,pass,email;
    private DatabaseReference mrootref;
    Button registered;
    ProgressDialog pd;
    private FirebaseAuth auth;
    private TextView Alreadyuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Gettting id//
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        name=findViewById(R.id.Name);
        Alreadyuser =findViewById(R.id.alreadyuser);
        registered=findViewById(R.id.registerbutton);
        pd=new ProgressDialog(this);
        mrootref= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        //
        Alreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
        //Calling register for firebase activity
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtuname = username.getText().toString();
                String txtname = name.getText().toString();
                String txtmail = email.getText().toString();
                String txtpass = pass.getText().toString();
                if (txtuname.length() == 0 || txtname.length() == 0 || txtmail.length() == 0 || txtpass.length() == 0)
                    Toast.makeText(RegisterActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                else {
                    Toast.makeText(RegisterActivity.this, "Successfully Registerd", Toast.LENGTH_SHORT).show();
                    registeruser(txtname,txtuname,txtmail,txtpass);
                }
            }});

}

    private void registeruser(String nname, String uuname, String MMail, String ppass) {
        pd.setMessage("Please Wait!");
        pd.show();
        auth.createUserWithEmailAndPassword(MMail,ppass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("Name",nname);
                map.put("Email",MMail);
                map.put("Username",uuname);
                map.put("ID",auth.getCurrentUser().getUid());
                map.put("bio","");
                map.put("imageurl","default");
                mrootref.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        { pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "Update the Profile \n For Better Experience", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }