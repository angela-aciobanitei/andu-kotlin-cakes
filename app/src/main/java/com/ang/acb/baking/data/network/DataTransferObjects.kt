package com.ang.acb.baking.data.network

import com.ang.acb.baking.data.database.Ingredient
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.data.database.Step
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    val id: Int? = null,
    val name: String?,
    val servings: Int?,
    val image: String?,
    val ingredients: List<NetworkIngredient>?,
    val steps: List<NetworkStep>?
)

data class NetworkIngredient(
    val shortDescription: String?,
    val description: String?,
    val videoURL: String?,
    val thumbnailURL: String?
)


data class NetworkStep(
    @Json(name = "id")
    val id: Int?,
    val quantity: Double?,
    val measure: String?,
    val ingredient: String?
)



fun NetworkRecipe.recipe(): Recipe {
    return Recipe(
        id = id,
        name = name,
        servings = servings,
        image = image
    )
}


fun NetworkRecipe.ingredients(): List<Ingredient>? {
    return ingredients?.map {
        Ingredient(
            ingredientId = 0,
            recipeId = id,
            shortDescription = it.shortDescription,
            description = it.description,
            videoURL = it.videoURL,
            thumbnailURL = it.thumbnailURL
        )
    }?.toList()
}


fun NetworkRecipe.steps(): List<Step>? {
    return steps?.map {
        Step(
            stepId = 0,
            recipeId = id,
            index = it.id,
            quantity = it.quantity,
            measure = it.measure,
            ingredient = it.ingredient
        )
    }?.toList()
}





