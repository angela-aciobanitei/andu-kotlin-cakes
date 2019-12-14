package com.ang.acb.baking.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * A factory class for creating ViewModels. Dagger 2 allows multibindings and
 * with it the possibility to create a "map" of objects with a specific key:
 *      the key: for example the class type of ViewModel MyViewModel::class
 *      the value: the instance of ViewModel MyViewModel(repository, …)
 *
 * See: https://github.com/android/android-architecture-components/tree/GithubBrowserSample
 * See: https://blog.kotlin-academy.com/understanding-dagger-2-multibindings-viewmodel-8418eb372848
 */

@Singleton
class ViewModelFactory
@Inject constructor(
    // Maps a Class that extends ViewModel as key, and a ViewModel Provider(
    // a Dagger 2-specific class that let us provide — and so instantiate —
    // a dependency-injected class) as value.
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Get the possible Provider for the given ViewModel from the Map.
        // If our Providers map doesn't have that specific key, check
        // if there is a subclass of the ViewModel we must instantiate.
        // If getting a valid Provider from the map fails, throw an exception.
        val creator = creators[modelClass] ?:creators.entries.firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value ?: throw IllegalArgumentException("unknown model class $modelClass")
        try {
            @Suppress("UNCHECKED_CAST")
            // Finally, let Dagger create our ViewModel by invoking the get() method
            // on the Provider object and casting it to our final type.
            return creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}