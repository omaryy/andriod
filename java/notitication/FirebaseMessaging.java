package notitication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.image.R;
import com.example.image.chatActivity;
import com.example.image.childrenPostsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseMessaging extends FirebaseMessagingService {

    private static final String ADMIN_ChaNNEL_ID="admin_channel";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
        String savedcurrentuser=sp.getString("Current_USERID","None");
        String Notificationtype=remoteMessage.getData().get("notificationtype");
        if(Notificationtype.equals("postnotification"))
        {
            String sender=remoteMessage.getData().get("sender");
            String pid=remoteMessage.getData().get("pid");
            String ptitle=remoteMessage.getData().get("ptitle");
            String pdescription=remoteMessage.getData().get("city");
if(!sender.equals(savedcurrentuser))
{
    showpostnotification(""+pid,""+ptitle,""+pdescription);
}

        }
        else if(Notificationtype.equals("ChatNotification"))
        {
            String sent =remoteMessage.getData().get("sent");
            String user=remoteMessage.getData().get("user");
            FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
            if(fuser!=null&&sent.equals(fuser.getUid()))
            {
                if(!savedcurrentuser.equals(user))
                {
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                    {
                        sendoandabovenotification(remoteMessage);
                    }
                    else {
                        sendnormalnotification(remoteMessage);
                    }
                }
            }

        }

    }

    private void showpostnotification(String pid, String ptitle, String pdescription) {
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId=new Random().nextInt(3000);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            setuppostnotificationchannel(notificationManager);

        }
Intent intent=new Intent(this, childrenPostsActivity.class);
        intent.putExtra("postid",pid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pintent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Bitmap largeicon= BitmapFactory.decodeResource(getResources(), R.drawable.ic_my_icon);
        Uri notificationsounduri=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationbuilder=new NotificationCompat.Builder(this,""+ADMIN_ChaNNEL_ID).setSmallIcon(R.drawable.splash)
                .setLargeIcon(largeicon)
                .setContentTitle(ptitle)
                .setContentText(pdescription)
                .setSound(notificationsounduri)
                .setContentIntent(pintent);
        notificationManager.notify(notificationId,notificationbuilder.build());



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setuppostnotificationchannel(NotificationManager notificationManager) {
        CharSequence channelname="New Notification";
        String Channeldescription="Device to Device Post notification";
        NotificationChannel adminchannel=new NotificationChannel(ADMIN_ChaNNEL_ID,channelname,NotificationManager.IMPORTANCE_HIGH);
        adminchannel.setDescription(Channeldescription);
        adminchannel.enableLights(true);
        adminchannel.setLightColor(Color.RED);
        adminchannel.enableVibration(true);
        if(notificationManager!=null)
        {
            notificationManager.createNotificationChannel(adminchannel);

        }
    }

    private void sendnormalnotification(RemoteMessage remoteMessage) {
        String user =remoteMessage.getData().get("user");
        String icon =remoteMessage.getData().get("icon");
        String title =remoteMessage.getData().get("title");
        String body =remoteMessage.getData().get("body");
        RemoteMessage.Notification notification =remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this, chatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("hisuid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pintent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defsounduri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentText(body)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defsounduri)
                .setContentIntent(pintent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int j=0;
        if(i>0)
        {
            j=i;

        }
        notificationManager.notify(j,builder.build());



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendoandabovenotification(RemoteMessage remoteMessage) {
        String user =remoteMessage.getData().get("user");
        String icon =remoteMessage.getData().get("icon");
        String title =remoteMessage.getData().get("title");
        String body =remoteMessage.getData().get("body");
        RemoteMessage.Notification notification =remoteMessage.getNotification();
        int i=Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent=new Intent(this, chatActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("hisuid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pintent=PendingIntent.getActivity(this,i,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defsounduri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    oreoandabovenotification notification1=new oreoandabovenotification(this);
        Notification.Builder builder =notification1.getNotifications(title,body,pintent,defsounduri,icon);
        int j=0;
        if(i>0)
        {
            j=i;

        }
       notification1.getManager().notify(j,builder.build());

    }
}