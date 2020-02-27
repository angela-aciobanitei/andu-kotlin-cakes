package com.ang.acb.baking.ui.recipedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ang.acb.baking.data.database.Step
import com.ang.acb.baking.databinding.ItemStepBinding

/**
 * A custom [ListAdapter] for the [Step] list.
 */
class StepsAdapter(val stepClickListener: StepClickListener) :
    ListAdapter<Step, StepViewHolder>(StepDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        return StepViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepClickListener,getItem(position), position)
    }
}

/**
 * A simple ViewHolder that can bind a [Step] item.
 */
class StepViewHolder constructor(
    val binding: ItemStepBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(stepClickListener: StepClickListener, step: Step, position: Int) {
        binding.step = step
        binding.stepClickListener = stepClickListener
        binding.stepPosition = position
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): StepViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemStepBinding.inflate(inflater, parent, false)

            return StepViewHolder(binding)
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
class StepDiffCallback : DiffUtil.ItemCallback<Step>() {
    override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem.stepId == newItem.stepId
    }

    override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem == newItem
    }
}

/**
 * Handles [Step] item click events.
 */
class StepClickListener(val clickListener: (position:Int) -> Unit){
    fun onClick(position:Int) = clickListener(position)
}