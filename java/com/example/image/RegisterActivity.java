package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText emailedittexr,passwordedittext;
    Button registerbutton;
    TextView haveaccounttv;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        emailedittexr=findViewById(R.id.emailedittext);
        passwordedittext=findViewById(R.id.passwordedittext);
        registerbutton=findViewById(R.id.registerbutton);
        haveaccounttv=findViewById(R.id.haveaccounttv);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Registeration user   ");

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailedittexr.getText().toString().trim();
                String password=passwordedittext.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emailedittexr.setError("Invalid Email");
                    emailedittexr.setFocusable(true);
                }
                else if (password.length()<6)
                {
                    passwordedittext.setError("password length at least 6 characters");
                    passwordedittext.setFocusable(true);

                }
                else {
                    registeruser(email,password);
                }


            }
        });
        haveaccounttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this,loginActivity.class));
                finish();
            }
        });


    }

    private void registeruser(String email, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email=user.getEmail();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name","");
                            hashMap.put("onlineStatus","online");
                            hashMap.put("phone","");
                            hashMap.put("image","");
                            hashMap.put("cover","");
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("users");

                            reference.child(uid).setValue(hashMap);


                            Toast.makeText(RegisterActivity.this,"Registerd ",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  boolean onSupportNavigateUp()
    {
        onBackPressed();
        return  super.onSupportNavigateUp();
    }
}
