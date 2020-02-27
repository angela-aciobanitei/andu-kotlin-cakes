package com.ang.acb.baking.di

import android.app.Application
import com.ang.acb.baking.RecipeApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Definition of a Dagger component that adds info from the different modules to the graph.
 * Classes annotated with @Singleton will have a unique instance in this Component.
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        RoomModule::class,
        RestModule::class,
        MainActivityModule::class,
        DetailsActivityModule::class,
        ServiceBuilderModule::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(recipeApplication: RecipeApplication)
}