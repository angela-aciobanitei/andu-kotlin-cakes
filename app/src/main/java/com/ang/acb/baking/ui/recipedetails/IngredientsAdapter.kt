package com.ang.acb.baking.ui.recipedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ang.acb.baking.data.database.Ingredient
import com.ang.acb.baking.databinding.ItemIngredientBinding

/**
 * A custom [ListAdapter] for the [Ingredient] list.
 */
class IngredientsAdapter: ListAdapter<Ingredient, IngredientViewHolder>(IngredientDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

/**
 * A simple ViewHolder that can bind an [Ingredient] item.
 */
class IngredientViewHolder constructor(
    val binding: ItemIngredientBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: Ingredient) {
        binding.ingredient = ingredient
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): IngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemIngredientBinding
                .inflate(inflater, parent, false)

            return IngredientViewHolder(binding)
        }
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by [ListAdapter] to calculate the minimum number of changes between
 * and old list and a new list that's been passed to `submitList`.
 * See: https://codelabs.developers.google.com/codelabs/kotlin-android-training-diffutil-databinding
 */
class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.ingredientId == newItem.ingredientId
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}

