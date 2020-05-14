package com.github.alekdevninja.sulejowekaircheck.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.github.alekdevninja.sulejowekaircheck.MainActivity;
import com.github.alekdevninja.sulejowekaircheck.R;
import com.github.alekdevninja.sulejowekaircheck.sensorTools.Scraper;
import com.github.alekdevninja.sulejowekaircheck.sensorTools.Sensor;
import com.github.alekdevninja.sulejowekaircheck.sensorTools.SensorDB;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewAppWidgetConfigureActivity NewAppWidgetConfigureActivity}
 */
public class NewAppWidget extends AppWidgetProvider {
    static String pm25Value = "obtaining data,\n please wait\n no internet maybe?\n please delete add this widget again while being connected :)";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //obtaining data for the widget view
        final Scraper scraper = new Scraper("http://looko2.com/tracker.php?lan=&search=5CCF7F1A546F");

        try {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    pm25Value = scraper.extractPM2Value();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

        //setting text to the main widget TextView
        views.setTextViewText(R.id.appwidget_text, pm25Value);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            NewAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

