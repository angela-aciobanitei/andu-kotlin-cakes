package com.ang.acb.baking.ui.recipedetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ang.acb.baking.R
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


const val EXTRA_STEP_POSITION = "EXTRA_STEP_POSITION"
const val EXTRA_IS_TWO_PANE = "EXTRA_IS_TWO_PANE"
const val EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID"
const val INVALID_RECIPE_ID = -1

/**
 * The UI Controller for displaying the details of a recipe loaded from the network.
 */
class DetailsActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var navigationController: NavigationController

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector


    override fun onCreate(savedInstanceState: Bundle?) {
        // When using Dagger with activities, inject as early as possible.
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Get the recipe ID passed as intent extra.
        val recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, INVALID_RECIPE_ID)
        if (recipeId == INVALID_RECIPE_ID) closeOnError()

        if (savedInstanceState == null) {
            // Display only recipe details (ingredients list ans step list) on phones.
            navigationController.navigateToRecipeDetails(
                recipeId, isTwoPane()
            )

            // Display both recipe details (ingredients list ans step list)
            // and step details on tablets.
            if (isTwoPane()) navigationController.navigateToStepDetails(
                recipeId, 0, isTwoPane()
            )
        }
    }

    private fun isTwoPane(): Boolean {
        return findViewById<View?>(R.id.step_details_fragment_container) != null
    }

    private fun closeOnError() {
        finish()
        Toast.makeText(
            this,
            R.string.detail_error_message,
            Toast.LENGTH_SHORT)
                .show()
    }
}
