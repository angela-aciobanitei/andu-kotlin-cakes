package com.ang.acb.baking.util

import com.ang.acb.baking.data.network.NetworkIngredient
import com.ang.acb.baking.data.network.NetworkRecipe
import com.ang.acb.baking.data.network.NetworkStep

object PojosTestUtil {

    fun createIngredients(
        count: Int,
        quantity: Double,
        measure: String,
        ingredient: String
    ): List<NetworkIngredient> {
        return (0 until count).map {
            NetworkIngredient(
                quantity = quantity + it,
                measure = measure + it,
                ingredient = ingredient + it
            )
        }
    }

    fun createSteps(
        count:Int,
        stepId: Int,
        shortDescription: String?,
        description: String?,
        videoURL: String?,
        thumbnailURL: String?
    ): List<NetworkStep> {
        return (0 until count).map {
            NetworkStep(
                id = stepId + it,
                shortDescription = shortDescription + it,
                description = description + it,
                videoURL = videoURL + it,
                thumbnailURL = thumbnailURL + it
            )
        }
    }

    private fun createNetworkRecipe(recipeId: Int): NetworkRecipe {
        val ingredients = createIngredients(
            count = 5,
            quantity = 1.0,
            measure = "grams",
            ingredient = "ingredient"
        )

       val steps = createSteps(
            count = 5,
            stepId = 0,
            shortDescription = "shortDescription",
            description = "description",
            videoURL = "videoURL",
            thumbnailURL = "thumbnailURL"
        )

        return NetworkRecipe(
            id = recipeId,
            name = "recipe name",
            servings = 5,
            image = "imageURL",
            ingredients = ingredients,
            steps = steps
        )
    }

    fun createNetworkRecipes(count: Int): List<NetworkRecipe> {
        return (0 until count).map {
            createNetworkRecipe(it)
        }
    }
}