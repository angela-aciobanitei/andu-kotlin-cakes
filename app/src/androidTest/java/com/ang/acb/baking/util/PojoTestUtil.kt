package com.ang.acb.baking.util

import com.ang.acb.baking.data.database.Ingredient
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.data.database.RecipeDetails
import com.ang.acb.baking.data.database.Step

object PojoTestUtil {

    fun createIngredients(
        count: Int,
        recipeId: Int
    ): List<Ingredient> {
        return (0 until count).map {
            Ingredient(
                ingredientId = it + 1,
                recipeId = recipeId,
                quantity = it +  1.0,
                measure = "measure$it",
                ingredient = "ingredient$it"
            )
        }
    }

    fun createSteps(
        count:Int,
        recipeId: Int
    ): List<Step> {
        return (0 until count).map {
            Step(
                stepId = it + 1,
                recipeId = recipeId,
                index = it,
                shortDescription = "shortDescription$it",
                description = "description$it",
                videoURL = "videoURL$it",
                thumbnailURL = "thumbnailURL$it"
            )
        }
    }
}