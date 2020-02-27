package com.ang.acb.baking.ui.recipelist


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.baking.databinding.FragmentRecipeListBinding
import com.ang.acb.baking.ui.recipedetails.DetailsActivity
import com.ang.acb.baking.ui.recipedetails.EXTRA_RECIPE_ID
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A [Fragment] that displays a list of recipes.
 */
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecipeListViewModel by viewModels { viewModelFactory }
    private var binding: FragmentRecipeListBinding by autoCleared()

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
            }
        )
        binding.rvRecipeList.adapter = adapter

        viewModel.recipes.observe(viewLifecycleOwner, Observer {resourceList ->
            binding.resource = resourceList
            adapter.submitList(resourceList.data?.map { it.recipe })
        })

        viewModel.navigateToRecipeDetails.observe(viewLifecycleOwner, Observer {
            // Only proceed if the event has never been handled.
            it.getContentIfNotHandled()?.let{recipeId ->
                val intent = Intent(view.context, DetailsActivity::class.java)
                intent.putExtra(EXTRA_RECIPE_ID, recipeId)
                view.context.startActivity(intent)
            }
        })
    }
}
