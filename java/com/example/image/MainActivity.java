package com.example.image;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button registerbutton,loginbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Locale.getDefault().getLanguage().equals("en")  ) {
            setlocate("en");
            loadlocale();
        }
        else  if(Locale.getDefault().getLanguage().equals("EG")  ){
            setlocate("EG");
            loadlocale();
        }

        registerbutton=findViewById(R.id.registerbutton);
        loginbutton=findViewById(R.id.loginbutton);
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,loginActivity.class));
            }
        });
        if(isopenalready())
        {
            Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
    private boolean isopenalready() {
        SharedPreferences sharedPreferences=getSharedPreferences("dash", Context.MODE_PRIVATE);
        boolean result=sharedPreferences.getBoolean("dash",false);
        return result;
    }

    private void setlocate(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration configue=new Configuration();
        configue.locale=locale;
        getBaseContext().getResources().updateConfiguration(configue,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor=getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My Lang",lang);
        editor.apply();

    }
    public void loadlocale()
    {
        SharedPreferences sh=getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language=sh.getString("My Lang","");
        setlocate(language);

    }
}