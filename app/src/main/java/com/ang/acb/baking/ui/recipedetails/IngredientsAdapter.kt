package com.ang.acb.baking.ui.recipedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ang.acb.baking.data.database.Ingredient
import com.ang.acb.baking.databinding.ItemIngredientBinding


class IngredientsAdapter:
    ListAdapter<Ingredient, IngredientViewHolder>(IngredientDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        return IngredientViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class IngredientViewHolder constructor(val binding: ItemIngredientBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(ingredient: Ingredient) {
        binding.ingredient = ingredient
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): IngredientViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemIngredientBinding.inflate(inflater, parent, false)

            return IngredientViewHolder(binding)
        }
    }
}


class IngredientDiffCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.ingredientId == newItem.ingredientId
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}

