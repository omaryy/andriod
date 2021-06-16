package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.Adapterposts;
import models.ModelPosts;

public class filtersearchActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ModelPosts> postsList;
    Adapterposts adapterposts;
String s1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtersearch);
        Intent intent1=getIntent();
        String s=intent1.getStringExtra("city");
        s1=intent1.getStringExtra("key");

        recyclerView=findViewById(R.id.postsrcyclerview2);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        postsList=new ArrayList<>();
            searchposts(s);

    }
    private void searchposts(final String searchQuery){

        DatabaseReference databaseReference= null;
        if(s1.equals("Children")||s1.equals("الاطفال"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsChildren");
        }
        if(s1.equals("Documents")||s1.equals("الاوراق"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsDocuments");
        }
        if(s1.equals("Financial Aids")||s1.equals("الدعم المالي"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsFinancial Aids");
        }
        if(s1.equals("Phones")||s1.equals("التليفونات"))
        {
            databaseReference= FirebaseDatabase.getInstance().getReference("postsPhones");
        }
        if(s1.equals("Money")||s1.equals("النقود"))
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
                    modelPosts.setuDp(ds.child("udp").getValue().toString());
                    modelPosts.setUname(ds.child("uName").getValue().toString());
                    modelPosts.setCity(ds.child("city").getValue().toString());
                    modelPosts.setCity(ds.child("city").getValue().toString());


                    if(modelPosts.getCity().toLowerCase().equals(searchQuery.toLowerCase()))
                    {
                        postsList.add(modelPosts);

                    }
                    adapterposts=new Adapterposts(filtersearchActivity.this,postsList);
                    recyclerView.setAdapter(adapterposts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(filtersearchActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }

}