package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Adapters.Adapterposts;
import models.ModelPosts;

public class ThereprofileActivity extends AppCompatActivity {
    RecyclerView postsrecyclRecyclerView;
    FirebaseAuth firebaseAuth;
    List<ModelPosts> postsList;
    Adapterposts adapterposts;
    TextView nametv,emailtv,phonetv;
    ImageView avatar,coverim;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thereprofile);
        postsrecyclRecyclerView=findViewById(R.id.recyclerviewposts);
        nametv=findViewById(R.id.nametv);
        emailtv=findViewById(R.id.emailtv);
        phonetv=findViewById(R.id.phonetv);
        avatar=findViewById(R.id.avatar);
        coverim=findViewById(R.id.coverim);

        firebaseAuth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        Query query=FirebaseDatabase.getInstance().getReference("users").orderByChild("uid").equalTo(uid);
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
        postsList=new ArrayList<>();
        checkuserstatus();
        loadmyposts();

    }
    private void loadmyposts() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
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


                    adapterposts=new Adapterposts(ThereprofileActivity.this,postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereprofileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

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


                    adapterposts=new Adapterposts(ThereprofileActivity.this,postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereprofileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

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


                    adapterposts=new Adapterposts(ThereprofileActivity.this,postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereprofileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

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


                    adapterposts=new Adapterposts(ThereprofileActivity.this,postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereprofileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

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


                    adapterposts=new Adapterposts(ThereprofileActivity.this,postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereprofileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
    public void searchhisposts(final String searchquery)
    {
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
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


                    adapterposts=new Adapterposts(ThereprofileActivity.this,postsList);
                    postsrecyclRecyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThereprofileActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }

    private void checkuserstatus()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {

        }
        else {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
 //       menu.findItem(R.id.action_add_post).setVisible(false);
      /*  MenuItem searchItem = menu.findItem(R.id.action_search);

        SearchView searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchhisposts(s);
                }
                else {
                    loadmyposts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchhisposts(s);
                }
                else {
                    loadmyposts();
                }
                return false;
            }
        });

       */
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}