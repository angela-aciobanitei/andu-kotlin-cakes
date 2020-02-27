package com.ang.acb.baking.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * A factory class for creating ViewModels. Dagger 2 allows multibindings and
 * with it the possibility to create a "map" of objects with a specific key:
 *      the key: for example the class type of ViewModel: MyViewModel::class
 *      the value: the instance of ViewModel: MyViewModel(repository)
 *
 * See: https://github.com/android/android-architecture-components/tree/GithubBrowserSample
 * See: https://blog.kotlin-academy.com/understanding-dagger-2-multibindings-viewmodel-8418eb372848
 */
@Singleton
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass] ?: creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        @Suppress("UNCHECKED_CAST")
        return creator.get() as T
    }
}