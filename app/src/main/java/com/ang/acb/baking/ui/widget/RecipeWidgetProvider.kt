package com.ang.acb.baking.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.ang.acb.baking.R
import com.ang.acb.baking.ui.recipelist.MainActivity

/**
 * The [AppWidgetProvider] class implementation. Defines the basic methods that allow
 * you to programmatically interact with the App Widget, based on broadcast events.
 * Through it, you will receive broadcasts when the App Widget is updated, enabled,
 * disabled and deleted.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#AppWidgetProvider
 */
class RecipeWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them.
        updateRecipeWidget(context, appWidgetManager, appWidgetIds)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created.
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled.
    }

    companion object {
        fun updateRecipeWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray
        ) {
            // Update each of the app widgets with the remote adapter
            appWidgetIds.forEach { appWidgetId ->
                // Set up the intent that starts the RecipeRemoteViewsService,
                // which will provide the views for this collection.
                val serviceIntent =
                    Intent(context, RecipeRemoteViewsService::class.java).apply {
                        // Add the app widget ID to the intent extras.
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                    }

                // Instantiate the RemoteViews object for the app widget layout.
                val remoteViews =
                    RemoteViews(context.packageName, R.layout.widget_recipe).apply {
                        // Set widget title.
                        setTextViewText(
                            R.id.widget_ingredients_list_title,
                            PreferencesUtils.getWidgetTitle(context)
                        )
                        // Set up the RemoteViews object to use a RemoteViews adapter. This
                        // adapter connects to a RemoteViewsService  through the specified
                        // intent. This is how we populate the data.
                        setRemoteAdapter(R.id.widget_ingredients_list_items, serviceIntent)
                        // Set the empty view which is displayed when the collection has no items.
                        // It should be a sibling of the collection view.
                        setEmptyView(R.id.widget_ingredients_list_items, R.id.empty_view)
                    }

                // Create an pending intent to launch MainActivity.
                val mainPendingIntent: PendingIntent =
                    Intent(context, MainActivity::class.java).let { mainActivityIntent ->
                        PendingIntent.getActivity(context, appWidgetId, mainActivityIntent, 0)
                    }

                // When using collections (ListView, StackView etc.) in widgets, it is costly
                // to set PendingIntents on the individual items, and is hence not permitted.
                // Instead this method should be used to set a single PendingIntent template
                // on the collection, and individual items can differentiate their on-click
                // behavior using RemoteViews#setOnClickFillInIntent(int, Intent).
                // https://developer.android.com/guide/topics/appwidgets#setting-up-the-pending-intent-template
                remoteViews.setPendingIntentTemplate(
                    R.id.widget_ingredients_list_items,
                    mainPendingIntent
                )

                remoteViews.setOnClickPendingIntent(
                    R.id.widget_recipe_container,
                    mainPendingIntent
                )

                // Tell the AppWidgetManager to perform an update on the current app widget.
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }
}