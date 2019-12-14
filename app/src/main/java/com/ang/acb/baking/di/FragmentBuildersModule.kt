package com.ang.acb.baking.di

import com.ang.acb.baking.ui.recipedetails.RecipeDetailsFragment
import com.ang.acb.baking.ui.recipedetails.StepDetailsFragment
import com.ang.acb.baking.ui.recipelist.RecipeListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeRecipeListFragment(): RecipeListFragment

    @ContributesAndroidInjector
    abstract fun contributeRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeStepDetailsFragment(): StepDetailsFragment
}