package com.ang.acb.baking.ui.recipelist


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

import com.ang.acb.baking.R
import com.ang.acb.baking.databinding.FragmentRecipeListBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // Use Fragment KTX to init view model
    private val viewModel: RecipeListViewModel by viewModels {
        viewModelFactory
    }

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
        val binding = FragmentRecipeListBinding.inflate(inflater)

        // Allow data binding to observe LiveData with the lifecycle of this fragment.
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
