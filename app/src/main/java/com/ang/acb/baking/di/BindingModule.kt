package com.ang.acb.baking.di

import com.ang.acb.baking.ui.recipedetails.DetailsActivity
import com.ang.acb.baking.ui.recipelist.MainActivity
import com.ang.acb.baking.ui.widget.RecipeRemoteViewsService
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * A Dagger module for adding the [MainActivity] to the dependency graph.
 */
@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentsMainModule::class])
    abstract fun contributeMainActivity(): MainActivity
}

/**
 * A Dagger module for adding the [DetailsActivity] to the dependency graph.
 */
@Module
abstract class DetailsActivityModule {
    @ContributesAndroidInjector(modules = [FragmentsDetailsModule::class])
    abstract fun contributeDetailsActivity(): DetailsActivity
}

/**
 * A Dagger module for adding the [RecipeRemoteViewsService] to the dependency graph.
 */
@Module
abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeRecipeRemoteViewsService(): RecipeRemoteViewsService
}