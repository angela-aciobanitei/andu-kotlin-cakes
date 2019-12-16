package com.ang.acb.baking.ui.recipedetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ang.acb.baking.data.database.Step
import com.ang.acb.baking.databinding.ItemStepBinding


class StepsAdapter(val stepClickListener: StepClickListener) :
    ListAdapter<Step, StepViewHolder>(StepDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        return StepViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(stepClickListener,getItem(position))
    }
}


class StepViewHolder constructor(val binding: ItemStepBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(stepClickListener: StepClickListener, step: Step) {
        binding.step = step
        binding.stepClickListener = stepClickListener
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


class StepDiffCallback : DiffUtil.ItemCallback<Step>() {
    override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem.stepId == newItem.stepId
    }

    override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
        return oldItem == newItem
    }
}


class StepClickListener(val clickListener: (id:Int?) -> Unit){
    fun onClick(step: Step) = clickListener(step.stepId)
}