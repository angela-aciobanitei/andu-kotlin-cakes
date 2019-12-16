package com.ang.acb.baking.data.database

import androidx.room.*


@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val servings: Int?,
    val image: String?
)


@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Long,
    val recipeId: Int?,
    val quantity: Double?,
    val measure: String?,
    val ingredient: String?
)


@Entity(tableName = "steps")
data class Step(
    @PrimaryKey(autoGenerate = true)
    val stepId: Int,
    val recipeId: Int?,
    val index: Int?,
    val shortDescription: String?,
    val description: String?,
    val videoURL: String?,
    val thumbnailURL: String?
)

// See: https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a
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









