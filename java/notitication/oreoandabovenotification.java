package notitication;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.TreeMap;

public class oreoandabovenotification  extends ContextWrapper {
    private  static  final String ID="some_id";
    private  static  final  String Name="BigBoxApp";
    private NotificationManager notificationManager;

    public oreoandabovenotification(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            createchannel();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createchannel() {
        NotificationChannel notificationChannel=new NotificationChannel(ID,Name,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);


    }
    public NotificationManager getManager()
    {
        if(notificationManager==null)
        {
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return  notificationManager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotifications(String title, String body, PendingIntent pintent, Uri sounduri, String icon)
    {
        return  new Notification.Builder(getApplicationContext(),ID).setContentIntent(pintent).setContentTitle(title)
                .setContentText(body)
                .setSound(sounduri)
                .setAutoCancel(true)
                .setSmallIcon(Integer.parseInt(icon));

    }

}
