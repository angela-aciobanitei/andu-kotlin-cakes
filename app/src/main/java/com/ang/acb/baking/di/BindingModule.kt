package com.ang.acb.baking.di

import com.ang.acb.baking.ui.recipedetails.DetailsActivity
import com.ang.acb.baking.ui.recipelist.MainActivity
import com.ang.acb.baking.ui.widget.RecipeRemoteViewsService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @ContributesAndroidInjector(modules = [FragmentsMainModule::class])
    abstract fun contributeMainActivity(): MainActivity
}


@Module
abstract class DetailsActivityModule {
    @ContributesAndroidInjector(modules = [FragmentsDetailsModule::class])
    abstract fun contributeDetailsActivity(): DetailsActivity
}


@Module
abstract class ServiceBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeRecipeRemoteViewsService(): RecipeRemoteViewsService
}