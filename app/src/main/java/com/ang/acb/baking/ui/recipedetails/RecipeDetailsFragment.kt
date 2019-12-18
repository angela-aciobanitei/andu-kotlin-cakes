package com.ang.acb.baking.ui.recipedetails


import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.baking.R
import com.ang.acb.baking.databinding.FragmentRecipeDetailsBinding
import com.ang.acb.baking.ui.widget.PreferencesUtils
import com.ang.acb.baking.ui.widget.RecipeWidgetProvider
import com.ang.acb.baking.utils.autoCleared
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class RecipeDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigationController: NavigationController

    // Use delegated properties: val/var <property name>: <Type> by <expression>.
    private val viewModel: RecipeDetailsViewModel by viewModels { viewModelFactory }
    private var binding: FragmentRecipeDetailsBinding by autoCleared()

    private var recipeId = -1
    private var isTwoPane = false
    private lateinit var recipeName: String


    companion object {
        fun newInstance(recipeId: Int,isTwoPane: Boolean) =
            RecipeDetailsFragment().apply {
                arguments = bundleOf(
                    EXTRA_RECIPE_ID to recipeId,
                    EXTRA_IS_TWO_PANE to isTwoPane
                )
            }
    }


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

        // Save the recipe ID sent via intent extras
        arguments?.let {
            recipeId = it.getInt(EXTRA_RECIPE_ID)
            isTwoPane = it.getBoolean(EXTRA_IS_TWO_PANE, false)
        }
        viewModel.setId(recipeId)

        // Report that this fragment would like to populate the options menu.
        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeRecipeDetails()
        navigateToStepDetails()
    }

    private fun observeRecipeDetails() {
        val ingredientsAdapter = IngredientsAdapter()
        binding.rvIngredients.adapter = ingredientsAdapter
        val stepsAdapter = StepsAdapter(
            stepClickListener = StepClickListener { position  ->
                viewModel.navigateToStepDetailsEvent(position)
            }
        )
        binding.rvSteps.adapter = stepsAdapter

        viewModel.recipeDetails.observe(viewLifecycleOwner, Observer {
            // Show recipe name on action bar.
            (activity as AppCompatActivity).supportActionBar?.title = it.recipe?.name
            recipeName = it.recipe?.name.toString()
            ingredientsAdapter.submitList(it.ingredients)
            stepsAdapter.submitList(it.steps)
        })
    }

    private fun navigateToStepDetails() {
        viewModel.navigateToStepDetails.observe(viewLifecycleOwner, Observer {
            // Only proceed if the event has never been handled.
            it.getContentIfNotHandled()?.let { position ->
                navigationController.navigateToStepDetails(
                    recipeId = recipeId,
                    stepPosition = position,
                    isTwoPane = isTwoPane)
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipe_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_create_widget -> {
                addWidgetToHomeScreen(recipeId, recipeName)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addWidgetToHomeScreen(recipeId: Int, recipeName: String) {
        PreferencesUtils.setWidgetTitle(requireContext(), recipeName)
        PreferencesUtils.setWidgetRecipeId(requireContext(), recipeId)
        val appWidgetManager = AppWidgetManager.getInstance(requireContext())
        val appWidgetIds = appWidgetManager.getAppWidgetIds(
            ComponentName(requireContext(), RecipeWidgetProvider::class.java)
        )
        appWidgetManager.notifyAppWidgetViewDataChanged(
            appWidgetIds, R.id.widget_ingredients_list_items
        )
        RecipeWidgetProvider.updateRecipeWidget(
            requireContext(), appWidgetManager, appWidgetIds
        )
    }
}
