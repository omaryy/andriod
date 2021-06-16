package com.example.image;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment {


    FirebaseAuth firebaseAuth;
    ImageView children;
    ImageView Documents;
    ImageView Phones;
    ImageView Financial;
    ImageView MoneyButton;
    private DrawerLayout mdrawelayout;
private  ActionBarDrawerToggle mtoggle;
    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth= FirebaseAuth.getInstance();
        children=view.findViewById(R.id.childrnbutton);
        Documents=view.findViewById(R.id.DocumentsButton);
        Phones=view.findViewById(R.id.phonesbutton);
        Financial=view.findViewById(R.id.Financialbutton);
        MoneyButton=view.findViewById(R.id.moneybutton);

        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),AddsearchActivity.class);
                intent.putExtra("key","Children");
                startActivity(intent);
            }
        });
        Documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),AddsearchActivity.class);
                intent.putExtra("key","Documents");
                startActivity(intent);

            }
        });
        Phones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),AddsearchActivity.class);
                intent.putExtra("key","Phones");
                startActivity(intent);

            }
        });
        Financial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),AddsearchActivity.class);
                intent.putExtra("key","Financial Aids");
                startActivity(intent);

            }
        });
        MoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),AddsearchActivity.class);
                intent.putExtra("key","Money");
                startActivity(intent);

            }
        });




        return view;
    }
    /*

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            checkuserstatus();
        }
        else if(id==R.id.action_add_post)
        {
            startActivity(new Intent(getActivity(),ADDPOSTSActivity.class));
        }
        else if(id==R.id.action_Settings)
        {
            startActivity(new Intent(getContext(),SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public  void onCreate( @Nullable Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

     */
    private void checkuserstatus()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {

        }
        else {
            startActivity(new Intent(getActivity(),MainActivity.class));
            getActivity().finish();
        }
    }


}