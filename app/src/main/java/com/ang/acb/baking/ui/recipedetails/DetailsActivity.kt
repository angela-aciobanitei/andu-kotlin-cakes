package com.ang.acb.baking.ui.recipedetails

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.fragment.app.Fragment
import com.ang.acb.baking.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

const val EXTRA_STEP_POSITION = "EXTRA_STEP_POSITION"
const val EXTRA_IS_TWO_PANE = "EXTRA_IS_TWO_PANE"
const val EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID"
const val INVALID_RECIPE_ID = -1

class DetailsActivity : AppCompatActivity(), HasSupportFragmentInjector {


    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>


    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        // Note: a DispatchingAndroidInjector<T> performs members-injection
        // on instances of core Android types (e.g. Activity, Fragment) that
        // are constructed by the Android framework and not by Dagger.
        return dispatchingAndroidInjector
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        // When using Dagger with activities, inject as early as possible.
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Get the recipe ID passed as intent extra.
        val recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID)
        if (recipeId == INVALID_RECIPE_ID) closeOnError()

        val navigationController = NavigationController(this)

        if (savedInstanceState == null) {
            navigationController.navigateToRecipeDetails(recipeId, isTwoPane())
            if (isTwoPane()) {
                navigationController.navigateToStepDetails(recipeId, 0, isTwoPane())
            }
        }

        // Handle Up navigation
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    private fun isTwoPane(): Boolean {
        return findViewById<View?>(R.id.step_details_fragment_container) != null
    }


    private fun closeOnError() {
        finish()
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show()
    }

    // See: https://stackoverflow.com/questions/12276027/how-can-i-return-to-a-parent-activity-correctly
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
