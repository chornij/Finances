package com.chornij.finances.widget;

import java.sql.Date;
import java.text.SimpleDateFormat;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.chornij.finances.R;
import com.chornij.finances.SettingsActivity;

public class MyProvider extends AppWidgetProvider {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
        Intent configIntent = new Intent(context, SettingsActivity.class);

        configIntent.setAction("com.chornij.intent.action.OPEN_SETTINGS");
        PendingIntent settingsPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.btnSettings, settingsPendingIntent);
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

        setUpdateTV(rv, context, appWidgetId);
        setList(rv, context, appWidgetId);
        setListClick(rv, context, appWidgetId);

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    void setUpdateTV(RemoteViews rv, Context context, int appWidgetId) {
        rv.setTextViewText(R.id.tvUpdate, sdf.format(new Date(System.currentTimeMillis())));
        Intent updIntent = new Intent(context, MyProvider.class);
        updIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
        PendingIntent updPIntent = PendingIntent.getBroadcast(context, appWidgetId, updIntent, 0);
        rv.setOnClickPendingIntent(R.id.tvUpdate, updPIntent);
    }

    void setList(RemoteViews rv, Context context, int appWidgetId) {
        Intent adapter = new Intent(context, MyService.class);
        adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        rv.setRemoteAdapter(R.id.lvList, adapter);
    }

    void setListClick(RemoteViews rv, Context context, int appWidgetId) {

    }

}
