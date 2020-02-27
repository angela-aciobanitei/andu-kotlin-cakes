package com.ang.acb.baking.data.network

import com.ang.acb.baking.data.database.Ingredient
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.data.database.Step
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


/**
 * A Data Transfer Object representing a detailed recipe,
 * including its steps and its ingredients.
 */
@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    val id: Int? = null,
    val name: String?,
    val servings: Int?,
    val image: String?,
    val ingredients: List<NetworkIngredient>?,
    val steps: List<NetworkStep>?
)

/**
 * A Data Transfer Object representing a recipe step.
 */
data class NetworkStep(
    @Json(name = "id")
    val id: Int?,
    val shortDescription: String?,
    val description: String?,
    val videoURL: String?,
    val thumbnailURL: String?
)

/**
 * A Data Transfer Object representing a recipe ingredient.
 */
data class NetworkIngredient(
    val quantity: Double?,
    val measure: String?,
    val ingredient: String?
)

/**
 * Converts recipe details network response to a [Recipe] database object.
 */
fun NetworkRecipe.asRecipe(): Recipe {
    return Recipe(
        id = id,
        name = name,
        servings = servings,
        image = image
    )
}

/**
 * Converts recipe details network response to a list of database objects,
 * i.e. an [Ingredient] list.
 */
fun NetworkRecipe.asIngredients(): List<Ingredient>? {
    return ingredients?.map {
        Ingredient(
            ingredientId = 0,
            recipeId = id,
            quantity = it.quantity,
            measure = it.measure,
            ingredient = it.ingredient
        )
    }?.toList()
}

/**
 * Converts recipe details network response to a list of database objects,
 * i.e. an [Step] list.
 */
fun NetworkRecipe.asSteps(): List<Step>? {
    return steps?.map {
        Step(
            stepId = 0,
            recipeId = id,
            index = it.id,
            shortDescription = it.shortDescription,
            description = it.description,
            videoURL = it.videoURL,
            thumbnailURL = it.thumbnailURL
        )
    }?.toList()
}

/**
 * Converts recipe details network response to a list of [RecipeDetails]
 */
fun NetworkRecipe.asRecipeDetails(): RecipeDetails {
    val recipeDetails = RecipeDetails()
    recipeDetails.recipe = asRecipe()
    recipeDetails.ingredients = asIngredients()
    recipeDetails.steps = asSteps()

    return recipeDetails
}



