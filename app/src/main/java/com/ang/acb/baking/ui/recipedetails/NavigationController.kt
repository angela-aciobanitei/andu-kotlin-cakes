package com.ang.acb.baking.ui.recipedetails

import androidx.fragment.app.FragmentManager
import com.ang.acb.baking.R
import timber.log.Timber
import javax.inject.Inject

/**
 * A utility class that handles navigation in DetailsActivity.
 *
 * See: https://github.com/android/architecture-components-samples/tree/7f3179f6599e15d4a21e8406c047bfe1e277cf69/GithubBrowserSample
 */
class NavigationController
@Inject constructor(detailsActivity: DetailsActivity) {

    private val recipeDetailsContainerId: Int = R.id.partial_details_fragment_container
    private val stepsDetailsContainerId: Int = R.id.step_details_fragment_container
    private val fragmentManager: FragmentManager = detailsActivity.supportFragmentManager


    fun navigateToRecipeDetails(recipeId: Int, isTwoPane: Boolean) {
        val fragment = RecipeDetailsFragment
            .newInstance(recipeId, isTwoPane)
        val tag = "Recipe $recipeId"

        fragmentManager.beginTransaction()
            .replace(recipeDetailsContainerId, fragment, tag)
            .commit()
    }


    fun navigateToStepDetails(recipeId: Int, stepPosition: Int, isTwoPane: Boolean) {
        val tag = "Recipe $recipeId, step $stepPosition, isTwoPane $isTwoPane"
        if (isTwoPane) {
            // Add StepDetailsFragment to its own separate fragment container.
            val fragment = StepDetailsFragment
                .newInstance(recipeId, stepPosition, true)

            fragmentManager.beginTransaction()
                .replace(stepsDetailsContainerId, fragment, tag)
                .commit()
        } else {
            // Replace RecipeDetailsFragment with StepDetailsFragment
            // using the same fragment container.
            val fragment2 = StepDetailsFragment
                .newInstance(recipeId, stepPosition, false)

            fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(recipeDetailsContainerId, fragment2, tag)
                .commit()
        }
    }
}