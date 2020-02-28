package com.ang.acb.baking.util

import com.ang.acb.baking.data.network.NetworkIngredient
import com.ang.acb.baking.data.network.NetworkRecipe
import com.ang.acb.baking.data.network.NetworkStep

object PojosTestUtil {

    fun createIngredients(count: Int): List<NetworkIngredient> {
        return (0 until count).map {
            NetworkIngredient(
                quantity = 0.0 + it,
                measure = "measure$it",
                ingredient = "ingredient$it"
            )
        }
    }

    fun createSteps(count:Int): List<NetworkStep> {
        return (0 until count).map {
            NetworkStep(
                id = it,
                shortDescription = "shortDescription$it",
                description = "description$it",
                videoURL = "videoURL$it",
                thumbnailURL = "thumbnailURL$it"
            )
        }
    }

    fun createNetworkRecipe(recipeId: Int): NetworkRecipe {
        return NetworkRecipe(
            id = recipeId,
            name = "recipe name",
            servings = 5,
            image = "imageURL",
            ingredients = createIngredients(5),
            steps = createSteps(5)
        )
    }

    fun createNetworkRecipes(count: Int): List<NetworkRecipe> {
        return (0 until count).map {
            createNetworkRecipe(it)
        }
    }
}