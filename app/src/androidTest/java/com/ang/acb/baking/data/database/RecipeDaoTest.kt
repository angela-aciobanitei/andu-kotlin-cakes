package com.ang.acb.baking.data.database

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ang.acb.baking.util.PojoTestUtil.createIngredients
import com.ang.acb.baking.util.PojoTestUtil.createSteps
import com.ang.acb.baking.util.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Test for the [RecipeDao].
 */
@RunWith(AndroidJUnit4::class)
class RecipeDaoTest {

    private lateinit var database: RecipesDatabase

    // Swaps the background executor used by the Architecture Components with
    // a different one which executes each task synchronously.
    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Swaps the background executor used by the Architecture Components with
    // a different one which counts the tasks as they are started and finished.
    @get: Rule
    val countingTaskExecutorRule = CountingTaskExecutorRule()

    @Before
    fun initDb() {
        // Using an in-memory database because the information stored
        // here disappears when the process is killed.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RecipesDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() {
        countingTaskExecutorRule.drainTasks(10, TimeUnit.SECONDS)
        database.close()
    }

    @Test
    fun insertRecipe_getsCorrectRecipe() {
        // GIVEN - insert a recipe into the database
        val recipe = Recipe(2, "apple pie", 4, "/apple_pie.png")
        database.recipeDao.insertRecipe(recipe)

        // WHEN - get the recipe by ID from the database
        val dbData = database.recipeDao.getSimpleRecipe(recipeId = 2).getOrAwaitValue()

        // THEN - the loaded data contains the expected values
        assertThat(dbData, notNullValue())
        assertThat(dbData.id, `is`(2))
        assertThat(dbData.name, `is`("apple pie"))
        assertThat(dbData.servings, `is`(4))
        assertThat(dbData.image, `is`("/apple_pie.png"))
    }

    @Test
    fun insertRecipe_thenUpdateIt() {
        // GIVEN - insert a recipe into the database
        val recipe = Recipe(4, "apple pie", 4, "/apple_pie.png")
        database.recipeDao.insertRecipe(recipe)

        // WHEN - update recipe
        val newRecipe = Recipe(4, "mud pie", 4, "/mud_pie.png")
        database.recipeDao.insertRecipe(newRecipe)

        // THEN - the loaded data contains the expected values
        val dbData = database.recipeDao.getSimpleRecipe(4).getOrAwaitValue()
        assertThat(dbData, notNullValue())
        assertThat(dbData.id, `is`(4))
        assertThat(dbData.name, `is`("mud pie"))
        assertThat(dbData.servings, `is`(4))
        assertThat(dbData.image, `is`("/mud_pie.png"))
    }

    @Test
    fun insertIngredients_getsCorrectIngredientsList() {
        // GIVEN - insert ingredients list for a specific recipe ID into the db
        val recipeId = 1
        val ingredients = createIngredients(4, recipeId)

        database.recipeDao.insertIngredients(ingredients)

        // WHEN - get the ingredients list by recipe ID  from the database
        val loaded = database.recipeDao.getIngredients(recipeId).getOrAwaitValue()

        // THEN - the loaded data contains the expected values
        assertThat(loaded, notNullValue())
        assertThat(loaded.size, `is`(4))

        val first = loaded[0]
        assertThat(first.ingredientId, `is`(1))
        assertThat(first.recipeId, `is`(recipeId))
        assertThat(first.quantity, `is`(1.0))
        assertThat(first.measure, `is`("measure0"))
        assertThat(first.ingredient, `is`("ingredient0"))

        val second = loaded[1]
        assertThat(second.ingredientId, `is`(2))
        assertThat(second.recipeId, `is`(recipeId))
        assertThat(second.quantity, `is`(2.0))
        assertThat(second.measure, `is`("measure1"))
        assertThat(second.ingredient, `is`("ingredient1"))
    }

    @Test
    fun insertIngredients_thenUpdateRecipe() {
        val recipeId = 5
        val recipe = Recipe(recipeId, "cheese cake", 8, "/cheese_cake.png")
        val ingredient = Ingredient(4, recipeId, 4.0, "tb", "flour")
        database.runInTransaction {
            database.recipeDao.insertRecipe(recipe)
            database.recipeDao.insertIngredients(listOf(ingredient))
        }

        var loadedIngredients =
            database.recipeDao.getIngredients(recipeId).getOrAwaitValue()
        assertThat(loadedIngredients.size, `is`(1))

        val newRecipe = Recipe(recipeId, "lava cake", 8, "/lava_cake.png")
        database.recipeDao.insertRecipe(newRecipe)
        loadedIngredients = database.recipeDao.getIngredients(recipeId).getOrAwaitValue()
        assertThat(loadedIngredients.size, `is`(1))
    }

    @Test
    fun insertSteps_getsCorrectStepList() {
        // GIVEN - insert step list for a specific recipe ID into the db
        val recipeId = 5
        val steps = createSteps(5, recipeId)
        Timber.d(steps.toString())

        database.recipeDao.insertSteps(steps)

        // WHEN - get the ingredients list by recipe ID  from the database
        val loaded = database.recipeDao.getSteps(recipeId).getOrAwaitValue()
        Timber.d(loaded.toString())

        // THEN - the loaded data contains the expected values
        assertThat(loaded, notNullValue())
        assertThat(loaded.size, `is`(5))

        val first = loaded[0]
        assertThat(first.stepId, `is`(1))
        assertThat(first.recipeId, `is`(recipeId))
        assertThat(first.index, `is`(0))
        assertThat(first.shortDescription, `is`("shortDescription0"))
        assertThat(first.description, `is`("description0"))

        val last = loaded[4]
        assertThat(last.stepId, `is`(5))
        assertThat(last.recipeId, `is`(recipeId))
        assertThat(last.index, `is`(4))
        assertThat(last.shortDescription, `is`("shortDescription4"))
        assertThat(last.description, `is`("description4"))
    }

    @Test
    fun insertSteps_thenUpdateRecipe() {
        val recipeId = 5
        val recipe = Recipe(recipeId, "cheese cake", 8, "/cheese_cake.png")
        val steps = createSteps(7, recipeId)
        database.runInTransaction {
            database.recipeDao.insertRecipe(recipe)
            database.recipeDao.insertSteps(steps)
        }

        var loadedSteps =
            database.recipeDao.getSteps(recipeId).getOrAwaitValue()
        assertThat(loadedSteps.size, `is`(7))

        val newRecipe = Recipe(recipeId, "lava cake", 8, "/lava_cake.png")
        database.recipeDao.insertRecipe(newRecipe)
        loadedSteps = database.recipeDao.getSteps(recipeId).getOrAwaitValue()
        assertThat(loadedSteps.size, `is`(7))
    }
}