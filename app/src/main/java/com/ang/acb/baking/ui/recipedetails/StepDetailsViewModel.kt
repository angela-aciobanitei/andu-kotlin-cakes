package com.ang.acb.baking.ui.recipedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.ang.acb.baking.data.repository.RecipeRepository
import javax.inject.Inject

class StepDetailsViewModel
@Inject constructor(private val recipesRepository: RecipeRepository): ViewModel() {

    // Stores the current recipe ID.
    private val _recipeId = MutableLiveData<Int>()
    private val _stepPosition = MutableLiveData<Int>()

    val steps = Transformations.switchMap(_recipeId) {
        recipesRepository.getRecipeSteps(it)
    }

    val currentStep = Transformations.map(_stepPosition) {
        steps.value?.get(_stepPosition.value!!)
    }
    fun setRecipeId(recipeId: Int) {
        _recipeId.value = recipeId
    }

}