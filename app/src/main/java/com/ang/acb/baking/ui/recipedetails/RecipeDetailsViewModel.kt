package com.ang.acb.baking.ui.recipedetails

import androidx.lifecycle.ViewModel
import com.ang.acb.baking.data.repository.RecipeRepository
import javax.inject.Inject

class RecipeDetailsViewModel
@Inject constructor(private val recipesRepository: RecipeRepository): ViewModel() {

}