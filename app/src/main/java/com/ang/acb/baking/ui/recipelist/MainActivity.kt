package com.ang.acb.baking.ui.recipelist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ang.acb.baking.R
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * The UI Controller for displaying a list of recipes loaded from the network.
 */
class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        // When using Dagger for injecting Activity objects,
        // inject as early as possible.
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            // Navigate to recipe list fragment.
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, RecipeListFragment())
                .commit()
        }
    }
}
