package com.ang.acb.baking.ui.recipedetails


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
import androidx.navigation.fragment.navArgs

import com.ang.acb.baking.R
import com.ang.acb.baking.databinding.FragmentStepDetailsBinding
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.reflect.jvm.internal.impl.renderer.ClassifierNamePolicy


class StepDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: StepDetailsViewModel by viewModels { viewModelFactory }
    private val args: StepDetailsFragmentArgs by navArgs()
    private var binding: FragmentStepDetailsBinding by autoCleared()

    override fun onAttach(context: Context) {
        // When using Dagger with Fragments, inject as early as possible.
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for this fragment.
        binding = FragmentStepDetailsBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.stepDetailsViewModel = viewModel

        // Save the recipe ID and current step position sent via Navigation safe args
        viewModel.init(args.recipeId, args.stepPosition)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.currentStep.observe(viewLifecycleOwner, Observer {
            binding.step = it
        })

        binding.prevButton.setOnClickListener {
            viewModel.onPrev()
            Toast.makeText(requireContext(), viewModel.currentPos().toString(),
                Toast.LENGTH_SHORT).show()
        }

        binding.nextButton.setOnClickListener {
            viewModel.onNext()
            Toast.makeText(requireContext(), viewModel.currentPos().toString(),
                Toast.LENGTH_SHORT).show()
        }
    }
}
