package com.ang.acb.baking.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.ang.acb.baking.data.database.RecipeDao
import dagger.android.AndroidInjection
import javax.inject.Inject


/**
 * A service to be connected to for a remote adapter to request RemoteViews.
 * It provides the RemoteViewsFactory used to populate the remote collection view.
 * RemoteViewsFactory is an interface for an adapter between a remote collection
 * view (such as ListView or GridView) and the underlying data for that view.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsservice-class
 * https://stackoverflow.com/questions/51017541/how-to-provide-database-to-myfirebasemessagingservice-using-dagger-2-so-that-i-c
 */
class RecipeRemoteViewsService : RemoteViewsService() {

    @Inject
    lateinit var recipeDao: RecipeDao

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RecipeRemoteViewsFactory(applicationContext, recipeDao)
    }
}