package com.ang.acb.baking.di

import com.ang.acb.baking.ui.recipedetails.RecipeDetailsFragment
import com.ang.acb.baking.ui.recipedetails.StepDetailsFragment
import com.ang.acb.baking.ui.recipelist.MainActivity
import com.ang.acb.baking.ui.recipedetails.DetailsActivity
import com.ang.acb.baking.ui.recipelist.RecipeListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * A Dagger module for the fragments hosted by the [MainActivity]
 */
@Module
abstract class FragmentsMainModule {

    @ContributesAndroidInjector
    abstract fun contributeRecipeListFragment(): RecipeListFragment
}

/**
 * A Dagger module for the fragments hosted by the [DetailsActivity]
 */
@Module
abstract class FragmentsDetailsModule {

    @ContributesAndroidInjector
    abstract fun contributeRecipeDetailsFragment(): RecipeDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeStepDetailsFragment(): StepDetailsFragment
}