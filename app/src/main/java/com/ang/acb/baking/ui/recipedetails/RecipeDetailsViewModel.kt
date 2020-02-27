package com.ang.acb.baking.ui.recipedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.ang.acb.baking.data.repository.RecipeRepository
import com.ang.acb.baking.utils.Event
import javax.inject.Inject

/**
 * The [ViewModel] for [RecipeDetailsFragment].
 */
class RecipeDetailsViewModel @Inject constructor(
    private val recipesRepository: RecipeRepository
): ViewModel() {

    // Stores the current recipe ID.
    private val _recipeId = MutableLiveData<Int>()

    val recipeDetails =
        switchMap(_recipeId, recipesRepository::getRecipeDetails)

    fun setId(id: Int) {
        _recipeId.value = id
    }

    // Handles navigation to the selected step details.
    private val _navigateToStepDetails = MutableLiveData<Event<Int>>()
    val navigateToStepDetails: LiveData<Event<Int>>
        get() = _navigateToStepDetails

    fun navigateToStepDetailsEvent(position: Int) {
        // Trigger the event by setting a new Event as a new value.
        _navigateToStepDetails.value = Event(position)
    }
}