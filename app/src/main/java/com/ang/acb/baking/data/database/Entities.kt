package com.ang.acb.baking.data.database

import androidx.room.*


@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val id: Int,
    val name: String,
    val servings: Int,
    val image: String
)


@Entity(tableName = "ingredients")
data class Ingredient(
    val recipeId: Int,
    val shortDescription: String,
    val description: String,
    val videoURL: String,
    val thumbnailURL: String
) {
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int = 0
}


@Entity(tableName = "steps")
data class Step(
    val recipeId: Int,
    val index: Int,
    val quantity: Double,
    val measure: String,
    val ingredient: String
) {
    @PrimaryKey(autoGenerate = true)
    val stepId: Int = 0
}


class RecipeDetails {

    @Embedded
    var recipe: Recipe? = null

    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "recipeId",
        entity = Ingredient::class)
    var ingredients: List<Ingredient>? = null

    @Relation(
        parentColumn = "stepId",
        entityColumn = "recipeId",
        entity = Step::class)
    var steps: List<Step>? = null
}








