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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.ang.acb.baking.databinding.FragmentRecipeDetailsBinding
import com.ang.acb.baking.ui.recipelist.RecipeListFragmentDirections
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class RecipeDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // Use delegated properties: val/var <property name>: <Type> by <expression>.
    private val viewModel: RecipeDetailsViewModel by viewModels() { viewModelFactory }
    private val args: RecipeDetailsFragmentArgs by navArgs()
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

        // Save the recipe ID sent via Navigation safe args
        viewModel.setId(args.recipeId)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeIngredients()
        observeSteps()
        navigateToStepDetails()
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
            stepClickListener = StepClickListener { position  ->
                viewModel.navigateToStepDetailsEvent(position)
            }
        )
        binding.rvSteps.adapter = stepsAdapter
        viewModel.steps.observe(viewLifecycleOwner, Observer {
            stepsAdapter.submitList(it)
        })
    }

    private fun navigateToStepDetails() {
        viewModel.navigateToStepDetails.observe(viewLifecycleOwner, Observer {
            // Only proceed if the event has never been handled.
            it.getContentIfNotHandled()?.let{position ->
                val action = RecipeDetailsFragmentDirections.actionShowStepDetails(
                    recipeId = args.recipeId,
                    stepPosition = position)
                this.findNavController().navigate(action)
            }
        })
    }
}
