package com.ang.acb.baking.ui.recipedetails

import androidx.fragment.app.FragmentManager
import com.ang.acb.baking.R
import timber.log.Timber
import javax.inject.Inject

/**
 * A utility class that handles navigation in DetailsActivity.
 */
class NavigationController @Inject constructor(detailsActivity: DetailsActivity) {

    private val recipeDetailsContainerId: Int = R.id.partial_details_fragment_container
    private val stepsDetailsContainerId: Int = R.id.step_details_fragment_container
    private val fragmentManager: FragmentManager = detailsActivity.supportFragmentManager


    fun navigateToRecipeDetails(recipeId: Int, isTwoPane: Boolean) {
        fragmentManager.beginTransaction()
            .replace(
                recipeDetailsContainerId,
                RecipeDetailsFragment.newInstance(recipeId, isTwoPane)
            )
            .commit()
        Timber.d("Recipe [id=%s]: navigate to RecipeDetailsFragment.", recipeId)
    }


    fun navigateToStepDetails(recipeId: Int, stepPosition: Int, isTwoPane: Boolean) {
        if (isTwoPane) {
            // Add StepDetailsFragment to its own separate fragment container.
            fragmentManager.beginTransaction()
                .replace(
                    stepsDetailsContainerId,
                    StepDetailsFragment.newInstance(recipeId, stepPosition, true)
                )
                .commit()
        } else {
            // Replace RecipeDetailsFragment with StepDetailsFragment
            // using the same fragment container.
            fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(
                    recipeDetailsContainerId,
                    StepDetailsFragment.newInstance(recipeId, stepPosition, false)
                )
                .commit()
        }
        Timber.d("Recipe [id=%s]: navigate to StepDetailsFragment.", recipeId)
    }
}