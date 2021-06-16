package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.HashMap;

public class loginActivity extends AppCompatActivity {
    private CallbackManager mcallbackmanager;


    private static final int RC_SIGN_IN = 100;
    private  static  final int fc=200;

    GoogleSignInClient mGoogleSignInClient;
    EditText emaledittext,passwordedittext;
    TextView nothaveaccount,forgetpasstv;
    Button loginbutton;

    ImageView fb1;
    ProgressDialog pd;
    private AccessTokenTracker accessTokenTracker;

private  FirebaseAuth.AuthStateListener authStateListener;
ImageView fb;

LoginButton loginbuttonfacebook;
    private FirebaseAuth mAuth;
    private static final String Tag="FaceBook Authutication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
fb1=findViewById(R.id.fb1);
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();
        fb =  findViewById(R.id.fb);

        emaledittext=findViewById(R.id.emailedittext);

        passwordedittext=findViewById(R.id.passwordedittext);

        loginbutton=findViewById(R.id.Loginbutton);
loginbuttonfacebook=findViewById(R.id.login_button);
mcallbackmanager=CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
loginbuttonfacebook.registerCallback(mcallbackmanager, new FacebookCallback<LoginResult>() {
    @Override
    public void onSuccess(LoginResult loginResult) {

        Log.d(Tag,"On success"+loginResult);

        handlefacebooktoken(loginResult.getAccessToken());


    }

    @Override
    public void onCancel() {
        Log.d(Tag,"On Cancel");


    }

    @Override
    public void onError(FacebookException error) {
        Log.d(Tag,"On Cancel"+error);

    }
});


        forgetpasstv=findViewById(R.id.forgetpasswordtv);

        pd=new ProgressDialog(this);
        pd.setMessage("logging in......");

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emaledittext.getText().toString();
                String password=passwordedittext.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    emaledittext.setError("Invalid Email");
                    emaledittext.setFocusable(true);
                }
                else {
                    loginuser(email,password);
                }



            }
        });
        forgetpasstv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showrecoverpassworddialoge();
            }
        });


        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    startActivity(new Intent(loginActivity.this,DashboardActivity.class));
                }
                else {
                    Toast.makeText(loginActivity.this,"Error",Toast.LENGTH_LONG).show();
                }
            }
        };
        accessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
if(currentAccessToken==null)
{
    mAuth.signOut();
}
            }
        };


    }
    public void onClick(View v) {
        if (v == fb) {
            loginbuttonfacebook.performClick();
        }

    }

    private void handlefacebooktoken(AccessToken accessToken) {
        Log.d(Tag,"handle Face Book Token"+ accessToken);
AuthCredential credential= FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            if(task.getResult().getAdditionalUserInfo().isNewUser())
                            {
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

                            }

                            startActivity(new Intent(loginActivity.this, DashboardActivity.class));
                            finish();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginActivity.this,"login failed",Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    private void showrecoverpassworddialoge() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LinearLayout linearLayout=new LinearLayout(this);
        builder.setTitle("Recover password");
        final EditText emailed=new EditText(this);
        emailed.setHint("Email");
        emailed.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailed.setMinEms(16);
        linearLayout.addView(emailed);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String email=emailed.getText().toString().trim();
                beginrecovery(email);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    private void beginrecovery(String email) {
        pd.setMessage("Sending email.....");
        pd.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                if(task.isSuccessful())
                {
                    Toast.makeText(loginActivity.this,"Email sent",Toast.LENGTH_LONG).show();

                }
                else {
                    Toast.makeText(loginActivity.this,"failed",Toast.LENGTH_LONG).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(loginActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


    }

    private void loginuser(String email, String password) {
        pd.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            pd.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(loginActivity.this, DashboardActivity.class));
                            finish();

                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.

                            Toast.makeText(loginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(loginActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



    public  boolean onSupportNavigateUp()
    {
        onBackPressed();
        return  super.onSupportNavigateUp();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

    mcallbackmanager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                // ...
            }
        }

    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            if(task.getResult().getAdditionalUserInfo().isNewUser())
                            {
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

                            }

                            startActivity(new Intent(loginActivity.this, DashboardActivity.class));
                            finish();
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(loginActivity.this,"login failed",Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener!=null)
        {
            mAuth.removeAuthStateListener(authStateListener);

        }
    }
}