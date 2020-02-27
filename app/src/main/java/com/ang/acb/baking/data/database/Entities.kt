package com.ang.acb.baking.data.database

import androidx.room.*

/**
 * Immutable model class for a recipe.
 */
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey
    val id: Int?,
    val name: String?,
    val servings: Int?,
    val image: String?
)


/**
 * Immutable model class for a recipe ingredient.
 */
@Entity(tableName = "ingredients")
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    val ingredientId: Int,
    val recipeId: Int?,
    val quantity: Double?,
    val measure: String?,
    val ingredient: String?
)


/**
 * Immutable model class for a recipe step.
 */
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












