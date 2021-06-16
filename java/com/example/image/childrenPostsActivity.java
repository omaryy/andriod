package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.Adapterposts;
import models.ModelPosts;


public class childrenPostsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelPosts>postsList;
    Adapterposts adapterposts;
    String s="";
String postid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_posts);
        firebaseAuth=FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.postsrcyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        postsList=new ArrayList<>();
        Intent intent1=getIntent();
         s=intent1.getStringExtra("key");
         Intent intent2=getIntent();
         postid=intent2.getStringExtra("postid");
        loadposts();
    }
    private void searchposts(final String searchQuery){
        DatabaseReference databaseReference= null;
        if(s.equals("Children")||s.equals("الاطفال"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsChildren");
        }
        if(s.equals("Documents")||s.equals("الاوراق"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsDocuments");
        }
        if(s.equals("Financial Aids")||s.equals("الدعم المالي"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsFinancial Aids");
        }
        if(s.equals("Phones")||s.equals("التليفونات"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsPhones");
        }
        if(s.equals("Money")||s.equals("النقود"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsMoney");
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    ModelPosts modelPosts=ds.getValue(ModelPosts.class);
                    modelPosts.setCity(ds.child("city").getValue().toString());
                    if(modelPosts.getPtitle().toLowerCase().contains(searchQuery)||(modelPosts.getCity().toLowerCase().contains(searchQuery)))
                    {

                        postsList.add(modelPosts);

                    }


                    adapterposts=new Adapterposts(childrenPostsActivity.this,postsList);
                    recyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(childrenPostsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

       /* MenuItem searchItem = menu.findItem(R.id.action_search);



        SearchView searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchposts(s);
                }
                else {
                    loadposts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(!TextUtils.isEmpty(s.trim()))
                {
                    searchposts(s);
                }
                else {
                    loadposts();
                }
                return false;
            }
        });

        */
        return super.onCreateOptionsMenu(menu);
    }



    private void loadposts() {
        DatabaseReference databaseReference = null;
        if(s.equals("Children")||s.equals("الاطفال"))
        {
             databaseReference= FirebaseDatabase.getInstance().getReference("postsChildren");
        }
        if(s.equals("Documents")||s.equals("الاوراق"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsDocuments");
        }
        if(s.equals("Financial Aids")||s.equals("الدعم المالي"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsFinancial Aids");
        }
        if(s.equals("Phones")||s.equals("التليفونات"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsPhones");
        }
        if(s.equals("Money")||s.equals("النقود"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsMoney");
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {

                    ModelPosts modelPosts=ds.getValue(ModelPosts.class);

                    modelPosts.setUname(ds.child("uName").getValue().toString());
                    modelPosts.setuDp(ds.child("udp").getValue().toString());
                    modelPosts.setCity(ds.child("city").getValue().toString());
                    postsList.add(modelPosts);
                    adapterposts=new Adapterposts(childrenPostsActivity.this,postsList);
                    recyclerView.setAdapter(adapterposts);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(childrenPostsActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
}