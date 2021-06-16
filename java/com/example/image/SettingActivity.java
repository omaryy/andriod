package com.example.image;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingActivity extends AppCompatActivity {
    SwitchCompat postswitch;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private static  final  String topic_post_notification="Post";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        postswitch = findViewById(R.id.postswitch);
        sp=getSharedPreferences("notification_sp",MODE_PRIVATE);
        boolean ispostenabled=sp.getBoolean(""+topic_post_notification,false);
        // if enable check switch otherwise not
        if(ispostenabled)
        {
            postswitch.setChecked(true);
        }
        else {
            postswitch.setChecked(false);
        }
        postswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor=sp.edit();
                editor.putBoolean(""+topic_post_notification,isChecked);
                editor.apply();
                if(isChecked)
                {
                    subscribepostnotification();
                }
                else {
                    unsubscripepostnotification();
                }
            }
        });
    }

    private void unsubscripepostnotification() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+topic_post_notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String message="You Will Not  Receive Post Notification";
                if(!task.isSuccessful())
                {
                    message="un Subscription Failed";
                }
                Toast.makeText(SettingActivity.this,message,Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void subscribepostnotification() {
        FirebaseMessaging.getInstance().subscribeToTopic(""+topic_post_notification).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String message="You Will Receive Post Notification";
if(!task.isSuccessful())
{
    message="Subscribtion Failed";
}
                Toast.makeText(SettingActivity.this,message,Toast.LENGTH_LONG).show();

            }
        });
    }
}