package com.example.image;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import Adapters.Adapterposts;
import models.ModelPosts;


public class profileFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ImageView avatar,coverim;
    TextView nametv,emailtv,phonetv;
    FloatingActionButton fab;
    ProgressDialog pd;
    private Uri filePath;
    String profileorcover;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<ModelPosts> postsList;
    Adapterposts adapterposts;
    String uid;
    RecyclerView postsrecyclRecyclerView;




    // request code

    public static final  int ImagePickcode=1000;
    public  static  final  int Permission_code=1001;
    public static final  int permission_code_camera=1002;
    public static final  int Image_pick_Camera_Request_code=1003;



    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;


    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        avatar=view.findViewById(R.id.avatar);
        nametv=view.findViewById(R.id.nametv);
        emailtv=view.findViewById(R.id.emailtv);
        phonetv=view.findViewById(R.id.phonetv);
        coverim=view.findViewById(R.id.coverim);
        fab=view.findViewById(R.id.fab);
        pd=new ProgressDialog(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseDatabase= FirebaseDatabase.getInstance();
        postsrecyclRecyclerView=view.findViewById(R.id.recyclerviewposts);
        databaseReference=firebaseDatabase.getReference("users");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showeditprofiledialogue();
            }
        });
        Query query=databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    String name=""+ds.child("name").getValue();
                    String email=""+ds.child("email").getValue();
                    String phone=""+ds.child("phone").getValue();
                    String image=""+ds.child("image").getValue();
                    String cover=""+ds.child("cover").getValue();
                    nametv.setText(name);
                    emailtv.setText(email);
                    phonetv.setText(phone);
                    try {
                        Picasso.get().load(image).into(avatar);

                    }
                    catch (Exception e)
                    {
                        Picasso.get().load(R.drawable.ic_baseline_account_circle_24).into(avatar);

                    }
                    try {
                        Picasso.get().load(cover).into(coverim);

                    }
                    catch (Exception e)
                    {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Inflate the layout for this fragment
        postsList= new ArrayList<>();
        checkuserstatus();
        loadmyposts();
        return view;
    }
    private void checkuserstatus()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            uid=user.getUid();
        }
        else {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }
    private void loadmyposts() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postsrecyclRecyclerView.setLayoutManager(layoutManager);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference("postsChildren");
        Query query1=databaseReference1.orderByChild("uid").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts myposts=ds.getValue(ModelPosts.class);

                    myposts.setUname(ds.child("uName").getValue().toString());
                    myposts.setuDp(ds.child("udp").getValue().toString());

                    postsList.add(myposts);


                    adapterposts=new Adapterposts(getActivity(),postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference("postsDocuments");
        Query query2=databaseReference2.orderByChild("uid").equalTo(uid);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts myposts=ds.getValue(ModelPosts.class);
                    myposts.setUname(ds.child("uName").getValue().toString());
                    myposts.setuDp(ds.child("udp").getValue().toString());

                    postsList.add(myposts);


                    adapterposts=new Adapterposts(getActivity(),postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        DatabaseReference databaseReference3=FirebaseDatabase.getInstance().getReference("postsFinancial Aids");
        Query query3=databaseReference3.orderByChild("uid").equalTo(uid);
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts myposts=ds.getValue(ModelPosts.class);
                    myposts.setUname(ds.child("uName").getValue().toString());
                    myposts.setuDp(ds.child("udp").getValue().toString());

                    postsList.add(myposts);


                    adapterposts=new Adapterposts(getActivity(),postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        DatabaseReference databaseReference4=FirebaseDatabase.getInstance().getReference("postsPhones");
        Query query4=databaseReference4.orderByChild("uid").equalTo(uid);
        query4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts myposts=ds.getValue(ModelPosts.class);
                    myposts.setUname(ds.child("uName").getValue().toString());
                    myposts.setuDp(ds.child("udp").getValue().toString());

                    postsList.add(myposts);


                    adapterposts=new Adapterposts(getActivity(),postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
        DatabaseReference databaseReference5=FirebaseDatabase.getInstance().getReference("postsMoney");
        Query query5=databaseReference5.orderByChild("uid").equalTo(uid);
        query5.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts myposts=ds.getValue(ModelPosts.class);
                    myposts.setUname(ds.child("uName").getValue().toString());
                    myposts.setuDp(ds.child("udp").getValue().toString());

                    postsList.add(myposts);


                    adapterposts=new Adapterposts(getActivity(),postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    private void searchmyposts(final String searchquery) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postsrecyclRecyclerView.setLayoutManager(layoutManager);
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("posts");
        Query query=databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts myposts=ds.getValue(ModelPosts.class);
                    if(myposts.getPdescrp().toLowerCase().contains(searchquery)) {
                        postsList.add(myposts);


                    }


                    adapterposts=new Adapterposts(getActivity(),postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showeditprofiledialogue() {
        String options[]={"Edit Profile Picture","Edit Cover picture", "Edit Name","Edit Phone"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0)
                {
                    pd.setMessage("Updating profile picture");

                  profileorcover="image";
                    showimagepicturedialogue();
                }
                else    if(i==1)
                {
                    pd.setMessage("Updating cover photo");
                 profileorcover="cover";
                    showimagepicturedialogue();

                }
                else     if(i==2)
                {
                    pd.setMessage("Updating Name");
                   shownamephoneupdatedialogue("name");

                }
                else if (i==3)
                {
                    pd.setMessage("Updating phone");
                   shownamephoneupdatedialogue("phone");


                }

            }
        });
        builder.create().show();

    }
    private void shownamephoneupdatedialogue(final String name) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        builder.setTitle("update "+name);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText=new EditText(getActivity());
        editText.setHint("Enter "+name);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String value= editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value))
                {
                    pd.show();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put(name,value);
                    databaseReference.child(firebaseUser.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(),"updated ",Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    });
                }
                else {
                    Toast.makeText(getActivity(),"Pleae enter "+name,Toast.LENGTH_LONG).show();
                }


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }



    private void showimagepicturedialogue() {
        String options[]={"Camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick Image From ");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0)
                {
                   if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                   {
                       if(getActivity().checkSelfPermission(Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED||getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)
                       {
                           String[] permission={Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                           requestPermissions(permission,permission_code_camera);
                       }
                       else {
                           opencamera();
                       }
                   }
                   else {
                       opencamera();
                   }
                }


                if(i==1)
                {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                    {

                        if(getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED)
                        {

                            String []permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permissions,Permission_code);
                        }
                        else {
                            SelectImage();

                        }

                    }
                    else {
                        SelectImage();

                    }

                }


            }
        });
        builder.create().show();

    }

    private void opencamera() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");
        filePath=getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent cameraintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,filePath);
        startActivityForResult(cameraintent,Image_pick_Camera_Request_code);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {


        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
        if (requestCode == ImagePickcode  ) {


            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getActivity().getContentResolver(),
                                filePath);
                if (profileorcover.equals("image")) {
                    avatar.setImageBitmap(bitmap);
                }
                if (profileorcover.equals("cover")) {
                    coverim.setImageBitmap(bitmap);
                }
                uploadImage();
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
                if (requestCode == Image_pick_Camera_Request_code) {

                    // Setting image on image view using Bitmap

                    if (profileorcover.equals("image")) {
                        avatar.setImageURI(filePath);
                    }
                    if (profileorcover.equals("cover")) {
                        coverim.setImageURI(filePath);
                    }

                    uploadImage();
                }
            }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==Permission_code)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                SelectImage();
            }

        }
        if(requestCode==permission_code_camera)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED)
            {
                opencamera();
            }
            else {

            }
        }

    }


    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(

                intent,
                ImagePickcode);

    }

    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    Task<Uri> uritak=taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uritak.isSuccessful());

                                    Uri downloaduri=uritak.getResult();

                                    if(uritak.isSuccessful())
                                    {
                                        HashMap<String,Object> results= new HashMap<>();
                                        results.put(profileorcover,downloaduri.toString());
                                        databaseReference.child(firebaseUser.getUid()).updateChildren(results).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                pd.dismiss();
                                                Toast.makeText(getActivity(),"Image Updated",Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(getActivity()," Error  Updating Image",Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    }
                                    else {
                                        pd.dismiss();
                                        Toast.makeText(getActivity(),"Some error occured", Toast.LENGTH_LONG).show();
                                    }

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkuserstatus();
        }
        else if(id==R.id.action_Settings)
        {
            startActivity(new Intent(getContext(),SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}