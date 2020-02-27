package com.ang.acb.baking.ui.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.RemoteViews
import android.widget.RemoteViewsService.RemoteViewsFactory
import com.ang.acb.baking.R
import com.ang.acb.baking.data.database.RecipeDao
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.ui.recipedetails.DetailsActivity
import com.ang.acb.baking.ui.recipedetails.EXTRA_RECIPE_ID
import com.ang.acb.baking.ui.recipelist.MainActivity
import java.util.*
import javax.inject.Inject

/**
 * A custom class that implements the [RemoteViewsFactory] interface and
 * provides the app widget with the data for the items in its collection.
 *
 * https://developer.android.com/guide/topics/appwidgets#remoteviewsfactory-interface
 */
class RecipeRemoteViewsFactory @Inject constructor(
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

    override fun getCount(): Int = ingredients.size ?: 0

    override fun getViewAt(position: Int): RemoteViews? {
        if (position == AdapterView.INVALID_POSITION || ingredients == null) {
            return null
        }
        // Construct a remote views item based on the app widget item XML file
        return RemoteViews(context.packageName, R.layout.widget_ingredient_item). apply {
            // Set the remote views item text based on the position.
            setTextViewText(R.id.widget_ingredient_item, ingredients[position])

            // Set a fill-intent, which will be used to fill in the pending intent
            // template that is set on the collection view.
            // https://developer.android.com/guide/topics/appwidgets#setting-the-fill-in-intent
            val fillInIntent = Intent(context, MainActivity::class.java)
            setOnClickFillInIntent(R.id.widget_ingredient_item, fillInIntent)
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true
}