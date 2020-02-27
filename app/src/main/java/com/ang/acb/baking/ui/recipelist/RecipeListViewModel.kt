package com.ang.acb.baking.ui.recipelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ang.acb.baking.data.repository.RecipeRepository
import com.ang.acb.baking.utils.Event
import javax.inject.Inject

/**
 * The [ViewModel] for [RecipeListFragment].
 */
class RecipeListViewModel
@Inject constructor(private val recipesRepository: RecipeRepository): ViewModel() {

    val recipes = recipesRepository.loadAllRecipes()

    // Handles navigation to the selected recipe details.
    private val _navigateToRecipeDetails = MutableLiveData<Event<Int?>>()
    val navigateToRecipeDetails: LiveData<Event<Int?>>
        get() = _navigateToRecipeDetails

    fun navigateToRecipeDetailsEvent(id: Int?) {
        // Trigger the event by setting a new Event as a new value.
        _navigateToRecipeDetails.value = Event(id)
    }
}
