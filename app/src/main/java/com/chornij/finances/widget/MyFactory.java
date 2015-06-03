package com.chornij.finances.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.IOException;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


import com.chornij.finances.R;
import com.chornij.finances.SettingsActivity;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFactory implements RemoteViewsFactory {

    public static final String TAG = "----------->";

    ArrayList<String> data;
    Context context;
    SimpleDateFormat sdf;
    int widgetID;
    SharedPreferences settings;

    MyFactory(Context ctx, Intent intent) {
        context = ctx;
        sdf = new SimpleDateFormat("HH:mm:ss");
        widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        data = new ArrayList<String>();

        settings = context.getSharedPreferences(SettingsActivity.SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.item);
        rView.setTextViewText(R.id.tvItemText, data.get(position));

        return rView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    OkHttpClient client = new OkHttpClient();


    @Override
    public void onDataSetChanged() {
        data.clear();

        // todo: apply secondary server
//        etSecondaryServer.setText(settings.getString("secondaryServer", ""));

        String primaryServer = settings.getString("primaryServer", "");
        Log.d(TAG, "primary server: " + primaryServer);
        Request request = new Request.Builder().url(primaryServer).build();

        try {
            Response response = client.newCall(request).execute();

            JSONObject jObject = new JSONObject(response.body().string());

            JSONArray jArray = jObject.getJSONArray("sources");

            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject incomeSource = jArray.getJSONObject(i);

                    Log.d(TAG, " ==== " + incomeSource.getString("name"));
                    data.add(i, incomeSource.getString("name") + " - " + incomeSource.getString("amount"));
                } catch (JSONException e) {
                    Log.d(TAG, "JSONException: " + e.toString());
                }
            }

            Log.d(TAG, response.body().string());
        } catch (IOException e) {
            Log.d(TAG, "IOException: " + e.toString());
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.toString());
        }
    }

    @Override
    public void onDestroy() {

    }

}
