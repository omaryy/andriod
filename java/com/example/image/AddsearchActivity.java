package com.example.image;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class AddsearchActivity extends AppCompatActivity {
Button Addpost;
Button searchbutton,searchinspecificcity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsearch);

        Addpost=findViewById(R.id.Addbutton);
        searchbutton=findViewById(R.id.searchButton);
        searchinspecificcity=findViewById(R.id.searchinspecificcity);
        Addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=getIntent();
                String s=intent1.getStringExtra("key");
                Intent intent2 =new Intent(AddsearchActivity.this,ADDPOSTSActivity.class);
                intent2.putExtra("key",s);
                startActivity(intent2);

            }
        });

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=getIntent();
                String s=intent1.getStringExtra("key");
                Intent intent2 =new Intent(AddsearchActivity.this,childrenPostsActivity.class);
                intent2.putExtra("key",s);
                startActivity(intent2);

            }
        });
        searchinspecificcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=getIntent();
                String s=intent1.getStringExtra("key");
                Intent intent2 =new Intent(AddsearchActivity.this,FilterActivity.class);
                intent2.putExtra("key",s);
                startActivity(intent2);

            }
        });
    }

}