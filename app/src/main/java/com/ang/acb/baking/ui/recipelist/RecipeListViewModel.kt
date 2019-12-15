package com.ang.acb.baking.ui.recipelist

import android.view.animation.Transformation
import androidx.lifecycle.ViewModel
import com.ang.acb.baking.data.repository.RecipeRepository
import javax.inject.Inject

class RecipeListViewModel
@Inject constructor(private val recipesRepository: RecipeRepository): ViewModel(){

    val recipes = recipesRepository.loadAllRecipes()
}