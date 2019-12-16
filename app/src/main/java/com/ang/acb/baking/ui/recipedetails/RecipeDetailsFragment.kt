package com.ang.acb.baking.ui.recipedetails


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.ang.acb.baking.databinding.FragmentRecipeDetailsBinding
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class RecipeDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecipeDetailsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentRecipeDetailsBinding by autoCleared()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // FIXME Get arguments from safe args
        // val arguments = RecipeDetailsFragmentArgs.fromBundle(arguments!!)
        // viewModel.setId(arguments.recipeId)
        val recipeId = arguments?.getInt("recipeId")
        recipeId?.let { viewModel.setId(it) }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeIngredients()
        observeSteps()
    }


    private fun observeIngredients(){
        val ingredientsAdapter = IngredientsAdapter()
        binding.rvIngredients.adapter = ingredientsAdapter
        viewModel.ingredients.observe(viewLifecycleOwner, Observer {
            ingredientsAdapter.submitList(it)
        })
    }


    private fun observeSteps() {
        val stepsAdapter = StepsAdapter(
            stepClickListener = StepClickListener {
                viewModel.navigateToRecipeDetailsEvent(it)
            }
        )
        binding.rvSteps.adapter = stepsAdapter
        viewModel.steps.observe(viewLifecycleOwner, Observer {
            stepsAdapter.submitList(it)
        })
    }
}
