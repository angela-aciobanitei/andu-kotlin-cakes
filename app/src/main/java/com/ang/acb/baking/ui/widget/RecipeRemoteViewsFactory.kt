package com.ang.acb.baking.ui.widget

import android.content.Context
import android.content.Intent
import android.widget.AdapterView
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import androidx.room.Room
import com.ang.acb.baking.R
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.data.database.RecipesDatabase
import com.ang.acb.baking.data.repository.RecipeRepository
import java.util.*

/**
 * A custom class that implements the RemoteViewsFactory interface and provides
 * the app widget with the data for the items in its collection.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsfactory-interface
 */
class RecipeRemoteViewsFactory
internal constructor(private val context: Context) : RemoteViewsFactory {

    // TODO Use repo
    private val database: RecipesDatabase =
        Room.databaseBuilder(context, RecipesDatabase::class.java, "recipes.db")
            .fallbackToDestructiveMigration()
            .build()

    private var ingredients: MutableList<String>? = null

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val recipeId = PreferencesUtils.getWidgetRecipeId(context)
        if (recipeId != -1) {
            ingredients = ArrayList()
            val recipeDetails: RecipeDetails =
                database.recipeDao.getRecipeDetailsForWidget(recipeId)
            recipeDetails.ingredients?.forEach { ingredient ->
                ingredients?.add(String.format(
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