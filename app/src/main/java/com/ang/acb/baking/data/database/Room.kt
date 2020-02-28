package com.ang.acb.baking.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Interface for database access on [Recipe] related operations.
 */
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
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    abstract fun getSimpleRecipe(recipeId: Int): LiveData<Recipe>

    @Transaction
    @Query("SELECT * FROM ingredients where recipeId = :recipeId")
    abstract fun getIngredients(recipeId: Int): LiveData<List<Ingredient>>

    @Transaction
    @Query("SELECT * FROM steps where recipeId = :recipeId")
    abstract fun getSteps(recipeId: Int): LiveData<List<Step>>

    @Transaction
    @Query("SELECT * FROM steps where recipeId = :recipeId AND stepId = :stepId")
    abstract fun getCurrentStep(recipeId: Int, stepId: Int): LiveData<Step>

    // Note: the return value of this method includes a POJO with @Relation.
    // Annotate it with @Transaction to avoid possibility of inconsistent
    // results between the POJO and its relations.
    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    abstract fun getRecipeDetails(recipeId: Int): LiveData<RecipeDetails>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id= :recipeId")
    abstract fun getRecipeDetailsSync(recipeId: Int): RecipeDetails?

    @Transaction
    @Query("SELECT * FROM recipes")
    abstract fun getDetailedRecipes(): LiveData<List<RecipeDetails>>
}

/**
 * The [Room] database for this app.
 */
@Database(
    entities = [Recipe::class, Ingredient::class, Step::class],
    version = 4,
    exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}