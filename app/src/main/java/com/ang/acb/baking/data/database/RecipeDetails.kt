package com.ang.acb.baking.data.database

import androidx.room.Embedded
import androidx.room.Relation

/**
 * A simple POJO containing a recipe details, including its ingredients and ints steps.
 *
 * https://medium.com/@magdamiu/android-room-persistence-library-relations-75bbe02e8522
 * https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
 */
class RecipeDetails {
    @Embedded
    var recipe: Recipe? = null

    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId",
        entity = Ingredient::class)
    var ingredients: List<Ingredient>? = null

    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId",
        entity = Step::class)
    var steps: List<Step>? = null
}