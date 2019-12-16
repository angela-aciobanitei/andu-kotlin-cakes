package com.ang.acb.baking.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ang.acb.baking.data.network.NetworkRecipe

@Dao
abstract class RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRecipes(recipes: List<Recipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertIngredients(ingredients: List<Ingredient>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertSteps(steps: List<Step>?)

    @Transaction
    @Query("SELECT * FROM recipes WHERE id= :recipeId")
    abstract fun getSimpleRecipe(recipeId: Int): LiveData<Recipe>

    @Transaction
    @Query("SELECT * FROM ingredients where recipeId= :recipeId")
    abstract fun getRecipeIngredients(recipeId: Int): LiveData<List<Ingredient>>

    @Transaction
    @Query("SELECT * FROM steps where recipeId= :recipeId")
    abstract fun getRecipeSteps(recipeId: Int): LiveData<List<Step>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id= :recipeId")
    abstract fun getRecipeDetails(recipeId: Int): LiveData<RecipeDetails>

    @Transaction
    @Query("SELECT * FROM recipes")
    abstract fun getSimpleRecipes(): LiveData<List<Recipe>>

    @Transaction
    @Query("SELECT * FROM recipes")
    abstract fun getAllRecipes(): LiveData<List<RecipeDetails>>
}


@Database(
    entities = [Recipe::class, Ingredient::class, Step::class],
    version = 3,
    exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}