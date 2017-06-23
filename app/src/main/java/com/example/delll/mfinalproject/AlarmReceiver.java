package com.example.delll.mfinalproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.net.URI;


public class AlarmReceiver extends BroadcastReceiver {
	@Override
    public void onReceive(Context context, Intent intent) {
            Bundle bundle=intent.getExtras();
            NotificationManager manager=(NotificationManager)context.getSystemService((Context.NOTIFICATION_SERVICE));

       // Notification n=new Notification();
        /* set the sound of the alarm. There are two way of setting the sound */
        // n.sound=Uri.parse("file:///sdcard/alarm.mp3");
       // n.sound= Uri.withAppendedPath(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "20");
        // Post a notification to be shown in the status bar
        Uri ringUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder=new Notification.Builder(context);
        builder.setContentTitle("Doge Book")
                    .setContentText("您还有"+bundle.getString("text")+"本书要读，继续努力哦~")
                    .setTicker("Doge Book喊你回来读书啦~")
                .setSound(ringUri)
                    .setLargeIcon( BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_book_and_glasses))
                    .setSmallIcon(R.drawable.icon_book_and_glasses)
                    .setAutoCancel(true);
            Intent mintent=new Intent(context,MainActivity.class);
            final PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mintent,0);
            builder.setContentIntent(mPendingIntent);
            Notification notify=builder.build();
            manager.notify(0,notify);

        }

}
