package com.ang.acb.baking.util

import com.ang.acb.baking.data.database.Ingredient
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.data.database.Step

object PojosTestUtil {

    fun createIngredientsList(
        count: Int,
        ingredientId: Int,
        recipeId: Int,
        quantity: Double,
        measure: String,
        ingredient: String
    ): List<Ingredient> {
        return (0 until count).map {
            Ingredient(
                ingredientId = ingredientId + it,
                recipeId = recipeId,
                quantity = quantity + it,
                measure = measure + it,
                ingredient = ingredient + it
            )
        }
    }

    fun createStepsList(
        count:Int,
        stepId: Int,
        recipeId: Int,
        index: Int,
        shortDescription: String?,
        description: String?,
        videoURL: String?,
        thumbnailURL: String?
    ): List<Step> {
        return (0 until count).map {
            Step(
                stepId = stepId + it,
                recipeId = recipeId,
                index = index + it,
                shortDescription = shortDescription + it,
                description = description + it,
                videoURL = videoURL + it,
                thumbnailURL = thumbnailURL + it
            )
        }
    }

    fun createDetailedRecipe(recipeId: Int): RecipeDetails {
        val recipeDetails = RecipeDetails()
        recipeDetails.recipe = Recipe(
            id = recipeId,
            name = "recipe name",
            servings = 5,
            image = "imageURL"
        )

        recipeDetails.ingredients = createIngredientsList(
            count = 5,
            ingredientId = 0,
            recipeId = recipeId,
            quantity = 1.0,
            measure = "grams",
            ingredient = "ingredient"
        )

        recipeDetails.steps = createStepsList(
            count = 5,
            stepId = 0,
            recipeId = recipeId,
            index = 0,
            shortDescription = "shortDescription",
            description = "description",
            videoURL = "videoURL",
            thumbnailURL = "thumbnailURL"
        )

        return recipeDetails
    }
}