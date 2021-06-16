package com.example.image;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FilterActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
Button searchbtn;
String s;
String section="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        searchbtn=findViewById(R.id.searchbtn);
        Spinner myspanner=(Spinner)findViewById(R.id.filterspinner);

        myspanner.setOnItemSelectedListener(this);

        ArrayAdapter<String> myadapter=new ArrayAdapter<String>(FilterActivity.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.names));
        myadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        myspanner.setAdapter(myadapter);

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(FilterActivity.this,filtersearchActivity.class);
                Intent intent1=getIntent();
                section=intent1.getStringExtra("key");
                intent.putExtra("city",s);
                intent.putExtra("key",section);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        ((TextView)arg0.getChildAt(0)).setTextColor(Color.BLACK);

s=getResources().getStringArray(R.array.names)[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}