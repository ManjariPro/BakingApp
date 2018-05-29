package com.propelld.app.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.propelld.app.bakingapp.intentService.BakingIntentService;
import com.propelld.app.bakingapp.MainActivity;
import com.propelld.app.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider
{
    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId)
    {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_Layout, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds)
    {
        BakingIntentService.startActionUpdateBakingWidgets(context);
    }

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                String ingredients,
                                String bakingName,
                                int appWidgetId)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);

        views.setTextViewText(R.id.appwidget_Headtext, bakingName);
        views.setTextViewText(R.id.appwidget_text, ingredients);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateBakingWidgets(Context context,
                                           AppWidgetManager appWidgetManager,
                                           String ingredients,
                                           String bakingName,
                                           int[] appWidgets)
    {
        for (int appWidgetId : appWidgets)
        {
            updateAppWidget(context,
                    appWidgetManager,
                    ingredients,
                    bakingName,
                    appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context)
    {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context)
    {
        // Enter relevant functionality for when the last widget is disabled
    }
}