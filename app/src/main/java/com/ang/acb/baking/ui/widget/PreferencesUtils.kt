package com.ang.acb.baking.ui.widget

import android.content.Context
import androidx.preference.PreferenceManager
import com.ang.acb.baking.R

object PreferencesUtils {
    fun setWidgetRecipeId(context: Context, recipeId: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt(context.getString(R.string.pref_widget_key), recipeId)
            .apply()
    }

    fun getWidgetRecipeId(context: Context): Int {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getInt(context.getString(R.string.pref_widget_key), -1)
    }

    fun setWidgetTitle(context: Context, title: String?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(context.getString(R.string.pref_title_key), title)
            .apply()
    }

    fun getWidgetTitle(context: Context): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(
                context.getString(R.string.pref_title_key),
                context.getString(R.string.widget_text)
            )
    }
}