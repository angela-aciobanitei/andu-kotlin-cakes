package com.ang.acb.baking.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.AdapterView
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.ang.acb.baking.R
import com.ang.acb.baking.data.database.RecipeDao
import com.ang.acb.baking.data.database.RecipeDetails
import java.util.*
import javax.inject.Inject

/**
 * A custom class that implements the RemoteViewsFactory interface and provides
 * the app widget with the data for the items in its collection.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsfactory-interface
 */
class RecipeRemoteViewsFactory
@Inject constructor(
    private val context: Context,
    private val recipeDao: RecipeDao
) : RemoteViewsFactory {

    private lateinit var ingredients: MutableList<String>

    override fun onCreate() {}

    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter.
     * This allows a RemoteViewsFactory to respond to data changes by updating
     * any internal references. Note: expensive tasks can be safely performed
     * synchronously within this method. In the interim, the old data will be
     * displayed within the widget.
     */
    override fun onDataSetChanged() {
        val recipeId = PreferencesUtils.getWidgetRecipeId(context)
        if (recipeId != -1) {
            ingredients = ArrayList()
            val recipeDetails: RecipeDetails? = recipeDao.getRecipeDetailsSync(recipeId)
            recipeDetails?.ingredients?.forEach { ingredient ->
                ingredients.add(String.format(
                        Locale.getDefault(),
                        "%.1f %s %s",
                        ingredient.quantity,
                        ingredient.measure,
                        ingredient.ingredient
                    )
                )
            }
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int {
        return if (ingredients == null) 0 else ingredients!!.size
    }

    override fun getViewAt(position: Int): RemoteViews? {
        if (position == AdapterView.INVALID_POSITION || ingredients == null) {
            return null
        }
        // Construct a remote views item based on the app widget item XML file.
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.widget_ingredient_item
        )
        // Set the remote views item text based on the position.
        remoteViews.setTextViewText(
            R.id.widget_ingredient_item,
            ingredients!![position]
        )
        // When using collections (eg. ListView, StackView etc.) in widgets,
        // it is very costly to set PendingIntents on the individual items,
        // and is hence not permitted. Instead this method should be used
        // to set a single PendingIntent template on the collection, and
        // individual items can differentiate their on-click behavior using
        // RemoteViews#setOnClickFillInIntent(int, Intent).
        // https://developer.android.com/guide/topics/appwidgets#setting-the-fill-in-intent
        remoteViews.setOnClickFillInIntent(
            R.id.widget_ingredient_item,
            Intent()
        )
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}