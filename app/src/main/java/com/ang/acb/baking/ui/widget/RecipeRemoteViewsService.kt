package com.ang.acb.baking.ui.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * A service to be connected to for a remote adapter to request RemoteViews.
 * It provides the RemoteViewsFactory used to populate the remote collection view.
 * RemoteViewsFactory is an interface for an adapter between a remote collection
 * view (such as ListView or GridView) and the underlying data for that view.
 *
 * See: https://developer.android.com/guide/topics/appwidgets#remoteviewsservice-class
 */
class RecipeRemoteViewsService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RecipeRemoteViewsFactory(applicationContext)
    }
}