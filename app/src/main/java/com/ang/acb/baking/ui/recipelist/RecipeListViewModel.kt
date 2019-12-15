package com.ang.acb.baking.ui.recipelist

import android.view.animation.Transformation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.data.network.Resource
import com.ang.acb.baking.data.repository.RecipeRepository
import javax.inject.Inject

class RecipeListViewModel
@Inject constructor(private val recipesRepository: RecipeRepository): ViewModel(){


    val simpleRecipes = recipesRepository.getSimpleRecipes()

    init {
        val recipes = recipesRepository.loadAllRecipes()
    }
}