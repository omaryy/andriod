package com.example.image;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import notitication.Tokens;

public class DashboardActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener
{
    FirebaseAuth firebaseAuth;
    ActionBar actionBar;
    String muid;
Toolbar toolbar;
private DrawerLayout mdrwablelayout;
    private NavigationView navigationView;
private ActionBarDrawerToggle actionBarDrawerToggle;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setNavigationViewListener();
mdrwablelayout=(DrawerLayout)findViewById(R.id.drawer);
actionBarDrawerToggle=new ActionBarDrawerToggle(this,mdrwablelayout,R.string.Open,R.string.Close);
mdrwablelayout.addDrawerListener(actionBarDrawerToggle);
actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.main_sidebar);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar=getSupportActionBar();
        actionBar.setTitle("profile ");


        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#ff3f3d56"));
        actionBar.setBackgroundDrawable(colorDrawable);
        firebaseAuth= FirebaseAuth.getInstance();
        BottomNavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        actionBar.setTitle("Home");

        HomeFragment homeFragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment,homeFragment)
                .commit();
        checkuserstatus();

updatetoken(FirebaseInstanceId.getInstance().getToken());


            SharedPreferences.Editor editor=getSharedPreferences("dash",MODE_PRIVATE).edit();
            editor.putBoolean("dash",true);
            editor.commit();







    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener selectedListener =new BottomNavigationView.OnNavigationItemSelectedListener(){

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.navigation_home:
                    actionBar.setTitle("Home");
                    HomeFragment homeFragment=new HomeFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, homeFragment)

                            .commit();

                    return true;



                case R.id.profile_dashboard:
                    actionBar.setTitle("Profile");
                    profileFragment Fragment2=new profileFragment();

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, Fragment2)
                            .commit();

                    return true;




                case R.id.nav_chat:
                    actionBar.setTitle("Chats");
                    chatlistFragment Fragment4=new chatlistFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, Fragment4)
                            .commit();
                    return true;
                case R.id.nav_Favourite:
                    actionBar.setTitle("Chats");
                    FavouriteFragment favouriteFragment=new FavouriteFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, favouriteFragment)
                            .commit();
                    return true;

              


            }
            return false;
        }
    };
    private void checkuserstatus()
    {
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null)
        {
         muid=user.getUid();

            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",muid);
            editor.apply();
        }
        else {
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }
    public  void  onBackPressed()
    {
        super.onBackPressed();
        finish();

    }
    protected  void onstart()
    {
        checkuserstatus();
        super.onStart();
    }
    private  void updatetoken(String token)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        Tokens mtoken=new Tokens(token);
        ref.child(muid).setValue(mtoken);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id=item.getItemId();
        if(id==R.id.action_logout)
        {
            firebaseAuth.signOut();
            SharedPreferences preferences =getSharedPreferences("dash",DashboardActivity.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.apply();
            finish();
            checkuserstatus();
        }
      /*  else if(id==R.id.action_add_post)
        {
            startActivity(new Intent(this,ADDPOSTSActivity.class));
        }

       */
        else if(id==R.id.action_Settings)
        {
            startActivity(new Intent(this,SettingActivity.class));
        }
        else  if(id==R.id.action_Privacy_Policy)
        {
            startActivity(new Intent(this,PrivacyPolicyActivity.class));

        }
        else if(id==R.id.action_Help)
        {
            startActivity(new Intent(this,HelpActivity.class));

        }
        else if(id==R.id.action_termsconditions)
        {
            startActivity(new Intent(this,TermsAndconditionsActivity.class));

        }
        //close navigation drawer
        mdrwablelayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_sidebar);
        navigationView.setNavigationItemSelectedListener(this);
    }

}