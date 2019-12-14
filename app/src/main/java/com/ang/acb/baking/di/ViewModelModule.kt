package com.ang.acb.baking.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.baking.ui.recipedetails.RecipeDetailsViewModel
import com.ang.acb.baking.ui.recipedetails.StepDetailsViewModel
import com.ang.acb.baking.ui.recipelist.RecipeListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

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