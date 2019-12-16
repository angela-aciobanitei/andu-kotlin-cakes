package com.ang.acb.baking.ui.recipelist


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.ang.acb.baking.R
import com.ang.acb.baking.data.database.Recipe
import com.ang.acb.baking.databinding.FragmentRecipeListBinding
import com.ang.acb.baking.ui.recipedetails.RecipeDetailsFragmentDirections
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // Use Fragment KTX to init view model
    private val viewModel: RecipeListViewModel by viewModels { viewModelFactory }
    private var binding by autoCleared<FragmentRecipeListBinding>()
    private var adapter by autoCleared<RecipesAdapter>()

    override fun onAttach(context: Context) {
        // When using Dagger with Fragments, inject as early as possible.
        // This prevents inconsistencies if the Fragment is reattached.
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment and get an instance of the binding class.
        binding = FragmentRecipeListBinding.inflate(inflater)

        // Allow data binding to observe LiveData with the lifecycle of this fragment.
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Init recipes adapter
        val adapter = RecipesAdapter(
            recipeClickListener = RecipeClickListener {
                viewModel.navigateToRecipeDetailsEvent(it)
            })
        binding.rvRecipeList.adapter = adapter
        binding.rvRecipeList.addItemDecoration(
            DividerItemDecoration(requireContext(),
                LinearLayoutManager.VERTICAL ))

        viewModel.recipes.observe(viewLifecycleOwner, Observer {resourceList ->
            binding.resource = resourceList
            adapter.submitList(resourceList.data?.map { it.recipe })
        })


        viewModel.navigateToRecipeDetails.observe(viewLifecycleOwner, Observer {
            // Only proceed if the event has never been handled.
            it.getContentIfNotHandled()?.let{
                this.findNavController().navigate(RecipeListFragmentDirections.actionShowRecipeDetails())
            }
        })
    }
}
