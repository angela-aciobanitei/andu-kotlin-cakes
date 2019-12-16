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

import com.ang.acb.baking.R
import com.ang.acb.baking.databinding.FragmentRecipeDetailsBinding
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RecipeDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // Use Fragment KTX to init view model
    private val viewModel: RecipeDetailsViewModel by viewModels { viewModelFactory }
    private var binding by autoCleared<FragmentRecipeDetailsBinding>()

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
        binding = FragmentRecipeDetailsBinding.inflate(inflater)

        // Allow data binding to observe LiveData with the lifecycle of this fragment.
        binding.lifecycleOwner = this

        // FIXME Get arguments form safe args
        // val arguments = RecipeDetailsFragmentArgs.fromBundle(arguments!!)
        // viewModel.setId(arguments.recipeId)
        val recipeId = arguments?.getInt("recipeId")
        recipeId?.let { viewModel.setId(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.recipeDetails.observe(viewLifecycleOwner, Observer {
            binding.recipeDetails = it
        })
    }
}
