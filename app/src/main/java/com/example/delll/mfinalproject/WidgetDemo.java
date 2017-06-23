package com.example.delll.mfinalproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;


public class WidgetDemo extends AppWidgetProvider {
    private static final String STATICACION = "com.example.delll.mfinalproject.staticreceiver";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        rv.setTextViewText(R.id.haha1, "《书目》");
        rv.setTextViewText(R.id.haha2, "完成日期：");
        rv.setTextViewText(R.id.haha3, "xx/yy");
        rv.setImageViewResource(R.id.widget_image, R.mipmap.icon_book_and_glasses);

        Intent clickInt = new Intent(STATICACION);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, clickInt, 0);
        rv.setOnClickPendingIntent(R.id.widget_image, pi);

        Intent startActivityIntent=new Intent(context ,MainActivity.class);
        PendingIntent Pintent= PendingIntent.getActivity(context, 0, startActivityIntent, 0);
        rv.setOnClickPendingIntent(R.id.widget_image, Pintent);
        rv.setOnClickPendingIntent(R.id.haha1, Pintent);
        rv.setOnClickPendingIntent(R.id.haha2, Pintent);
        rv.setOnClickPendingIntent(R.id.haha3, Pintent);
        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        final RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_demo);
        final Bundle bundle = intent.getExtras();
        if (intent.getAction().equals(STATICACION)) {
            rv.setTextViewText(R.id.haha1, "《"+bundle.getString("names")+"》");
            rv.setTextViewText(R.id.haha2, "计划日期：");
            rv.setTextViewText(R.id.haha3, bundle.getString("dates"));
            rv.setImageViewResource(R.id.widget_image, R.mipmap.icon_book_and_glasses);
            AppWidgetManager am = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = am.getAppWidgetIds(new ComponentName(context, WidgetDemo.class));
            am.updateAppWidget(appWidgetIds, rv);
            Intent mmintent=new Intent(context,MainActivity.class);
            PendingIntent Pfullintent=PendingIntent.getActivity(context, 0,  mmintent,0);

        }
    }
}








