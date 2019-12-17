package com.ang.acb.baking.ui.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.ang.acb.baking.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    // NavigationUI uses an AppBarConfiguration object to manage the behavior of the
    // Navigation button in the upper-left corner of your app's display area. By default,
    // the Navigation button is hidden when a user is at a top-level destination of a
    // navigation graph and appears as an Up button in any other destination. To use
    // the start destination of your navigation graph as the only top-level destination,
    // you can create an AppBarConfiguration object and pass in the navigation graph.
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        // When using Dagger for injecting Activity objects, inject as early as possible.
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // See: https://developer.android.com/guide/navigation/navigation-ui#action_bar
        val navController = this.findNavController(R.id.recipes_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.recipes_nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
