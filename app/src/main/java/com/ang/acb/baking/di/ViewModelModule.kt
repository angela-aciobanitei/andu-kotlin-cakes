package com.ang.acb.baking.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.baking.ui.recipedetails.RecipeDetailsViewModel
import com.ang.acb.baking.ui.recipedetails.StepDetailsViewModel
import com.ang.acb.baking.ui.recipelist.RecipeListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * A Dagger module that provides the view models for this app and a Dagger2-backed
 * factory for creating them.
 *
 * Note: Dagger allows binding several objects into a collection even when the objects
 * are bound in different modules using multibindings. Here, we use a Map multibinding.
 *
 * See: https://dagger.dev/multibindings.html
 */
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecipeListViewModel::class)
    abstract fun bindRecipeListViewModel(recipeListViewModel: RecipeListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeDetailsViewModel::class)
    abstract fun bindRecipeDetailsViewModel(recipeDetailsViewModel: RecipeDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StepDetailsViewModel::class)
    abstract fun bindStepDetailsViewModel(stepDetailsViewModel: StepDetailsViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}