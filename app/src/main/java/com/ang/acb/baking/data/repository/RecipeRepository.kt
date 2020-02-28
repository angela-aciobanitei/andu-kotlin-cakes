package com.ang.acb.baking.data.repository

import androidx.lifecycle.LiveData
import com.ang.acb.baking.data.database.*
import com.ang.acb.baking.data.network.*
import com.ang.acb.baking.utils.AppExecutors
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class RecipeRepository @Inject constructor (
    private val appExecutors: AppExecutors,
    private val database: RecipesDatabase,
    private val recipeDao: RecipeDao,
    private val apiService: ApiService
) {

    fun loadAllRecipes(): LiveData<Resource<List<RecipeDetails>>> {
        // Note that we are using the NetworkBoundResource<ResultType, RequestType> class
        // that we've created earlier which can provide a resource backed by both the
        // SQLite database and the network. It defines two type parameters, ResultType
        // and RequestType, because the data type used locally might not match the data
        // type returned from the API.
        return object : NetworkBoundResource<List<RecipeDetails>, List<NetworkRecipe>>(appExecutors){
            override fun createCall(): LiveData<ApiResponse<List<NetworkRecipe>>> {
                // Create the API call to load the all recipes.
                return apiService.getAllRecipes()
            }

            override fun saveCallResult(result: List<NetworkRecipe>) {
                // Save the result of the API response into the database.
                for (item in result) {
                    database.runInTransaction {
                        recipeDao.insertRecipe(item.asRecipe())
                        recipeDao.insertIngredients(item.asIngredients())
                        recipeDao.insertSteps(item.asSteps())
                    }
                }
            }

            override fun shouldFetch(data: List<RecipeDetails>?): Boolean {
                // Fetch fresh data only if it doesn't exist in database.
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<RecipeDetails>> {
                return recipeDao.getDetailedRecipes()
            }
        }.asLiveData()
    }

    fun getRecipeDetails(recipeId: Int): LiveData<RecipeDetails> {
        return recipeDao.getRecipeDetails(recipeId)
    }

    fun getRecipeIngredients(recipeId: Int): LiveData<List<Ingredient>> {
        return recipeDao.getIngredients(recipeId)
    }

    fun getRecipeSteps(recipeId: Int): LiveData<List<Step>> {
        return recipeDao.getSteps(recipeId)
    }
}
