package com.example.image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ADDPOSTSActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{
    ActionBar actionBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ImageView postiv;
    EditText titleet;
    Button uploadbtn;
    Uri image_uri=null;
    String name,email,uid,dp;
    String city1;
    ProgressDialog pd;
    private  static  final  int Camera_request_code=100;
    private  static  final int storage_request_code=200;
    private  static  final int image_pick_camera_code=300;
    private  static  final int image_pick_Gallery_code=400;

    String []camerapermissions;
    String []storagepermissions;
    String s="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_d_d_p_o_s_t_s);
Spinner myspanner=(Spinner)findViewById(R.id.spinner);

ArrayAdapter<String> myadapter=new ArrayAdapter<String>(ADDPOSTSActivity.this,android.R.layout.simple_list_item_1,
        getResources().getStringArray(R.array.names));
myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
myspanner.setAdapter(myadapter);
        myspanner.setOnItemSelectedListener(this);


        camerapermissions=new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagepermissions=new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Intent intent=getIntent();
        s=intent.getStringExtra("key");


        pd=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();

        checkuserstatus();

        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        Query query=databaseReference.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {

                    name=""+ds.child("name").getValue();
                    email=""+ds.child("email").getValue();
                    dp=""+ds.child("image").getValue();

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        titleet=findViewById(R.id.ptitleEt);

        postiv=findViewById(R.id.pimageiv);
        uploadbtn=findViewById(R.id.uploadbtn);
        postiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showimagepickdialogue();
            }
        });
        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=titleet.getText().toString().trim();

                if(TextUtils.isEmpty(title))
                {
                    Toast.makeText(ADDPOSTSActivity.this,"Enter Title",Toast.LENGTH_LONG).show();
                    return;
                }


                if(image_uri==null)
                {
                    uploaddata(title,"noimage",s);
                }
                else {
                    uploaddata(title,String.valueOf(image_uri),s);


                }
            }
        });

        checkuserstatus();

    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        ((TextView)arg0.getChildAt(0)).setTextColor(Color.BLACK);

        city1=getResources().getStringArray(R.array.names)[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    private void uploaddata(final String title, String uri, final String s) {
        pd.setMessage("publishing Post");
        pd.show();
        String filepathandname="";
        String data="";
        final String time=String.valueOf(System.currentTimeMillis());
        if(s.equals("Children")||s.equals("الاطفال"))
        {
             filepathandname="PostsChildren/"+"PostChildren_"+time;
             data="posts"+"Children";
        }
        if(s.equals("Documents")||s.equals("الاوراق"))
        {
          filepathandname="PostsDocuments/"+"PostDocuments_"+time;
            data="posts"+"Documents";
        }
        if(s.equals("Phones")||s.equals("التليفونات"))
        {
             filepathandname="PostsPhones/"+"PostPhones_"+time;
            data="posts"+"Phones";
        }
        if(s.equals("Financial Aids")||s.equals("الدعم المالي"))
        {
             filepathandname="PostsFinancial/"+"PostFinancial_"+time;
            data="posts"+"Financial Aids";

        }
        if(s.equals("Money")||s.equals("النقود"))
        {
            filepathandname ="PostsMoney/"+"PostMoney_"+time;
            data="posts"+"Money";

        }
      //  Date currentTime = Calendar.getInstance().getTime();





        if(!uri.equals("noimage"))
        {
            StorageReference storageReference= FirebaseStorage.getInstance().getReference().child(filepathandname);
            storageReference.putFile(Uri.parse(uri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    String downloaduri=uriTask.getResult().toString();
                    if(uriTask.isSuccessful())
                    {
                        HashMap<Object,String> hashMap=new HashMap<>();
                        hashMap.put("city",city1);
                        hashMap.put("pid",time);
                        hashMap.put("pimage",downloaduri);
                        hashMap.put("ptime",time);
                        hashMap.put("ptitle",title);
                        hashMap.put("uName",name);
                        hashMap.put("udp",dp);
                        hashMap.put("uemail",email);
                        hashMap.put("uid",uid);
                        String data="posts"+s;
                        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(data);
                        databaseReference.child(time).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                pd.dismiss();
                                Toast.makeText(ADDPOSTSActivity.this,"Post published",Toast.LENGTH_SHORT).show();

                                titleet.setText("");

                                postiv.setImageURI(null);
                                image_uri=null;
                                preparenotification(""+time,""+name+" added new post"
                                ,""+title+"\n"+city1,"postnotification","Post");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ADDPOSTSActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(ADDPOSTSActivity.this,"Post published",Toast.LENGTH_SHORT).show();


                }
            });
        }
        else {


            HashMap<Object,String> hashMap=new HashMap<>();
            hashMap.put("city",city1);
            hashMap.put("pid",time);
            hashMap.put("pimage","noimage");
            hashMap.put("ptime",time);
            hashMap.put("ptitle",title);
            hashMap.put("uName",name);
            hashMap.put("udp",dp);
            hashMap.put("uemail",email);
            hashMap.put("uid",uid);







            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(data);
            databaseReference.child(time).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    pd.dismiss();
                    Toast.makeText(ADDPOSTSActivity.this,"Post published",Toast.LENGTH_SHORT).show();

                    titleet.setText("");

                    postiv.setImageURI(null);
                    image_uri=null;
                    preparenotification(""+time,""+name+" added new post"
                            ,""+title+"\n"+city1,"postnotification","Post");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(ADDPOSTSActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });
        }

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

    @Override
    protected void onStart() {
        super.onStart();
        checkuserstatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkuserstatus();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
       // menu.findItem(R.id.action_add_post).setVisible(false);
  //      menu.findItem(R.id.action_search).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkuserstatus();
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkuserstatus()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
            email=user.getEmail();
            uid=user.getUid();

        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
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
    public void preparenotification(String pid,String title ,String city,String notificationtype,String notificationtopic )
    {
        // prepare data for notification
        String notification_topic="/topics/"+notificationtopic;
        String notification_title=title;
        String notification_message=city;
        String notification_type=notificationtype;
        JSONObject notificationjo=new JSONObject();
        JSONObject notificationbodyjo=new JSONObject();
        try {
            notificationbodyjo.put("notificationtype",notification_type);
            notificationbodyjo.put("sender",uid);
            notificationbodyjo.put("pid",pid);
            notificationbodyjo.put("ptitle",notification_title);
            notificationbodyjo.put("city",notification_message);
            notificationjo.put("to",notification_topic);
            notificationjo.put("data",notificationbodyjo);


        } catch (JSONException e) {
           Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        sendpostnotification(notificationjo);


    }

    private void sendpostnotification(JSONObject notificationjo) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationjo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Fcm Response","on Response"+response.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
 Toast.makeText(ADDPOSTSActivity.this,""+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> headers=new HashMap<>();
                headers.put("Content-Type","application/json");
                headers.put("Authorization","key=AAAAYjVIJVY:APA91bFMx8yzS8h-MRHjq1Mizc-dpB56FZ3CZcgPbGsN4uLxvG0Cg1wJqYfkQz02e4082xh8EaNMgjyXLb6nWBeKIrWn4acKSXCCcaB7JsiOf7OOudlxN2ORaj38A975MQaXc6TnEWfC");

                return headers;
            }
        };
        Volley.newRequestQueue(this).add(jsonObjectRequest);
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
                postiv.setImageURI(image_uri);

            }
            else  if(resultCode==image_pick_camera_code)
            {
                postiv.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}