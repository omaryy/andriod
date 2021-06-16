package com.example.image;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapters.Adapter_Chat;
import models.Model_Chat;
import models.Model_users;
import notitication.Apiservice;
import notitication.Client;
import notitication.Data;
import notitication.Response;
import notitication.Sender;
import notitication.Tokens;
import retrofit2.Call;
import retrofit2.Callback;


public class chatActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileiv;
    TextView nametv,statetv;
    EditText messageedittext;
    ImageView sendbutton,attachbutton;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener seenlistner;
    DatabaseReference userReforseen;
    String hisuid;
    String myuid;
    List<Model_Chat>chatList;
    Adapter_Chat adapter_chat;
    String hisimage;
    Uri image_uri=null;
    Apiservice apiservice;
    boolean notify=false;

    private  static  final  int Camera_request_code=100;
    private  static  final int storage_request_code=200;
    private  static  final int image_pick_camera_code=300;
    private  static  final int image_pick_Gallery_code=400;

    String []camerapermissions;
    String []storagepermissions;

String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.chatrcycleview);
        profileiv=findViewById(R.id.profileiv);
        nametv=findViewById(R.id.nametvchat);

        messageedittext=findViewById(R.id.messageedittext);
        sendbutton=findViewById(R.id.sendbutton);
        attachbutton=findViewById(R.id.attachbutton);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        camerapermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Intent intent=getIntent();
        apiservice=Client.getRetrofit("https://fcm.googleapis.com/").create(Apiservice.class);

        hisuid=intent.getStringExtra("hisuid");

        uid=intent.getStringExtra("uid");
        if(uid!=null) {
            hisuid = uid;
        }
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");
        Query userquery=databaseReference.orderByChild("uid").equalTo(hisuid);

        userquery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    String name=""+ds.child("name").getValue();

                    hisimage=""+ds.child("image").getValue();



                    nametv.setText(name);
                    try {
                        Picasso.get().load(hisimage).placeholder(R.drawable.ic_baseline_account_circle_24).into(profileiv);

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
        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify=true;
                String message=messageedittext.getText().toString().trim();
                if(TextUtils.isEmpty(message))
                {
                    Toast.makeText(chatActivity.this,"can not send empty message",Toast.LENGTH_SHORT).show();
                }
                else {
                    sendmessage(message);

                }
            }
        });
        attachbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showimagepickdialogue();
            }
        });
        readmessages();
        //seenmessage();


        final DatabaseReference chatrefrence1=FirebaseDatabase.getInstance().getReference("chatlist").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid())).child(String.valueOf(hisuid));
        chatrefrence1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {

                    chatrefrence1.child("id").setValue(String.valueOf(hisuid));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference chatrefrence2=FirebaseDatabase.getInstance().getReference("chatlist").child(String.valueOf(hisuid)).child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()));
        chatrefrence2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatrefrence2.child("id").setValue(String.valueOf(myuid));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showimagepickdialogue() {
        String options[]={"camera","Gallery"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Choose Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0)
                {
                    if(!checkcamerapermission())
                    {
                        requestcamerapermission();
                    }
                    else {
                        pickfromcamera();
                    }
                }
                if(i==1)
                {
                    if(!checkstoragepermission())
                    {
                        requeststoragepermission();
                    }
                    else {
                        pickfromgallery();
                    }

                }

            }
        });
        builder.create().show();
    }
    private void pickfromgallery() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,image_pick_Gallery_code);

    }

    private void pickfromcamera() {
        ContentValues contentValues=new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"temp Pick");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"temp Pick");
        image_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,image_pick_camera_code);
    }
    private  boolean checkstoragepermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED;
        boolean result1= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;

        return result&&result1;
    }
    private void requestcamerapermission()
    {
        ActivityCompat.requestPermissions(this,camerapermissions,Camera_request_code);
    }
    private  boolean checkcamerapermission()
    {
        boolean result= ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return result;
    }
    private void requeststoragepermission()
    {
        ActivityCompat.requestPermissions(this,storagepermissions,storage_request_code);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==Camera_request_code)
        {
            if(grantResults.length>0)
            {
                boolean cameraaccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                boolean storageacccepted=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                if(cameraaccepted&&storageacccepted)
                {
                    pickfromcamera();
                }
                else {
                    Toast.makeText(this,"camera&&storage permissions are nessecary",Toast.LENGTH_SHORT).show();
                }
            }
            else {

            }

        }
        if(requestCode==storage_request_code)
        {
            if(grantResults.length>0)
            {
                boolean storageaccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                if(storageaccepted)
                {
                    pickfromgallery();
                }
                else {
                    Toast.makeText(this,"storage permissions are nessecary",Toast.LENGTH_SHORT).show();
                }
            }
            else {

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK)
        {
            if(requestCode==image_pick_Gallery_code)
            {
                image_uri=data.getData();
                try {
                    sendimagemessage(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else  if(resultCode==image_pick_camera_code)
            {
                try {
                    sendimagemessage(image_uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void sendimagemessage(final Uri image_uri) throws IOException {

        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sending Image");
        progressDialog.show();
        final String time=""+System.currentTimeMillis();
        String filenameandpath="ChatImages/"+"post_"+time;
        Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),image_uri);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        final byte[]data=baos.toByteArray();
        StorageReference ref= FirebaseStorage.getInstance().getReference().child(filenameandpath);
        ref.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String downloaduri=uriTask.getResult().toString();
                if(uriTask.isSuccessful())
                {
                    DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
                    HashMap<String,Object>hashMap=new HashMap<>();
                    hashMap.put("sender",myuid);
                    hashMap.put("receiver",hisuid);
                    hashMap.put("message",downloaduri);
                    hashMap.put("type","image");
                    hashMap.put("isSeen",false);
                    hashMap.put("time",time);
                    databaseReference.child("Chats").push().setValue(hashMap);
                    DatabaseReference database=FirebaseDatabase.getInstance().getReference("users").child(myuid);
                    database.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Model_users model_users=snapshot.getValue(Model_users.class);
                            if(notify)
                            {
                                sendnotification(hisuid,model_users.getName(),"Sent You a Photo");

                            }
                            notify=false;


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    final  DatabaseReference chatref1=FirebaseDatabase.getInstance().getReference("chatlist").child(String.valueOf(myuid)).child(String.valueOf(hisuid));
                    chatref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists())
                            {
                                chatref1.child("id").setValue(String.valueOf(hisuid));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    final  DatabaseReference chatref2=FirebaseDatabase.getInstance().getReference("chatlist").child(String.valueOf(hisuid)).child(String.valueOf(myuid));
                    chatref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(!snapshot.exists())
                            {
                                chatref2.child("id").setValue(String.valueOf(myuid));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();

            }
        });



    }

    @Override
    protected void onPause() {

        checkuserstatus();
        String time = String.valueOf(System.currentTimeMillis());
        checkonlinestatus(time);

        super.onPause();

    }
    protected  void onResume()
    {
        checkonlinestatus("online");
        super.onResume();
    }
/*
    private void seenmessage() {
        userReforseen = FirebaseDatabase.getInstance().getReference("Chats");
        seenlistner=  userReforseen.addValueEventListener(new ValueEventListener() {
                                                              @Override
                                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                  for (DataSnapshot ds:snapshot.getChildren())
                                                                  {
                                                                      Model_Chat chat=ds.getValue(Model_Chat.class);
                                                                      if(chat==null)
                                                                      {
                                                                          continue;
                                                                      }
                                                                      String sender=chat.getSender();
                                                                      String reciever=chat.getReceiver();
                                                                      if(sender==null||reciever==null)
                                                                      {
                                                                          continue;
                                                                      }

                                                                      if(chat.getReceiver().equals(myuid)&& chat.getSender().equals(hisuid)){
                                                                          HashMap <String ,Object> hashMap=new HashMap<>();
                                                                          hashMap.put("isSeen",true);
                                                                          ds.getRef().updateChildren(hashMap);


                                                                      }


                                                                      adapter_chat=new Adapter_Chat(chatActivity.this,chatList,hisimage);
                                                                      LinearLayoutManager manager = new LinearLayoutManager(chatActivity.this);
                                                                      recyclerView.setHasFixedSize(true);
                                                                      recyclerView.setLayoutManager(manager);

                                                                      adapter_chat.notifyDataSetChanged();
                                                                      recyclerView.setAdapter(adapter_chat);
                                                                  }
                                                              }

                                                              @Override
                                                              public void onCancelled(@NonNull DatabaseError error) {

                                                              }

                                                          }

        );
    }

 */

    private void readmessages() {
        chatList =new ArrayList<>();
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot ds:snapshot.getChildren())
                {
                    Model_Chat chat=ds.getValue(Model_Chat.class);
                    if(chat==null)
                    {
                        continue;
                    }
                    String sender=chat.getSender();
                    String reciever=chat.getReceiver();
                    if(sender==null||reciever==null)
                    {
                        continue;
                    }


                        if ((chat.getReceiver().equals(myuid) && chat.getSender().equals(hisuid)) || (chat.getReceiver().equals(hisuid) && chat.getSender().equals(myuid))) {

                            chatList.add(chat);
                        }


                    adapter_chat=new Adapter_Chat(chatActivity.this,chatList,hisimage);
                    adapter_chat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter_chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendmessage(final String message) {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
        String time=String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myuid);
        hashMap.put("receiver",hisuid);
        hashMap.put("message",message);
        hashMap.put("time",time);
        hashMap.put("isSeen",false);
        hashMap.put("type","text");

        databaseReference.child("Chats").push().setValue(hashMap);
        messageedittext.setText("");
        final DatabaseReference database=FirebaseDatabase.getInstance().getReference("users").child(myuid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Model_users user=snapshot.getValue(Model_users.class);
                if(notify)
                {
                    sendnotification(hisuid,user.getName(),message);
                }
                notify=false;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final  DatabaseReference chatref1=FirebaseDatabase.getInstance().getReference("chatlist").child(String.valueOf(myuid)).child(String.valueOf(hisuid));
        chatref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatref1.child("id").setValue(String.valueOf(hisuid));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final  DatabaseReference chatref2=FirebaseDatabase.getInstance().getReference("chatlist").child(String.valueOf(hisuid)).child(String.valueOf(myuid));
        chatref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatref2.child("id").setValue(String.valueOf(myuid));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendnotification(final String hisuid, final String name, final String message) {
        DatabaseReference alltokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=alltokens.orderByKey().equalTo(hisuid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    final Tokens token=ds.getValue(Tokens.class);
                    Data data=new Data(myuid,name+":"+message,"New Message",hisuid,"ChatNotification",R.drawable.ic_baseline_account_circle_24);
                    Sender sender=new Sender(data,token.getToken());
                    apiservice.sendNotification(sender)
                            .enqueue(new Callback<Response>() {
                                @Override
                                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                                    Toast.makeText(chatActivity.this,""+response.message(),Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(Call<Response> call, Throwable t) {

                                }
                            });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    protected  void onStart(){
        checkuserstatus();
        checkonlinestatus("online");
        super.onStart();
    }

    private void checkuserstatus()
    {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            myuid=user.getUid();
        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
    private void checkonlinestatus(String status)
    {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users").child(myuid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);
        databaseReference.updateChildren(hashMap);
    }
    public  boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main,menu);
      //  menu.findItem(R.id.action_search).setVisible(false);
        return  super.onCreateOptionsMenu(menu);
    }
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }
}