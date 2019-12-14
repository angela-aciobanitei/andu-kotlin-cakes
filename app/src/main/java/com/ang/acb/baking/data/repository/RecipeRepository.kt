package com.ang.acb.baking.data.repository

import androidx.lifecycle.LiveData
import com.ang.acb.baking.data.database.*
import com.ang.acb.baking.data.network.ApiService
import com.ang.acb.baking.utils.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository
@Inject constructor (
    private val appExecutors: AppExecutors,
    private val database: RecipesDatabase,
    private val apiService: ApiService
) {

    // fun loadAllRecipes(): LiveData<Resource<List<RecipeDetails>>> {}

    fun getSimpleRecipes(): LiveData<List<Recipe>> {
        return database.recipeDao.getSimpleRecipes()
    }

    fun getSimpleRecipe(recipeId: Int): LiveData<Recipe> {
        return database.recipeDao.getSimpleRecipe(recipeId)
    }

    fun getRecipeSteps(recipeId: Int): LiveData<List<Step>> {
        return database.recipeDao.getRecipeSteps(recipeId)
    }

    fun getRecipeIngredients(recipeId: Int): LiveData<List<Ingredient>> {
        return database.recipeDao.getRecipeIngredients(recipeId)
    }
}
