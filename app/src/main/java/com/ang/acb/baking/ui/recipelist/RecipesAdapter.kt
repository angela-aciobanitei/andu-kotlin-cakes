package com.ang.acb.baking.ui.recipelist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.databinding.ItemRecipeBinding

/**
 * A custom [ListAdapter] for the [Recipe] list.
 */
class RecipesAdapter(
    val recipeClickListener: RecipeClickListener
) : ListAdapter<Recipe, RecipeViewHolder>(RecipeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipeClickListener,getItem(position))
    }
}

/**
 * A simple ViewHolder that can bind a [Recipe] item.
 */
class RecipeViewHolder private constructor(
    val binding: ItemRecipeBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipeClickListener: RecipeClickListener, recipe: Recipe) {
        binding.recipe = recipe
        binding.recipeClickListener = recipeClickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RecipeViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemRecipeBinding.inflate(inflater, parent, false)

            return RecipeViewHolder(binding)
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
class RecipeDiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}

/**
 * Handles [Recipe] item click events.
 */
class RecipeClickListener(val clickListener: (id:Int?) -> Unit){
    fun onClick(recipe: Recipe) = clickListener(recipe.id)
}