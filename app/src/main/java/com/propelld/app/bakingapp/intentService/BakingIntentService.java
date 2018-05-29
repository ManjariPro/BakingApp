package com.propelld.app.bakingapp.intentService;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import com.propelld.app.bakingapp.Configuration;
import com.propelld.app.bakingapp.widgets.BakingAppWidgetProvider;

public class BakingIntentService extends IntentService
{
    public static final String ACTION_UPDATE_BAKING =
            "android.appwidget.action.APPWIDGET_UPDATE";

    public BakingIntentService()
    {
        super("BAKING_INTENT_SERVICE");
    }

    public static void startActionUpdateBakingWidgets(Context context)
    {
        Intent intent = new Intent(context, BakingIntentService.class);
        intent.setAction(ACTION_UPDATE_BAKING);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();

            if (ACTION_UPDATE_BAKING.equals(action))
            {
                handleActionUpdateBakingWidgets(getApplicationContext());
            }
        }
    }

    private void handleActionUpdateBakingWidgets(Context context)
    {
        // We can Also store the ingredients in Shared Preference...
        // And Retrieve it....
        SharedPreferences pref = context
                .getApplicationContext()
                .getSharedPreferences(Configuration.BAKING_PREFERENCE, Context.MODE_PRIVATE);

        String ingredients = pref.getString(Configuration.INGREDIENTS_PREFERENCE_KEY, "");
        String bakingName = pref.getString(Configuration.BAKINGNAME_PREFERENCE_KEY, "");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.updateBakingWidgets(this,
                appWidgetManager,
                ingredients,
                bakingName,
                appWidgetIds);
    }
}