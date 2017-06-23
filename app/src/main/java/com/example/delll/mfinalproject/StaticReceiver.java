package com.example.delll.mfinalproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/**
 * Created by delll on 2016/10/20.
 */
public class StaticReceiver extends BroadcastReceiver {
    private  static  final String STATICACION="com.example.delll.mfinalproject.staticreceiver";
    private Bitmap icon;
    @Override


    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();

        NotificationManager manager=(NotificationManager)context.getSystemService((Context.NOTIFICATION_SERVICE));
        Notification.Builder builder=new Notification.Builder(context);
        builder.setContentTitle("DogeBook")
                .setContentText("您正在读《"+bundle.getString("names")+"》，一分耕耘一份收获~加油喔！")
                .setTicker("您有一条新消息")
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_book_and_glasses) )
                .setSmallIcon(R.drawable.icon_book_and_glasses)
                .setAutoCancel(true);
        Intent mintent=new Intent(context,MainActivity.class);
        final PendingIntent mPendingIntent=PendingIntent.getActivity(context,0,mintent,0);
        builder.setContentIntent(mPendingIntent);
        Notification notify=builder.build();
        manager.notify(0,notify);
//觉得这个广播太sb的话可以把onreceive中置空，但是这个类不要删

        }

    }


